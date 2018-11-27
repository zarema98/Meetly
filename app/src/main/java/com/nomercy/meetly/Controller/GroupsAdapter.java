package com.nomercy.meetly.Controller;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nomercy.meetly.Model.Groups;
import com.nomercy.meetly.R;

import java.util.List;

public class GroupsAdapter  extends RecyclerView.Adapter <GroupsAdapter.GroupsViewHolder>{
    private List<Groups> groupsList;


    public class GroupsViewHolder extends RecyclerView.ViewHolder {

        public TextView groupName;
        public ImageView groupImage;
        public TextView idOfGroup;

        public GroupsViewHolder(View view) {
            super(view);
            groupName = (TextView) view.findViewById(R.id.nameGroup);
            groupImage = (ImageView) view.findViewById(R.id.icon_group);
            idOfGroup = view.findViewById(R.id.groupId);
        }

//        @Override
//        public void onClick(View view) {
//            int pos = getAdapterPosition();
//            Context context = view.getContext();
//            Intent intent = new Intent(context, AboutGroup.class);
//            intent.putExtra("groupName", getItemName(pos));
//            context.startActivity(intent);
//
//        }
    }


    public GroupsAdapter(List<Groups> groupsList) {
        this.groupsList = groupsList;
    }

    @Override
    public GroupsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.participant_group, parent, false);

        return new GroupsViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(GroupsViewHolder holder, int position) {
        Groups groups = groupsList.get(position);
        holder.groupName.setText(groups.getGroupName());
        holder.groupImage.setImageResource(R.drawable.groups);
        holder.idOfGroup.setText(String.valueOf(groups.getGroup_id()));

    }

    public int getItemid (int pos) {
        return groupsList.get(pos).group_id;
    }

    public String getItemName(int pos) {
        return groupsList.get(pos).groupName;
    }


    @Override
    public int getItemCount() {
        return groupsList.size();
    }
}

