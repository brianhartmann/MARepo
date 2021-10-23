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
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UpdateAccountInfoFragment extends Fragment {
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
            currentUserEmail = (TextView) v.findViewById(R.id.currentUserEmail);
            currentUserEmail.setText(email);

            updatedEmail = v.findViewById(R.id.updatedEmail);
            updateEmailBtn = v.findViewById(R.id.updateEmailBtn);

            updatedEmail.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    updateEmailBtn.setEnabled(false);
                }

                @Override
                public void onTextChanged(CharSequence str, int i, int i1, int i2) {
                    if(str.toString().trim().length() != 0) {

                        if(str.toString().equals(email)) {
                            updatedEmail.setError("Cannot update email, same as current email.");

                        } else {
                            updateEmailBtn.setEnabled(true);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    // Auto-generated method
                }
            });

            updateEmailBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    user.updateEmail(updatedEmail.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("UpdateAccountInfo", "User email address updated.");
                                        Toast.makeText(getContext(), "Email address was updated. ", Toast.LENGTH_LONG).show();

                                        updatedEmail.setText("");
                                        updateEmailBtn.setEnabled(false);
                                        currentUserEmail.setText(user.getEmail());

                                    } else {
                                        Toast.makeText(getContext(), "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            });

            // Handle updating a user's name
            currentUserName = (TextView) v.findViewById(R.id.currentUserName);
            currentUserName.setText(name);

            updatedName = v.findViewById(R.id.updatedName);
            updateNameBtn = v.findViewById(R.id.updateNameBtn);

            updatedName.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    updateNameBtn.setEnabled(false);
                }

                @Override
                public void onTextChanged(CharSequence str, int i, int i1, int i2) {
                    if(str.toString().trim().length() != 0) {

                        if(str.toString().equals(name)) {
                            updatedName.setError("Cannot update name, same as current name.");

                        } else {
                            updateNameBtn.setEnabled(true);
                        }
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {
                    // Auto-generated method
                }
            });

            updateNameBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(updatedName.getText().toString().trim())
                            .build();

                    user.updateProfile(profileUpdates)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("UpdateAccountInfo", "User profile (name) updated.");
                                        Toast.makeText(getContext(), "Name was updated. ", Toast.LENGTH_LONG).show();

                                        updatedName.setText("");
                                        updateNameBtn.setEnabled(false);
                                        currentUserName.setText(user.getDisplayName());

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