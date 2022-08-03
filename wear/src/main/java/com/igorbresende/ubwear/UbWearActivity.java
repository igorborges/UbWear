package com.igorbresende.ubwear;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

import com.igorbresende.ubwear.databinding.ActivityUbwearBinding;
import com.orhanobut.hawk.Hawk;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UbWearActivity extends Activity {

    private TextView mUpperMessage;
    private TextView mMain;
    private TextView mTimeUnit;
    private TextView mLowerMessage;
    private TextView mLeftMessage;
    private ActivityUbwearBinding binding;
    private ActivityUbwearBinding bindingLogin;
    private int time = 500;
    private String timeMessage;
    private String timeUnit;
    String cityName = null;
    String street = null;
    String number = null;
    String csid = "1.1661358874304.jjDBLcc2Ui7hLKmQEknqKPnru4he/339065GoCL7KEY=";
    String sid = "QA.CAESEGa32UvsU0UqkeCyC4SPBbkYmqaZmAYiATEqJDlkYTM0NjYwLWY4MzItNGMyOS04NWE0LTk3ZjM4M2RmOTMzNDI8xV84r6joTrez2AhIaViCrDZ4TGbFCE9xj9xfAWY41MGgksbLbRKwbwi6MD59cyQr27lGvfyGg2tRpcH0OgExQgh1YmVyLmNvbQ.hMbvC6Ab5gSwRcohUTzBB2oNbYHFApocxIn30qXKfT8";
    private String jsonData;

    private Map<String, String> languageMaps = new LanguageMaps().getMap(Locale.getDefault().getLanguage());

    private double lat;
    private double lon;

    Handler handler = new Handler();
    Runnable runnable;
    int refreshTime = 5000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        binding = ActivityUbwearBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getStatus();
    }

    public void getStatus() {

//blabla();
        getLatLong();
        String cookies = Hawk.get("cookies");
//        String cookies = "csid=" + csid + "; sid=" + sid;
//        lat = -19.9241351;
//        lon = -43.9521718;

        getAddressFromLatLong();


//        System.out.println("cookies sid csid");
//        System.out.println(cookies);

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "{\"latitude\":" + lat + ",\"longitude\":" + lon + "}");
        Request request = new Request.Builder()
                .url("https://m.uber.com/api/getStatus?localeCode=pt-BR")
                .method("POST", body)
                .addHeader("content-type", "application/json")
                .addHeader("cookie", cookies)
                .addHeader("x-csrf-token", "x")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String jsonData = response.body().string();
            JSONObject Jobject = new JSONObject(jsonData);
            JSONObject Jdata = (JSONObject) Jobject.get("data");
            JSONObject JclientStatus = (JSONObject) Jdata.get("clientStatus");
            String statusDaCorrida = JclientStatus.getString("status");

            System.out.println("status: " + JclientStatus.get("status"));
            System.out.println("status: " + Jdata);
            if (statusDaCorrida.equalsIgnoreCase("Looking")) {
                JSONObject Jeyeball = (JSONObject) Jdata.get("eyeball");
                JSONObject JnearbyVehicles = (JSONObject) Jeyeball.get("nearbyVehicles");

                Iterator<String> keys = JnearbyVehicles.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    if (JnearbyVehicles.get(key) instanceof JSONObject) {
                        JSONObject Jvehicle = (JSONObject) JnearbyVehicles.get(key);
                        if (Jvehicle.has("etaStringShort")) {
                            int vehicleTime = Integer.parseInt(Jvehicle.get("etaStringShort").toString().split(" ")[0]);
                            String vehicleUnit = Jvehicle.get("etaStringShort").toString().split(" ")[1];
                            if (vehicleTime < time) {
                                time = vehicleTime;
                                timeUnit = vehicleUnit;
                            }
                        }
//                    System.out.println(key);
                    }
                }

                // time remaining
                System.out.println("--blabla----");
                System.out.println(time);
                System.out.println(timeUnit);

                if (time != 500) {

                    binding.main.setText(Integer.toString(time));
                    mMain = binding.main;

                    // time unit
                    binding.timeUnit.setText(timeUnit);
                    mTimeUnit = binding.timeUnit;

                    // upper message
//                binding.upperMessage.setText(statusDaCorrida);
                    binding.upperMessage.setText(languageMaps.get("estimatedTimeMessage"));
                    binding.upperMessage.setTextSize(10F);
                    mUpperMessage = binding.upperMessage;

                    // lower message
//                binding.lowerMessage.setText("Kellerson Vinicius\nCorolla Preto - PWD7654");
                    binding.lowerMessage.setText(cityName + "\n" + street + ", " + number);
                    mLowerMessage = binding.lowerMessage;

                    // left message
//                cont += ".";
//                binding.leftMessage.setText("" + cont);
//                binding.leftMessage.setText("");
//                mLeftMessage = binding.leftMessage;
                } else {
                    binding.main.setText("");
                    mMain = binding.main;

                    // time unit
                    binding.timeUnit.setText("");
                    mTimeUnit = binding.timeUnit;

                    // upper message
//                binding.upperMessage.setText(statusDaCorrida);
                    binding.upperMessage.setText(languageMaps.get("noGpsMessage"));
                    binding.upperMessage.setTextSize(15F);
                    mUpperMessage = binding.upperMessage;

                    // lower message
//                binding.lowerMessage.setText("Kellerson Vinicius\nCorolla Preto - PWD7654");
                    binding.lowerMessage.setText("");
                    mLowerMessage = binding.lowerMessage;

                }

            } else if (statusDaCorrida.equalsIgnoreCase("WaitingForPickup")) {
                JSONObject Jtrip = (JSONObject) Jdata.get("trip");
                time = Integer.parseInt(Jtrip.get("etaStringShort").toString().split(" ")[0]);
                timeUnit = Jtrip.get("etaStringShort").toString().split(" ")[1];

                JSONObject Jvehicle = (JSONObject) Jtrip.get("vehicle");
                JSONObject JvehicleType = (JSONObject) Jvehicle.get("vehicleType");
                String vehicleModel = JvehicleType.getString("model");
                String vehicleColor = Jvehicle.getString("vehicleColorTranslatedName");
                String vehicleLicensePlate = Jvehicle.getString("licensePlate");

                JSONObject Jdriver = (JSONObject) Jtrip.get("driver");
                String driverName = Jdriver.getString("name");

//                binding.upperMessage.setText(statusDaCorrida);
                binding.upperMessage.setText(languageMaps.get("driverOnTheWay"));
                mUpperMessage = binding.upperMessage;

                binding.lowerMessage.setText(String.format("%s\n%s %s - %s", driverName, vehicleModel, vehicleColor, vehicleLicensePlate));
                mLowerMessage = binding.lowerMessage;

//                binding.leftMessage.setText(" " + driverName);
//                mLeftMessage = binding.leftMessage;

                binding.main.setText(Integer.toString(time));
                mMain = binding.main;

                binding.timeUnit.setText(timeUnit);
                mTimeUnit = binding.timeUnit;
            } else if (statusDaCorrida.equalsIgnoreCase("Dispatching")) {
//                binding.upperMessage.setText(statusDaCorrida);
                binding.upperMessage.setText(languageMaps.get("requestingUber"));
                mUpperMessage = binding.upperMessage;

                binding.lowerMessage.setText(languageMaps.get("waitingForDriver"));
                mLowerMessage = binding.lowerMessage;

//                binding.leftMessage.setText("test");
//                mLeftMessage = binding.leftMessage;

                binding.main.setText("...");
                mMain = binding.main;

                binding.timeUnit.setText("");
                mTimeUnit = binding.timeUnit;
            } else if (statusDaCorrida.equalsIgnoreCase("OnTrip")) {
                JSONObject Jtrip = (JSONObject) Jdata.get("trip");
                time = Integer.parseInt(Jtrip.get("etaStringShort").toString().split(" ")[0]);
                timeUnit = Jtrip.get("etaStringShort").toString().split(" ")[1];

                JSONObject jTripStatusMessage = (JSONObject) Jtrip.get("tripStatusMessage");
                String onTripLowerMessage = jTripStatusMessage.getString("title");

                binding.upperMessage.setText(languageMaps.get("estimatedTripTime"));
                mUpperMessage = binding.upperMessage;

                binding.lowerMessage.setText(onTripLowerMessage);
                mLowerMessage = binding.lowerMessage;

                binding.main.setText(Integer.toString(time));
                mMain = binding.main;

                binding.timeUnit.setText(timeUnit);
                mTimeUnit = binding.timeUnit;

            } else {
                binding.upperMessage.setText(statusDaCorrida);
                mUpperMessage = binding.upperMessage;
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }

    private void getAddressFromLatLong() {
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses = null;
        try {
            addresses = gcd.getFromLocation(lat, lon, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
//            System.out.println(location.getLatitude() + location.getLongitude());
        if (addresses.size() > 0) {
            cityName = addresses.get(0).getSubAdminArea() == null ? "" : addresses.get(0).getSubAdminArea();
            street = addresses.get(0).getThoroughfare() == null ? "" : addresses.get(0).getThoroughfare();
            number = addresses.get(0).getSubThoroughfare() == null ? "" : addresses.get(0).getSubThoroughfare();
        }
    }

    private void getLatLong() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions((Activity) this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            return;
        }

        LocationListener locationListener = new MyLocationListener();

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, locationListener);
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if(location == null)
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (location != null) {
            lat = location.getLatitude();
            lon = location.getLongitude();
        }

    }

    @Override
    protected void onResume() {
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                handler.postDelayed(runnable, refreshTime);
                getStatus();
            }
        }, refreshTime);
        super.onResume();
    }

}
