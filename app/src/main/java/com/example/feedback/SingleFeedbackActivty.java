package com.example.feedback;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.feedback.Model.TeacherDetails;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.hsalf.smilerating.BaseRating;
import com.hsalf.smilerating.SmileRating;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class SingleFeedbackActivty extends AppCompatActivity implements View.OnClickListener {
    CircleImageView teacher_profile_image;
    SmileRating param1, param2, param3, param4;
    Button submit_btn;
    String teacher_uid = null;
    DatabaseReference teacherRef,student_feedback_ref;
    TeacherDetails teacherDetails = null;
    FirebaseUser firebaseUser;
    DatabaseReference studentFeedbackRef;

    String TAG="test1";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_feedback);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        teacher_uid = getIntent().getStringExtra("teacher_uid");

        studentFeedbackRef= FirebaseDatabase.getInstance().getReference().child("StudentFeedback").child(FirebaseAuth.getInstance().getCurrentUser().getUid());


        teacherRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Teacher");
        student_feedback_ref=FirebaseDatabase.getInstance().getReference().child("StudentFeedback").child(firebaseUser.getUid());
        teacherRef.child(teacher_uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    teacherDetails = dataSnapshot.getValue(TeacherDetails.class);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        teacher_profile_image = findViewById(R.id.teacher_profile_image);
        param1 = findViewById(R.id.smileRating1);
        param2 = findViewById(R.id.smileRating2);
        param3 = findViewById(R.id.smileRating3);
        param4 = findViewById(R.id.smileRating4);
        submit_btn = findViewById(R.id.submit_btn);
        submit_btn.setOnClickListener(this);

        student_feedback_ref.child(teacher_uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){

                        String key=dataSnapshot1.getKey();
                        int value=Integer.parseInt(dataSnapshot1.getValue().toString());
                        Log.d(TAG,key+" "+value);

                        switch (key){
                            case "param1":
                                if(value==5){
                                    param1.setSelectedSmile(BaseRating.GREAT,true);
                                }
                                else if(value==4){
                                    param1.setSelectedSmile(BaseRating.GOOD,true);
                                }
                                else if(value==3){
                                    param1.setSelectedSmile(BaseRating.OKAY,true);
                                }
                                else if(value==2){
                                    param1.setSelectedSmile(BaseRating.BAD,true);
                                }
                                else if(value==1){
                                    param1.setSelectedSmile(BaseRating.TERRIBLE,true);
                                }
                                else{
                                    param1.setSelectedSmile(BaseRating.NONE,true);
                                }

                            case "param2":
                                if(value==5){
                                    param2.setSelectedSmile(BaseRating.GREAT,true);
                                }
                                else if(value==4){
                                    param2.setSelectedSmile(BaseRating.GOOD,true);
                                }
                                else if(value==3){
                                    param2.setSelectedSmile(BaseRating.OKAY,true);
                                }
                                else if(value==2){
                                    param2.setSelectedSmile(BaseRating.BAD,true);
                                }
                                else if(value==1){
                                    param2.setSelectedSmile(BaseRating.TERRIBLE,true);
                                }
                                else{
                                    param2.setSelectedSmile(BaseRating.NONE,true);
                                }

                            case "param3":
                                if(value==5){
                                    param3.setSelectedSmile(BaseRating.GREAT,true);
                                }
                                else if(value==4){
                                    param3.setSelectedSmile(BaseRating.GOOD,true);
                                }
                                else if(value==3){
                                    param3.setSelectedSmile(BaseRating.OKAY,true);
                                }
                                else if(value==2){
                                    param3.setSelectedSmile(BaseRating.BAD,true);
                                }
                                else if(value==1){
                                    param3.setSelectedSmile(BaseRating.TERRIBLE,true);
                                }
                                else{
                                    param3.setSelectedSmile(BaseRating.NONE,true);
                                }

                            case "param4":
                                if(value==5){
                                    param4.setSelectedSmile(BaseRating.GREAT,true);
                                }
                                else if(value==4){
                                    param4.setSelectedSmile(BaseRating.GOOD,true);
                                }
                                else if(value==3){
                                    param4.setSelectedSmile(BaseRating.OKAY,true);
                                }
                                else if(value==2){
                                    param4.setSelectedSmile(BaseRating.BAD,true);
                                }
                                else if(value==1){
                                    param4.setSelectedSmile(BaseRating.TERRIBLE,true);
                                }
                                else{
                                    param4.setSelectedSmile(BaseRating.NONE,true);
                                }
                        }

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.submit_btn) {
           if(param1.getRating()==0 || param2.getRating()==0 || param3.getRating()==0 || param4.getRating()==0){
               Toast.makeText(this, "Please select rating ", Toast.LENGTH_SHORT).show();
           }
           else{
               submitTeacherRating();

           }
        }
    }

    private void submitTeacherRating() {
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("param1",param1.getRating());
        hashMap.put("param2",param2.getRating());
        hashMap.put("param3",param3.getRating());
        hashMap.put("param4",param4.getRating());

        student_feedback_ref.child(teacher_uid).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(SingleFeedbackActivty.this, "Successfully Uploaded", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });




    }


}
