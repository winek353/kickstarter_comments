package sample;

import model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import service.CommentsParserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.List;

public class ServerConnection {

    public static StringBuffer getAllComments() throws IOException{
//        https://www.kickstarter.com/projects/petersengames/startropolis/comments?cursor=20899652
        URL url = new URL("https://www.kickstarter.com/projects/petersengames/startropolis/comments?cursor=20899652");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("x-requested-with", "XMLHttpRequest");
        con.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        con.setRequestProperty("Accept", "application/json");
        con.connect();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine+"\n");
        }
        in.close();
        con.disconnect();

        return content;
    }

    public static void main(String[] args) throws IOException {
        CommentsParserService commentsParserService = new CommentsParserService();
        List<Comment> commentList = commentsParserService.parse(getAllComments().toString());

        commentList.forEach(System.out::println);
    }
}
