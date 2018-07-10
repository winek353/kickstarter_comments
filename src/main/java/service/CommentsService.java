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

    private JsonCommentsService jsonCommentsService;

    @Autowired
    public CommentsService(ServerConnectionService serverConnectionService,
                           CommentsParserService commentsParserService,
                           JsonCommentsService jsonCommentsService) {
        this.serverConnectionService = serverConnectionService;
        this.commentsParserService = commentsParserService;
        this.jsonCommentsService = jsonCommentsService;
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
        jsonCommentsService.writeToFile(commentList, jsonFileName);
    }

    public List<Comment> getAllCommentsFromJsonFile(String jsonFileName) throws FileNotFoundException, ParseException {
        return jsonCommentsService.readFromFile(jsonFileName);
    }
}
