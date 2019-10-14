package com.example.feedback;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentMainActivity extends AppCompatActivity {
FirebaseAuth firebaseAuth;
Toolbar toolbar;
DatabaseReference studentRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);
        firebaseAuth=FirebaseAuth.getInstance();
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        if(firebaseAuth.getCurrentUser()==null){
            Intent intent=new Intent(StudentMainActivity.this,LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        studentRef= FirebaseDatabase.getInstance().getReference().child("Users").child("Student");
        studentRef.child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.hasChild("student_full_name")){
                    Intent intent=new Intent(StudentMainActivity.this,Student_profile_activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.student_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                logout();
                return true;
            case R.id.student_edit_profile:
                openStudentProfile();
                return true;

        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {

        firebaseAuth.signOut();
        Intent intent=new Intent(StudentMainActivity.this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    private void openStudentProfile(){
        Intent intent=new Intent(StudentMainActivity.this,Student_profile_activity.class);
        startActivity(intent);
    }
}
