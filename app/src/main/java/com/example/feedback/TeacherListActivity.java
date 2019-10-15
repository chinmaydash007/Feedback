package com.example.feedback;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedback.Adapter.TeacherDatailsAdapter;
import com.example.feedback.Model.TeacherDetails;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class TeacherListActivity extends AppCompatActivity implements View.OnLongClickListener, View.OnClickListener {
    RecyclerView recyclerView;
    TeacherDatailsAdapter teacherDatailsAdapter;
    DatabaseReference teacherRef;
    BottomAppBar bottomAppBar;
    FloatingActionButton floatingActionButton;

    public boolean is_in_action_mode = false;
    TextView counter_text;
    ImageView unselect_all_item;

    int counter = 0;
    ArrayList<TeacherDetails> teacherDetailsArrayList = new ArrayList<>();

    ArrayList<TeacherDetails> selection_list = new ArrayList<>();

    DatabaseReference feedbackRef;

    String student_index;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_list);
        recyclerView = findViewById(R.id.teacher_recyclerview);
        bottomAppBar = findViewById(R.id.bottomAppBar);
        floatingActionButton = findViewById(R.id.fab);


        counter_text = findViewById(R.id.counter_text);
        unselect_all_item = findViewById(R.id.unselect_all_items);

        unselect_all_item.setVisibility(View.INVISIBLE);
        counter_text.setVisibility(View.GONE);
        floatingActionButton.setVisibility(View.GONE);
        bottomAppBar.setVisibility(View.GONE);

        student_index=getIntent().getExtras().getString("student_index");


        teacherRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Teacher");
        feedbackRef=FirebaseDatabase.getInstance().getReference().child("Feedback");

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setHasFixedSize(true);
        teacherDetailsArrayList.clear();

        teacherDatailsAdapter = new TeacherDatailsAdapter(teacherDetailsArrayList, this);


        recyclerView.setAdapter(teacherDatailsAdapter);

        teacherRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        TeacherDetails teacherDetails = dataSnapshot1.getValue(TeacherDetails.class);
                        teacherDetailsArrayList.add(teacherDetails);

                    }

                }
                teacherDatailsAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        unselect_all_item.setOnClickListener(this);



    }


    @Override
    public boolean onLongClick(View view) {
        is_in_action_mode = true;
        unselect_all_item.setVisibility(View.VISIBLE);
        counter_text.setVisibility(View.VISIBLE);
        //floatingActionButton.setVisibility(View.VISIBLE);
        bottomAppBar.setVisibility(View.VISIBLE);
        teacherDatailsAdapter.notifyDataSetChanged();
        return true;

    }

    public void prepareSelection(View view, int position) {
        if (((CheckBox) view).isChecked()) {
            selection_list.add(teacherDetailsArrayList.get(position));
            counter++;
            updateCounter(counter);
            if(counter>5){
                floatingActionButton.setVisibility(View.VISIBLE);
                floatingActionButton.setOnClickListener(this);
            }
        } else {
            selection_list.remove(teacherDetailsArrayList.get(position));
            counter--;
            updateCounter(counter);
        }
        if(counter>5){
            floatingActionButton.setVisibility(View.VISIBLE);
            floatingActionButton.setOnClickListener(this);
        }
        else{
            floatingActionButton.setVisibility(View.GONE);

        }
    }

    public void updateCounter(int counter) {
        if (counter == 0) {
            counter_text.setText("0 item selected");
        } else {
            counter_text.setText(counter + " item selected");
        }
    }

    @Override
    public void onBackPressed() {
        if (is_in_action_mode) {
            clearActionMode();
        }
        else{
            super.onBackPressed();

        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.unselect_all_items) {
           clearActionMode();
        }
        if(view.getId()==R.id.fab){
            uploadTeacherFeedbackList();
        }
    }

    private void uploadTeacherFeedbackList() {

        HashMap<String,Object> hashMap=new HashMap<>();

        for(TeacherDetails teacherDetails:selection_list){
                hashMap.put(teacherDetails.getUid(),"0");
            Log.d("test", teacherDetails.getUid());
        }

        feedbackRef.child(student_index).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(TeacherListActivity.this, "Feedback setup successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TeacherListActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    void clearActionMode(){
        is_in_action_mode = false;
        teacherDatailsAdapter.notifyDataSetChanged();
        unselect_all_item.setVisibility(View.GONE);
        counter_text.setVisibility(View.GONE);
        floatingActionButton.setVisibility(View.GONE);
        bottomAppBar.setVisibility(View.GONE);
        counter = 0;
        selection_list.clear();

    }
}
