package com.app.connection;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.BatteryManager;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.BroadcastReceiver;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button button,logout_btn,cam_btn;
    TextView textView,internet,battery,batteryper,latitudeTextView, longitTextView,timeStamp,IMEI;
    FusedLocationProviderClient mFusedLocationClient;
    FirebaseUser user;
    int REQUEST_CODE = 101;
    int PERMISSION_ID = 44;
    String imei;
    Context context;
    Intent intent;
    Uri uri;
    int level,status;
    boolean isCharging;
    Location location;
    TelephonyManager telephonyManager;
    DatabaseReference reference;
    FirebaseDatabase firebaseDatabase;
    String inter,battery_per,battery_pow,lan,logi,times,user_mail,number,image;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=getApplicationContext();
        auth = FirebaseAuth.getInstance();
        imageView=findViewById(R.id.image_view);
        button = findViewById(R.id.get_data);
        textView = findViewById(R.id.user_details);
        internet=findViewById(R.id.connection);
        battery=findViewById(R.id.battery);
        cam_btn=findViewById(R.id.camera);
        batteryper=findViewById(R.id.batteryper);
        latitudeTextView = findViewById(R.id.latTextView);
        longitTextView = findViewById(R.id.lonTextView);
        timeStamp=findViewById(R.id.timestamp);
        IMEI=findViewById(R.id.IMEI);
        logout_btn=findViewById(R.id.logout_btn);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


try{
        int permisI=ContextCompat.checkSelfPermission(this,Manifest.permission.READ_PHONE_STATE);
        if(permisI==PackageManager.PERMISSION_GRANTED){
            telephonyManager=(TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                imei=telephonyManager.getImei().toString();
            }
        }
        else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_PHONE_STATE},123);
        }
        }
catch (Exception e)
{
    imei="Not Available for this Device";
}

        this.registerReceiver(this.broadcastReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));

        user = auth.getCurrentUser();
        if (user == null) {
            Intent intent = new Intent(getApplicationContext(), Login.class);
            startActivity(intent);
        }
        else {
            textView.setText(user.getEmail());
        }
        logout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(),Login.class);
                startActivity(intent);

            }
        });
        cam_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(MainActivity.this)
                        .crop()	    			//Crop image(Optional), Check Customization for more option
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
        final Handler handler=new Handler();
        Runnable runnable=new Runnable() {
            @Override
            public void run() {
                //TIMESTAMP
                Long tsLong = System.currentTimeMillis()/1000;
                String ts = tsLong.toString();
                timeStamp.setText("TimeStamp:-"+ts);

                //LOCATION
                getLastLocation();
                if (location == null) {
                    requestNewLocationData();
                } else {
                    latitudeTextView.setText(location.getLatitude() + "");
                    longitTextView.setText(location.getLongitude() + "");
                }
                //BATTERY
                batteryper.setText("Battery Percentage: " + level);
                if (isCharging) {
                    battery.setText("Charger connected, Battery Charging..");

                }
                else if (status == BatteryManager.BATTERY_STATUS_DISCHARGING) {
                    battery.setText("Charger disconnected");
                }
                //INTERNET
                if (isConnected()) {
                    internet.setText("Internet Connected");
                } else {
                    internet.setText("Internet Not Connected");
                }
                //IMEI
                IMEI.setText("IMEI NUMBER:-"+imei);
                handler.postDelayed(this,90000);
                getValues();

            }
        };
        handler.post(runnable);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TIMESTAMP
                Long tsLong = System.currentTimeMillis()/1000;
                String ts = tsLong.toString();
                timeStamp.setText("TimeStamp:-"+ts);

                //LOCATION
                getLastLocation();
                if (location == null) {
                    requestNewLocationData();
                } else {
                    latitudeTextView.setText(location.getLatitude() + "");
                    longitTextView.setText(location.getLongitude() + "");
                }
                //BATTERY
                batteryper.setText("Battery Percentage: " + level);
                if (isCharging) {
                    battery.setText("Charger connected, Battery Charging..");

                }
                else if (status == BatteryManager.BATTERY_STATUS_DISCHARGING) {
                    battery.setText("Charger disconnected");
                }
                //INTERNET
                if (isConnected()) {
                    internet.setText("Internet Connected");
                } else {
                    internet.setText("Internet Not Connected");
                }
                //IMEI
                IMEI.setText("IMEI NUMBER:-"+imei);
                getValues();
            }
        });
    }

    private void getValues() {
        inter=internet.getText().toString();
        battery_per=batteryper.getText().toString();
        battery_pow=battery.getText().toString();
        lan=latitudeTextView.getText().toString();
        logi=longitTextView.getText().toString();
        times=timeStamp.getText().toString();
        user_mail=user.getEmail();
        number=imei;
        image=String.valueOf(uri);
Values values=new Values(inter,battery_per,battery_pow,lan,logi,times,user_mail,number,image);
firebaseDatabase=FirebaseDatabase.getInstance();
reference=firebaseDatabase.getReference("Values");
reference.child("Value").setValue(values);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uri=data.getData();
        imageView.setImageURI(uri);
        image=String.valueOf(uri);
    }

    //LOCATION
    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        location = task.getResult();
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }
    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }
    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            latitudeTextView.setText("Latitude: " + mLastLocation.getLatitude() + "");
            longitTextView.setText("Longitude: " + mLastLocation.getLongitude() + "");
        }
    };
    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;

    }
    private void requestPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }

    }
    @Override
    public void onResume() {
        super.onResume();
        if (checkPermissions()) {
            getLastLocation();
        }
    }


    //BATTERY PERCENTAGE
    public BroadcastReceiver broadcastReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status ==
                    BatteryManager.BATTERY_STATUS_FULL;

        }
    };



    //INTERNET CONNECTED
    public boolean isConnected() {
        boolean connected = false;
        try {
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        } catch (Exception e) {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }
}