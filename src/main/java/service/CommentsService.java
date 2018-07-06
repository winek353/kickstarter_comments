package service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("commentsService")
public class CommentsService {
    private ServerConnectionService serverConnectionService;

    private  JsonCommentsWriter jsonCommentsWriter;

    @Autowired
    public CommentsService(ServerConnectionService serverConnectionService, JsonCommentsWriter jsonCommentsWriter) {
        this.serverConnectionService = serverConnectionService;
        this.jsonCommentsWriter = jsonCommentsWriter;
    }

}
