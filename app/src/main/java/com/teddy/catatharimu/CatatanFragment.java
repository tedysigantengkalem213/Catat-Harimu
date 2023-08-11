package com.teddy.catatharimu;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

// Eddy Rochman
// 10120052
// IF-2
public class CatatanFragment extends Fragment {
    Button buttonAddNote;
    Date dt = new Date();
    private RecyclerView listCatatan;
    private DatabaseReference databaseReference;
    private FirebaseHelper firebaseHelper;

    public CatatanFragment() {
        // Initialize Firebase components
        databaseReference = FirebaseDatabase.getInstance().getReference();
        firebaseHelper = new FirebaseHelper();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_catatan, container, false);

        initializeViews(rootView);
        setButtonClickListener();
        setupRecyclerView();

        RefreshListNote();

        return rootView;
    }

    private void initializeViews(View rootView) {
        buttonAddNote = rootView.findViewById(R.id.buttonAddNote);
        listCatatan = rootView.findViewById(R.id.listCatatan);
    }

    private void setButtonClickListener() {
        buttonAddNote.setOnClickListener(view -> {
            Intent intent = new Intent(requireContext(), AddNoteActivity.class);
            startActivity(intent);
        });
    }

    private void setupRecyclerView() {
        MyAdapter adapter = new MyAdapter(new ArrayList<>(), this);
        listCatatan.setLayoutManager(new LinearLayoutManager(getActivity()));
        listCatatan.setAdapter(adapter);
    }

    public void RefreshListNote() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userId = user.getUid();
            DatabaseReference noteRef = databaseReference.child("note").child(userId);

            noteRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<CatatanModel> notes = new ArrayList<>();
                    for (DataSnapshot noteSnapshot : dataSnapshot.getChildren()) {
                        CatatanModel note = noteSnapshot.getValue(CatatanModel.class);
                        notes.add(note);
                    }

                    MyAdapter adapter = new MyAdapter(notes, CatatanFragment.this);
                    listCatatan.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle the error, if any
                }
            });
        }
    }

    public static class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
        private List<CatatanModel> notes;
        private CatatanFragment fragment;

        public MyAdapter(List<CatatanModel> notes, CatatanFragment fragment) {
            this.notes = notes;
            this.fragment = fragment;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            CatatanModel note = notes.get(position);

            holder.noteTitle.setText(note.judul);
            holder.noteKategori.setText(note.kategori);
            holder.noteCatatan.setText(note.isi);
        }

        @Override
        public int getItemCount() {
            return notes.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView noteTitle;
            TextView noteKategori;
            TextView noteCatatan;

            public ViewHolder(View itemView) {
                super(itemView);
                noteTitle = itemView.findViewById(R.id.titleTextView);
                noteKategori = itemView.findViewById(R.id.kategoriTextView);
                noteCatatan = itemView.findViewById(R.id.catatanTextView);

                itemView.setOnClickListener(view -> {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        CatatanModel clickedNote = notes.get(position);
                        Intent intent = new Intent(view.getContext(), UpdateNoteActivity.class);
                        intent.putExtra("noteId", clickedNote.getId());
                        view.getContext().startActivity(intent);
                    }
                });

            }
        }
    }

}

