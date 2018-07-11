package launcher;

import model.Comment;
import model.Project;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import service.CommentsParserService;
import service.CommentsService;
import service.ProjectsService;;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public class SpringMain {

    public static void main(String[] args) throws IOException, ParseException {
        ApplicationContext ctx = new AnnotationConfigApplicationContext("service");
        CommentsService commentsService =
                (CommentsService) ctx.getBean("commentsService");
        ProjectsService projectsService =
                (ProjectsService) ctx.getBean("projectsService");

        String kickstarterProjectUrl = "https://www.kickstarter.com/projects/bdgames/assault-on-doomrock-doompocalypse";
        commentsService.getAllCommentsToJsonFile(kickstarterProjectUrl, "com");
//        List<Comment> comments = commentsService.getAllComments(kickstarterProjectUrl);
//        comments.forEach(c-> System.out.println(c));
//        System.out.println(comments.size());
        commentsService.getAllCommentsFromJsonFile("com").forEach(c-> System.out.println(c));
        
//        projectsService.saveProject("startropolis",
//                "https://www.kickstarter.com/projects/petersengames/startropolis");
//        projectsService.saveProject("Assault on Doomrock - Doompocalypse",
//                "https://www.kickstarter.com/projects/bdgames/assault-on-doomrock-doompocalypse");
//        projectsService.getProjects().forEach(p-> System.out.println(p));
    }
}
