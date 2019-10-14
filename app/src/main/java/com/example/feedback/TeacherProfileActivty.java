package com.example.feedback;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class TeacherProfileActivty extends AppCompatActivity {
    TextInputLayout name, phone;
    Button submit;
    DatabaseReference teacherRef;
    FirebaseAuth firebaseAuth;
    Spinner spinner_profession;
    CircleImageView profile_image;
    Uri mainImageURI;
    StorageReference userProfileImageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_profile_activty);


        name = findViewById(R.id.teacher_name);
        phone = findViewById(R.id.teacher_phone_number);
        submit = findViewById(R.id.btn_submit);

        profile_image = findViewById(R.id.student_profile_image);

        spinner_profession = findViewById(R.id.profession);
        firebaseAuth = FirebaseAuth.getInstance();
        userProfileImageRef = FirebaseStorage.getInstance().getReference().child("Profile Images");


        teacherRef = FirebaseDatabase.getInstance().getReference().child("Users").child("Teacher");
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validateData();
            }
        });


        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ContextCompat.checkSelfPermission(TeacherProfileActivty.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(TeacherProfileActivty.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                } else {
                    CropImage.activity()
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setAspectRatio(1, 1)
                            .start(TeacherProfileActivty.this);
                }
            }
        });

    }

    private void validateData() {
        String teacher_name = name.getEditText().getText().toString().trim();
        String teacher_phone_number = phone.getEditText().getText().toString().trim();
        String profession = spinner_profession.getSelectedItem().toString();

        if (TextUtils.isEmpty(teacher_name) || TextUtils.isEmpty(teacher_phone_number)) {
            Toast.makeText(this, "Empty fields are not allowed", Toast.LENGTH_SHORT).show();
        } else {
            UploadTeacherData(teacher_name, teacher_phone_number, profession);
        }
    }

    private void UploadTeacherData(String teacher_name, String teacher_phone_number, String profession) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("teacher_name", teacher_name);
        hashMap.put("teacher_phone", teacher_phone_number);
        hashMap.put("profession", profession);

        teacherRef.child(firebaseAuth.getCurrentUser().getUid()).updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(TeacherProfileActivty.this, "succesfully updated", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TeacherProfileActivty.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
                final String current_user_id = firebaseAuth.getCurrentUser().getUid();

                StorageReference storageReference = userProfileImageRef.child(current_user_id + ".jpg");
                storageReference.putFile(mainImageURI).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            task.getResult().getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String download_uri = uri.toString();
                                    teacherRef.child(current_user_id).child("profile_image").setValue(download_uri).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                Toast.makeText(TeacherProfileActivty.this, "Successfully uploaded the image", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Toast.makeText(TeacherProfileActivty.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
}
