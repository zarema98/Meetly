package com.nomercy.meetly;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Movie;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.support.design.widget.NavigationView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nomercy.meetly.api.APIInterface;
import com.nomercy.meetly.api.Constants;
import com.nomercy.meetly.api.User;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MeetlyApp extends AppCompatActivity implements AuthorizationFragment.onSomeEventListener,NavigationView.OnNavigationItemSelectedListener{

    // Инициализация переменных и объектов:
    FrameLayout frameHead, frameBody;
    RecyclerView feed;
    ConstraintLayout meetsScreen, newMeetBar;
    int id;
    Activity activity;
    LinearLayout ll;

    TextView txtEmpty;
    Adapter adapter;
    LinearLayoutManager manager;
    private SwipeRefreshLayout swipeContainer;

    private ArrayList<Meet> meetList = new ArrayList<>();
    private RecyclerView recyclerView;
    private MeetAdapter mAdapter;
    Retrofit retrofit;
    DBHelper mDBHelper;
    int auth;
    private Paint p = new Paint();
    ArrayList<Meet> curMeet= new ArrayList<>();


    public static DrawerLayout drawer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.feed);
        ll = findViewById(R.id.linearLayout);




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
        mDBHelper = new DBHelper(this);
        // Определение переменных и объектов:
        frameHead = findViewById(R.id.frameHead);
        frameBody = findViewById(R.id.frameBody);
        meetsScreen = findViewById(R.id.meetsScreen);
        swipeContainer =  findViewById(R.id.swipeContainer);
        newMeetBar = findViewById(R.id.newMeetScreen);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        authorization();
        addListenerOnButton();

        getMeets();
//        curMeet = getArrayList("meets");
//        if(curMeet != null) {
//            if(curMeet.size() > 0) {
//                generateMeets(curMeet);
//            }
////             else if(curMeet.size() <= 0) {
////                meetsScreen.setVisibility(View.GONE);
////                explainText();
////            }
//        }

      //  enableSwipe();
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getMeets();


            }
        });
    }

    private void enableSwipe(){
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();

                if (direction == ItemTouchHelper.LEFT){
                    final Meet deletedModel = (Meet) mAdapter.getItem(position);
                    final int deletedPosition = position;
//                    word = mDBHelper.getValue(deletedModel);
//                    mDBHelper.removeBookmark(deletedModel);
                    mAdapter.removeItem(position);
                    mAdapter.notifyDataSetChanged();

                    // showing snack bar with Undo option
                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(recyclerView.getWindowToken(), 0);
                    //getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

                    Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), " Удалено " + deletedModel, Snackbar.LENGTH_LONG);
                    snackbar.setAction("Отменить", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                           getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                            // undo is selected, restore the deleted item
                            mAdapter.restoreItem(deletedModel, deletedPosition);
                            //mDBHelper.addBookmark(word.key, word.value);
                            mAdapter.notifyDataSetChanged();
                        }
                    });
                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();
                } else {
                    final Meet deletedModel = (Meet) mAdapter.getItem(position);
                    final int deletedPosition = position;
//                    word = mDBHelper.getValue(deletedModel);
//                    mDBHelper.removeBookmark(deletedModel);
                    mAdapter.removeItem(position);
                    mAdapter.notifyDataSetChanged();
                    // showing snack bar with Undo option
                    InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(recyclerView.getWindowToken(), 0);
                    // getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

                    Snackbar snackbar = Snackbar.make(getWindow().getDecorView().getRootView(), " Удалено " + deletedModel, Snackbar.LENGTH_LONG);
                    snackbar.setAction("Отменить", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                            // undo is selected, restore the deleted item
                            mAdapter.restoreItem(deletedModel, deletedPosition);
//                            mDBHelper.addBookmark(word.key, word.value);
//                            adapter.notifyDataSetChanged();
                        }
                    });
                    snackbar.setActionTextColor(Color.YELLOW);
                    snackbar.show();
                }
            }

            @Override
            public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                if(actionState == ItemTouchHelper.ACTION_STATE_SWIPE){
                    View itemView = viewHolder.itemView;
                    float height = (float) itemView.getBottom() - (float) itemView.getTop();
                    float width = height / 3;
                    if(dX > 0){
                        p.setColor(Color.parseColor("#692069"));
                        RectF background = new RectF((float) itemView.getLeft(), (float) itemView.getTop(), dX,(float) itemView.getBottom());
                        c.drawRect(background,p);
                        RectF icon_dest = new RectF((float) itemView.getLeft() + width ,(float) itemView.getTop() + width,(float) itemView.getLeft()+ 2*width,(float)itemView.getBottom() - width);
                        Drawable drawable = ContextCompat.getDrawable(MeetlyApp.this,R.drawable.ic_delete);
                        Bitmap icon2 = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(icon2);
                        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                        drawable.draw(canvas);
                        c.drawBitmap(icon2, null, icon_dest, p);
                    } else {
                        p.setColor(Color.parseColor("#692069"));
                        RectF background = new RectF((float) itemView.getRight() + dX, (float) itemView.getTop(),(float) itemView.getRight(), (float) itemView.getBottom());
                        c.drawRect(background,p);
                        RectF icon_dest = new RectF((float) itemView.getRight() - 2*width ,(float) itemView.getTop() + width,(float) itemView.getRight() - width,(float)itemView.getBottom() - width);
                        Drawable drawable = ContextCompat.getDrawable(MeetlyApp.this,R.drawable.ic_delete);
                        Bitmap icon2 = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(icon2);
                        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
                        drawable.draw(canvas);
                        c.drawBitmap(icon2, null, icon_dest, p);

                    }
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

//    public void explainText() {
//        meetsScreen.setVisibility(View.GONE);
//        TextView textView = new TextView(getApplicationContext());
//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
//                LinearLayout.LayoutParams.WRAP_CONTENT, // Width of TextView
//                LinearLayout.LayoutParams.WRAP_CONTENT); // Height of TextView
//        textView.setLayoutParams(lp);
//        textView.setText("У вас пока нет встреч. ");
//        textView.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
//        textView.setTextSize(20);
//        textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
//        textView.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL);
//        ll.addView(textView);
//
//    }

//    public void saveArrayList(ArrayList<Meet> list, String key){
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        SharedPreferences.Editor editor = prefs.edit();
//        Gson gson = new Gson();
//        String json = gson.toJson(list);
//        editor.putString(key, json);
//        editor.apply();     // This line is IMPORTANT !!!
//    }
//
//    public ArrayList<Meet> getArrayList(String key){
//        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        Gson gson = new Gson();
//        String json = prefs.getString(key, null);
//        Type type = new TypeToken<ArrayList<Meet>>(){}.getType();
//        ArrayList<Meet> meets = gson.fromJson(json, type);
//        return meets;
//    }

    public void getMeets() {
        APIInterface service = retrofit.create(APIInterface.class);
        int id = mDBHelper.getId();
        Call<MeetList> call = service.get(id);
        call.enqueue(new Callback<MeetList>() {
            @Override
            public void onResponse(Call<MeetList> call, Response<MeetList> response) {
               // saveArrayList(response.body().getMeetArrayList(), "meets");
                generateMeets(response.body().getMeetArrayList());

            }

            @Override
            public void onFailure(Call<MeetList> call, Throwable t) {
                // handle execution failures like no internet connectivity
            }
        });

    }




    public void generateMeets (ArrayList<Meet> meetDataList) {
        if(meetDataList.size() <=0) {
//               explainText();
            swipeContainer.setRefreshing(false);
        }else {
            mAdapter = new MeetAdapter(meetDataList);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            //recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
            recyclerView.setAdapter(mAdapter);
            swipeContainer.setRefreshing(false);
        }



    }

    // Сканер косаний:
    public void addListenerOnButton() {
    }


    // Fragment listener:
    @Override
    public void someEvent(String s) {
        switch(s) {
            case "toMain":
                // Переход к главному экрану:
                frameBody.setVisibility(View.GONE);
                frameHead.setVisibility(View.VISIBLE);
                meetsScreen.setVisibility(View.VISIBLE);


                HeadFragment headFragment = new HeadFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.frameHead, headFragment)
                        .commit();

                //feed();
                break;

            case "toNewMeeting":
                // Переход к созданию встречи:
                meetsScreen.setVisibility(View.GONE);


                NewMeetingFragment newMeetingFragment = new NewMeetingFragment();
                FragmentManager fragmentManager1 = getSupportFragmentManager();
                fragmentManager1.beginTransaction()
                        .replace(R.id.frameBody, newMeetingFragment)
                        .commit();
                frameBody.setVisibility(View.VISIBLE);
                break;

            case "backToMain":
                frameBody.setVisibility(View.GONE);
                meetsScreen.setVisibility(View.VISIBLE);
        }
    }


    // Переход к авторизации:
    void authorization() {
        frameBody.setVisibility(View.VISIBLE);
        AuthorizationFragment authorizationFragment = new AuthorizationFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.frameBody, authorizationFragment)
                .commit();

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.btnMenu) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_settings) {
//            String activeFragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container).getClass().getSimpleName();
//            if(!activeFragment.equals(BookmarkFragment.class.getSimpleName())) {
//                goToFragment(bookmarkFragment, false);
//            }
        }

        if (id == R.id.nav_group) {
            Intent intent = new Intent(this, GroupsMain.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.nav_money) {

        } else if (id == R.id.nav_help) {

        }  else if (id == R.id.nav_share) {
            Intent myIntent = new Intent(Intent.ACTION_SEND);
            myIntent.setType("text/plain");
            String shareBody = "Зацени какое крутое приложение! Держи ссылку на скачивание: ...";
            String shareSub = "Yout Subject here";
            myIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub);
            myIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
            startActivity(Intent.createChooser(myIntent,"Поделиться"));

        } else if (id == R.id.nav_friends) {
            meetsScreen.setVisibility(View.GONE);
            frameHead.setVisibility(View.GONE);
            frameBody.setVisibility(View.GONE);

            ContactsFragment contactsFragment = new ContactsFragment();
            FragmentManager fragmentManager1 = getSupportFragmentManager();
            fragmentManager1.beginTransaction()
                    .replace(R.id.frameBody, contactsFragment)
                    .commit();
            frameBody.setVisibility(View.VISIBLE);

        }  else if (id == R.id.nav_exit) {
            APIInterface service = retrofit.create(APIInterface.class);
            final int user_id = mDBHelper.getId();

            Call<User> call = service.logout(id);
            call.enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    mDBHelper.deleteUser(user_id);
                    finishAffinity();
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    // handle execution failures like no internet connectivity
                }

            });

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
