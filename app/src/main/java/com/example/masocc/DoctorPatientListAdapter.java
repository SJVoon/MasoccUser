package com.example.masocc;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class DoctorPatientListAdapter extends RecyclerView.Adapter<DoctorPatientListAdapter.ViewHolder> {
    private List<User> viewList;
    private List<String> keyList;
    private LayoutInflater mInflater;
    private Context context;
    private ItemClickListener mClickListener;
    private String key;

    // data is passed into the constructor
    DoctorPatientListAdapter(Context c, List<User> userList, List<String> key) {
        this.context = c;
        this.mInflater = LayoutInflater.from(c);
        this.viewList = userList;
        this.keyList = key;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.doctor_patient_list_recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final int p = position;
        String name = viewList.get(p).getFullName();
        String date = viewList.get(p).getIcNumber();
        holder.myTextView.setText(name);
        holder.myTextView2.setText(date);

        holder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, DoctorPatientExerciseList.class);
                        intent.putExtra("userKey",keyList.get(p));
                        context.startActivity(intent);
                    }
                }
        );
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return viewList.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView, myTextView2;

        ViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            myTextView = itemView.findViewById(R.id.tvLeft);
            myTextView2 = itemView.findViewById(R.id.tvRight);
        }

        @Override
        public void onClick(View view) {

        }
    }

    // convenience method for getting data at click position
    User getItem(int id) {
        System.out.println("id" + id);
        return viewList.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
