package com.example.feedback;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.feedback.Adapter.FinalFeedbackResultWithViewpgerAdapter;
import com.example.feedback.Model.FeedbackParams;
import com.example.feedback.Model.FeedbackWIthTeacherUidAndParams;
import com.example.feedback.Model.StudentDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FeedbackFinalResultActivity extends AppCompatActivity {
    String myclass = "";
    DatabaseReference studentRef, teacherRef, feedbackRef, totalFeedbackRef;
    List<StudentDetails> studentDetailsList;
    List<String> studentSubmittedFeedbackList, teacherList;
    int totalStudentInTheClass, totalStudentSubmitted;

    List<FeedbackWIthTeacherUidAndParams> feedbackWIthTeacherUidAndParamsList;
    List<FeedbackWIthTeacherUidAndParams> finalfeedbackWIthTeacherUidAndParamsList;


    Long param1 = 0L, param2 = 0L, param3 = 0L, param4 = 0L;

    ViewPager2 viewPager;
    FinalFeedbackResultWithViewpgerAdapter finalFeedbackResultWithViewpgerAdapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_final_result);

        viewPager=findViewById(R.id.viewpager);
        viewPager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPager.setPageTransformer(new DepthPageTransformer());

        myclass = getIntent().getExtras().getString("class");
        Toast.makeText(this, myclass, Toast.LENGTH_SHORT).show();

        studentDetailsList = new ArrayList<>();
        studentSubmittedFeedbackList = new ArrayList<>();
        teacherList = new ArrayList<>();
        feedbackWIthTeacherUidAndParamsList = new ArrayList<>();
        finalfeedbackWIthTeacherUidAndParamsList = new ArrayList<>();


        studentRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Student");
        teacherRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Teacher");
        feedbackRef = FirebaseDatabase.getInstance().getReference().child("StudentFeedback");
        totalFeedbackRef = FirebaseDatabase.getInstance().getReference().child("Feedback");


        FetchAllStudent();

        FetchTeacherListOfClass();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                calcualteAllResult();
            }
        }, 5000);

    }

    // It fetch all the teachers from the "Feedback" node and put it inside a list.
    public void FetchTeacherListOfClass() {
        totalFeedbackRef.child(myclass).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        teacherList.add(dataSnapshot1.getKey());
                        Log.d("yahoo", dataSnapshot1.getKey());
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    //It fetch all the student from Users-->Student node and put it inside an arrayList.
    public void FetchAllStudent() {
        studentRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        StudentDetails studentDetails = dataSnapshot1.getValue(StudentDetails.class);
                        if (studentDetails.getStudent_index().equals(myclass)) {
                            studentDetailsList.add(studentDetails);
                        }
                    }
                    totalStudentInTheClass = studentDetailsList.size();
                    FetchAllFeedbackStudentKeys();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    //It fetches all the feedback of the student using "student_index" value by comparing
    //with the list fetched from student node.
    public void FetchAllFeedbackStudentKeys() {
        feedbackRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        for (StudentDetails studentDetails : studentDetailsList) {
                            if (studentDetails.getUid().equals(dataSnapshot1.getKey())) {
                                studentSubmittedFeedbackList.add(dataSnapshot1.getKey());
                                totalStudentSubmitted++;
                            }
                        }
                    }
                    Log.d("tester1", totalStudentInTheClass + " " + totalStudentSubmitted);
                    FetchAllFeedback();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void FetchAllFeedback() {
        for (String uid : studentSubmittedFeedbackList) {
            feedbackRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                            String teacherUID = dataSnapshot1.getKey();
                            FeedbackParams params = dataSnapshot1.getValue(FeedbackParams.class);

                            feedbackWIthTeacherUidAndParamsList.add(new FeedbackWIthTeacherUidAndParams(teacherUID, params));
                            Log.d("google", teacherUID + " " + params.getParam1() + " " + params.getParam2() + " " + params.getParam3() + " " + params.getParam4());


                        }
                        Log.d("hello", feedbackWIthTeacherUidAndParamsList.size() + " ");

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }

    public void calcualteAllResult() {
        Log.d("tesla", "hello");
        for (String teacherUID : teacherList) {
            for (FeedbackWIthTeacherUidAndParams teacherUidAndParams : feedbackWIthTeacherUidAndParamsList) {
                if (teacherUID.equals(teacherUidAndParams.getName())) {
                    FeedbackParams feedbackParams = teacherUidAndParams.getFeedbackParams();
                    param1 = feedbackParams.getParam1() + param1;
                    param2 = feedbackParams.getParam2() + param2;
                    param3 = feedbackParams.getParam3() + param3;
                    param4 = feedbackParams.getParam4() + param4;

                    Log.d("tesla", teacherUID + " " + feedbackParams.getParam1() + " " + feedbackParams.getParam2() + " " + feedbackParams.getParam3() + " " + feedbackParams.getParam4());
                }
            }
            Log.d("tesla", "\n hello+ " + String.valueOf(param1) + " " + String.valueOf(param2) + " " + String.valueOf(param3) + " " + String.valueOf(param4));
            FeedbackParams feedbackParams = new FeedbackParams(param1, param2, param3, param4);
            finalfeedbackWIthTeacherUidAndParamsList.add(new FeedbackWIthTeacherUidAndParams(teacherUID, feedbackParams));
            param1 = 0L;
            param2 = 0L;
            param3 = 0L;
            param4 = 0L;


        }
        showFinalData();
    }


    public void showFinalData() {
        for(FeedbackWIthTeacherUidAndParams feedbackWIthTeacherUidAndParams:finalfeedbackWIthTeacherUidAndParamsList){
            FeedbackParams feedbackParams=feedbackWIthTeacherUidAndParams.getFeedbackParams();
            String teacherUID=feedbackWIthTeacherUidAndParams.getName();

            Log.d("qwe", String.valueOf(feedbackParams.getParam1()+feedbackParams.getParam2()+feedbackParams.getParam3()+feedbackParams.getParam4()));
            Log.d("qwe", String.valueOf(feedbackParams.getParam1())+" "+String.valueOf(feedbackParams.getParam2())+" "+String.valueOf(feedbackParams.getParam3())+" "+String.valueOf(feedbackParams.getParam4()));
            Log.d("qwe", teacherUID);
            Log.d("qwe", String.valueOf(finalfeedbackWIthTeacherUidAndParamsList.size()));
            Log.d("qwe", "\n \n ");

        }

        finalFeedbackResultWithViewpgerAdapter=new FinalFeedbackResultWithViewpgerAdapter(this,finalfeedbackWIthTeacherUidAndParamsList);
        viewPager.setAdapter(finalFeedbackResultWithViewpgerAdapter);





    }
}
