package com.example.chatterbot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.chatterbot.model.data.BackupItem;
import com.example.chatterbot.view.adapter.BackupAdapter;
import com.example.chatterbot.view.model.MainViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class BackupActivity extends AppCompatActivity {

    private static final String TAG = "xzy";
    private MainViewModel mainViewModel;
    private RecyclerView recyclerView;
    private BackupAdapter backupAdapter;

    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup);
        initComponents();
    }

    private void initComponents() {

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();

        BackupItem backupItem = new BackupItem("sdfkf");
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        recyclerView = findViewById(R.id.recyclerViewBackup);
        backupAdapter = mainViewModel.getBackupAdapter();
        recyclerView.setAdapter(backupAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.smoothScrollToPosition(backupAdapter.getItemCount());

        BackupItem backupItem1 = new BackupItem("jdsfg");
        BackupItem backupItem2 = new BackupItem("jdsfgsadsadsad");
        backupAdapter.addMessage(backupItem1);
        backupAdapter.addMessage(backupItem2);

        leerDatos();

    }

    private void leerDatos() {
        Query query = databaseReference.child("item");
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                BackupItem item = dataSnapshot.getValue(BackupItem.class);
                Log.v(TAG, "ITEM: "+item.toString());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
