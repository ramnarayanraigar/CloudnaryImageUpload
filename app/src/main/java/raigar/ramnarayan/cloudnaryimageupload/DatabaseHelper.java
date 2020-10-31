package raigar.ramnarayan.cloudnaryimageupload;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    // database name
    private static final String DB_NAME = "CLOUDNARY.DB";
    // database version
    private static final int DB_VERSION = 1;
    // table name
    public static final String TBL_IMAGES = "IMAGES";
    // column name
    public static final String COL_ID = "ID";
    public static final String COL_IMAGE_PATH = "IMAGE_PATH";

    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        deleteTbl(db, TBL_IMAGES);
        onCreate(db);
    }

    // create table query
    private static final String CREATE_TABLE = "CREATE TABLE " + TBL_IMAGES + " ( " + COL_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COL_IMAGE_PATH + " TEXT NOT NULL );";

    // delete table
    private static void deleteTbl(SQLiteDatabase db, String tableName) {
        db.execSQL("DROP TABLE IF EXISTS " + tableName);
    }
}