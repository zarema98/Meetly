package com.nomercy.meetly.Controller;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nomercy.meetly.Model.User;
import com.nomercy.meetly.R;

import java.util.ArrayList;

public class UserAdapter2 extends RecyclerView.Adapter<UserAdapter2.ViewHolder>{

    private static final String TAG = "EmployeesAdapter";

    private ArrayList<User> mUsers;
    private Context mContext;

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView name;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "onClick: selected employee: " + mUsers.get(getAdapterPosition()));

                    //open a dialog for inserting a message into the database
                    ((UserListActivity)mContext).openMessageDialog(mUsers.get(getAdapterPosition()).getUser_id());
                }
            });
        }
    }

    public UserAdapter2(Context context, ArrayList<User> users) {
        mUsers = users;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        //inflate the custom layout
        View view = inflater.inflate(R.layout.layout_user_listitem, parent, false);

        //return a new holder instance
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) throws NullPointerException{
        holder.name.setText(mUsers.get(position).getName());
    }


    @Override
    public int getItemCount() {
        return mUsers.size();
    }
}