package back.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import back.model.Comment;
import back.model.Project;
import org.springframework.stereotype.Service;

import java.io.*;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.List;

@Service("jsonFileService")
class JsonFileService {

    <T> void writeToFile(List<T> ListToWrite, String outDirectory) throws ParseException {
        try (Writer writer = new FileWriter(new File(outDirectory + ".json") )) {
            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            gson.toJson(ListToWrite, writer);
        } catch (IOException e) {
            e.getMessage();
        }
    }

    <T> List<T> readFromFile(String fileName, Class clazz) throws ParseException, IOException {
        Gson gson = new Gson();
        Type type;
        type = pickType(clazz);

        //create file if does not exists
        File yourFile = new File(fileName+ ".json");
        yourFile.createNewFile(); // if file already exists will do nothing

        JsonReader reader = new JsonReader(new FileReader(fileName+ ".json"));
        return gson.fromJson(reader, type);
    }

    private Type pickType(Class clazz){
        if(clazz.getName().equals("back.model.Project"))
            return PROJECT_TYPE;
        else
            return COMMENT_TYPE;
    }

    private static final Type COMMENT_TYPE = new TypeToken<List<Comment>>() {
    }.getType();

    private static final Type PROJECT_TYPE = new TypeToken<List<Project>>() {
    }.getType();
}
