package com.example.feedback;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.feedback.Model.StudentDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Student_profile_activity extends AppCompatActivity {
    TextInputLayout full_name, mobile_number, year, branch, roll_no;
    Spinner section;
    Button submit;
    CircleImageView profile_image;
    Uri mainImageURI;


    FirebaseUser firebaseUser;
    DatabaseReference studentReference;
    StorageReference userProfileImageRef;
    FirebaseUser currentUser;
    String current_user_id;
    DatabaseReference userref;
    Toolbar toolbar;
    ChipGroup chipGroup;
    String student_gender = null;
    FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile_activity);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Profile Settings");
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);



        full_name = findViewById(R.id.student_full_name);
        mobile_number = findViewById(R.id.student_mobile_number);
        year = findViewById(R.id.student_batch);
        branch = findViewById(R.id.student_branch);
        roll_no = findViewById(R.id.student_roll_no);
        section = findViewById(R.id.student_section);
        submit = findViewById(R.id.btn_submit);
        chipGroup = findViewById(R.id.student_gender);

        profile_image = findViewById(R.id.student_profile_image);


        firebaseAuth = FirebaseAuth.getInstance();

        studentReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Student");
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        current_user_id = currentUser.getUid();


        RetriveOldUserData();

        userref = FirebaseDatabase.getInstance().getReference().child("Users").child("Student").child(current_user_id);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SaveUserSettings();
            }
        });

        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(Student_profile_activity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(Student_profile_activity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1, 1)
                            .start(Student_profile_activity.this);
                }
            }
        });

        chipGroup.setOnCheckedChangeListener(new ChipGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ChipGroup chipGroup, int i) {

                Chip chip = chipGroup.findViewById(i);
                if (chip == null) {
                    Toast.makeText(Student_profile_activity.this, "Select your gender", Toast.LENGTH_SHORT).show();
                    student_gender = null;

                }
                if (chip != null) {
                    Toast.makeText(getApplicationContext(), "Chip is " + chip.getText(), Toast.LENGTH_SHORT).show();
                    student_gender = chip.getText().toString().trim();
                }


            }
        });

        roll_no.getEditText().setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_NEXT) {
                    section.performClick();
                    roll_no.clearFocus();
                    return true;
                }
                return false;
            }
        });


    }

    private void RetriveOldUserData() {
        studentReference.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.hasChild("student_full_name")) {
                    StudentDetails studentDetails = dataSnapshot.getValue(StudentDetails.class);


                    if (dataSnapshot.hasChild("profile_image")) {
                        String pro_img = dataSnapshot.child("profile_image").getValue().toString();
                        Glide.with(Student_profile_activity.this).load(pro_img).placeholder(R.drawable.profile_image).into(profile_image);
                    }

                    full_name.getEditText().setText(studentDetails.getStudent_full_name());
                    mobile_number.getEditText().setText(studentDetails.getStudent_mobile_number());
                    year.getEditText().setText(studentDetails.getStudent_batch().substring(2, 4));
                    branch.getEditText().setText(studentDetails.getStudent_branch());
                    roll_no.getEditText().setText(studentDetails.getStudent_roll_no().substring(5, 8));
                    String gender = studentDetails.getStudent_gender();
                    if (gender.equals("Male")) {
                        chipGroup.check(R.id.male);
                    } else {
                        chipGroup.check(R.id.female);
                    }
                    if (studentDetails.getStudent_section().equals("Section-A")) {
                        section.setSelection(0, true);
                    } else if (studentDetails.getStudent_section().equals("Section-B")) {
                        section.setSelection(1, true);
                    } else if (studentDetails.getStudent_section().equals("Section-C")) {
                        section.setSelection(2, true);
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SaveUserSettings() {
        String student_full_name = full_name.getEditText().getText().toString().trim();
        String student_mobile_number = mobile_number.getEditText().getText().toString().trim();
        String student_year = year.getEditText().getText().toString().trim();
        String student_branch = branch.getEditText().getText().toString().trim();
        String student_roll_no = student_year + student_branch + roll_no.getEditText().getText().toString().trim();
        String student_section = section.getSelectedItem().toString();
        String student_index = "20" + student_year + "_" + student_branch + "_" + student_section;


        if (TextUtils.isEmpty(student_full_name) || TextUtils.isEmpty(student_mobile_number) || TextUtils.isEmpty(student_branch) || TextUtils.isEmpty(student_year) || TextUtils.isEmpty(student_roll_no)) {
            Toast.makeText(this, "All the fields are importants", Toast.LENGTH_SHORT).show();
        } else if (student_gender == null) {
            Toast.makeText(this, "Select your gender", Toast.LENGTH_SHORT).show();
        } else {
            SaveSettingstoFirebase(student_full_name, student_mobile_number, student_year, student_branch, student_roll_no, student_section, student_gender, student_index);
        }
    }

    private void SaveSettingstoFirebase(String student_full_name, String student_mobile_number, String student_year, String student_branch, String student_roll_no, String student_section, String student_gender, String student_index) {
        String uid = firebaseUser.getUid();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("uid", uid);
        hashMap.put("student_full_name", student_full_name);
        hashMap.put("student_mobile_number", student_mobile_number);
        String student_batch = "20" + student_year + "-20" + (Integer.parseInt(student_year) + 4);
        hashMap.put("student_batch", student_batch);
        hashMap.put("student_branch", student_branch);
        hashMap.put("student_roll_no", student_roll_no);
        hashMap.put("student_section", student_section);
        hashMap.put("student_gender", student_gender);
        hashMap.put("student_index", student_index);


        studentReference.child(uid).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(Student_profile_activity.this, "Successfully updated!!", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Student_profile_activity.this, e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageURI = result.getUri();
                profile_image.setImageURI(mainImageURI);
                StorageReference storageReference = userProfileImageRef.child(current_user_id + ".jpg");
                storageReference.putFile(mainImageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String download_uri = uri.toString();
                                    userref.child("profile_image").setValue(download_uri).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(Student_profile_activity.this, "Successfully uploaded the image", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(Student_profile_activity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                }
                            });
                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Toast.makeText(getApplicationContext(), "Back button clicked", Toast.LENGTH_SHORT).show();
                break;
        }
        return true;
    }


}
