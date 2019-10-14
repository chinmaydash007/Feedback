package com.example.feedback;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedback.Adapter.StudentDetailsAdapter;
import com.example.feedback.Model.StudentDetails;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ShowEligibleStudentForFeedback extends AppCompatActivity {

    String student_batch;
    String student_branch;
    String student_section;
    DatabaseReference studentRef;
    RecyclerView recyclerView;
    StudentDetailsAdapter studentDetailsAdapter;


    FloatingActionButton floatingActionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_eligible_student_for_feedback);
        floatingActionButton=findViewById(R.id.fab);

        final Intent intent = getIntent();
        student_batch = intent.getExtras().getString("student_batch").substring(0,4);
        student_branch = intent.getExtras().getString("student_branch");
        student_section = intent.getExtras().getString("student_section");
        studentRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Student");


        String student_index=student_batch+"_"+student_branch+"_"+student_section;
        Query query=studentRef.orderByChild("student_index").equalTo(student_index);


        recyclerView = findViewById(R.id.student_details_recycler_view);

        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(gridLayoutManager);
        Toast.makeText(this,
                student_batch+" "+student_branch+" "+student_section, Toast.LENGTH_SHORT).show();


        FirebaseRecyclerOptions<StudentDetails> options = new FirebaseRecyclerOptions.Builder<StudentDetails>()
                .setQuery(query, StudentDetails.class).build();
        studentDetailsAdapter = new StudentDetailsAdapter(options, this);
        recyclerView.setAdapter(studentDetailsAdapter);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(ShowEligibleStudentForFeedback.this,TeacherListActivity.class);
                startActivity(intent1);

            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        studentDetailsAdapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        studentDetailsAdapter.stopListening();
    }
}
