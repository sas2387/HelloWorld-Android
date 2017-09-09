package in.siddharthshah.interchat;

/**
 * Created by siddharthshah on 9/9/17.
 */

public class Chat {

    private int id;
    private String name;
    private String emailId;
    private String lastAccessDateTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getLastAccessDateTime() {
        return lastAccessDateTime;
    }

    public void setLastAccessDateTime(String lastAccessDateTime) {
        this.lastAccessDateTime = lastAccessDateTime;
    }
}
