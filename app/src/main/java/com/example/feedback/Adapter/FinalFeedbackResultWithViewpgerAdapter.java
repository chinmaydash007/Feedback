package com.example.feedback.Adapter;

import android.content.Context;
import android.graphics.Color;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.feedback.Model.FeedbackParams;
import com.example.feedback.Model.FeedbackWIthTeacherUidAndParams;
import com.example.feedback.Model.TeacherDetails;
import com.example.feedback.R;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class FinalFeedbackResultWithViewpgerAdapter extends RecyclerView.Adapter<FinalFeedbackResultWithViewpgerAdapter.MyViewHolder> {
    Context context;
    List<FeedbackWIthTeacherUidAndParams> list;
    DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference().child("Users").child("Teacher");
    int color[]={R.color.pass,R.color.fail};


    public FinalFeedbackResultWithViewpgerAdapter(Context context, List<FeedbackWIthTeacherUidAndParams> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.single_feedback_result_layout_with_graph,parent,false);
        MyViewHolder myViewHolder=new MyViewHolder(view);
        return myViewHolder;

    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        FeedbackWIthTeacherUidAndParams feedbackWIthTeacherUidAndParams=list.get(position);
        FeedbackParams feedbackParams=feedbackWIthTeacherUidAndParams.getFeedbackParams();
        String teacherUID=feedbackWIthTeacherUidAndParams.getName();

        databaseReference.child(teacherUID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    TeacherDetails teacherDetails=dataSnapshot.getValue(TeacherDetails.class);
                    holder.teachername.setText(teacherDetails.getTeacher_name());
                    Glide.with(context).load(teacherDetails.getProfile_image()).placeholder(R.drawable.profile_image).centerCrop().into(holder.profile_image);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        Long totalmarks=Long.valueOf(list.size()*5);
//-------------------------------------------------------------------
        ArrayList<PieEntry> arrayList1=new ArrayList<>();
        arrayList1.add(new PieEntry(Float.valueOf(feedbackParams.getParam1())));
        arrayList1.add(new PieEntry(Float.valueOf(totalmarks-feedbackParams.getParam1())));



        PieDataSet pieDataSet1=new PieDataSet(arrayList1,"Param1");
        pieDataSet1.setColors(color,context);
        PieData pieData1=new PieData(pieDataSet1);
//------------------------------------------------------------------
        ArrayList<PieEntry> arrayList2=new ArrayList<>();
        arrayList2.add(new PieEntry(Float.valueOf(feedbackParams.getParam2())));
        arrayList2.add(new PieEntry(Float.valueOf(totalmarks-feedbackParams.getParam2())));


        PieDataSet pieDataSet2=new PieDataSet(arrayList2,"Param2");
        pieDataSet2.setColors(color,context);
        PieData pieData2=new PieData(pieDataSet2);
//-------------------------------------------------------------------------
        ArrayList<PieEntry> arrayList3=new ArrayList<>();
        arrayList3.add(new PieEntry(Float.valueOf(feedbackParams.getParam3())));
        arrayList3.add(new PieEntry(Float.valueOf(totalmarks-feedbackParams.getParam3())));


        PieDataSet pieDataSet3=new PieDataSet(arrayList3,"Param3");
        pieDataSet3.setColors(color,context);
        PieData pieData3=new PieData(pieDataSet3);
//----------------------------------------------------------------------------
        ArrayList<PieEntry> arrayList4=new ArrayList<>();
        arrayList4.add(new PieEntry(Float.valueOf(feedbackParams.getParam4())));
        arrayList4.add(new PieEntry(Float.valueOf(totalmarks-feedbackParams.getParam4())));


        PieDataSet pieDataSet4=new PieDataSet(arrayList4,"Param4");
        pieDataSet4.setColors(color,context);
        PieData pieData4=new PieData(pieDataSet4);

//---------------------------------------------------------------
        ArrayList<PieEntry> arrayListTotal=new ArrayList<>();
        arrayListTotal.add(new PieEntry(Float.valueOf(feedbackParams.getParam1()+feedbackParams.getParam2()+feedbackParams.getParam3()+feedbackParams.getParam4())));
        arrayListTotal.add(new PieEntry(Float.valueOf(totalmarks*4-feedbackParams.getParam1()+feedbackParams.getParam2()+feedbackParams.getParam3()+feedbackParams.getParam4())));


        PieDataSet pieDataSetTotal=new PieDataSet(arrayListTotal,"Total");
        pieDataSetTotal.setColors(color,context);
        PieData pieDataTotal=new PieData(pieDataSetTotal);


        holder.pieChartParam1.setData(pieData1);
        holder.pieChartParam2.setData(pieData2);
        holder.pieChartParam3.setData(pieData3);
        holder.pieChartParam4.setData(pieData4);
        holder.pieChartTotal.setData(pieDataTotal);

        holder.pieChartParam1.setRotationEnabled(false);
        holder.pieChartParam2.setRotationEnabled(false);
        holder.pieChartParam3.setRotationEnabled(false);
        holder.pieChartParam4.setRotationEnabled(false);
        holder.pieChartTotal.setRotationEnabled(false);

        holder.pieChartParam1.setUsePercentValues(true);
        holder.pieChartParam2.setUsePercentValues(true);
        holder.pieChartParam3.setUsePercentValues(true);
        holder.pieChartParam4.setUsePercentValues(true);
        holder.pieChartTotal.setUsePercentValues(true);


        holder.pieChartParam1.getDescription().setEnabled(false);
        holder.pieChartParam2.getDescription().setEnabled(false);
        holder.pieChartParam3.getDescription().setEnabled(false);
        holder.pieChartParam4.getDescription().setEnabled(false);
        holder.pieChartTotal.getDescription().setEnabled(false);

       // holder.pieChartParam1.setDrawHoleEnabled(true);
        holder.pieChartParam1.setHoleColor(Color.TRANSPARENT);
        holder.pieChartParam2.setHoleColor(Color.TRANSPARENT);
        holder.pieChartParam3.setHoleColor(Color.TRANSPARENT);
        holder.pieChartParam4.setHoleColor(Color.TRANSPARENT);
        holder.pieChartTotal.setHoleColor(Color.TRANSPARENT);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        CircleImageView profile_image;
        PieChart pieChartTotal;
        PieChart pieChartParam1;
        PieChart pieChartParam2;
        PieChart pieChartParam3;
        PieChart pieChartParam4;
        TextView teachername;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            pieChartTotal=itemView.findViewById(R.id.pie_chart_total);
            pieChartParam1=itemView.findViewById(R.id.pie_chart_param1);
            pieChartParam2=itemView.findViewById(R.id.pie_chart_param2);
            pieChartParam3=itemView.findViewById(R.id.pie_chart_param3);
            pieChartParam4=itemView.findViewById(R.id.pie_chart_param4);
            profile_image=itemView.findViewById(R.id.teacher_profile_image);
            teachername=itemView.findViewById(R.id.teacher_name);

        }
    }

}
