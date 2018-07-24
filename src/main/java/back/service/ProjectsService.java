package back.service;

import back.model.Project;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

@Service("projectsService")
public class ProjectsService {
    private static String FILE_NAME = "projects";

    private JsonFileService jsonFileService;

    @Autowired
    public ProjectsService(JsonFileService jsonFileService) {
        this.jsonFileService = jsonFileService;
    }

    public String fixUrl(String url){
        return url.replaceAll("/comments", "");
    }

    public void saveProject(String name, String url){
        Project project = new Project(name, url);
        try {
            List<Project> projects = jsonFileService.readFromFile(FILE_NAME, Project.class);
            if(projects == null)
                projects = new ArrayList<>();
            projects.add(project);
            jsonFileService.writeToFile(projects, FILE_NAME);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }
    public List<Project> getProjects (){
        try {
            return jsonFileService.readFromFile(FILE_NAME, Project.class);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteProject(String name){
        try {
            List<Project> projects = jsonFileService.readFromFile(FILE_NAME, Project.class);
            projects.removeIf(project -> project.getName().equals(name));
            jsonFileService.writeToFile(projects, FILE_NAME);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

    private Project findByName(String name){
        try {
            List<Project> projects = jsonFileService.readFromFile(FILE_NAME, Project.class);
            return projects.stream()
                    .filter(project -> project.getName().equals(name))
                    .findFirst()
                    .get();
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
