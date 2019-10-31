package com.example.feedback;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.feedback.Adapter.FeedbackResultOverallAdapter;
import com.example.feedback.Model.FeedbackresultAvailable;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FeedbackResultOverall extends AppCompatActivity {
RecyclerView recyclerView;
List<FeedbackresultAvailable> availableList;
DatabaseReference feedbackRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_result_overall);
        recyclerView=findViewById(R.id.feedbackResultOverallRecyclerView);
        availableList=new ArrayList<>();

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        final FeedbackResultOverallAdapter adapter=new FeedbackResultOverallAdapter(availableList,this);
        recyclerView.setAdapter(adapter);

        feedbackRef= FirebaseDatabase.getInstance().getReference().child("Feedback");
        feedbackRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                        String available=dataSnapshot1.getKey();
                        String batch=available.substring(0,4);
                        String branch=available.substring(5,8);
                        String section=available.substring(9);
                        availableList.add(new FeedbackresultAvailable(batch,branch,section));
                        adapter.notifyDataSetChanged();

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        



    }
}
