package launcher;

import model.Comment;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.CommentsService;
import service.JsonCommentsWriter;
import service.ServerConnectionService;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public class SpringMain {

    public static void main(String[] args) throws IOException, ParseException {
        ApplicationContext ctx = new AnnotationConfigApplicationContext("service");
        ServerConnectionService serverConnectionService =
                (ServerConnectionService) ctx.getBean("serverConnectionService");

        JsonCommentsWriter jsonCommentsWriter = (JsonCommentsWriter)  ctx.getBean("jsonCommentsWriter");

        List<Comment> commentList = serverConnectionService
                .getAllComments("https://www.kickstarter.com/projects/amabrush/amabrush-worlds-first-automatic-toothbrush");

        jsonCommentsWriter.writeToFile(commentList, "comments.json");

        commentList.forEach(System.out::println);
        System.out.println(commentList.size());
    }


}
