package com.example.feedback.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.feedback.Model.TeacherDetails;
import com.example.feedback.R;
import com.example.feedback.TeacherListActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class TeacherDatailsAdapter extends FirebaseRecyclerAdapter<TeacherDetails, TeacherDatailsAdapter.TeacherViewHolder> {
    Context context;
    TeacherListActivity teacherListActivity;


    public TeacherDatailsAdapter(@NonNull FirebaseRecyclerOptions<TeacherDetails> options, Context context) {
        super(options);
        this.context=context;
        this.teacherListActivity= (TeacherListActivity) context;
        


    }

    @Override
    protected void onBindViewHolder(@NonNull TeacherViewHolder holder, int position, @NonNull TeacherDetails model) {
        holder.name.setText(model.getTeacher_name());
        Glide.with(context).load(model.getProfile_image()).placeholder(R.drawable.profile_image).centerCrop().into(holder.profile_image);



    }

    @NonNull
    @Override
    public TeacherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.teacher_profile_cardview_layout,parent,false);
        TeacherViewHolder teacherViewHolder=new TeacherViewHolder(view);
        return teacherViewHolder;
    }

    class TeacherViewHolder extends RecyclerView.ViewHolder{
        ImageView profile_image;
        TextView name;
        CheckBox checkBox;


        public TeacherViewHolder(@NonNull View itemView) {
            super(itemView);
            profile_image=itemView.findViewById(R.id.teacher_profile_image);
            name=itemView.findViewById(R.id.teacher_full_name);
            checkBox=itemView.findViewById(R.id.checkBox);

        }
    }
}
