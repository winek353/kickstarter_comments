package service;

import model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

@Service("commentsService")
public class CommentsService {
    private ServerConnectionService serverConnectionService;

    private CommentsParserService commentsParserService;

    private JsonCommentsWriter jsonCommentsWriter;

    @Autowired
    public CommentsService(ServerConnectionService serverConnectionService,
                           CommentsParserService commentsParserService,
                           JsonCommentsWriter jsonCommentsWriter) {
        this.serverConnectionService = serverConnectionService;
        this.commentsParserService = commentsParserService;
        this.jsonCommentsWriter = jsonCommentsWriter;
    }

    public List<Comment> getAllComments(String kickstarterProjectUrl) throws IOException {
        String toParse = serverConnectionService.getFirstCommentsFromKickstarter(kickstarterProjectUrl).toString();
        List<Comment> commentList = commentsParserService
                .parseFromHtml(toParse);
        String cursor;

        while (commentsParserService.isMoreComments(toParse)){
            cursor = String.valueOf(
                    commentList
                            .get(commentList.size()-1)
                            .getId());
            toParse = serverConnectionService.getJsonFromKickstarter(kickstarterProjectUrl, cursor).toString();

            commentList.addAll(commentsParserService
                    .parse(toParse));
        }
        return commentList;
    }

    public void getAllCommentsToJsonFile(String kickstarterProjectUrl, String jsonFileName) throws IOException, ParseException {
        List<Comment> commentList =
                getAllComments(kickstarterProjectUrl);
        jsonCommentsWriter.writeToFile(commentList, jsonFileName);
    }

}
