package com.example.myapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ListOpener extends AppCompatActivity {

    ListView listView;

    CsCheck cs = new CsCheck();
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cslist);
        CSLIST cslist = new CSLIST();
        listView = (ListView)findViewById(R.id.cslistview);

        initLoad();
    }

    public void initLoad() {

        DataBaseHelper dbhelper =new DataBaseHelper(this);
        SQLiteDatabase db = dbhelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM student",null);
        CSLIST cslist = new CSLIST();

        String va1="";
        String va2="";
        String va3="";
        String va4="";
        while(cursor.moveToNext()){
            cslist.addItemToList(cursor.getString(1),cursor.getString(0),cursor.getString(6),cursor.getString(7));
//            va1 = cursor.getString(1);
//            va2 = cursor.getString(0);
//            va3 = cursor.getString(6);
//            va4= cursor.getString(7);
        }
//        Toast.makeText(getApplicationContext(),va3,Toast.LENGTH_LONG).show();

        listView.setAdapter(cslist);
        dbhelper.close();
        db.close();
    }
//

}
