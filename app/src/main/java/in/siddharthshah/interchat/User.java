package in.siddharthshah.interchat;

/**
 * Created by siddharthshah on 9/9/17.
 */

public class User {

    String name;
    String uid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public User(String name, String uid) {
        this.name = name;
        this.uid = uid;
    }
}
