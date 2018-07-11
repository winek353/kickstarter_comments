package service;

import model.Project;
import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

@Service("projectsService")
public class ProjectsService {
    private static String FILE_NAME = "projects";

    private JsonFileService jsonFileService;

    @Autowired
    public ProjectsService(JsonFileService jsonFileService) {
        this.jsonFileService = jsonFileService;
    }

    public void saveProject(String name, String url){
        Project project = new Project(name, url);
        try {
            List<Project> projects = jsonFileService.readFromFile2(FILE_NAME, Project.class);
            if(projects == null)
                projects = new ArrayList<>();
            projects.add(project);
            jsonFileService.writeToFile2(projects, FILE_NAME);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }
    public List<Project> getProjects (){
        try {
            return jsonFileService.readFromFile2(FILE_NAME, Project.class);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteProject(String name){
        try {
            List<Project> projects = jsonFileService.readFromFile2(FILE_NAME, Project.class);
            projects.removeIf(project -> project.getName().equals(name));
            jsonFileService.writeToFile2(projects, FILE_NAME);
        } catch (ParseException | IOException e) {
            e.printStackTrace();
        }
    }

//    private Project findByName(String name){
//        try {
//            List<Project> projects = jsonFileService.readFromFile2(FILE_NAME, Project.class);
//            return projects.stream()
//                    .filter(project -> project.getName().equals(name))
//                    .findFirst()
//                    .get();
//        } catch (ParseException | IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}
