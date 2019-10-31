package com.example.feedback.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.feedback.FeedbackFinalResultActivity;
import com.example.feedback.Model.FeedbackresultAvailable;
import com.example.feedback.R;

import java.util.List;

public class FeedbackResultOverallAdapter extends RecyclerView.Adapter<FeedbackResultOverallAdapter.SectionViewHolder> {
    List<FeedbackresultAvailable> feedbackresultAvailables;
    Context context;

    public FeedbackResultOverallAdapter(List<FeedbackresultAvailable> feedbackresultAvailables, Context context) {
        this.feedbackresultAvailables = feedbackresultAvailables;
        this.context = context;
    }

    @NonNull
    @Override
    public SectionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.feedback_result_overall_card_view_layout,parent,false);
        SectionViewHolder viewHolder=new SectionViewHolder(view);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull SectionViewHolder holder, final int position) {
        holder.batch.setText(feedbackresultAvailables.get(position).getBatch());
        holder.branch.setText(feedbackresultAvailables.get(position).getBranch());
        holder.section.setText(feedbackresultAvailables.get(position).getSection());
        holder.show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, FeedbackFinalResultActivity.class);
                intent.putExtra("class",feedbackresultAvailables.get(position).getBatch()+"_"+
                        feedbackresultAvailables.get(position).getBranch()+"_"
                        +feedbackresultAvailables.get(position).getSection());

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return feedbackresultAvailables.size();
    }

    class SectionViewHolder extends RecyclerView.ViewHolder{
        TextView batch;
        TextView branch;
        TextView section;
        ImageButton show;


        public SectionViewHolder(@NonNull View itemView) {
            super(itemView);
            batch=itemView.findViewById(R.id.batch);
            branch=itemView.findViewById(R.id.branch);
            section=itemView.findViewById(R.id.section);
            show=itemView.findViewById(R.id.show);

        }
    }

}
