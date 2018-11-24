package com.nomercy.meetly;

import android.widget.Filter;

import com.nomercy.meetly.Controller.UserAdapter;
import com.nomercy.meetly.Model.User;

import java.util.ArrayList;

public class CustomFilter extends Filter {
    UserAdapter adapter;
    ArrayList<User> filterList;

    public CustomFilter(ArrayList<User> filterList,UserAdapter adapter)
    {
        this.adapter = adapter;
        this.filterList= filterList;

    }

    //FILTERING OCURS
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();

        //CHECK CONSTRAINT VALIDITY
        if(constraint != null && constraint.length() > 0)
        {
            //CHANGE TO UPPER
            constraint=constraint.toString().toUpperCase();
            //STORE OUR FILTERED PLAYERS
            ArrayList<User> filteredPlayers=new ArrayList<>();

            for (int i=0;i<filterList.size();i++)
            {
                //CHECK
                if(filterList.get(i).getName().toUpperCase().contains(constraint))
                {
                    //ADD PLAYER TO FILTERED PLAYERS
                    filteredPlayers.add(filterList.get(i));
                }
            }

            results.count=filteredPlayers.size();
            results.values=filteredPlayers;
        }else
        {
            results.count=filterList.size();
            results.values=filterList;

        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {

        adapter.users= (ArrayList<User>) results.values;

        //REFRESH
        adapter.notifyDataSetChanged();
    }
}
