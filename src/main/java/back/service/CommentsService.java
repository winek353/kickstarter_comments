package back.service;

import back.model.Comment;
import back.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service("commentsService")
public class CommentsService {
    private ServerConnectionService serverConnectionService;

    private CommentsParserService commentsParserService;

    private JsonFileService jsonFileService;

    private ProjectsService projectsService;



    @Autowired
    public CommentsService(ServerConnectionService serverConnectionService,
                           CommentsParserService commentsParserService,
                           JsonFileService jsonFileService,
                           ProjectsService projectsService) {
        this.serverConnectionService = serverConnectionService;
        this.commentsParserService = commentsParserService;
        this.jsonFileService = jsonFileService;
        this.projectsService = projectsService;
    }

//    public CommentsService(ServerConnectionService serverConnectionService,
//                           CommentsParserService commentsParserService,
//                           JsonFileService jsonFileService ) {
//        this.serverConnectionService = serverConnectionService;
//        this.commentsParserService = commentsParserService;
//        this.jsonFileService = jsonFileService;
//    }

    public List<Comment> getAllComments(String kickstarterProjectUrl) throws IOException {
        String toParse = serverConnectionService.getFirstCommentsFromKickstarter(kickstarterProjectUrl).toString();
        String cursor = commentsParserService.getCursorFromHtml(toParse);
        return getComments(kickstarterProjectUrl, cursor, 0L);
    }

    public void getAllCommentsToJsonFile(String kickstarterProjectUrl, String jsonFileName) throws IOException, ParseException {
        List<Comment> commentList =
                getAllComments(kickstarterProjectUrl);
        jsonFileService.writeToFile(commentList, jsonFileName);
    }

    public List<Comment> getAllCommentsFromJsonFile(String jsonFileName) {
        try {
            return jsonFileService.readFromFile(jsonFileName, Comment.class);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Comment> getComments(String kickstarterProjectUrl, String cursor,
                                      final Long boundaryCursor) throws IOException {
        String toParse;
        List<Comment> commentList = new ArrayList<>();
        do{
            toParse = serverConnectionService.getJsonFromKickstarter(kickstarterProjectUrl, cursor).toString();

            commentList.addAll(commentsParserService
                    .parse(toParse));

            cursor = String.valueOf(
                    commentList
                            .get(commentList.size()-1)
                            .getId());
        }
        while (commentsParserService.isMoreComments(toParse)
                && boundaryCursor.compareTo(Long.valueOf(cursor)) <=0);

        return commentList;
    }

    public void updateCommentsInFile(String kickstarterProjectUrl, String filename) throws IOException, ParseException {
        List<Comment> commentList = getAllCommentsFromJsonFile(filename);

        if(commentList != null && !commentList.isEmpty() ){
            Long newestCursorFromFile = commentList.get(0).getId();

            String toParse = serverConnectionService.getFirstCommentsFromKickstarter(kickstarterProjectUrl).toString();

            String cursor = commentsParserService.getCursorFromHtml(toParse);

            List<Comment> newCommentList = getComments(kickstarterProjectUrl, cursor, newestCursorFromFile);

            newCommentList.removeIf(comment -> comment.getId().compareTo(newestCursorFromFile)<=0);
            newCommentList.addAll(commentList);
            jsonFileService.writeToFile(newCommentList, filename);
        }
        else
            getAllCommentsToJsonFile(kickstarterProjectUrl, filename);
    }

    public void updateCommentsInFile(Project project) throws IOException, ParseException {
        String filename = project.getName();
        List<Comment> commentList = getAllCommentsFromJsonFile(filename);

        if(commentList != null && !commentList.isEmpty() ){
            Long newestCursorFromFile = commentList.get(0).getId();

            String toParse = serverConnectionService.getFirstCommentsFromKickstarter(project.getUrl()).toString();

            String cursor = commentsParserService.getCursorFromHtml(toParse);

            List<Comment> newCommentList = getComments(project.getUrl(), cursor, newestCursorFromFile);

            newCommentList.removeIf(comment -> comment.getId().compareTo(newestCursorFromFile)<=0);

            if(!newCommentList.isEmpty()){
                project.setFirstUpdatedCommentId(newCommentList.get(newCommentList.size()-1).getId());
                System.out.println(project.getFirstUpdatedCommentId());
                projectsService.updateProject(project);
            }

            newCommentList.addAll(commentList);
            jsonFileService.writeToFile(newCommentList, filename);
        }
        else
            getAllCommentsToJsonFile(project.getUrl(), filename);
    }
}
