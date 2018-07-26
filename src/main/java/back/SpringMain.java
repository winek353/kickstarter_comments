package back;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import back.service.CommentsService;
import back.service.ProjectsService;

import java.io.IOException;
import java.text.ParseException;

public class SpringMain {

    public static void main(String[] args) throws IOException, ParseException {
        ApplicationContext ctx = new AnnotationConfigApplicationContext("back/service");
        CommentsService commentsService =
                (CommentsService) ctx.getBean("commentsService");
        ProjectsService projectsService =
                (ProjectsService) ctx.getBean("projectsService");

        String kickstarterProjectUrl = "https://www.kickstarter.com/projects/bdgames/assault-on-doomrock-doompocalypse";
//        List<Comment> comments = commentsService.getAllComments(kickstarterProjectUrl);
//        comments.forEach(c-> System.out.println(c));
//        System.out.println(comments.size());

        commentsService.getAllCommentsToJsonFile(kickstarterProjectUrl, "com", null);
//        commentsService.updateCommentsInFile(kickstarterProjectUrl, "com");
//        commentsService.getAllCommentsFromJsonFile("com").forEach(c-> System.out.println(c.getId()));
        System.out.println(commentsService.getAllCommentsFromJsonFile("com").size());



//        projectsService.saveProject("startropolis",
//                "https://www.kickstarter.com/projects/petersengames/startropolis");
//        projectsService.saveProject("Assault on Doomrock - Doompocalypse",
//                "https://www.kickstarter.com/projects/bdgames/assault-on-doomrock-doompocalypse");
//        projectsService.getProjects().forEach(p-> System.out.println(p));
//        projectsService.deleteProject("startropolis");
    }
}
