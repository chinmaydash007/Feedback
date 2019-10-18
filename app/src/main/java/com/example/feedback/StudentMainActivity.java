package com.example.feedback;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedback.Adapter.StudentMainPageFeedbackAdapter;
import com.example.feedback.Model.TeacherDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentMainActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    Toolbar toolbar;
    DatabaseReference studentRef;
    DatabaseReference feedbackRef;
    DatabaseReference teacherRef;

    String stduent_index;
    ArrayList<String> teacherinsidefeedbacklist;
    ArrayList<TeacherDetails> teacherDetailsArrayList;
    StudentMainPageFeedbackAdapter studentMainPageFeedbackAdapter;
    RecyclerView recyclerView;
    Button entertoSubmit;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_main);
        firebaseAuth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        entertoSubmit=findViewById(R.id.btn_submit);



        teacherinsidefeedbacklist = new ArrayList<>();
        teacherDetailsArrayList=new ArrayList<>();
        recyclerView=findViewById(R.id.recyclerView);
        studentMainPageFeedbackAdapter=new StudentMainPageFeedbackAdapter(this,teacherDetailsArrayList);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(studentMainPageFeedbackAdapter);


        entertoSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(StudentMainActivity.this,FeedbackActivtiy.class);
                intent.putParcelableArrayListExtra("teacherdata",(ArrayList)teacherDetailsArrayList);


                startActivity(intent);

            }
        });




        if (firebaseAuth.getCurrentUser() == null) {
            Intent intent = new Intent(StudentMainActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        studentRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Student");
        teacherRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Teacher");
        feedbackRef = FirebaseDatabase.getInstance().getReference().child("Feedback");


        studentRef.child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.hasChild("student_full_name")) {
                    Intent intent = new Intent(StudentMainActivity.this, Student_profile_activity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    fetchStudentIndex();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void fetchStudentIndex() {
        studentRef.child(firebaseAuth.getCurrentUser().getUid()).child("student_index").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    stduent_index = "" + dataSnapshot.getValue();
                    fetchTeacherChoosenforFeedback(stduent_index);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void fetchTeacherChoosenforFeedback(String stduent_index) {
        teacherinsidefeedbacklist.clear();
        feedbackRef.child(stduent_index).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        teacherinsidefeedbacklist.add(dataSnapshot1.getKey());


                    }
                }
                addTeacherDetailstoTeacherDetailsArrayList(teacherinsidefeedbacklist);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addTeacherDetailstoTeacherDetailsArrayList(final ArrayList<String> teacherinsidefeedbacklist) {
        teacherDetailsArrayList.clear();
        teacherRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                    TeacherDetails teacherDetails=dataSnapshot1.getValue(TeacherDetails.class);
                    if(teacherinsidefeedbacklist.contains(teacherDetails.getUid())){
                        teacherDetailsArrayList.add(teacherDetails);

                    }

                }
                studentMainPageFeedbackAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.student_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
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
        Intent intent = new Intent(StudentMainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void openStudentProfile() {
        Intent intent = new Intent(StudentMainActivity.this, Student_profile_activity.class);
        startActivity(intent);
    }
}
