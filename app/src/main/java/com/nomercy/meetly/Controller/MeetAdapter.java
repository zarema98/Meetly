package com.nomercy.meetly.Controller;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nomercy.meetly.Model.Meet;
import com.nomercy.meetly.R;

import java.util.ArrayList;

public class MeetAdapter extends RecyclerView.Adapter<MeetAdapter.MyViewHolder> {
        private ArrayList<Meet> meetList;
    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView nameMeet, descriptionMeet, dateMeet, idMeet;

        public MyViewHolder(View view) {
            super(view);
            nameMeet = view.findViewById(R.id.nameMeet);
            descriptionMeet =view.findViewById(R.id.descriptionMeet);
            dateMeet = view.findViewById(R.id.dataMeet);
            idMeet = view.findViewById(R.id.meetId);
        }
    }

    public MeetAdapter(ArrayList<Meet> meetList) {
        this.meetList = meetList;
    }

    @NonNull
    @Override
    public MeetAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MeetAdapter.MyViewHolder holder, int position) {
        Meet meet = meetList.get(position);
        holder.nameMeet.setText(meet.getName());
        holder.descriptionMeet.setText(meet.getDescription());
        holder.dateMeet.setText(meet.getDate());
        holder.idMeet.setText(String.valueOf(meet.getMeet_id()));
    }

    public Meet getItem (int pos) {
        return meetList.get(pos);

    }

    public int getItemid (int pos) {
        return meetList.get(pos).meet_id;
    }

    public void restoreItem(Meet model, int position) {
        meetList.add(position, model);
        // notify item added by position
        notifyItemInserted(position);
        notifyItemRangeChanged(position, meetList.size());
    }

    @Override
    public int getItemCount() {
        return meetList.size();
    }

    public void clear() {
        meetList.clear();
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        meetList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, meetList.size());
    }

    // Add a list of items -- change to type used
    public void addAll(ArrayList<Meet> list) {
        meetList.addAll(list);
        notifyDataSetChanged();
    }
}
