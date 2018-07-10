package launcher;

import model.Comment;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.CommentsParserService;
import service.CommentsService;;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public class SpringMain {

    public static void main(String[] args) throws IOException, ParseException {
        ApplicationContext ctx = new AnnotationConfigApplicationContext("service");
        CommentsService commentsService =
                (CommentsService) ctx.getBean("commentsService");

        String kickstarterProjectUrl = " https://www.kickstarter.com/projects/bdgames/assault-on-doomrock-doompocalypse";
//        commentsService.getAllCommentsToJsonFile(kickstarterProjectUrl, "com");
        List<Comment> comments = commentsService.getAllComments(kickstarterProjectUrl);
        comments.forEach(c-> System.out.println(c));
        System.out.println(comments.size());




//        CommentsParserService commentsParserService =
//                (CommentsParserService) ctx.getBean("commentsParserService");
//
//        String text = "So Space Station in a box....\\n<br />Will we have to complete with the Russians and Chinese on this project lol as they currently hold the monolopy on the space stations :p\\n<br />Seriously though its great idea for an Add on";
//        text = text.replaceAll("\\n", "").replace("\r", "");
//        System.out.println(text);
//        System.out.println(commentsParserService.removeTagsFromText(text));
    }
}
