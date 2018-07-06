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

    StringBuffer getFirstCommentsFromKickstarter(String kickstarterProjectUrl) throws IOException{
        URL url = new URL(kickstarterProjectUrl + "/comments");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.connect();

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();
        con.disconnect();

//        System.out.println(content);
        return content;
    }


    StringBuffer getJsonFromKickstarter(String kickstarterProjectUrl, String cursor) throws IOException{
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
}
