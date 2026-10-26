package com.example.midexam;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "student_db";
    private static final int DB_VERSION = 1;

    private static final String TABLE_STUDENT = "student";
    private static final String COL_ID = "id";
    private static final String COL_NAME = "student_name";
    private static final String COL_STUDENT_ID = "student_id";
    private static final String COL_ENROLLED = "enrolled";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_STUDENT + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_NAME + " TEXT NOT NULL, " +
                COL_STUDENT_ID + " TEXT NOT NULL UNIQUE, " +
                COL_ENROLLED + " INTEGER NOT NULL DEFAULT 0)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENT);
        onCreate(db);
    }

    public boolean addStudent(StudentModel student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_NAME, student.getStudentName());
        values.put(COL_STUDENT_ID, student.getStudentId());
        values.put(COL_ENROLLED, student.isEnrolled() ? 1 : 0);

        long result = db.insert(TABLE_STUDENT, null, values);
        return result != -1;
    }

    public List<StudentModel> getStudentList() {
        List<StudentModel> studentList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(
                TABLE_STUDENT,
                null,
                null,
                null,
                null,
                null,
                COL_ID + " DESC"
        );

        try {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COL_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COL_NAME));
                String studentId = cursor.getString(
                        cursor.getColumnIndexOrThrow(COL_STUDENT_ID));
                boolean enrolled = cursor.getInt(
                        cursor.getColumnIndexOrThrow(COL_ENROLLED)) == 1;

                studentList.add(new StudentModel(id, name, studentId, enrolled));
            }
        } finally {
            cursor.close();
        }

        return studentList;
    }

    public boolean updateStudent(StudentModel student) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COL_NAME, student.getStudentName());
        values.put(COL_STUDENT_ID, student.getStudentId());
        values.put(COL_ENROLLED, student.isEnrolled() ? 1 : 0);

        int result = db.update(
                TABLE_STUDENT,
                values,
                COL_ID + " = ?",
                new String[]{String.valueOf(student.getId())}
        );

        return result > 0;
    }

    public boolean deleteStudent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();

        int result = db.delete(
                TABLE_STUDENT,
                COL_ID + " = ?",
                new String[]{String.valueOf(id)}
        );

        return result > 0;
    }
}