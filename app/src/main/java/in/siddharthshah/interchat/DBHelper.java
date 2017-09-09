package in.siddharthshah.interchat;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by siddharthshah on 9/9/17.
 */

public class DBHelper extends SQLiteOpenHelper {

    Context context;

    private static final String DATABASE_NAME = "interchat.db";
    private static final int DATABASE_VERSION = 1;

    public static String TABLE_PREVIOUSCHATS = "previouschats";
    public static String COLUMN_ID = "chatid";
    public static String COLUMN_NAME = "name";
    public static String COLUMN_EMAILID = "emailid";
    public static String COLUMN_LASTMESSAGETIME = "lastaccesstime";

    private static final String DATABASE_CREATE_PREVIOUSCHATS = "create table "
            + TABLE_PREVIOUSCHATS + "("
            + COLUMN_ID + " integer not null,"
            + COLUMN_NAME + " text not null,"
            + COLUMN_EMAILID + " text not null,"
            + COLUMN_LASTMESSAGETIME + " text not null);";

    public DBHelper(Context context) {
        //Add a DATABASE_VERSION parameter if you will need to update the database structure in the future
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        Log.d("DATABASE", "CREATING DATABASE");
        Log.d("TABLE", DATABASE_CREATE_PREVIOUSCHATS);
        database.execSQL(DATABASE_CREATE_PREVIOUSCHATS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
