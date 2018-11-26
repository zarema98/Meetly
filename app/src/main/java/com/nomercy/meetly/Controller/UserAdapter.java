package com.nomercy.meetly.Controller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.nomercy.meetly.CustomFilter;
import com.nomercy.meetly.ItemClickListener;
import com.nomercy.meetly.Model.User;
import com.nomercy.meetly.R;
import com.nomercy.meetly.api.APIInterface;
import com.nomercy.meetly.api.Constants;

import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder> implements Filterable {

    public Context context;
    public ArrayList<User> users = new ArrayList<>();
   public ArrayList<User> checkedUsers = new ArrayList<>();
    CustomFilter filter;
    Retrofit retrofit;

    public UserAdapter(ArrayList<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public UserAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_item, parent, false);

        MyViewHolder holder = new MyViewHolder(itemView);
        return holder;
    }


    @Override
    public void onBindViewHolder(@NonNull final UserAdapter.MyViewHolder holder, int position) {
        final User user = users.get(position);
        holder.memberName.setText(user.getName());
        holder.checkBox.setOnCheckedChangeListener(null);

        //if true, your checkbox will be selected, else unselected
        holder.checkBox.setChecked(user.isSelected);

        holder.setItemClickListener( new ItemClickListener(){
            @Override
            public void onItemClick(View v, int pos) {
                CheckBox checkBox = (CheckBox)v;
                final User member = users.get(pos);

                if(checkBox.isChecked()) {
                    member.setSelected(true);
                    APIInterface service = retrofit.create(APIInterface.class);
                    String telephone = member.getTelephone().replaceAll("[()\\s-]+", "");
                    final Call<User> call = service.checkPhoneNumber(telephone);
                    call.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            response.body();
                            int id = response.body().getId();
                          //  Toast.makeText(context, "id: " + id , Toast.LENGTH_LONG).show();
                            if(id != 0) {
                                member.setId(id);
                                checkedUsers.add(member);
                            } else {
                                checkedUsers.add(member);
                            }

                        }
                        @Override
                        public void onFailure(Call<User> call, Throwable t) {

                        }
                    });



                } else if(!checkBox.isChecked()) {
                    member.setSelected(false);
                    checkedUsers.remove(member);

                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return users.size();
    }


    public String getItemName (int position) {
        return users.get(position).name;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView memberName;
        public CheckBox checkBox;

        ItemClickListener itemClickListener;

        public MyViewHolder (View itemView) {
            super(itemView);
            memberName = itemView.findViewById(R.id.membersName);
            checkBox = itemView.findViewById(R.id.checkbox);
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            //The logging interceptor will be added to the http client
            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
            httpClient.addInterceptor(logging);

            retrofit = new Retrofit.Builder()
                    .client(httpClient.build())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(Constants.BaseUrl)
                    .build();

            checkBox.setOnClickListener(this);
        }
        public void setItemClickListener (ItemClickListener ic) {
            this.itemClickListener = ic;
        }
        @Override
        public void onClick (View v) {
            this.itemClickListener.onItemClick(v, getLayoutPosition());
        }

    }

    @Override
    public Filter getFilter() {
        if(filter==null)
        {
            filter=new CustomFilter(users,this);
        }

        return filter;
    }




}
