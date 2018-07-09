package launcher;

import model.Comment;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.CommentsService;;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public class SpringMain {

    public static void main(String[] args) throws IOException, ParseException {
        ApplicationContext ctx = new AnnotationConfigApplicationContext("service");
        CommentsService commentsService =
                (CommentsService) ctx.getBean("commentsService");

        String kickstarterProjectUrl = "https://www.kickstarter.com/projects/petersengames/startropolis";
//        commentsService.getAllCommentsToJsonFile(kickstarterProjectUrl, "com");
        List<Comment> comments = commentsService.getAllComments(kickstarterProjectUrl);
        comments.forEach(c-> System.out.println(c));
        System.out.println(comments.size());
    }
}
