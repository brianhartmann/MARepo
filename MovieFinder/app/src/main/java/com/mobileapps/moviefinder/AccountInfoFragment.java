package com.mobileapps.moviefinder;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountInfoFragment extends Fragment {
    private TextView userEmailTextView, userNameTextView;
    private Button updateAccountInfoBtn, updatePasswordBtn, deleteAccountBtn;

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
        updateAccountInfoBtn = v.findViewById(R.id.updateAccount);
        updatePasswordBtn = v.findViewById(R.id.updatePasswordPage);
        deleteAccountBtn = v.findViewById(R.id.deleteAccount);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            String name = user.getDisplayName();

            userEmailTextView = (TextView) v.findViewById(R.id.userEmail);
            userEmailTextView.setText(email);

            userNameTextView = (TextView) v.findViewById(R.id.userName);
            userNameTextView.setText(name);
        }


        updateAccountInfoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("AccountInfo", (String) updateAccountInfoBtn.getText());
                startActivity(new Intent(activity, UpdateAccountInfoActivity.class));
            }
        });

        updatePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("AccountInfo", (String) updatePasswordBtn.getText());
                startActivity(new Intent(activity, ResetPasswordActivity.class));
            }
        });

        deleteAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("AccountInfo", (String) deleteAccountBtn.getText());

                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
                alertBuilder.setMessage("Are you sure you want to DELETE your account?").setCancelable(true)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // Delete the user's account
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                        if (user != null) {
                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Log.d("AccountInfo", "User account deleted.");
                                                Toast.makeText(getContext(), "Account was deleted. ", Toast.LENGTH_LONG).show();

                                                FirebaseAuth.getInstance().signOut();
                                                startActivity(new Intent(getContext(), MainActivity.class));

                                            } else {
                                                Toast.makeText(getContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                        }

                    }
                })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });

                AlertDialog alertDialog = alertBuilder.create();
                alertDialog.setTitle("Delete Account");
                alertDialog.show();
            }
        });

        return v;
    }

}