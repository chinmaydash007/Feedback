package com.example.feedback.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.feedback.Model.TeacherDetails;
import com.example.feedback.R;

import java.util.ArrayList;

public class StudentMainPageFeedbackAdapter extends RecyclerView.Adapter<StudentMainPageFeedbackAdapter.TeacherViewHolder> {
    Context context;
    ArrayList<TeacherDetails> teacherDetails;

    public StudentMainPageFeedbackAdapter(Context context, ArrayList<TeacherDetails> teacherDetails) {
        this.context = context;
        this.teacherDetails = teacherDetails;
    }

    @NonNull
    @Override
    public TeacherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.teacher_profile_single_layout, parent, false);
        TeacherViewHolder teacherViewHolder = new TeacherViewHolder(view);
        return teacherViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull TeacherViewHolder holder, int position) {
        holder.teacher_name.setText(teacherDetails.get(position).getTeacher_name());

        RequestOptions requestOptions = new RequestOptions().placeholder(R.drawable.profile_image);
        Glide.with(context).load(teacherDetails.get(position).getProfile_image()).apply(requestOptions).centerCrop().into(holder.teacher_profile_image);
    }

    @Override
    public int getItemCount() {
        return teacherDetails.size();
    }

    class TeacherViewHolder extends RecyclerView.ViewHolder {
        ImageView teacher_profile_image;
        TextView teacher_name;

        public TeacherViewHolder(@NonNull View itemView) {
            super(itemView);
            teacher_profile_image = itemView.findViewById(R.id.teacher_profile_image);
            teacher_name = itemView.findViewById(R.id.teacher_full_name);

        }
    }

}
