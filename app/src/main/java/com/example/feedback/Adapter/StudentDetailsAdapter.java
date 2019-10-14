package com.example.feedback.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.feedback.Model.StudentDetails;
import com.example.feedback.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class StudentDetailsAdapter extends FirebaseRecyclerAdapter<StudentDetails, StudentDetailsAdapter.StudentDetailsViewHolder> {
    Context context;


    public StudentDetailsAdapter(@NonNull FirebaseRecyclerOptions<StudentDetails> options,Context context) {
        super(options);
        this.context=context;

    }

    @Override
    protected void onBindViewHolder(@NonNull StudentDetailsViewHolder holder, int position, @NonNull StudentDetails model) {
        holder.name.setText(model.getStudent_full_name());
        holder.roll_no.setText(model.getStudent_roll_no());
        Glide.with(context).load(model.getProfile_image()).placeholder(R.drawable.profile_image).centerCrop().into(holder.profile_image);



    }

    @NonNull
    @Override
    public StudentDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.student_profile_cardview_layout,parent,false);
        StudentDetailsViewHolder detailsViewHolder=new StudentDetailsViewHolder(view);
        return detailsViewHolder;
    }

    class StudentDetailsViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView roll_no;
        ImageView profile_image;


        public StudentDetailsViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.student_full_name);
            roll_no=itemView.findViewById(R.id.student_roll_no);
            profile_image=itemView.findViewById(R.id.student_profile_image);

        }
    }
}
