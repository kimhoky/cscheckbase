package com.example.myapplication;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class CsCheck extends AppCompatActivity {
    long mNow;
    Date mDate;
    SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    TextView mTextView;
    Button mRefreshBtn;
    Button CSstart;

    Button button1;
    Button csstart;
    TextView longitudevie;
    TextView latitudevie;
    TextView Con;
    TextView nlongitudevie;
    TextView nlatitudevie;
    TextView name;


    private static final String PRIMAY_CHANNEL_ID = "primary_notification_channel";

    private NotificationManager mNotificationManager;

    private static final int NOTIFICATION_ID=0;


    int alram_val = 0;

    RadioGroup radioGroup;

    RadioButton GSRadioButton;
    RadioButton STRadioButton;
    double platitude =0;
    double plongtitude = 0;

    String csCheck;

    //private GpsTracker gpsTracker;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cscheck);

        radibtn();
        createNotificationChannel();

        Button tolist = (Button) findViewById(R.id.toList);
        tolist.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(),ListOpener.class);
                startActivity(intent);
            }
        });
    }

    public void createNotificationChannel(){
        mNotificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel nfcc = new NotificationChannel(PRIMAY_CHANNEL_ID,"Test",mNotificationManager.IMPORTANCE_HIGH);
            nfcc.enableLights(true);
            nfcc.setLightColor(Color.RED);
            nfcc.enableVibration(true);
            nfcc.setDescription("NOTIFICATION");

            mNotificationManager.createNotificationChannel(nfcc);

        }

    }
    private NotificationCompat.Builder getNotificationBuilder(){
        Intent notify = new Intent(this,CsCheck.class);
       // notify.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent notifyPending = PendingIntent.getActivity
                (this,NOTIFICATION_ID,notify,PendingIntent.FLAG_MUTABLE|PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notifyBuilder = new NotificationCompat.Builder(this,PRIMAY_CHANNEL_ID)
                .setContentTitle("출석알람!")
                .setContentText("출석하세요!")
                .setSmallIcon(R.drawable.clock)
                .setContentIntent(notifyPending)
                .setAutoCancel(true);
        return notifyBuilder;
    }
    public void sendNotification(){
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        mNotificationManager.notify(NOTIFICATION_ID,notifyBuilder.build());
    }




    public void radibtn() {
        GSRadioButton = (RadioButton) findViewById(R.id.GSradibtn);
        STRadioButton = (RadioButton) findViewById(R.id.STDradibtn);
       /* GSRadioButton.setOnClickListener(radioButtonClickListener);
        STRadioButton.setOnClickListener(radioButtonClickListener); */
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(radioGroupButtonChangeListener);

        button1 = (Button) findViewById(R.id.refreshBtn);
        csstart = (Button) findViewById(R.id.CSstart);



    }

        RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener = new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if (i == R.id.STDradibtn) {
                    if(alram_val==1)
                    {sendNotification();}
                    csstart.setEnabled(false);

                    stbtns();


                } else if (i == R.id.GSradibtn) {
                    csstart.setEnabled(true);
                    gsbtns();
                }
            }
        };


    public void gsbtns(){
        button1.setVisibility(View.INVISIBLE);
        csstart.setVisibility(View.VISIBLE);

        btnchange();
    }
    public void stbtns(){
        button1.setVisibility(View.VISIBLE);
        csstart.setVisibility(View.INVISIBLE);
        btns();
    }

    // 타이머 TimeRest에 전달된 시간만큼 타이머 작동
    //onTick은 초마다 실행할 명령어
    //onfinish로 출석체크 버튼 비활성화

    class TimerRest extends CountDownTimer {

        public TimerRest(long millisInFuture, long countDownInterval){
            super(millisInFuture,countDownInterval);
        }
        public void onTick(long millisInFuture){

        }
        public void onFinish(){
            button1.setEnabled(false);
            onResume();

        }
    }

    //학생 좌표받기
    public class gpsresult{
        GpsTracker gpsTracker = new GpsTracker(CsCheck.this);
        double lat = gpsTracker.getLatitude();
        double lon = gpsTracker.getLongitude();



    }
    //교수 좌표받기
    public class pgpsresult{
        GpsTracker gpsTracker = new GpsTracker(CsCheck.this);
        double lat = gpsTracker.getPRLatitude();
        double lon = gpsTracker.getPRLongitude();



    }





    //출석체크 버튼 클릭시 발생 이벤트 / 교수의 좌표를 받아 전달하고 타이머 5분 시작
    public void btnchange(){

        Button CSstart = (Button) findViewById(R.id.CSstart);
        //5분 타이머
        TimerRest timer = new TimerRest(300000,1000);
        // prime pr = new prime();
        pgpsresult pgp = new pgpsresult();
        CSstart.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View arg0)
            {
                double prlatitude =0;
                double prlongitude =0;



                prlatitude = pgp.lat;
                prlongitude = pgp.lon;
                platitude = prlatitude;
                plongtitude = prlongitude;
                button1.setEnabled(true);
                timer.start();
                alram_val=1;

            }

        });


    }




    //User 리스트에 db값 전달

    public List<User> userList ;

    public void initLoadDB() {

        DataAdapter mDbHelper = new DataAdapter(getApplicationContext());
        mDbHelper.createDatabase();
        mDbHelper.open();

        // db에 있는 값들을 model을 적용해서 넣는다.
        userList = mDbHelper.getTableData();

        // db 닫기
        mDbHelper.close();
    }

//    public void initLoad() {
//
//        DataBaseHelper dbhelper =new DataBaseHelper(this);
//        SQLiteDatabase db = dbhelper.getReadableDatabase();
//        Cursor cursor = db.rawQuery("SELECT * FROM student",null);
//        CSLIST cslist = new CSLIST();
//
//        String va1="";
//        String va2="";
//        String va3="";
//        String va4="";
//        while(cursor.moveToNext()){
//            cslist.addItemToList(cursor.getString(1),cursor.getString(0),cursor.getString(6),cursor.getString(7));
////            va1 = cursor.getString(1);
////            va2 = cursor.getString(0);
////            va3 = cursor.getString(6);
////            va4= cursor.getString(7);
//        }
////        Toast.makeText(getApplicationContext(),va3,Toast.LENGTH_LONG).show();
//
//
//        dbhelper.close();
//        db.close();
//    }



    //    public class prime{
//
//        double platitude =0;
//        double plongtitude = 0;
//    }
    public class dist{

        double dislat = 0;
        double dislon = 0;
    }
    public void btns(){
        mTextView = (TextView) findViewById(R.id.textView);

        mRefreshBtn = (Button) findViewById(R.id.refreshBtn);

        //bind listener
        mRefreshBtn.setOnClickListener(this::onClick);




        longitudevie = (TextView)findViewById(R.id.longitudevie);
        latitudevie = (TextView)findViewById(R.id.latitudevie);

        Con = (TextView)findViewById(R.id.Con);

        nlongitudevie = (TextView)findViewById(R.id.nlongitudevie);
        nlatitudevie = (TextView)findViewById(R.id.nlatitudevie);

        if (!checkLocationServicesStatus()) {

            showDialogForLocationServiceSetting();
        }else {

            checkRunTimePermission();
        }


        //  prime pr = new prime();
        dist di = new dist();
        gpsresult gp = new gpsresult();





        TextView textview_latitude = (TextView)findViewById(R.id.latitudevie);
        TextView textview_longitude = (TextView)findViewById(R.id.longitudevie);


        Button ShowLocationButton = (Button) findViewById(R.id.refreshBtn);
        ShowLocationButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                gpsresult gp = new gpsresult();

                double latitude=0;
                double longitude=0;


                latitude = gp.lat;
                longitude = gp.lon;

                if((Math.abs(latitude)>=Math.abs(platitude)))
                    di.dislat = Math.abs(latitude) - Math.abs(platitude);
                else
                    di.dislat = Math.abs(platitude) - Math.abs(latitude);

                if((Math.abs(longitude)>=Math.abs(plongtitude)))
                    di.dislon = Math.abs(longitude) - Math.abs(plongtitude);
                else
                    di.dislon = Math.abs(plongtitude) - Math.abs(longitude);

                String dilon = String.format("%.7f",di.dislon);
                String dilat = String.format("%.7f", di.dislat);


                mTextView.setText(getTime());
               textview_latitude.setText(String.valueOf(latitude));
               textview_longitude.setText(String.valueOf(longitude));


                nlongitudevie.setText(dilon);
               nlatitudevie.setText(dilat);

//                nlongitudevie.setText(String.valueOf(di.dislon));
//                nlatitudevie.setText(String.valueOf(di.dislat));
                if(Math.abs(di.dislat) <= 0.0002 && Math.abs(di.dislon) <=0.002)
                    csCheck = "O";
                else
                    csCheck = "X";



                Con.setText(String.valueOf(csCheck));
               // initLoadDB();
                getVal();
                //initLoad();









            }
        });
    }

    //데이터 베이스 값 읽기
    String val;
    void getVal(){

        //Dbhelper의 읽기모드 객체를 가져와 SQLiteDatabase에 담아 사용준비
        DataBaseHelper helper = new DataBaseHelper(this);
        SQLiteDatabase database = helper.getReadableDatabase();

//        try
//        {
//           helper.DoCopyDB();
//        }
//        catch (IOException mIOException)
//        {
//            Log.e(TAG, mIOException.toString() + "  UnableToCreateDatabase");
//            throw new Error("UnableToCreateDatabase");
//        }

        //Cursor라는 그릇에 목록을 담아주기
        Cursor cursor = database.rawQuery("SELECT * FROM student WHERE Username = '김경호'" ,null);

         helper.insertData(getTime(),csCheck,"김강호");
//        helper.insertTimeData(getTime(),"김경호");
//        helper.insertCSData(csCheck,"김경호");
//        if(cursor != null && cursor.moveToFirst())
        //목록의 개수만큼 순회하여 adapter에 있는 list배열에 add
        while(cursor.moveToNext()){
            //num 행은 가장 첫번째에 있으니 0번이 되고, name은 1번

            val = cursor.getString(7);
//            if(isInserted==true)
//

        }


      //  boolean isInserted =



                name=(TextView)findViewById(R.id.name);
        name.setText(String.valueOf(val));
        cursor.close();
        helper.close();



    }





    String getTime(){
        mNow = System.currentTimeMillis();
        mDate = new Date(mNow);
        SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return mFormat.format(mDate);
    }

    public void onClick(View v) {
        switch (v.getId()){
            case R.id.refreshBtn:
                mTextView.setText(getTime());
                break;
            default:
                break;
        }
    }



//    void MKList(){
//        ArrayList<User> user;
//
//    }









    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        super.onRequestPermissionsResult(permsRequestCode, permissions, grandResults);
        if (permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면

            boolean check_result = true;


            // 모든 퍼미션을 허용했는지 체크합니다.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if (check_result) {

                //위치 값을 가져올 수 있음
                ;
            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다.2 가지 경우가 있습니다.

                if (ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(CsCheck.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();


                } else {

                    Toast.makeText(CsCheck.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    void checkRunTimePermission(){

        //런타임 퍼미션 처리
        // 1. 위치 퍼미션을 가지고 있는지 체크합니다.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(CsCheck.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(CsCheck.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. 이미 퍼미션을 가지고 있다면
            // ( 안드로이드 6.0 이하 버전은 런타임 퍼미션이 필요없기 때문에 이미 허용된 걸로 인식합니다.)


            // 3.  위치 값을 가져올 수 있음



        } else {  //2. 퍼미션 요청을 허용한 적이 없다면 퍼미션 요청이 필요합니다. 2가지 경우(3-1, 4-1)가 있습니다.

            // 3-1. 사용자가 퍼미션 거부를 한 적이 있는 경우에는
            if (ActivityCompat.shouldShowRequestPermissionRationale(CsCheck.this, REQUIRED_PERMISSIONS[0])) {

                // 3-2. 요청을 진행하기 전에 사용자가에게 퍼미션이 필요한 이유를 설명해줄 필요가 있습니다.
                Toast.makeText(CsCheck.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                // 3-3. 사용자게에 퍼미션 요청을 합니다. 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(CsCheck.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다.
                // 요청 결과는 onRequestPermissionResult에서 수신됩니다.
                ActivityCompat.requestPermissions(CsCheck.this, REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }


    public String getCurrentAddress( double latitude, double longitude) {

        //지오코더... GPS를 주소로 변환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try {

            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    7);
        } catch (IOException ioException) {
            //네트워크 문제
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";

        }



        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";

        }

        Address address = addresses.get(0);
        return address.getAddressLine(0).toString()+"\n";

    }


    //여기부터는 GPS 활성화를 위한 메소드들
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(CsCheck.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n"
                + "위치 설정을 수정하실래요?");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);

            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //사용자가 GPS 활성 시켰는지 검사
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d("@@@", "onActivityResult : GPS 활성화 되있음");
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }
    }

    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

}







