package service;

import model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service("commentsService")
public class CommentsService {
    private ServerConnectionService serverConnectionService;

    private CommentsParserService commentsParserService;

    private JsonFileService jsonFileService;

    @Autowired
    public CommentsService(ServerConnectionService serverConnectionService,
                           CommentsParserService commentsParserService,
                           JsonFileService jsonFileService) {
        this.serverConnectionService = serverConnectionService;
        this.commentsParserService = commentsParserService;
        this.jsonFileService = jsonFileService;
    }

    public List<Comment> getAllComments(String kickstarterProjectUrl) throws IOException {
        String toParse = serverConnectionService.getFirstCommentsFromKickstarter(kickstarterProjectUrl).toString();

        String cursor = commentsParserService.getCursorFromHtml(toParse);

        List<Comment> commentList = new ArrayList<>();

        while (commentsParserService.isMoreComments(toParse)){
            toParse = serverConnectionService.getJsonFromKickstarter(kickstarterProjectUrl, cursor).toString();

            commentList.addAll(commentsParserService
                    .parse(toParse));

            cursor = String.valueOf(
                    commentList
                            .get(commentList.size()-1)
                            .getId());
        }
        return commentList;
    }

    public void getAllCommentsToJsonFile(String kickstarterProjectUrl, String jsonFileName) throws IOException, ParseException {
        List<Comment> commentList =
                getAllComments(kickstarterProjectUrl);
        jsonFileService.writeToFile2(commentList, jsonFileName);
    }

    public List<Comment> getAllCommentsFromJsonFile(String jsonFileName) {
        try {
            return jsonFileService.readFromFile2(jsonFileName, Comment.class);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void updateCommentsInFile(String kickstarterProjectUrl, String filename) throws IOException, ParseException {
        List<Comment> commentList = getAllCommentsFromJsonFile(filename);

        if(commentList != null && !commentList.isEmpty() ){
            Long newestCursorFromFile = commentList.get(0).getId();

            String toParse = serverConnectionService.getFirstCommentsFromKickstarter(kickstarterProjectUrl).toString();

            String cursor = commentsParserService.getCursorFromHtml(toParse);

            List<Comment> newCommentList = new ArrayList<>();

            while (commentsParserService.isMoreComments(toParse)
                    && newestCursorFromFile.compareTo(Long.valueOf(cursor)) <=0){
                toParse = serverConnectionService.getJsonFromKickstarter(kickstarterProjectUrl, cursor).toString();

                newCommentList.addAll(commentsParserService
                        .parse(toParse));

                cursor = String.valueOf(
                        newCommentList
                                .get(newCommentList.size()-1)
                                .getId());
            }

            newCommentList.removeIf(comment -> comment.getId().compareTo(newestCursorFromFile)<=0);
            newCommentList.addAll(commentList);
            jsonFileService.writeToFile2(newCommentList, filename);
        }
        else
            getAllCommentsToJsonFile(kickstarterProjectUrl, filename);
    }
}
