package in.siddharthshah.interchat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by siddharthshah on 9/9/17.
 */

public class ChatDataSource {

    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public ChatDataSource(Context context) {
        dbHelper = new DBHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public boolean insertChat(int id, String name, String emailid, String lastMessagetime) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_ID, id);
        values.put(DBHelper.COLUMN_NAME, name);
        values.put(DBHelper.COLUMN_EMAILID, emailid);
        values.put(DBHelper.COLUMN_LASTMESSAGETIME, lastMessagetime);
        long insertId = database.insert(DBHelper.TABLE_PREVIOUSCHATS, null,
                values);

        if(insertId==-1){
            return false;
        }

        return true;
    }

    /*public int updateMeeting(Meeting meeting){
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_MEETING_TITLE, meeting.getMeetingTitle());
        values.put(DBHelper.COLUMN_MEETING_DATE, meeting.getMeetingDate());
        values.put(DBHelper.COLUMN_MEETING_TIME, meeting.getMeetingTime());
        values.put(DBHelper.COLUMN_MEETING_ROOM, meeting.getMeetingRoom());
        values.put(DBHelper.COLUMN_MEETING_DESCRIPTION, meeting.getMeetingDescription());
        values.put(DBHelper.COLUMN_MEETING_STAFFNAME, meeting.getStaffName());

        String where = DBHelper.COLUMN_MEETING_ID + " = " + "? AND "+DBHelper.COLUMN_STUDENTID+ " = ?";
        String whereargs[]={""+meeting.getMeetingId(),studentID};
        return database.update(DBHelper.TABLE_MEETING, values, where, whereargs);
    }*/

    public List<Chat> getAllChatsSorted() {
        List<Chat> chats = new ArrayList<>();

        Cursor cursor = database.rawQuery("SELECT * FROM " + DBHelper.TABLE_PREVIOUSCHATS
                + " ORDER BY " + DBHelper.COLUMN_LASTMESSAGETIME + " DESC" , null);
        if(cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Chat chat = cursotToChat(cursor);
                chats.add(chat);
                cursor.moveToNext();
            }
        }
        // make sure to close the cursor
        cursor.close();
        return chats;
    }

    public void getInfo(){
        Cursor ti = database.rawQuery("PRAGMA table_info(" + DBHelper.TABLE_PREVIOUSCHATS + ")", null);
        if ( ti.moveToFirst() ) {
            do {
                Log.d("COLUMN", ti.getString(1));
            } while (ti.moveToNext());
        }
    }

    private Chat cursotToChat(Cursor cursor) {
        Chat chat = new Chat();
        chat.setId(cursor.getInt(0));
        chat.setName(cursor.getString(1));
        chat.setEmailId(cursor.getString(2));
        chat.setLastAccessDateTime(cursor.getString(3));
        return chat;
    }
}
