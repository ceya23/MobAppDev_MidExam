package com.example.midexam;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {

    private final Activity activity;
    private final List<StudentModel> studentList;
    private final DBHelper dbHelper;

    public StudentAdapter(Activity activity, List<StudentModel> studentList,
                          DBHelper dbHelper) {
        this.activity = activity;
        this.studentList = studentList;
        this.dbHelper = dbHelper;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_item_list, parent, false);
        return new StudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        StudentModel student = studentList.get(position);

        holder.textViewStudentName.setText(student.getStudentName());
        holder.textViewStudentId.setText("Student ID: " + student.getStudentId());
        holder.textViewEnrolled.setText(
                "Has Enrolled: " + (student.isEnrolled() ? "Yes" : "No"));

        holder.btnEdit.setOnClickListener(v -> showEditDialog(student));

        holder.btnDelete.setOnClickListener(v -> {
            new AlertDialog.Builder(activity)
                    .setTitle("Delete Student")
                    .setMessage("Are you sure you want to delete this student?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        boolean deleted = dbHelper.deleteStudent(student.getId());

                        if (deleted) {
                            int currentPosition = holder.getBindingAdapterPosition();

                            if (currentPosition != RecyclerView.NO_POSITION) {
                                studentList.remove(currentPosition);
                                notifyItemRemoved(currentPosition);
                            }

                            Toast.makeText(activity,
                                    "Student deleted successfully",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(activity,
                                    "Failed to delete student",
                                    Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    private void showEditDialog(StudentModel student) {
        LinearLayout layout = new LinearLayout(activity);
        layout.setOrientation(LinearLayout.VERTICAL);
        int padding = (int) (20 * activity.getResources()
                .getDisplayMetrics().density);
        layout.setPadding(padding, padding, padding, 0);

        EditText editName = new EditText(activity);
        editName.setHint("Student Name");
        editName.setSingleLine(true);
        editName.setText(student.getStudentName());
        layout.addView(editName);

        EditText editStudentId = new EditText(activity);
        editStudentId.setHint("Student ID");
        editStudentId.setSingleLine(true);
        editStudentId.setText(student.getStudentId());
        layout.addView(editStudentId);

        CheckBox checkBoxEnrolled = new CheckBox(activity);
        checkBoxEnrolled.setText("Has Enrolled");
        checkBoxEnrolled.setChecked(student.isEnrolled());
        layout.addView(checkBoxEnrolled);

        new AlertDialog.Builder(activity)
                .setTitle("Edit Student")
                .setView(layout)
                .setPositiveButton("SAVE", (dialog, which) -> {
                    String name = editName.getText().toString().trim();
                    String studentId = editStudentId.getText().toString().trim();
                    boolean enrolled = checkBoxEnrolled.isChecked();

                    if (name.isEmpty() || studentId.isEmpty()) {
                        Toast.makeText(activity,
                                "Name and Student ID cannot be empty",
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    StudentModel updatedStudent = new StudentModel(
                            student.getId(), name, studentId, enrolled);

                    if (dbHelper.updateStudent(updatedStudent)) {
                        int position = studentList.indexOf(student);

                        if (position != -1) {
                            studentList.set(position, updatedStudent);
                            notifyItemChanged(position);
                        }

                        Toast.makeText(activity,
                                "Student updated successfully",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(activity,
                                "Update failed. Student ID may already exist.",
                                Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("CANCEL", null)
                .show();
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    static class StudentViewHolder extends RecyclerView.ViewHolder {
        TextView textViewStudentName, textViewStudentId, textViewEnrolled;
        Button btnEdit, btnDelete;

        public StudentViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewStudentName = itemView.findViewById(R.id.textViewStudentName);
            textViewStudentId = itemView.findViewById(R.id.textViewStudentId);
            textViewEnrolled = itemView.findViewById(R.id.textViewEnrolled);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
