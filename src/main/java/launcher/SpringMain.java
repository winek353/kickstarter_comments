package launcher;

import model.Comment;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.ServerConnectionService;

import java.io.IOException;
import java.util.List;

public class SpringMain {

    public static void main(String[] args) throws IOException {
        ApplicationContext ctx = new AnnotationConfigApplicationContext("service");
        ServerConnectionService serverConnectionService =
                (ServerConnectionService) ctx.getBean("serverConnectionService");

        List<Comment> commentList = serverConnectionService
                .getAllComments("https://www.kickstarter.com/projects/amabrush/amabrush-worlds-first-automatic-toothbrush");

        commentList.forEach(System.out::println);
        System.out.println(commentList.size());
    }


}
