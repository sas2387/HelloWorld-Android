package in.siddharthshah.interchat;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by siddharthshah on 9/9/17.
 */

public class ChatMessage {

    private String msg;
    private int userid;
    private long timestamp;

    public ChatMessage(String msg, int userid, long timestamp) {
        this.msg = msg;
        this.userid = userid;
        this.timestamp = timestamp;
    }

    public ChatMessage() {

    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("userid", userid);
        result.put("msg", msg);
        result.put("timestamp", timestamp);
        return result;
    }
}
