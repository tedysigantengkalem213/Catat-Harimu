package com.teddy.catatharimu;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

// Eddy Rochman
// 10120052
// IF-2
public class UpdateNoteActivity extends AppCompatActivity {

    private EditText updatedTitleEditText, updatedCategoryEditText, updatedNoteEditText;
    private Button updateNote, deleteNote, backButton;
    private FirebaseHelper firebaseHelper;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_note);

        updatedTitleEditText = findViewById(R.id.updatedTitleEditText);
        updatedCategoryEditText = findViewById(R.id.updatedCategoryEditText);
        updatedNoteEditText = findViewById(R.id.updatedNoteEditText);
        updateNote = findViewById(R.id.updateNote);
        deleteNote = findViewById(R.id.deleteNote);
        backButton = findViewById(R.id.backButton);

        firebaseHelper = new FirebaseHelper();

        String noteId = getIntent().getStringExtra("noteId");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();

            databaseReference = FirebaseDatabase.getInstance().getReference();
            DatabaseReference noteRef = databaseReference.child("note").child(userId).child(noteId);

            noteRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        CatatanModel note = dataSnapshot.getValue(CatatanModel.class);
                        if (note != null) {
                            updatedTitleEditText.setText(note.getJudul());
                            updatedCategoryEditText.setText(note.getKategori());
                            updatedNoteEditText.setText(note.getIsi());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database read error if needed
                }
            });

            updateNote.setOnClickListener(view -> {
                String updatedTitle = updatedTitleEditText.getText().toString();
                String updatedCategory = updatedCategoryEditText.getText().toString();
                String updatedNote = updatedNoteEditText.getText().toString();

                firebaseHelper.updateNote(noteId, updatedTitle, updatedCategory, updatedNote);

                Toast.makeText(this, "Catatan Diperbarui!", Toast.LENGTH_SHORT).show();
                finish();
            });

            deleteNote.setOnClickListener(view -> {
                firebaseHelper.deleteNote(noteId);

                Toast.makeText(this, "Catatan Dihapus!", Toast.LENGTH_SHORT).show();
                finish();
            });

            backButton.setOnClickListener(view -> {
                finish();
            });
        }
    }

}

