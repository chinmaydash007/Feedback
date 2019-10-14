package com.example.feedback;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

public class LoginActivity extends AppCompatActivity {
    TextInputLayout login_email, login_password;
    ImageButton login_btn;
    FirebaseAuth firebaseAuth;
    TextView need_a_new_account;
    DatabaseReference userDatabaseRef;
    ChipGroup chipGroup;
    String usertype = null;
    boolean no_chip_seleted=true;

    @Override
    protected void onStart() {
        super.onStart();
        checkforAlreadyLoggedIn();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userDatabaseRef = FirebaseDatabase.getInstance().getReference().child("Users");


        login_email = findViewById(R.id.login_email);
        login_password = findViewById(R.id.login_password);
        login_btn = findViewById(R.id.btn_login);
        need_a_new_account = findViewById(R.id.need_a_new_account);
        chipGroup = findViewById(R.id.user_type);


        firebaseAuth = FirebaseAuth.getInstance();
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = login_email.getEditText().getText().toString();
                String password = login_password.getEditText().getText().toString();
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password) ||no_chip_seleted) {
                    Toast.makeText(LoginActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else {
                    login(email, password);
                }
            }
        });

        need_a_new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });


        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {

                Chip chip = chipGroup.findViewById(i);
                if (chip == null) {
                    no_chip_seleted=true;
                    Toast.makeText(LoginActivity.this, "Select your profession", Toast.LENGTH_SHORT).show();
                }
                if (chip != null) {
                    no_chip_seleted=false;
                    Toast.makeText(getApplicationContext(), "Chip is " + chip.getText(), Toast.LENGTH_SHORT).show();
                    usertype = chip.getText().toString().trim();
                }


            }
        });



    }

    private void checkforAlreadyLoggedIn() {
        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
            final String userID=FirebaseAuth.getInstance().getCurrentUser().getUid();
            DatabaseReference userRef=FirebaseDatabase.getInstance().getReference().child("Users");
            userRef.child("Student").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild(userID)){
                        sendToStudentActivity();
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            userRef.child("Teacher").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild(userID)){
                        sendToTeachersActivity();

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            userRef.child("Admin").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.hasChild(userID)){
                        sendToAdminActivtiy();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void sendToStudentActivity() {
        Intent intent=new Intent(LoginActivity.this,StudentMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    private void sendToAdminActivtiy() {
        Intent intent=new Intent(LoginActivity.this,AdminMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    private void sendToTeachersActivity() {
        Intent intent=new Intent(LoginActivity.this,TeacherMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void login(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                final String user = authResult.getUser().getUid();
                Toast.makeText(LoginActivity.this, user, Toast.LENGTH_SHORT).show();
                DatabaseReference userTypeRef = FirebaseDatabase.getInstance().getReference().child("Users").child(usertype);
                userTypeRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(user)) {
                            if(usertype.equals("Student")) {
                                Intent intent = new Intent(LoginActivity.this, StudentMainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();
                            }
                            if(usertype.equals("Teacher")){
                                Intent intent = new Intent(LoginActivity.this, TeacherMainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();

                            }
                            if(usertype.equals("Admin")){
                                Intent intent = new Intent(LoginActivity.this, AdminMainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                finish();

                            }
                        } else {

                            FirebaseAuth.getInstance().signOut();
                            Toast.makeText(LoginActivity.this, "You are not allowed to signin as " + usertype, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
}
