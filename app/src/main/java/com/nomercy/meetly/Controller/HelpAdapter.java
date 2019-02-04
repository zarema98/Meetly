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

public class HelpAdapter extends RecyclerView.Adapter<HelpAdapter.VHolder> {
    ArrayList<String> data, data2;
    Context context;


    private OnItemClickListener listener;


    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }


    public HelpAdapter(ArrayList<String> data, ArrayList<String> data2, Context context) {
        this.data = data;
        this.data2 = data2;

        this.context = context;
    }


    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.help_elements, parent, false);
        return new VHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull VHolder holder, int position) {
        holder.testText2.setText(data2.get(position));
        holder.testText.setText(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class VHolder extends RecyclerView.ViewHolder {

        TextView testText, testText2;

        public VHolder(final View itemView) {
            super(itemView);


            testText2 = itemView.findViewById(R.id.helpName);
            testText = itemView.findViewById(R.id.helpId);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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
