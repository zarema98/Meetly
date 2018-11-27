package com.nomercy.meetly.Controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.TextView;

import com.nomercy.meetly.CustomFilter;
import com.nomercy.meetly.ItemClickListener;
import com.nomercy.meetly.Model.User;
import com.nomercy.meetly.R;
import java.util.ArrayList;



public class GroupMemberAdapter extends RecyclerView.Adapter<GroupMemberAdapter.MyViewHolder> {

    public Context context;
 //   public ArrayList<User> members = new ArrayList<>();
    public ArrayList<String> members;

    public GroupMemberAdapter(ArrayList<String> members, Context context) {
        this.members = members;
        this.context = context;
    }

    @NonNull
    @Override
    public GroupMemberAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.about_members_item, parent, false);

        GroupMemberAdapter.MyViewHolder holder = new GroupMemberAdapter.MyViewHolder(itemView);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull final GroupMemberAdapter.MyViewHolder holder, int position) {
        final String name = members.get(position);
        holder.memberName.setText(name);

        //if true, your checkbox will be selected, else unselected


    }

    @Override
    public int getItemCount() {
        return members.size();
    }


    public String getItemName (int position) {
        return members.get(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView memberName;


        ItemClickListener itemClickListener;

        public MyViewHolder (View itemView) {
            super(itemView);
            memberName = itemView.findViewById(R.id.name_member);
        }
        public void setItemClickListener (ItemClickListener ic) {
            this.itemClickListener = ic;
        }
        @Override
        public void onClick (View v) {
            this.itemClickListener.onItemClick(v, getLayoutPosition());
        }

    }



}
