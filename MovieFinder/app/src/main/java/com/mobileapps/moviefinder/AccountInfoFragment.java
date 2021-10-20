package com.mobileapps.moviefinder;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountInfoFragment extends Fragment {
    private TextView userEmailTextView;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v;
        Activity activity = requireActivity();
        v = inflater.inflate(R.layout.fragment_account_info, container, false);

        // deal with account information
        /* Code was used and modified from Firebase documentation, firebase.google.com, for managing users */

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            userEmailTextView = (TextView) v.findViewById(R.id.userEmail);
            userEmailTextView.setText(email);
        }


        return v;
    }

}