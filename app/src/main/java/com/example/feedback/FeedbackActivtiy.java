package com.example.feedback;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.feedback.Adapter.TeacherFeedbackAdapter;
import com.example.feedback.Model.TeacherDetails;

import java.util.ArrayList;

public class FeedbackActivtiy extends AppCompatActivity {
ArrayList<TeacherDetails> teacherDetailsArrayList;
RecyclerView recyclerView;
TeacherFeedbackAdapter teacherFeedbackAdapter;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_activtiy);
        teacherDetailsArrayList=new ArrayList<>();
        recyclerView=findViewById(R.id.recyclerView);

        teacherDetailsArrayList=getIntent().getParcelableArrayListExtra("teacherdata");





        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        linearLayoutManager.setSmoothScrollbarEnabled(true);

        recyclerView.setLayoutManager(linearLayoutManager);


        teacherFeedbackAdapter=new TeacherFeedbackAdapter(this,teacherDetailsArrayList);
        recyclerView.setAdapter(teacherFeedbackAdapter);





    }
}
