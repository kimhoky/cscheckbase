package com.example.myapplication;

public class User {
    public String Username;
    public String Student_id;
    public String ID;
    public String Password;
    public String Email;
    public String is_professor;
    public String Time;
    public String Cs;

    public void Username(String username) {
        this.Username = username;
    }

    public void Student_id(String sid) {
        this.Student_id = sid;
    }

    public void ID(String id) {
        this.ID=id;
    }

    public void Password(String pwd) {
        this.Password = pwd;
    }

    public void Email(String email) {
        this.Email = email;
    }

    public void is_professor(String isp) {
        this.is_professor=isp;
    }

    public void Time(String time) {
        this.Time = time;
    }

    public void CsCheck(String csc) {
        this.Cs=csc;
    }

   /* public User(String Username,int Student_id,String ID,String Password,String Email,int is_professor,int Time,int CsCheck){
        this.Username =Username;
        this.Student_id=Student_id;
        this.ID=ID;
        this.Password=Password;
        this.Email=Email;
        this.is_professor=is_professor;
        this.Time=Time;
        this.CsCheck=CsCheck;
    }*/
}
