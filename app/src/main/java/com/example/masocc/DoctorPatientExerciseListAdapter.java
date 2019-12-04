package com.example.masocc;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class DoctorPatientExerciseListAdapter extends RecyclerView.Adapter<DoctorPatientExerciseListAdapter.ViewHolder> {
    private List<ExerciseRecord> exerciseList;
    private List<String> exerciseKeyList;
    private LayoutInflater mInflater;
    private Context context;
    private DoctorPatientExerciseListAdapter.ItemClickListener mClickListener;
    private String key;

    // data is passed into the constructor
    DoctorPatientExerciseListAdapter(Context c, List<ExerciseRecord> userList, List<String> list, String k) {
        this.context = c;
        this.mInflater = LayoutInflater.from(c);
        this.exerciseList = userList;
        this.exerciseKeyList = list;
        this.key = k;
    }

    // inflates the row layout from xml when needed
    @Override
    public DoctorPatientExerciseListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.doctor_patient_exercise_list_recyclerview_row, parent, false);
        return new DoctorPatientExerciseListAdapter.ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(DoctorPatientExerciseListAdapter.ViewHolder holder, int position) {
        final int p = position;
        String date = exerciseList.get(p).getDate();
        String check;
        if(exerciseList.get(p).getComment().equals("")) {
            check = "Unchecked";
            holder.ll.setBackgroundResource(R.color.colorPrimary);
        }
        else {
            check = "Checked";
            holder.ll.setBackgroundResource(R.color.colorAccent);
        }
        holder.myTextView.setText(date);
        holder.myTextView2.setText(check);

        holder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, DoctorPatientExerciseReview.class);
                        intent.putExtra("userKey",key);
                        intent.putExtra("exerciseKey",exerciseKeyList.get(p));
                        context.startActivity(intent);
                    }
                }
        );
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return exerciseList.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView, myTextView2;
        LinearLayout ll;

        ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            ll = itemView.findViewById(R.id.linear);
            myTextView = itemView.findViewById(R.id.tvLeft);
            myTextView2 = itemView.findViewById(R.id.tvRight);
        }

        @Override
        public void onClick(View view) {

        }
    }

    // convenience method for getting data at click position
    ExerciseRecord getItem(int id) {
        System.out.println("id" + id);
        return exerciseList.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(DoctorPatientExerciseListAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
