package com.example.feedback;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.example.feedback.Adapter.TeacherDatailsAdapter;
import com.example.feedback.Model.TeacherDetails;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TeacherListActivity extends AppCompatActivity {
RecyclerView recyclerView;
TeacherDatailsAdapter teacherDatailsAdapter;
DatabaseReference teacherRef;
BottomAppBar bottomAppBar;
FloatingActionButton floatingActionButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list);
        recyclerView=findViewById(R.id.teacher_recyclerview);
        bottomAppBar=findViewById(R.id.bottomAppBar);
        floatingActionButton=findViewById(R.id.fab);
//        floatingActionButton.setVisibility(View.GONE);



        teacherRef= FirebaseDatabase.getInstance().getReference().child("Users").child("Teacher");



        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);

        FirebaseRecyclerOptions<TeacherDetails> options=new FirebaseRecyclerOptions.Builder<TeacherDetails>()
                .setQuery(teacherRef,TeacherDetails.class)
                .build();

        teacherDatailsAdapter=new TeacherDatailsAdapter(options,this);
        recyclerView.setAdapter(teacherDatailsAdapter);



    }

    @Override
    protected void onStart() {
        super.onStart();
        teacherDatailsAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        teacherDatailsAdapter.stopListening();
    }
}
