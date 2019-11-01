package com.example.feedback.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.feedback.Model.TeacherDetails;
import com.example.feedback.R;
import com.example.feedback.SingleFeedbackActivty;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class TeacherFeedbackAdapter extends RecyclerView.Adapter<TeacherFeedbackAdapter.TeacherFeedbackAdapterViewHolder>{
    Context context;
    ArrayList<TeacherDetails> teacherDetailsArrayList;


    public TeacherFeedbackAdapter(Context context, ArrayList<TeacherDetails> teacherDetailsArrayList) {
        this.context = context;
        this.teacherDetailsArrayList = teacherDetailsArrayList;

    }

    @NonNull
    @Override
    public TeacherFeedbackAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_teacher_feedback_cardview,parent,false);
        TeacherFeedbackAdapterViewHolder viewHolder=new TeacherFeedbackAdapterViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherFeedbackAdapterViewHolder holder, final int position) {
        holder.teacher_name.setText(teacherDetailsArrayList.get(position).getTeacher_name());
        RequestOptions requestOptions=new RequestOptions().placeholder(R.drawable.profile_image);
        Glide.with(context).load(teacherDetailsArrayList.get(position).getProfile_image()).apply(requestOptions).centerCrop().into(holder.teacher_profile_image);

        holder.submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context,SingleFeedbackActivty.class);
                intent.putExtra("teacher_uid",teacherDetailsArrayList.get(position).getUid());
                context.startActivity(intent);


            }
        });

    }

    @Override
    public int getItemCount() {
        return teacherDetailsArrayList.size();
    }

    class TeacherFeedbackAdapterViewHolder extends RecyclerView.ViewHolder {
        CircleImageView teacher_profile_image;
        TextView teacher_name;
        FloatingActionButton submit_btn;


        public TeacherFeedbackAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            teacher_profile_image = itemView.findViewById(R.id.teacher_profile_image);
            teacher_name=itemView.findViewById(R.id.teacher_name);
            submit_btn=itemView.findViewById(R.id.submit_btn);



        }



    }
}
