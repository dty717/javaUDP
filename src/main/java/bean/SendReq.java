package bean;

public class SendReq {
    private int priority;
    private String content;
    private String type_id;

    public SendReq(int priority, String content, String type_id) {
        this.priority = priority;
        this.content = content;
        this.type_id = type_id;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }
}
