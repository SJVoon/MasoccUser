package com.example.masocc;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    private List<ExerciseRecord> exerciseList;
    private LayoutInflater mInflater;
    private Context context;
    private HistoryAdapter.ItemClickListener mClickListener;

    // data is passed into the constructor
    HistoryAdapter(Context c, List<ExerciseRecord> userList) {
        this.context = c;
        this.mInflater = LayoutInflater.from(c);
        this.exerciseList = userList;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.history_recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HistoryAdapter.ViewHolder holder, int position) {
        final int p = position;
        String date = exerciseList.get(p).getDate();
        String type = exerciseList.get(p).getType();
        holder.myTextView.setText(date);
        holder.myTextView2.setText(type);

        holder.itemView.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context,HistoryDisplay.class);
                        intent.putExtra("id",p);
                        intent.putExtra("date",date);
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
    ExerciseRecord getItem(int id) {
        return exerciseList.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(HistoryAdapter.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
