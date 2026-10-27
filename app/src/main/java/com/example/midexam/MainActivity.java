package com.example.midexam;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText etName, etId;
    private CheckBox cbEnrolled;
    private Button btnAdd, btnEdit, btnDelete, btnView;
    private ListView listView;

    private DatabaseHelper dbHelper;
    private final List<Student> students = new ArrayList<>();
    private ArrayAdapter<Student> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etName = findViewById(R.id.etName);
        etId = findViewById(R.id.etStudentId);
        cbEnrolled = findViewById(R.id.cbEnrolled);
        btnAdd = findViewById(R.id.btnAdd);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        btnView = findViewById(R.id.btnView);
        listView = findViewById(R.id.lvStudents);

        dbHelper = new DatabaseHelper(this);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, students);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener((parent, view, position, id) -> {
            Student s = students.get(position);
            etName.setText(s.getName());
            etId.setText(s.getId());
            cbEnrolled.setChecked(s.isEnrolled());
        });

        btnAdd.setOnClickListener(v -> addStudent());
        btnEdit.setOnClickListener(v -> editStudent());
        btnDelete.setOnClickListener(v -> deleteStudent());
        btnView.setOnClickListener(v -> loadData());

        loadData();
    }

    private void addStudent() {
        String name = etName.getText().toString().trim();
        String id = etId.getText().toString().trim();
        if (!validate(name, id)) return;

        if (dbHelper.insertStudent(id, name, cbEnrolled.isChecked())) {
            Toast.makeText(this, "Student added", Toast.LENGTH_SHORT).show();
            clearForm();
            loadData();
        } else {
            Toast.makeText(this, "Student ID already exists", Toast.LENGTH_SHORT).show();
        }
    }

    private void editStudent() {
        String name = etName.getText().toString().trim();
        String id = etId.getText().toString().trim();
        if (!validate(name, id)) return;

        // Student ID is the key, so it identifies which record gets updated
        if (dbHelper.updateStudent(id, name, cbEnrolled.isChecked())) {
            Toast.makeText(this, "Student updated", Toast.LENGTH_SHORT).show();
            clearForm();
            loadData();
        } else {
            Toast.makeText(this, "Student ID not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteStudent() {
        String id = etId.getText().toString().trim();
        if (id.isEmpty()) {
            etId.setError("Student ID is required");
            return;
        }

        if (dbHelper.deleteStudent(id)) {
            Toast.makeText(this, "Student deleted", Toast.LENGTH_SHORT).show();
            clearForm();
            loadData();
        } else {
            Toast.makeText(this, "Student ID not found", Toast.LENGTH_SHORT).show();
        }
    }

    private void loadData() {
        students.clear();
        students.addAll(dbHelper.getAllStudents());
        adapter.notifyDataSetChanged();
    }

    private boolean validate(String name, String id) {
        if (name.isEmpty()) {
            etName.setError("Student name is required");
            return false;
        }
        if (id.isEmpty()) {
            etId.setError("Student ID is required");
            return false;
        }
        return true;
    }

    private void clearForm() {
        etName.setText("");
        etId.setText("");
        cbEnrolled.setChecked(false);
        etName.requestFocus();
    }
}