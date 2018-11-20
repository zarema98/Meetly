package com.nomercy.meetly;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class NewMeetingFragment extends Fragment {

    Button button5;

    public interface onSomeEventListener {
        void someEvent(String s);
    }

    AuthorizationFragment.onSomeEventListener someEventListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity activity;

        if (context instanceof Activity){
            activity = (Activity) context;
            try {
                someEventListener = (AuthorizationFragment.onSomeEventListener) activity;
            } catch (ClassCastException e) {
                throw new ClassCastException(activity.toString() + " must implement onSomeEventListener");
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v2 =  inflater.inflate(R.layout.fragment_new_meeting, container, false);

        button5 = v2.findViewById(R.id.button5);

        addListenerOnButton();
        return v2;
    }

    // Сканер косаний:
    public void addListenerOnButton() {
        button5.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        someEventListener.someEvent("backToMain"); // глвынй экран
                    }
                }
        );
    }

}
