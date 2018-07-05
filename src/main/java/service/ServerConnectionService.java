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

    private StringBuffer getJsonFromKickstarter() throws IOException{
//        https://www.kickstarter.com/projects/petersengames/startropolis/comments?cursor=20899652
        URL url = new URL("https://www.kickstarter.com/projects/petersengames/startropolis/comments?cursor=20899652");
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

    public List<Comment> getAllComments() throws IOException {
        return commentsParserService.parse(getJsonFromKickstarter().toString());
    }
}
