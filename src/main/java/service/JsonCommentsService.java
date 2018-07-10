package service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import model.Comment;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.List;

@Service("jsonCommentsWriter")
public class JsonCommentsService {

    public void writeToFile(List<Comment> commentList, String outDirectory) throws ParseException {
        try (Writer writer = new FileWriter(new File(outDirectory + ".json") )) {
            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            gson.toJson(commentList, writer);
        } catch (IOException e) {
            e.getMessage();
        }
    }

    private static final Type COMMENT_TYPE = new TypeToken<List<Comment>>() {
    }.getType();

    public List<Comment> readFromFile(String fileName) throws ParseException, FileNotFoundException {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(fileName+ ".json"));
        List<Comment> comments = gson.fromJson(reader, COMMENT_TYPE);
        return comments;
    }
}
