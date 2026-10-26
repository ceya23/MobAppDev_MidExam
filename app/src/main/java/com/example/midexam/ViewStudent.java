package com.example.midexam;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ViewStudent extends Activity {

    private RecyclerView recyclerViewStudents;
    private DBHelper dbHelper;
    private StudentAdapter studentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_student);

        recyclerViewStudents = findViewById(R.id.recyclerViewStudents);
        recyclerViewStudents.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DBHelper(this);
        loadStudents();
    }

    private void loadStudents() {
        List<StudentModel> studentList = dbHelper.getStudentList();

        studentAdapter = new StudentAdapter(this, studentList, dbHelper);
        recyclerViewStudents.setAdapter(studentAdapter);

        if (studentList.isEmpty()) {
            Toast.makeText(this,
                    "No students found",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
