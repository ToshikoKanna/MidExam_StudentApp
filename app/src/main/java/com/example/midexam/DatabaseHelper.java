package com.example.midexam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "school.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE = "students";

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT NOT NULL, " +
                "student_id TEXT NOT NULL UNIQUE, " +
                "enrolled INTEGER NOT NULL DEFAULT 0)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    // ADD / INSERT
    public boolean insertStudent(String studentId, String name, boolean enrolled) {
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("student_id", studentId);
        cv.put("enrolled", enrolled ? 1 : 0);
        return getWritableDatabase().insert(TABLE, null, cv) != -1;
    }

    public boolean addStudent(String name, String studentId, boolean enrolled) {
        return insertStudent(studentId, name, enrolled);
    }

    // EDIT (find the row by Student ID)
    public boolean updateStudent(String studentId, String name, boolean enrolled) {
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("enrolled", enrolled ? 1 : 0);
        return getWritableDatabase().update(TABLE, cv, "student_id = ?", new String[]{studentId}) > 0;
    }

    // DELETE
    public boolean deleteStudent(String studentId) {
        return getWritableDatabase().delete(TABLE, "student_id = ?", new String[]{studentId}) > 0;
    }

    // VIEW
    public ArrayList<Student> getAllStudents() {
        ArrayList<Student> list = new ArrayList<>();
        Cursor c = getReadableDatabase().rawQuery(
                "SELECT student_id, name, enrolled FROM " + TABLE + " ORDER BY name", null);
        while (c.moveToNext()) {
            list.add(new Student(c.getString(0), c.getString(1), c.getInt(2) == 1));
        }
        c.close();
        return list;
    }
}
