package com.example.feedback.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class TeacherDetails implements Parcelable {
    String teacher_name, teacher_phone, email, profile_image, uid, profession, counter;

    public TeacherDetails() {
    }

    public TeacherDetails(String teacher_name, String teacher_phone, String email, String profile_image, String uid, String profession, String counter) {
        this.teacher_name = teacher_name;
        this.teacher_phone = teacher_phone;
        this.email = email;
        this.profile_image = profile_image;
        this.uid = uid;
        this.profession = profession;
        this.counter = counter;
    }

    public TeacherDetails(Parcel parcel) {
        this.teacher_name = parcel.readString();
        this.teacher_phone = parcel.readString();
        this.email = parcel.readString();
        this.profile_image = parcel.readString();
        this.uid = parcel.readString();
        this.profession = parcel.readString();
        this.counter = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.teacher_name);
        parcel.writeString(this.teacher_phone);
        parcel.writeString(this.email);
        parcel.writeString(this.profile_image);
        parcel.writeString(this.uid);
        parcel.writeString(this.profession);
        parcel.writeString(this.counter);
    }

    public static final Creator<TeacherDetails> CREATOR = new Creator<TeacherDetails>() {
        @Override
        public TeacherDetails createFromParcel(Parcel parcel) {
            return new TeacherDetails(parcel);

        }

        @Override
        public TeacherDetails[] newArray(int i) {
            return new TeacherDetails[i];
        }
    };


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

    public String getProfile_image() {
        return profile_image;
    }

    public void setProfile_image(String profile_image) {
        this.profile_image = profile_image;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }


}
