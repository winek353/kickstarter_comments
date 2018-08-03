package back.service;

import back.model.Comment;
import back.model.Project;
import javafx.concurrent.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;
import java.util.function.BiConsumer;

@Service("commentsService")
public class CommentsService {
    private ServerConnectionService serverConnectionService;

    private CommentsParserService commentsParserService;

    private JsonFileService jsonFileService;

    private ProjectsService projectsService;

    private Long commentNumber;

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

    public List<Comment> getAllComments(String kickstarterProjectUrl, BiConsumer<Long, Long> updateProgressBarConsumer) throws IOException {
        String toParse = serverConnectionService.getFirstCommentsFromKickstarter(kickstarterProjectUrl).toString();
        String cursor = commentsParserService.getCursorFromHtml(toParse);
        commentNumber = Long.valueOf(commentsParserService.getCommentNumberFromHtml(toParse));
        return getComments(kickstarterProjectUrl, cursor, 0L, updateProgressBarConsumer);
    }

    public void getAllCommentsToJsonFile(String kickstarterProjectUrl, String jsonFileName,
                                         BiConsumer<Long, Long> updateProgressBarConsumer) throws IOException, ParseException {
        List<Comment> commentList =
                getAllComments(kickstarterProjectUrl, updateProgressBarConsumer);
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

    private List<Comment> getComments(String kickstarterProjectUrl, String cursor, final Long boundaryCursor,
                                      BiConsumer<Long, Long> updateProgressBarConsumer) throws IOException {
        String toParse;
        List<Comment> commentList = new LinkedList<>();
        Long i = 0L;
        do{
            toParse = serverConnectionService.getJsonFromKickstarter(kickstarterProjectUrl, cursor).toString();

            commentList.addAll(commentsParserService
                    .parse(toParse));

            cursor = String.valueOf(
                    commentList
                            .get(commentList.size()-1)
                            .getId());

            if(updateProgressBarConsumer != null){
                if((i+1L)*50 > commentNumber)
                    updateProgressBarConsumer.accept(commentNumber, commentNumber);
                else
                    updateProgressBarConsumer.accept((i+1L)*50, commentNumber);
            }

            i++;
        }
        while (commentsParserService.isMoreComments(toParse)
                && boundaryCursor.compareTo(Long.valueOf(cursor)) <=0);

        return commentList;
    }


    public Task getAllCommentsToJsonFile2(String kickstarterProjectUrl, String jsonFileName) throws IOException, ParseException {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                List<Comment> commentList =
                        getAllComments(kickstarterProjectUrl, (workDone, max) -> updateProgress(workDone, max));
                jsonFileService.writeToFile(commentList, jsonFileName);
                return true;
            }
        };
    }

    public void updateCommentsInFile(String kickstarterProjectUrl, String filename) throws IOException, ParseException {
        List<Comment> commentList = getAllCommentsFromJsonFile(filename);

        if(commentList != null && !commentList.isEmpty() ){
            Long newestCursorFromFile = commentList.get(0).getId();

            String toParse = serverConnectionService.getFirstCommentsFromKickstarter(kickstarterProjectUrl).toString();

            String cursor = commentsParserService.getCursorFromHtml(toParse);

            List<Comment> newCommentList = getComments(kickstarterProjectUrl, cursor, newestCursorFromFile, null);

            newCommentList.removeIf(comment -> comment.getId().compareTo(newestCursorFromFile)<=0);
            newCommentList.addAll(commentList);
            jsonFileService.writeToFile(newCommentList, filename);
        }
        else
            getAllCommentsToJsonFile(kickstarterProjectUrl, filename, null);
    }

    public void updateCommentsInFile(Project project) throws IOException, ParseException {
        String filename = project.getName();
        List<Comment> commentList = getAllCommentsFromJsonFile(filename);

        if(commentList != null && !commentList.isEmpty() ){
            Long newestCursorFromFile = commentList.get(0).getId();

            String toParse = serverConnectionService.getFirstCommentsFromKickstarter(project.getUrl()).toString();

            String cursor = commentsParserService.getCursorFromHtml(toParse);

            List<Comment> newCommentList = getComments(project.getUrl(), cursor, newestCursorFromFile, null);

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
            getAllCommentsToJsonFile(project.getUrl(), filename, null);
    }
}
