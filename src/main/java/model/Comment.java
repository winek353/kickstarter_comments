package model;

public class Comment {
    private Long id;
    private String author;
    private String text;

    public Comment(Long id, String author, String text) {
        this.id = id;
        this.author = author;
        this.text = text;
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
                ", text='" + text + '\'' +
                '}';
    }
}



