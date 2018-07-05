package model;

public class Comment {
    private Long id;
    private String text;

    public Comment(Long id, String text) {
        this.id = id;
        this.text = text;
    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", text='" + text + '\'' +
                '}';
    }
}



