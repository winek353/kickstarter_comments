package service;

import com.sun.xml.internal.ws.api.ha.StickyFeature;
import model.Comment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    String getCursorFromHtml(String toParse){//do poprawy prawdopodobnie bo ucina pierwszy komentarz
        String result = parseCommentTexts(toParse, "<a class=\"grey-dark\" href=\"",
                ">").get(0);
        result = parseCommentTexts(result, "cursor=",
                "#comment-").get(0);
        return result;
    }

    private List<Long> parseIds(String toParse, String endingPatternQuote){
        List<Long> ids = new ArrayList<Long>();
        Pattern p = Pattern.compile("#comment-(.*?)" + Pattern.quote(endingPatternQuote));
        Matcher m = p.matcher(toParse);
        while(m.find())
        {
            ids.add(Long.parseLong(m.group(1)));
        }
        return ids;
    }

    private List<String> parseCommentTexts(String toParse, String startingPatternQuote, String endingPatternQuote){
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

    private List<String> parseAuthors(String toParse){
        List<String> authors = new ArrayList<String>();
        Pattern p = Pattern.compile("author green-dark" + "(.*?)"
                + "</a>");
        Matcher m = p.matcher(toParse);
        while(m.find())
        {
            authors.add(m.group(1));
        }

        return authors.stream()
                .map(a-> a.substring(a.lastIndexOf(">") +1))
                .collect(Collectors.toList());
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
        if(toParse.contains("&direction=desc") || toParse.contains("Show older comments</a>"))
            return true;
        else
            return false;
    }


    List <Comment> parse(String toParse){
        List<String> RawCommentsToParse = extractComments(toParse, "class=\\\"main clearfix pl3 ml3\\",
                "<span class=\\\"loading icon-loading-small");

        List<Comment> commentList = new ArrayList<>();
        for (String commentToParse: RawCommentsToParse) {
            Long id = parseIds(commentToParse, "\\").get(0);
            String author = parseAuthors(commentToParse).get(0);
            String commentText = parseCommentTexts(commentToParse, "</a>\\n</span>\\n</h3>\\n<p>",
                    "</p>\\n").get(0);
            List<String> badges = getBadges(commentToParse);
            commentList.add(new Comment(id, author, commentText, badges));
        }

        return commentList;
    }

}
