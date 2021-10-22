package com.mobileapps.moviefinder;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ResetPasswordFragment extends Fragment {
    private EditText updatedPassword, updatedPasswordConfirm;
    private Button resetPasswordBtn;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v;
        Activity activity = requireActivity();
        v = inflater.inflate(R.layout.fragment_reset_password, container, false);

        /* Code was used and modified from Firebase documentation, firebase.google.com, for managing users */

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {

            // Handle resetting/updating a user's password
            updatedPassword = v.findViewById(R.id.updatedPassword);
            updatedPasswordConfirm = v.findViewById(R.id.updatedPasswordConfirm);
            resetPasswordBtn = v.findViewById(R.id.resetPasswordBtn);

            updatedPasswordConfirm.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    resetPasswordBtn.setEnabled(false);
                }

                @Override
                public void onTextChanged(CharSequence str, int i, int i1, int i2) {
                    if(str.toString().trim().length() != 0) {

                        if(str.toString().trim().length() < 6) {
                            updatedPasswordConfirm.setError("Password must be at least 6 characters.");

                        } else {
                            if(str.toString().trim().equals(updatedPassword.getText().toString().trim())) {
                                resetPasswordBtn.setEnabled(true);

                            } else {
                                updatedPasswordConfirm.setError("Passwords must match.");
                            }
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    // Auto-generated method
                }
            });

            resetPasswordBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    user.updatePassword(updatedPasswordConfirm.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("ResetPassword", "User password updated.");
                                        Toast.makeText(getContext(), "Password was updated. ", Toast.LENGTH_LONG).show();

                                        updatedPassword.setText(null);
                                        updatedPasswordConfirm.setText("");
                                        resetPasswordBtn.setEnabled(false);

                                    } else {
                                        Toast.makeText(getContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            });
        }

        return v;
    }

}