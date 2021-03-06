package com.mobileapps.moviefinder;

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

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UpdateAccountInfoFragment extends ResetPasswordFragment {
    private EditText updatedEmail, updatedName;
    private TextView currentUserEmail, currentUserName;
    private Button updateEmailBtn, updateNameBtn;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_update_account_info, container, false);

        /* Code was used and modified from Firebase documentation, firebase.google.com, for managing users */

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            String name = user.getDisplayName();

            // Handle updating a user's email address
            currentUserEmail = v.findViewById(R.id.currentUserEmail);
            currentUserEmail.setText(email);

            updatedEmail = v.findViewById(R.id.updatedEmail);
            updateEmailBtn = v.findViewById(R.id.updateEmailBtn);
            disableButton(updateEmailBtn);

            updatedEmail.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    disableButton(updateEmailBtn);
                }

                @Override
                public void onTextChanged(CharSequence str, int i, int i1, int i2) {
                    if(str.toString().trim().length() != 0) {

                        String newEmail = user.getEmail();
                        if(str.toString().equals(newEmail)) {
                            updatedEmail.setError("Cannot update email, same as current email.");

                        } else {
                            enableButton(updateEmailBtn);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    // Auto-generated method
                }
            });

            updateEmailBtn.setOnClickListener((View view)  -> {
                user.updateEmail(updatedEmail.getText().toString().trim())
                    .addOnCompleteListener((@NonNull Task<Void> task) -> {
                        if (task.isSuccessful()) {
                            Log.d("UpdateAccountInfo", "User email address updated.");
                            Toast.makeText(getContext(), "Email address was updated. ", Toast.LENGTH_LONG).show();

                            updatedEmail.setText("");
                            disableButton(updateEmailBtn);
                            currentUserEmail.setText(user.getEmail());

                        } else {
                            Toast.makeText(getContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
            });

            // Handle updating a user's name
            currentUserName = v.findViewById(R.id.currentUserName);
            currentUserName.setText(name);

            updatedName = v.findViewById(R.id.updatedName);
            updateNameBtn = v.findViewById(R.id.updateNameBtn);
            disableButton(updateNameBtn);

            updatedName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    disableButton(updateNameBtn);
                }

                @Override
                public void onTextChanged(CharSequence str, int i, int i1, int i2) {
                    if(str.toString().trim().length() != 0) {

                        String newName = user.getDisplayName();
                        if(str.toString().equals(newName)) {
                            updatedName.setError("Cannot update name, same as current name.");

                        } else {
                            enableButton(updateNameBtn);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    // Auto-generated method
                }
            });

            updateNameBtn.setOnClickListener((View view) -> {
                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(updatedName.getText().toString().trim())
                        .build();

                user.updateProfile(profileUpdates)
                        .addOnCompleteListener((@NonNull Task<Void> task) -> {
                            if (task.isSuccessful()) {
                                Log.d("UpdateAccountInfo", "User profile (name) updated.");
                                Toast.makeText(getContext(), "Name was updated. ", Toast.LENGTH_LONG).show();

                                updatedName.setText("");
                                disableButton(updateNameBtn);
                                currentUserName.setText(user.getDisplayName());

                            } else {
                                Toast.makeText(getContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            });
        }

        return v;
    }

}