package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import java.lang.reflect.Array;

import java.util.ArrayList;
import java.util.List;
import com.example.myapplication.DataAdapter;

public class CSLIST extends BaseAdapter {

    ArrayList<ListAdapter> user = new ArrayList<ListAdapter>();

    public int getCount(){
        return user.size();
    }

    public long getItemId(int position){
        return position;
    }

    public Object getItem(int position)
    {
        return user.get(position);
    }


    public View getView(int position, View view, ViewGroup viewGroup){
        final Context context = viewGroup.getContext();
        //View view = mLayoutInflater.inflate(R.layout.activity_listitem, null);
        if(view == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.activity_listitem,viewGroup,false);
        }
        TextView STID = (TextView)view.findViewById(R.id.STID);
        TextView STNAME = (TextView)view.findViewById(R.id.STNAME);
        TextView STTIME = (TextView)view.findViewById(R.id.STTIME);
        TextView STCHECK = (TextView)view.findViewById(R.id.STCHECK);

        ListAdapter list = user.get(position);

        STID.setText(list.getStudent_id());
        STNAME.setText(list.getUsername());
        STTIME.setText(list.getTime());
        STCHECK.setText(list.getCs());

        return view;
    }

    public void addItemToList(String Student_id, String Username, String Time, String Cs){
        ListAdapter listdata = new ListAdapter();
        listdata.setStudent_id(Student_id);
        listdata.setUsername(Username);
        listdata.setTime(Time);
        listdata.setCs(Cs);

        user.add(listdata);


    }

}

