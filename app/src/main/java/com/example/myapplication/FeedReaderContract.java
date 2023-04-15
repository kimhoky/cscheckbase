package com.example.myapplication;

import android.provider.BaseColumns;

public class FeedReaderContract {
    private FeedReaderContract(){}

    public static class FeedEntry implements BaseColumns{


        public static  String Username;
        public static  String Student_id;
        public static  String ID;
        public static  String Password;
        public static  String Email;
        public static  String is_professor;
        public static  String Time;
        public static  String CsCheck;
    }
}
