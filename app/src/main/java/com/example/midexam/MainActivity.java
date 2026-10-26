package com.example.midexam;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    EditText editTextStudentName, editTextStudentId;
    CheckBox checkBoxEnrolled;
    Button btnAdd, btnView;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextStudentName = findViewById(R.id.editTextStudentName);
        editTextStudentId = findViewById(R.id.editTextStudentId);
        checkBoxEnrolled = findViewById(R.id.checkBoxEnrolled);
        btnAdd = findViewById(R.id.btnAdd);
        btnView = findViewById(R.id.btnView);

        dbHelper = new DBHelper(this);

        btnAdd.setOnClickListener(v -> {
            String name = editTextStudentName.getText().toString().trim();
            String studentId = editTextStudentId.getText().toString().trim();
            boolean enrolled = checkBoxEnrolled.isChecked();

            if (name.isEmpty() || studentId.isEmpty()) {
                Toast.makeText(this,
                        "Please enter Student Name and Student ID",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            StudentModel student =
                    new StudentModel(name, studentId, enrolled);

            boolean success = dbHelper.addStudent(student);

            if (success) {
                Toast.makeText(this,
                        "Student added successfully",
                        Toast.LENGTH_SHORT).show();

                editTextStudentName.setText("");
                editTextStudentId.setText("");
                checkBoxEnrolled.setChecked(false);
            } else {
                Toast.makeText(this,
                        "Failed to add student. Student ID may already exist.",
                        Toast.LENGTH_LONG).show();
            }
        });

        btnView.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, ViewStudent.class);
            startActivity(intent);
        });
    }
}
