package com.example.feedback;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;

public class TeacherMainActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    Toolbar toolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_main);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        firebaseAuth=FirebaseAuth.getInstance();




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.teacher_menu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.teacher_edit_profile:
                profileActivity();
                return true;
            case R.id.show_teacher_list:
                openTeacherList();
                return true;
            case R.id.logout:
                logout();
                return true;

        }
        return super.onOptionsItemSelected(item);

    }

    private void profileActivity() {
        Intent intent=new Intent(TeacherMainActivity.this,TeacherProfileActivty.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    private void openTeacherList() {
        Intent intent=new Intent(TeacherMainActivity.this,TeacherListActivity.class);
        startActivity(intent);
    }
    private void logout() {
        firebaseAuth.signOut();
        Intent intent=new Intent(TeacherMainActivity.this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

    }
}
