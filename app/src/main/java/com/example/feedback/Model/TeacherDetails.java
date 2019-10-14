package com.example.feedback.Model;

public class TeacherDetails {
    String teacher_name,teacher_phone,email,profile_image;

    public TeacherDetails() {
    }

    public TeacherDetails(String teacher_name, String teacher_phone, String email,String profile_image) {
        this.teacher_name = teacher_name;
        this.teacher_phone = teacher_phone;
        this.email = email;
        this.profile_image=profile_image;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public String getTeacher_phone() {
        return teacher_phone;
    }

    public void setTeacher_phone(String teacher_phone) {
        this.teacher_phone = teacher_phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
