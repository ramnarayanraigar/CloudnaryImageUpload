package raigar.ramnarayan.cloudnaryimageupload;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBManager {
    private DatabaseHelper dbHelper;
    private Context context;
    private SQLiteDatabase database;

    public DBManager(Context c) {
        context = c;
    }

    public DBManager open() throws SQLException {
        if (dbHelper == null) {
            dbHelper = new DatabaseHelper(context);
            database = dbHelper.getWritableDatabase();
        }

        return this;
    }

    public void close() {
        if (dbHelper != null)
            dbHelper.close();
    }

    public long insertInStudent(String name) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.COL_IMAGE_PATH, name);

        return database.insert(DatabaseHelper.TBL_IMAGES, null, contentValues);


    }

    public Cursor fetchFromStudent() {
        String[] columns = new String[] {DatabaseHelper.COL_IMAGE_PATH, DatabaseHelper.COL_ID};

        Cursor cursor = database.query(DatabaseHelper.TBL_IMAGES, columns, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        return cursor;
    }

    public int updateIntoStudent(int id, String name) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(DatabaseHelper.COL_IMAGE_PATH, name);

        return database.update(DatabaseHelper.TBL_IMAGES, contentValues, DatabaseHelper.COL_ID + " = " + id, null);
    }

    public int deleteFromStudent(String id) {
        return database.delete(DatabaseHelper.TBL_IMAGES, DatabaseHelper.COL_ID + " = " + id, null);
    }
}