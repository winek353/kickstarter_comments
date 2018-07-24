package back.model;

import java.util.List;

public class Comment {
    private Long id;
    private String author;
    private String text;
    private List<String> badges;
    private String date;

    public Comment(Long id, String author, String text, List<String> badges, String date) {
        this.id = id;
        this.author = author;
        this.text = text;
        this.badges = badges;
        this.date = date;
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

    public List<String> getBadges() {
        return badges;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", author='" + author + '\'' +
                ", text='" + text + '\'' +
                ", badges=" + badges +
                ", date='" + date + '\'' +
                '}';
    }
}



