package com.nomercy.meetly.Controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nomercy.meetly.R;

import java.util.ArrayList;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.VHolder> {

    ArrayList<String> data, data2;
    int p;

    Context context;

    private OnItemClickListener listener;
    // Define the listener interface

    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public ScoreAdapter(ArrayList<String> data, ArrayList<String> data2, Context context) {
        this.data = data;
        this.data2 = data2;

        this.context = context;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.test, parent, false);
        return new VHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder holder, int position) {
        holder.testText.setText(data.get(position));
        holder.testText2.setText(data2.get(position));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class VHolder extends RecyclerView.ViewHolder {

        TextView testText, testText2;

        public VHolder(final View itemView) {
            super(itemView);

            testText = itemView.findViewById(R.id.textView2);
            testText2 = itemView.findViewById(R.id.textView);
            //ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            // Setup the click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });
        }
    }

}
