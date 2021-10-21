package com.mobileapps.moviefinder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdateAccountInfoFragment extends Fragment {
    private EditText userEmail, userName, userPasword, userConfirmPassword;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v;
        Activity activity = requireActivity();
        v = inflater.inflate(R.layout.fragment_update_account_info, container, false);

        // Deal with updating account information
        /* Code was used and modified from Firebase documentation, firebase.google.com, for managing users */

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            String name = user.getDisplayName();

            userName = v.findViewById(R.id.name);
            userEmail = v.findViewById(R.id.email);
            userName.setHint(name);
            userEmail.setHint(email);

            //deal with updating info or resetting password/email
        }

        return v;
    }

}