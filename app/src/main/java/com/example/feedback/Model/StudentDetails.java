package com.example.feedback.Model;

public class StudentDetails {
String student_full_name,student_mobile_number,student_roll_no,student_section,student_branch,student_batch,profile_image,email,student_gender;

    public StudentDetails() {
    }

    public StudentDetails(String student_full_name, String student_mobile_number, String student_roll_no, String student_section, String student_branch, String student_batch, String profile_image, String email,String student_gender) {
        this.student_full_name = student_full_name;
        this.student_mobile_number = student_mobile_number;
        this.student_roll_no = student_roll_no;
        this.student_section = student_section;
        this.student_branch = student_branch;
        this.student_batch = student_batch;
        this.profile_image = profile_image;
        this.email = email;
        this.student_gender=student_gender;


    }

    public String getStudent_gender() {
        return student_gender;
    }

    public void setStudent_gender(String student_gender) {
        this.student_gender = student_gender;
    }

    public String getStudent_full_name() {
        return student_full_name;
    }

    public void setStudent_full_name(String student_full_name) {
        this.student_full_name = student_full_name;
    }

    public String getStudent_mobile_number() {
        return student_mobile_number;
    }

    public void setStudent_mobile_number(String student_mobile_number) {
        this.student_mobile_number = student_mobile_number;
    }

    public String getStudent_roll_no() {
        return student_roll_no;
    }

    public void setStudent_roll_no(String student_roll_no) {
        this.student_roll_no = student_roll_no;
    }

    public String getStudent_section() {
        return student_section;
    }

    public void setStudent_section(String student_section) {
        this.student_section = student_section;
    }

    public String getStudent_branch() {
        return student_branch;
    }

    public void setStudent_branch(String student_branch) {
        this.student_branch = student_branch;
    }

    public String getStudent_batch() {
        return student_batch;
    }

    public void setStudent_batch(String student_batch) {
        this.student_batch = student_batch;
    }

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
