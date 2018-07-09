package model;

import java.util.List;

public class Comment {
    private Long id;
    private String author;
    private String text;
    private List<String> badges;

//    public Comment(Long id, String author, String text) {
//        this.id = id;
//        this.author = author;
//        this.text = text;
//    }


    public Comment(Long id, String author, String text, List<String> badges) {
        this.id = id;
        this.author = author;
        this.text = text;
        this.badges = badges;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getAuthor(){
        return author;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", text='" + text + '\'' +
                '}';
    }
}



