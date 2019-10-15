package com.example.feedback.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.feedback.Model.TeacherDetails;
import com.example.feedback.R;
import com.example.feedback.Student_profile_activity;
import com.example.feedback.TeacherListActivity;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import java.util.ArrayList;

public class TeacherDatailsAdapter extends RecyclerView.Adapter<TeacherDatailsAdapter.TeacherViewHolder> {
TeacherListActivity teacherListActivity;
ArrayList<TeacherDetails> teacherDetails;
Context context;

    public TeacherDatailsAdapter(ArrayList<TeacherDetails> teacherDetails, Context context) {
        this.teacherListActivity = (TeacherListActivity) context;
        this.teacherDetails = teacherDetails;
        this.context = context;
    }

    @NonNull
    @Override
    public TeacherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.teacher_profile_cardview_layout,parent,false);
        TeacherViewHolder teacherViewHolder=new TeacherViewHolder(view,teacherListActivity);
        return teacherViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherViewHolder holder, int position) {
        holder.name.setText(teacherDetails.get(position).getTeacher_name());
        Glide.with(context).load(teacherDetails.get(position).getProfile_image()).placeholder(R.drawable.profile_image).into(holder.profile_image);
        if(!teacherListActivity.is_in_action_mode){
            holder.checkBox.setVisibility(View.GONE);
        }
        else{
            holder.checkBox.setVisibility(View.VISIBLE);
             holder.checkBox.setChecked(false);

        }




    }

    @Override
    public int getItemCount() {
        return teacherDetails.size();
    }


    class TeacherViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView profile_image;
        TextView name;
        CheckBox checkBox;
        TeacherListActivity teacherListActivity;
        CardView cardView;




        public TeacherViewHolder(@NonNull View itemView,TeacherListActivity teacherListActivity) {
            super(itemView);
            profile_image=itemView.findViewById(R.id.teacher_profile_image);
            name=itemView.findViewById(R.id.teacher_full_name);
            checkBox=itemView.findViewById(R.id.checkBox);
            this.teacherListActivity=teacherListActivity;
            cardView=itemView.findViewById(R.id.cardview);
            cardView.setOnLongClickListener(teacherListActivity);
            checkBox.setOnClickListener(this);




        }


        @Override
        public void onClick(View view) {
            teacherListActivity.prepareSelection(view,getAdapterPosition());

        }
    }
    public void updateAdapter(ArrayList<TeacherDetails> arrayList){
        for(TeacherDetails teacherDetails:arrayList){
            arrayList.remove(teacherDetails);

        }
        notifyDataSetChanged();
    }


}
