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

        String kickstarterProjectUrl = "https://www.kickstarter.com/projects/1041610927/znaps-the-9-magnetic-adapter-for-your-mobile-devic";
        commentsService.getAllCommentsToJsonFile(kickstarterProjectUrl, "com");
//        List<Comment> comments = commentsService.getAllComments(kickstarterProjectUrl);
//        comments.forEach(c-> System.out.println(c));
//        System.out.println(comments.size());




//        CommentsParserService commentsParserService =
//                (CommentsParserService) ctx.getBean("commentsParserService");
//
//        String text = "\">\\n<h3>\\n<a class=\\\"author green-dark\\\" href=\\\"/profile/126109380\\\">Jeffery Wong</a>\\n<div class=\\\"superbacker-badge tipsy_s\\\" original-title=\\\"Super! This backer has supported a lot of projects.\\\" style=\\\"\\\">\\nSuperbacker\\n</div>\\n\\n<span class=\\\"date normal f6\\\">\\n<a class=\\\"grey-dark\\\" href=\\\"/projects/1041610927/znaps-the-9-magnetic-adapter-for-your-mobile-devic/comments?cursor=20601425#comment-20601424\\\">on <time datetime=\\\"2018-05-29T14:22:44-04:00\\\" data-format=\\\"LL\\\" class=\\\"invisible-if-js js-adjust-time\\\">May 29, 2018</time></a>\\n</span>\\n</h3>\\n<p>This is my formal request for a refund of my pledge amount. I invoke my rights under Kickstarter's Terms of Use: <a rel=\\\"nofollow noopener\\\" href=\\\"https://www.kickstarter.com/terms-of-use\\\">https://www.kickstarter.com/terms-of-use</a> \\n<br />\\\"When a project is successfully funded, the creator must complete the project and fulfill each reward. Once a creator has done so, they’ve satisfied their obligation to their backers. \\n<br />Throughout the process, creators owe their backers a high standard of effort, honest communication, and a dedication to bringing the project to life. At the same time, backers must understand that when they back a project, they’re helping to create something new — not ordering something that already exists. There may be changes or delays, and there’s a chance something could happen that prevents the creator from being able to finish the project as promised. \\n<br />If a creator is unable to complete their project and fulfill rewards, they’ve failed to live up to the basic obligations of this agreement. To right this, they must make every reasonable effort to find another way of bringing the project to the best possible conclusion for backers. A creator in this position has only remedied the situation and met their obligations to backers if: \\n<br />they post an update that explains what work has been done, how funds were used, and what prevents them from finishing the project as planned; \\n<br />they work diligently and in good faith to bring the project to the best possible conclusion in a timeframe that’s communicated to backers; \\n<br />they’re able to demonstrate that they’ve used funds appropriately and made every reasonable effort to complete the project as promised; \\n<br />they’ve been honest, and have made no material misrepresentations in their communication to backers; and \\n<br />they offer to return any remaining funds to backers who have not received their reward (in proportion to the amounts pledged), or else explain how those funds will be used to complete the project in some alternate form. \\n<br />The creator is solely responsible for fulfilling the promises made in their project. If they’re unable to satisfy the terms of this agreement, they may be subject to legal action by backers.\\\"</p>\\n\n";
//        String result = commentsParserService.getDataBetween(text,
//                "#comment-",  "\\").get(0);
//
//        System.out.println(result);

        //        text = text.replaceAll("\\n", "").replace("\r", "");
//        System.out.println(text);
//        System.out.println(commentsParserService.removeTagsFromText(text));
    }
}
