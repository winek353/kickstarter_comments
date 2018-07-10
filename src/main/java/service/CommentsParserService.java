package service;

import model.Comment;
import org.jsoup.Jsoup;
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

    public List<String> getDataBetween(String toParse, String startingPatternQuote, String endingPatternQuote){
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

    private String getDate(String toParse){
        if(toParse.contains("datetime=\\\""))
            return getDataBetween(toParse, "datetime=\\\"", "\\\"").get(0);
        else{
            String result = getDataBetween(toParse, "#comment-", "</h3>\\n<p>")
                    .get(0);
            return getDataBetween(result, "\\\">", "</a>").get(0);
        }
    }

    private String getAuthor(String toParse){
        String result = getDataBetween(toParse, "href=\\\"/profile/",
                "<span").get(0);
        return getDataBetween(result, "\\\">",
                "</a>").get(0);
    }

    private String getComment(String toParse){
        return toParse.substring(toParse.indexOf("</a>\\n</span>\\n</h3>\\n<p>"));
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

    private boolean isCommentValid(String comment){
         if(comment.contains("This comment has been removed by Kickstarter") ||
                 comment.contains("The author of this comment has been deleted"))
             return false;
         else
             return true;
//         return ! comment.contains("This comment has been removed by Kickstarter")
    }

    private String removeHtmlTags(String html) {
        return Jsoup.parse(html).text();
    }

    List <Comment> parse(String toParse){
        List<String> rawCommentsToParse = extractComments(toParse, "class=\\\"main clearfix pl3 ml3\\",
                "<span class=\\\"loading icon-loading-small");

        List<Comment> commentList = new ArrayList<>();
        for (String commentToParse: rawCommentsToParse) {
            if(isCommentValid(commentToParse)){
                Long id = Long.valueOf(getDataBetween(commentToParse,
                        "#comment-",  "\\").get(0));
                String author = getAuthor(commentToParse);
//                String commentText = getComment(commentToParse);
                String commentText = getDataBetween(commentToParse, "</a>\\n</span>\\n</h3>\\n<p>",
                        "</p>\\n").get(0);

                commentText = removeHtmlTags(commentText);

                List<String> badges = getBadges(commentToParse);
                String date = getDate(commentToParse);
                commentList.add(new Comment(id, author, commentText, badges, date));
            }
        }

        return commentList;
    }

}
