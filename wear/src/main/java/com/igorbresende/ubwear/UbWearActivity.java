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

import org.json.JSONArray;
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
    String cityName = "";
    String street = "";
    String number = "";
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
//----------

//        RequestBody body = RequestBody.create(mediaType, "{\"operationName\":\"GetStatus\",\"variables\":{\"latitude\":" + lat + ",\"longitude\":" + lon + "},\"query\":\"query GetStatus($latitude: Float!, $longitude: Float!, $targetProductType: EnumRVWebCommonTargetProductType) {\\n  status(\\n    latitude: $latitude\\n    longitude: $longitude\\n    targetProductType: $targetProductType\\n  ) {\\n    ...StatusFragment\\n    __typename\\n  }\\n}\\n\\nfragment StatusFragment on RVWebCommonStatus {\\n  city {\\n    countryIso2\\n    id\\n    timezone\\n    __typename\\n  }\\n  clientStatus\\n  coordinate {\\n    latitude\\n    longitude\\n    __typename\\n  }\\n  nearbyVehicles {\\n    ...NearbyVehicleFragment\\n    __typename\\n  }\\n  pollingIntervalMs\\n  productsUnavailableMessage\\n  trip {\\n    ...TripFragment\\n    __typename\\n  }\\n  __typename\\n}\\n\\nfragment NearbyVehicleFragment on RVWebCommonVehicle {\\n  bearing\\n  coordinate {\\n    latitude\\n    longitude\\n    __typename\\n  }\\n  etaString\\n  etaStringShort\\n  id\\n  mapImageUrl\\n  __typename\\n}\\n\\nfragment TripFragment on RVWebCommonTrip {\\n  cancelable\\n  driver {\\n    name\\n    pictureUrl\\n    rating\\n    sms\\n    status\\n    uuid\\n    voice\\n    regulatoryLicenseDisplayString\\n    __typename\\n  }\\n  eta\\n  etaStringShort\\n  etaToDestination\\n  fareString\\n  isConnect\\n  isRAPU\\n  paymentProfileUUID\\n  polylines {\\n    id\\n    polyline\\n    provider\\n    __typename\\n  }\\n  productImageUrl\\n  renderRankingInformation\\n  statusMessage {\\n    detailMode\\n    title\\n    __typename\\n  }\\n  thirdPartyVendor {\\n    name\\n    logoUrl\\n    safetyFeatures\\n    __typename\\n  }\\n  useCredits\\n  uuid\\n  vehicle {\\n    bearing\\n    colorTranslatedName\\n    coordinate {\\n      latitude\\n      longitude\\n      __typename\\n    }\\n    description\\n    etaString\\n    etaStringShort\\n    licensePlate\\n    make\\n    mapImageUrl\\n    model\\n    __typename\\n  }\\n  waypoints {\\n    coordinate {\\n      latitude\\n      longitude\\n      __typename\\n    }\\n    displayMarker\\n    id\\n    nickname\\n    subtitle\\n    title\\n    type\\n    __typename\\n  }\\n  __typename\\n}\\n\"}");
//        Request request = new Request.Builder()
//                .url("https://m.uber.com/go/graphql")
//                .method("POST", body)
//                .addHeader("content-type", "application/json")
//                .addHeader("cookie", "sid=QA.CAESEG67p8wEsUPbpoGoxT22FqcYp8nlqQYiATEqJDk1ZDlmNTJhLWE5NzQtNGI5Yi05MjIzLWE5MTZkNzEwN2RkMDJA5eBhXzw18j5xqJ-LkHTd5kN52oSAKyjesD45k0HRbMH7plVS8l-AGx2_sQrb0SwnlGVtVv3DaYY6a2YhXFRswDoBMUIIdWJlci5jb20.699FJ1ptl-n-Ne4lcOVw0eUA9VehgPeuSvvBB9TBzf4; csid=1.1698260135470.ABIyFj6kCXla2kL1OMmVmdRgGsXPRZWH1YPGBQrVibs=; marketing_vistor_id=45919cf2-1dea-4bd0-90f3-81889a895301; _ua={\"session_id\":\"77c340a9-6f12-4fa2-904b-19df9395f076\",\"session_time_ms\":1695668135642}; jwt-session=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE2OTU2Njg0OTAsImV4cCI6MTY5NTc1NDg5MH0.1cVdnD7MK9k7a_tBz67m1UGm7fBZLVRUSclhRgqrnrw")
//                .addHeader("x-csrf-token", "x")
//                .build();
//        Response response = client.newCall(request).execute();
//----------

//        System.out.println("cookies sid csid");
        System.out.println(cookies);

        OkHttpClient client = new OkHttpClient().newBuilder()
                .build();
        MediaType mediaType = MediaType.parse("application/json");
//        RequestBody body = RequestBody.create(mediaType, "{\"latitude\":" + lat + ",\"longitude\":" + lon + "}");
        RequestBody body = RequestBody.create(mediaType, "{\"operationName\":\"GetStatus\",\"variables\":{\"latitude\":" + lat + ",\"longitude\":" + lon + "},\"query\":\"query GetStatus($latitude: Float!, $longitude: Float!, $targetProductType: EnumRVWebCommonTargetProductType) {\\n  status(\\n    latitude: $latitude\\n    longitude: $longitude\\n    targetProductType: $targetProductType\\n  ) {\\n    ...StatusFragment\\n    __typename\\n  }\\n}\\n\\nfragment StatusFragment on RVWebCommonStatus {\\n  city {\\n    countryIso2\\n    id\\n    timezone\\n    __typename\\n  }\\n  clientStatus\\n  coordinate {\\n    latitude\\n    longitude\\n    __typename\\n  }\\n  nearbyVehicles {\\n    ...NearbyVehicleFragment\\n    __typename\\n  }\\n  pollingIntervalMs\\n  productsUnavailableMessage\\n  trip {\\n    ...TripFragment\\n    __typename\\n  }\\n  __typename\\n}\\n\\nfragment NearbyVehicleFragment on RVWebCommonVehicle {\\n  bearing\\n  coordinate {\\n    latitude\\n    longitude\\n    __typename\\n  }\\n  etaString\\n  etaStringShort\\n  id\\n  mapImageUrl\\n  __typename\\n}\\n\\nfragment TripFragment on RVWebCommonTrip {\\n  cancelable\\n  driver {\\n    name\\n    pictureUrl\\n    rating\\n    sms\\n    status\\n    uuid\\n    voice\\n    regulatoryLicenseDisplayString\\n    __typename\\n  }\\n  eta\\n  etaStringShort\\n  etaToDestination\\n  fareString\\n  isConnect\\n  isRAPU\\n  paymentProfileUUID\\n  polylines {\\n    id\\n    polyline\\n    provider\\n    __typename\\n  }\\n  productImageUrl\\n  renderRankingInformation\\n  statusMessage {\\n    detailMode\\n    title\\n    __typename\\n  }\\n  thirdPartyVendor {\\n    name\\n    logoUrl\\n    safetyFeatures\\n    __typename\\n  }\\n  useCredits\\n  uuid\\n  vehicle {\\n    bearing\\n    colorTranslatedName\\n    coordinate {\\n      latitude\\n      longitude\\n      __typename\\n    }\\n    description\\n    etaString\\n    etaStringShort\\n    licensePlate\\n    make\\n    mapImageUrl\\n    model\\n    __typename\\n  }\\n  waypoints {\\n    coordinate {\\n      latitude\\n      longitude\\n      __typename\\n    }\\n    displayMarker\\n    id\\n    nickname\\n    subtitle\\n    title\\n    type\\n    __typename\\n  }\\n  __typename\\n}\\n\"}");
        Request request = new Request.Builder()
                .url("https://m.uber.com/go/graphql")
//                .url("https://m.uber.com/api/getStatus?localeCode=pt-BR")
                .method("POST", body)
                .addHeader("content-type", "application/json")
                .addHeader("cookie", cookies)
                .addHeader("x-csrf-token", "x")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String jsonData = response.body().string();
//            jsonData = "{\n" +
//                    "  \"data\": {\n" +
//                    "    \"status\": {\n" +
//                    "      \"city\": {\n" +
//                    "        \"countryIso2\": \"BR\",\n" +
//                    "        \"id\": 493,\n" +
//                    "        \"timezone\": \"America/Sao_Paulo\",\n" +
//                    "        \"__typename\": \"RVWebCommonCity\"\n" +
//                    "      },\n" +
//                    "      \"clientStatus\": \"WaitingForPickup\",\n" +
//                    "      \"coordinate\": {\n" +
//                    "        \"latitude\": -19.9245,\n" +
//                    "        \"longitude\": -43.95233,\n" +
//                    "        \"__typename\": \"RVWebCommonCoordinate\"\n" +
//                    "      },\n" +
//                    "      \"nearbyVehicles\": [],\n" +
//                    "      \"pollingIntervalMs\": 4000,\n" +
//                    "      \"productsUnavailableMessage\": \"Infelizmente, a Uber não está disponível na sua região.\",\n" +
//                    "      \"trip\": {\n" +
//                    "        \"cancelable\": true,\n" +
//                    "        \"driver\": {\n" +
//                    "          \"name\": \"Gilvan\",\n" +
//                    "          \"pictureUrl\": \"https://d1w2poirtb3as9.cloudfront.net/f11372ee51eeace5d8e5.jpeg?Expires=1695876062&Signature=Xzmh5e78LIqyBf-7N7Nx30EO7abqdsALupMfPIPmwbHxR8UHCuBXwf2g0srpO90rpXiVqJxEatYWuM8y-rM4eIgdIxZvHZ2CKrR7NCLN3D05rp5hN~tem8ZCKC1yWeTkqVO0annDyjEuqnrodKtivcBt9ViTwtcj-ilzI~6kGXtUfSYqpWlzvIjb9iC5yTDxDN~pHHHR2xL4QMKn6ILU7Xhd~bsxe-J~K1rjy78F7wprTXQo78zIUPuTZmUlyULIf0QlP~v1zGjC5G0o~q9NhdXNAHidIssE6ZBC0vfRhIREiPINeEcF4w5HmtlsmU4ySdr01LsZPRV50YZnZwiwmw__&Key-Pair-Id=K36LFL06Z5BT10\",\n" +
//                    "          \"rating\": 4.95,\n" +
//                    "          \"sms\": \"+553130580179\",\n" +
//                    "          \"status\": \"Accepted\",\n" +
//                    "          \"uuid\": \"ac4bf6dc-afe8-41cf-b72f-0b9870adae30\",\n" +
//                    "          \"voice\": \"+553130580179\",\n" +
//                    "          \"regulatoryLicenseDisplayString\": null,\n" +
//                    "          \"__typename\": \"RVWebCommonDriver\"\n" +
//                    "        },\n" +
//                    "        \"eta\": 2,\n" +
//                    "        \"etaStringShort\": \"20 min\",\n" +
//                    "        \"etaToDestination\": 638,\n" +
//                    "        \"fareString\": \"R$ 9,91\",\n" +
//                    "        \"isConnect\": false,\n" +
//                    "        \"isRAPU\": false,\n" +
//                    "        \"paymentProfileUUID\": \"c5e6607a-def3-5494-82ac-30538f7dbdd0\",\n" +
//                    "        \"polylines\": [\n" +
//                    "          {\n" +
//                    "            \"id\": \"bf2b7998-53a0-407e-a91d-00d067bb77bc-0\",\n" +
//                    "            \"polyline\": \"`rrxBbkgkGIh@BRBBPB{@d@c@TsBdAwAz@M?KCGOl@}DhDj@\",\n" +
//                    "            \"provider\": \"uber\",\n" +
//                    "            \"__typename\": \"RVWebCommonPolyline\"\n" +
//                    "          }\n" +
//                    "        ],\n" +
//                    "        \"productImageUrl\": \"https://d3a74cgiihgn4m.cloudfront.net/2018/ford/ka/1123042255946.png\",\n" +
//                    "        \"renderRankingInformation\": false,\n" +
//                    "        \"statusMessage\": {\n" +
//                    "          \"detailMode\": \"MinutesToPickup\",\n" +
//                    "          \"title\": \"Espere no local de partida\",\n" +
//                    "          \"__typename\": \"RVWebCommonStatusMessage\"\n" +
//                    "        },\n" +
//                    "        \"thirdPartyVendor\": null,\n" +
//                    "        \"useCredits\": false,\n" +
//                    "        \"uuid\": \"bf2b7998-53a0-407e-a91d-00d067bb77bc\",\n" +
//                    "        \"vehicle\": {\n" +
//                    "          \"bearing\": 90,\n" +
//                    "          \"colorTranslatedName\": \"Prata\",\n" +
//                    "          \"coordinate\": {\n" +
//                    "            \"latitude\": -19.9249681,\n" +
//                    "            \"longitude\": -43.95202499999999,\n" +
//                    "            \"__typename\": \"RVWebCommonCoordinate\"\n" +
//                    "          },\n" +
//                    "          \"description\": \"UberX\",\n" +
//                    "          \"etaString\": \"\",\n" +
//                    "          \"etaStringShort\": \"\",\n" +
//                    "          \"licensePlate\": \"QOR7551\",\n" +
//                    "          \"make\": \"Ford\",\n" +
//                    "          \"mapImageUrl\": \"https://d1a3f4spazzrp4.cloudfront.net/car-types/map70px/map-uberx.png\",\n" +
//                    "          \"model\": \"Ka\",\n" +
//                    "          \"__typename\": \"RVWebCommonVehicle\"\n" +
//                    "        },\n" +
//                    "        \"waypoints\": [\n" +
//                    "          {\n" +
//                    "            \"coordinate\": {\n" +
//                    "              \"latitude\": -19.9245,\n" +
//                    "              \"longitude\": -43.95233,\n" +
//                    "              \"__typename\": \"RVWebCommonCoordinate\"\n" +
//                    "            },\n" +
//                    "            \"displayMarker\": true,\n" +
//                    "            \"id\": \"0bc369ec-7b01-4e01-9eca-ceb02b347956\",\n" +
//                    "            \"nickname\": \"\",\n" +
//                    "            \"subtitle\": \"Rua Paracatú, Belo Horizonte - MG, 30180-094\",\n" +
//                    "            \"title\": \"Hospital Vera Cruz-PS\",\n" +
//                    "            \"type\": \"Pickup\",\n" +
//                    "            \"__typename\": \"RVWebCommonWaypoint\"\n" +
//                    "          },\n" +
//                    "          {\n" +
//                    "            \"coordinate\": {\n" +
//                    "              \"latitude\": -19.937014,\n" +
//                    "              \"longitude\": -43.95388,\n" +
//                    "              \"__typename\": \"RVWebCommonCoordinate\"\n" +
//                    "            },\n" +
//                    "            \"displayMarker\": false,\n" +
//                    "            \"id\": \"f6af5c4d-c603-4ccb-a52f-57537a006288\",\n" +
//                    "            \"nickname\": \"\",\n" +
//                    "            \"subtitle\": \"Av. Raja Gabaglia, 350 - Gutierrez, Belo Horizonte - MG, 30441-070\",\n" +
//                    "            \"title\": \"Círculo Militar de Belo Horizonte\",\n" +
//                    "            \"type\": \"Dropoff\",\n" +
//                    "            \"__typename\": \"RVWebCommonWaypoint\"\n" +
//                    "          }\n" +
//                    "        ],\n" +
//                    "        \"__typename\": \"RVWebCommonTrip\"\n" +
//                    "      },\n" +
//                    "      \"__typename\": \"RVWebCommonStatus\"\n" +
//                    "    }\n" +
//                    "  }\n" +
//                    "}";

            JSONObject Jobject = new JSONObject(jsonData);
            JSONObject Jdata = (JSONObject) Jobject.get("data");
            JSONObject JclientStatus = (JSONObject) Jdata.get("status");
            String statusDaCorrida = JclientStatus.getString("clientStatus");

//            System.out.println("status: " + JclientStatus.get("status"));
            System.out.println("status: " + Jdata);
            if (statusDaCorrida.equalsIgnoreCase("Looking")) {
//                JSONObject Jeyeball = (JSONObject) Jdata.get("eyeball");
                JSONArray JnearbyVehicles = (JSONArray) JclientStatus.get("nearbyVehicles");

                for (int i = 0; i < JnearbyVehicles.length(); i++) {
                    JSONObject Jvehicle = JnearbyVehicles.getJSONObject(i);

                    if (Jvehicle.has("etaStringShort")) {
                        int vehicleTime = Integer.parseInt(Jvehicle.get("etaStringShort").toString().split(" ")[0]);
                        String vehicleUnit = Jvehicle.get("etaStringShort").toString().split(" ")[1];

                        if (vehicleTime < time) {
                            time = vehicleTime;
                            timeUnit = vehicleUnit;
                        }
                    }
                    // System.out.println(Jvehicle.toString());
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
                    binding.lowerMessage.setText(cityName + "\n" + street + number);
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
                JSONObject Jtrip = (JSONObject) JclientStatus.get("trip");
                time = Integer.parseInt(Jtrip.get("etaStringShort").toString().split(" ")[0]);
                timeUnit = Jtrip.get("etaStringShort").toString().split(" ")[1];

                JSONObject Jvehicle = (JSONObject) Jtrip.get("vehicle");
                String JvehicleType = Jvehicle.getString("make");
                String vehicleModel = Jvehicle.getString("model");
                String vehicleColor = Jvehicle.getString("colorTranslatedName");
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
                JSONObject Jtrip = (JSONObject) JclientStatus.get("trip");
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
            number = addresses.get(0).getSubThoroughfare() == null ? "" : ", " + addresses.get(0).getSubThoroughfare();
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
