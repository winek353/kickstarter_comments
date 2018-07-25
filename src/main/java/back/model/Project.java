package back.model;

public class Project {
    private String name;
    private String url;
    private Long firstUpdatedCommentId;

    public Project(String name, String url) {
        this.name = name;
        this.url = url;
        this.firstUpdatedCommentId = 0L;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }

    public Long getFirstUpdatedCommentId() {
        return firstUpdatedCommentId;
    }

    public void setFirstUpdatedCommentId(Long firstUpdatedCommentId) {
        this.firstUpdatedCommentId = firstUpdatedCommentId;
    }

    @Override
    public String toString() {
        return "Project{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
