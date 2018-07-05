package service;

import model.Comment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service("commentsParserService")
public class CommentsParserService {
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

    private List <Comment> createComments(List<Long> ids,  List<String> commentTexts){
        List <Comment> comments = new ArrayList<>();

        for(int i=0;i<ids.size();i++){
            comments.add(new Comment(ids.get(i), commentTexts.get(i)));
        }

        return comments;
    }

    public Boolean isMoreComments(String toParse){
        if(toParse.contains("&direction=desc") || toParse.contains("Show older comments</a>"))
            return true;
        else
            return false;
    }

    public List <Comment> parse(String toParse){
        List<Long> ids = parseIds(toParse, "\\");
        List<String> commentTexts = parseCommentTexts(toParse, "</a>\\n</span>\\n</h3>\\n<p>",
                "</p>\\n");

        return createComments(ids, commentTexts);
    }

    public List <Comment> parseFromHtml(String toParse){
        List<Long> ids = parseIds(toParse, "\"");
        List<String> commentTexts = parseCommentTexts(toParse,"<p>","</p>");

        return createComments(ids, commentTexts);
    }
}
