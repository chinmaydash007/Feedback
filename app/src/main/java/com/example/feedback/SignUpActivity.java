package com.example.feedback;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class SignUpActivity extends AppCompatActivity {
    public static String TAG = "test";

    TextInputLayout emailLayout, passwordLayout, confirm_password_layout;
    MaterialButton reg_button;
    Toolbar toolbar;

    TextView openloginpage;
    FirebaseAuth firebaseAuth;
    ChipGroup chipGroup;
    String profession = null;
    DatabaseReference userRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        emailLayout = findViewById(R.id.register_email);
        passwordLayout = findViewById(R.id.register_password);
        confirm_password_layout = findViewById(R.id.register_confirm_password);
        reg_button = findViewById(R.id.btn_register);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Register");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        openloginpage = findViewById(R.id.already_have_an_account);

        firebaseAuth = FirebaseAuth.getInstance();
        chipGroup = findViewById(R.id.chipGroup);
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");


        openloginpage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {

                Chip chip = chipGroup.findViewById(i);
                if (chip == null) {
                    Toast.makeText(SignUpActivity.this, "Select your profession", Toast.LENGTH_SHORT).show();
                    profession = null;

                }
                if (chip != null) {
                    Toast.makeText(getApplicationContext(), "Chip is " + chip.getText(), Toast.LENGTH_SHORT).show();
                    profession = chip.getText().toString().trim();
                }

            }
        });


        reg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailLayout.getEditText().getText().toString();
                String password = passwordLayout.getEditText().getText().toString();
                final String confirm_password = confirm_password_layout.getEditText().getText().toString();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(confirm_password)) {
                    Toast.makeText(SignUpActivity.this, "All the fields are required!!", Toast.LENGTH_SHORT).show();
                } else if (password.length() <= 6) {
                    Toast.makeText(SignUpActivity.this, "Password should be atleast 6 character", Toast.LENGTH_SHORT).show();
                } else if (!confirm_password.equals(password) || profession == null) {
                    Toast.makeText(SignUpActivity.this, "Make sure you enter same password in both the fields", Toast.LENGTH_SHORT).show();
                } else {
                    register(email, password);
                }
            }
        });
    }

    public void register(final String email, String password) {

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                //TODO :write the code to get the current date and time and enter it the firebase..

                getCounterForUsersSignUp(profession,authResult,email);



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);

    }

    long getCounterForUsersSignUp(String profession, final AuthResult authResult, final String email) {
        final long[] counter = {0};
        if (profession.equals("Student")) {
            DatabaseReference studentReference;

            studentReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Student");

            studentReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        counter[0] = dataSnapshot.getChildrenCount();
                        Toast.makeText(SignUpActivity.this, String.valueOf(counter[0]), Toast.LENGTH_SHORT).show();
                        uploadUserData(authResult,email,counter[0]);

                    } else {
                        counter[0] = 0;
                        uploadUserData(authResult,email,counter[0]);

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        if (profession.equals("Teacher")) {
            DatabaseReference teacherReference;

            teacherReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Teacher");

            teacherReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        counter[0] = dataSnapshot.getChildrenCount();
                        uploadUserData(authResult,email,counter[0]);


                    } else {
                        counter[0] = 0;
                        uploadUserData(authResult,email,counter[0]);


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        if (profession.equals("Admin")) {
            DatabaseReference teacherReference;

            teacherReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Admin");

            teacherReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        counter[0] = dataSnapshot.getChildrenCount();
                        uploadUserData(authResult,email,counter[0]);


                    } else {
                        counter[0] = 0;
                        uploadUserData(authResult,email,counter[0]);


                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }


        return counter[0];

    }
    void uploadUserData(AuthResult authResult, String email, long counter){
        String user_id = authResult.getUser().getUid();
        Log.d(TAG, user_id);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("email", email);
        hashMap.put("counter", String.valueOf(counter));


        userRef.child(profession).child(user_id).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(SignUpActivity.this, "Registered Succesfully", Toast.LENGTH_SHORT).show();
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

    }
}
