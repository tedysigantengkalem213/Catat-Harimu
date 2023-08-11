package com.teddy.catatharimu;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

// Eddy Rochman
// 10120052
// IF-2
public class AddNoteActivity extends AppCompatActivity {

    private EditText titleEditText, categoryEditText, noteEditText;
    private Button saveButton, backButton;
    Date dt = new Date();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        titleEditText = findViewById(R.id.titleEditText);
        categoryEditText = findViewById(R.id.categoryEditText);
        noteEditText = findViewById(R.id.noteEditText);
        saveButton = findViewById(R.id.addButton);
        backButton = findViewById(R.id.backButton);

        saveButton.setOnClickListener(view -> saveNote());

        backButton.setOnClickListener(view -> {
            finish();
        });
    }

    private void saveNote() {
        String title = titleEditText.getText().toString();
        String category = categoryEditText.getText().toString();
        String note = noteEditText.getText().toString();
        String tanggal = new SimpleDateFormat("dd-MMM-yyyy").format(dt);
        String waktu = new SimpleDateFormat("HH:mm a").format(dt);

        String noteId = FirebaseDatabase.getInstance().getReference().child("note").push().getKey();

        FirebaseHelper firebaseHelper = new FirebaseHelper();
        firebaseHelper.addNote(noteId, tanggal, waktu, title, note, category);
        Toast.makeText(this, "Berhasil Buat Note!", Toast.LENGTH_LONG).show();

        finish();
    }
}


