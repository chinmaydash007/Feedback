package com.example.feedback;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;

public class SetupFeedbackActivity extends AppCompatActivity {
Spinner batch;
Spinner branch;
Spinner section;
Button show_student_list;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup_feedback);
        branch=findViewById(R.id.branch);
        batch=findViewById(R.id.batch);
        section=findViewById(R.id.section);
        show_student_list=findViewById(R.id.show_student_list);

        show_student_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String student_batch=batch.getSelectedItem().toString();
                String student_branch=branch.getSelectedItem().toString();
                String student_section=section.getSelectedItem().toString();
                Intent intent=new Intent(SetupFeedbackActivity.this,ShowEligibleStudentForFeedback.class);
                intent.putExtra("student_batch",student_batch);
                intent.putExtra("student_branch",student_branch);
                intent.putExtra("student_section",student_section);
                startActivity(intent);

            }
        });


    }
}
