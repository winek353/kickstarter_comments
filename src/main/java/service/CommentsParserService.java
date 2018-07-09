package service;

import model.Comment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("commentsParserService")
public class CommentsParserService {
    private static List<String> allBadges = Arrays.asList("Creator", "Collaborator", "Superbacker", "20-time creator");

    private List<String> extractComments(String toParse, String startingPatternQuote, String endingPatternQuote){
        List<String> comments = new ArrayList<>();
        Pattern p = Pattern.compile(Pattern.quote(startingPatternQuote) + "(.*?)"
                + Pattern.quote(endingPatternQuote));
        Matcher m = p.matcher(toParse);
        while(m.find())
        {
            comments.add(m.group(1));
        }
        return comments;
    }

    String getCursorFromHtml(String toParse){
        String result = getDataBetween(toParse, "<a class=\"grey-dark\" href=\"",
                ">").get(0);
        result = getDataBetween(result, "cursor=",
                "#comment-").get(0);
        return result;
    }

    private List<String> getDataBetween(String toParse, String startingPatternQuote, String endingPatternQuote){
        List<String> texts = new ArrayList<String>();
        Pattern p = Pattern.compile(Pattern.quote(startingPatternQuote) + "(.*?)"
                + Pattern.quote(endingPatternQuote));
        Matcher m = p.matcher(toParse);
        while(m.find())
        {
            texts.add(m.group(1));
        }
        return texts;
    }

    private List<String> getBadges(String toParse){
        List<String> badges = new ArrayList<>();

        for (String badge: allBadges) {
            if(toParse.contains(badge))
                badges.add(badge);
        }
        return badges;
    }


     Boolean isMoreComments(String toParse){
         String commentsCount = null;
         if(!getDataBetween(toParse,
                 "comments-count=\"", "\"").isEmpty()){
             commentsCount = getDataBetween(toParse,
                     "comments-count=\"", "\"").get(0);
         }

         return toParse.contains("&direction=desc") || commentsCount != null;
    }


    List <Comment> parse(String toParse){
        List<String> rawCommentsToParse = extractComments(toParse, "class=\\\"main clearfix pl3 ml3\\",
                "<span class=\\\"loading icon-loading-small");

        List<Comment> commentList = new ArrayList<>();
        for (String commentToParse: rawCommentsToParse) {
            Long id = Long.valueOf(getDataBetween(commentToParse,
                    "#comment-",  "\\").get(0));//parseIds(commentToParse, "\\").get(0);
            String author = getDataBetween(commentToParse, "author green-dark",
                    "</a>").get(0);
            String commentText = getDataBetween(commentToParse, "</a>\\n</span>\\n</h3>\\n<p>",
                    "</p>\\n").get(0);
            List<String> badges = getBadges(commentToParse);
            commentList.add(new Comment(id, author, commentText, badges));
        }

        return commentList;
    }

}
