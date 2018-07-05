package service;

import model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import service.CommentsParserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.List;

@Service("serverConnectionService")
public class ServerConnectionService {

    private CommentsParserService commentsParserService;

    @Autowired
    public ServerConnectionService(CommentsParserService commentsParserService) {
        this.commentsParserService = commentsParserService;
    }

    private StringBuffer getFirstCommentsFromKickstarter(String kickstarterProjectUrl) throws IOException{
        URL url = new URL(kickstarterProjectUrl + "/comments");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
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


    private StringBuffer getJsonFromKickstarter(String kickstarterProjectUrl, String cursor) throws IOException{
//        https://www.kickstarter.com/projects/petersengames/startropolis/comments?cursor=20899652
        URL url = new URL(kickstarterProjectUrl + "/comments?cursor=" + cursor);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("x-requested-with", "XMLHttpRequest");
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

    public List<Comment> getAllComments(String kickstarterProjectUrl) throws IOException {
        List<Comment> commentList = commentsParserService
                .parseFromHtml(getFirstCommentsFromKickstarter(kickstarterProjectUrl).toString());

        String cursor;
        while (commentList.size()%50 == 0){//rozwiązanie na szybko (być może się spętli gdy brak komentarzy!!!),
            // a serwer czasami nie zwraca 50 komentarzy
            cursor = String.valueOf(
                    commentList
                            .get(commentList.size()-1)
                            .getId());
            commentList.addAll(commentsParserService
                    .parse(getJsonFromKickstarter(kickstarterProjectUrl, cursor).toString()));
        }
        return commentList;
    }
}
