package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.Comment;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.util.List;

@Service("jsonCommentsWriter")
public class JsonCommentsWriter {

    public void writeToFile(List<Comment> commentList, String outDirectory) throws ParseException {
        try (Writer writer = new FileWriter(new File(outDirectory + ".json") )) {
            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            gson.toJson(commentList, writer);
        } catch (IOException e) {
            e.getMessage();
        }
    }
}
