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
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.widget.ImageView;
import android.widget.TextView;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import androidx.core.app.ActivityCompat;

import com.igorbresende.ubwear.databinding.ActivityUbwearBinding;
import com.orhanobut.hawk.Hawk;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.squareup.picasso.Picasso;


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

    private ImageView driverPicture;
    private ImageView carPicture;

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
//                .addHeader("cookie", cookies)
                                .addHeader("cookie", "marketing_vistor_id=45919cf2-1dea-4bd0-90f3-81889a895301; utag_main__sn=1; utag_main_ses_id=1701454963597%3Bexp-session; segmentCookie=b; utag_main_segment=a; utag_geo_code=US; utag_main_optimizely_segment=b; utag_main__ss=0%3Bexp-session; _hjSessionUser_960703=eyJpZCI6ImY2NTFjODMwLTMyNjAtNTE2YS04MDUyLWU1ZmQ1ZWViZTI4OCIsImNyZWF0ZWQiOjE3MDE0NTQ5Njk5NzMsImV4aXN0aW5nIjpmYWxzZX0=; _hjFirstSeen=1; _hjIncludedInSessionSample_960703=0; _hjSession_960703=eyJpZCI6ImE3MGJmMDk3LWEwZjMtNDllMS04ZWFlLTM4NDNiYjVmODYwMyIsImNyZWF0ZWQiOjE3MDE0NTQ5Njk5NzMsImluU2FtcGxlIjpmYWxzZSwic2Vzc2lvbml6ZXJCZXRhRW5hYmxlZCI6ZmFsc2V9; _hjAbsoluteSessionInProgress=0; _gid=GA1.2.1657561724.1701454970; UBER_CONSENTMGR=1701454971022|consent:true; CONSENTMGR=c1:1%7Cc2:1%7Cc3:1%7Cc4:1%7Cc5:1%7Cc6:1%7Cc7:1%7Cc8:1%7Cc9:1%7Cc10:1%7Cc11:1%7Cc12:1%7Cc13:1%7Cc14:1%7Cc15:1%7Cts:1701454971023%7Cconsent:true; x-uber-analytics-session-id=54977eba-beb3-4735-9070-5ead854a19e4; udi-id=GH2+FWFH49HO4uS4ThAxUIbTNDEHMPDJcZ0kGH+1+evoQaquqBpyJhzagIOjzWPSZjv97YF6H2ruSkW/Bk+Mc6KIgJ+GOytJzx2TwF15uuZVbWs46fKbvaKTPuZ57OOhud3STovETf3b7zZ7bI4RKUM94g7J8Pl1CgIcFB9k3ZifuJKhL/Bybux0jM/goXjzAa6DJ9wChv89iVUxnLSQug==hs3QObO5VBrXjmeGdNxtUw==WMcy7vxlWAj/yK7AusWB+AArq+OWqIPoI7qo6zw0/tY=; udi-fingerprint=AqvcdKQyk5p7yeztTRTHF8MCuYovISuugrd0szY8nNHItk1lMkcUEyPMLbz9Cvrwh5wGjckGheiiEM81QPSfrA==Jc+m3dzOfd88osMd30ci1wvRa8Iwem3OmBcXTYE81Gc=; _ua={\"session_id\":\"e2a92522-7f44-41bd-b235-c246beea3c65\",\"session_time_ms\":1701455003167}; sid=QA.CAESEAJplyc1xEmhnQc81gLC4S4Y7OPGrAYiATEqJDk1ZDlmNTJhLWE5NzQtNGI5Yi05MjIzLWE5MTZkNzEwN2RkMDJAdftTWGTSKN31TWh7-toceRWeJ0fpsEdhqd4k3URctZDObhmsIO4dMgQfOCSwvCzf6vo5c7n17zEf9RjQ9U08ljoBMUIIdWJlci5jb20.81GkKtE8yHb022WlRbp_QKUjRtNu8AzFEZrai5jc1sE; isWebLogin=true; csid=1.1704047084605.zowdRvAvB9JxOd0qz1EVscLxGJIdVhgbuuUOhywZ6pM=; utag_main__pn=2%3Bexp-session; _gat_gtag_UA_7157694_35=1; _ga=GA1.1.894534309.1701454970; utag_main__se=5%3Bexp-session; utag_main__st=1701456887893%3Bexp-session; _ga_XTGQLY6KPT=GS1.1.1701454969.1.1.1701455087.0.0.0; mp_adec770be288b16d9008c964acfba5c2_mixpanel=%7B%22distinct_id%22%3A%20%2295d9f52a-a974-4b9b-9223-a916d7107dd0%22%2C%22%24device_id%22%3A%20%2218c269e67dc1941-0d046e0388fe9b-16525634-1fa400-18c269e67dd2a69%22%2C%22%24search_engine%22%3A%20%22google%22%2C%22%24initial_referrer%22%3A%20%22https%3A%2F%2Fwww.google.com%2F%22%2C%22%24initial_referring_domain%22%3A%20%22www.google.com%22%2C%22%24user_id%22%3A%20%2295d9f52a-a974-4b9b-9223-a916d7107dd0%22%7D; jwt-session=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpYXQiOjE3MDE0NTUwODgsImV4cCI6MTcwMTU0MTQ4OH0.wNcrUR57OYo_9icaRCdpY9_gyIXvpNQCKcEx5CLqT7o\n")
                .addHeader("x-csrf-token", "x")
                .build();
        try {
            Response response = client.newCall(request).execute();
            String jsonData = response.body().string();
//            jsonData = "{\n" +
//                    "    \"data\": {\n" +
//                    "        \"status\": {\n" +
//                    "            \"city\": {\n" +
//                    "                \"countryIso2\": \"BR\",\n" +
//                    "                \"id\": 493,\n" +
//                    "                \"timezone\": \"America/Sao_Paulo\",\n" +
//                    "                \"__typename\": \"RVWebCommonCity\"\n" +
//                    "            },\n" +
//                    "            \"clientStatus\": \"OnTrip\",\n" +
//                    "            \"coordinate\": {\n" +
//                    "                \"latitude\": -19.9029,\n" +
//                    "                \"longitude\": -43.9572,\n" +
//                    "                \"__typename\": \"RVWebCommonCoordinate\"\n" +
//                    "            },\n" +
//                    "            \"nearbyVehicles\": [],\n" +
//                    "            \"pollingIntervalMs\": 4000,\n" +
//                    "            \"productsUnavailableMessage\": \"Unfortunately, Uber is currently unavailable in your area.\",\n" +
//                    "            \"trip\": {\n" +
//                    "                \"cancelable\": true,\n" +
//                    "                \"driver\": {\n" +
//                    "                    \"name\": \"Juliano\",\n" +
//                    "                    \"pictureUrl\": \"https://d1w2poirtb3as9.cloudfront.net/8e74481a414d3cc08592.jpeg?Expires=1697412924&Signature=v1v0tzksGt60XjtEC~cAMb08GLjNmoGvMTvhJBbCLE-Fcb-YyXP7PssWZRdNqkM4~T7Knmpf5x-HH7HNgb4oWjv2k6Fy-aeK9mt7yBnla7aWOc0jhbvF2v5m3uzgB1kDroDjVTLyZnK4mRCz00RDC6SgAKlYxMyNjlyQFsbpyWAmx-EPs6TCiLUhMRFIDggoCi0QAWNqYNqMlFp7odd4xYC~FjWHq~rC3bMAWuFBH7zronSHqwDhCrK8CcPmrVU23lPF2s7yPP-TwB6v1PMMog4coeE~TMmxMp7wnkYXGul6CEKjnvqQCRu8aeCBuUNuSMgQjZwgLjii6eaeRdSYzQ__&Key-Pair-Id=K36LFL06Z5BT10\",\n" +
//                    "                    \"rating\": 4.97,\n" +
//                    "                    \"sms\": \"+553130580535\",\n" +
//                    "                    \"status\": \"DrivingClient\",\n" +
//                    "                    \"uuid\": \"9cacc6eb-4626-43cd-b42f-0e3eb18a7ab0\",\n" +
//                    "                    \"voice\": \"+553130580535\",\n" +
//                    "                    \"regulatoryLicenseDisplayString\": null,\n" +
//                    "                    \"__typename\": \"RVWebCommonDriver\"\n" +
//                    "                },\n" +
//                    "                \"eta\": 8,\n" +
//                    "                \"etaStringShort\": \"8 mins\",\n" +
//                    "                \"etaToDestination\": 464,\n" +
//                    "                \"fareString\": \"R$13.94\",\n" +
//                    "                \"isConnect\": false,\n" +
//                    "                \"isRAPU\": false,\n" +
//                    "                \"paymentProfileUUID\": \"c5e6607a-def3-5494-82ac-30538f7dbdd0\",\n" +
//                    "                \"polylines\": [\n" +
//                    "                    {\n" +
//                    "                        \"id\": \"e1ef64b7-f82b-496e-a512-e08629835a2f-0\",\n" +
//                    "                        \"polyline\": \"nn|xBxzkkG?M?k@C_A?}D@mE@a@CMI[Ni@JOXWJGNCv@NL?C[?K_@kFCSUiCIsAMmAIg@Uo@GM[a@YSe@S_@Iq@Eu@DgANe@FeAJQAg@Ia@WUSo@s@qAyAY]KKmC{CyAkBo@s@[]QQQOMIUIk@M[Ce@G_@OIKOa@A{ACuBAo@Ak@Gw@GMI@_@Pe@`@eAp@MDIDK?eBb@QHQJe@ZmAZMBEDo@TwAt@oGhEYNa@L[FSBsBLq@Fa@HgB\\\\c@BOAq@KyAo@_@I[?m@LQFGGI@Uw@Og@IWKg@CSCOCQEQI]s@}BUi@QJc@NcBLe@ECe@HeA?QE]K]OYUYUKKEcAMOGKMKf@ATDLLLLRHTFd@C\\\\GRKJGBU@a@EYOaAyA]WCA\",\n" +
//                    "                        \"provider\": \"uber\",\n" +
//                    "                        \"__typename\": \"RVWebCommonPolyline\"\n" +
//                    "                    }\n" +
//                    "                ],\n" +
//                    "                \"productImageUrl\": \"https://d3a74cgiihgn4m.cloudfront.net/2019/ford/ka/1122904390756.png\",\n" +
//                    "                \"renderRankingInformation\": false,\n" +
//                    "                \"statusMessage\": {\n" +
//                    "                    \"detailMode\": \"TimeOfDropoff\",\n" +
//                    "                    \"title\": \"Heading to Beach Square\",\n" +
//                    "                    \"__typename\": \"RVWebCommonStatusMessage\"\n" +
//                    "                },\n" +
//                    "                \"thirdPartyVendor\": null,\n" +
//                    "                \"useCredits\": false,\n" +
//                    "                \"uuid\": \"e1ef64b7-f82b-496e-a512-e08629835a2f\",\n" +
//                    "                \"vehicle\": {\n" +
//                    "                    \"bearing\": 89.13737481774172,\n" +
//                    "                    \"colorTranslatedName\": \"Silver\",\n" +
//                    "                    \"coordinate\": {\n" +
//                    "                        \"latitude\": -19.975602791086228,\n" +
//                    "                        \"longitude\": -43.97501363676025,\n" +
//                    "                        \"__typename\": \"RVWebCommonCoordinate\"\n" +
//                    "                    },\n" +
//                    "                    \"description\": \"UberX\",\n" +
//                    "                    \"etaString\": \"\",\n" +
//                    "                    \"etaStringShort\": \"\",\n" +
//                    "                    \"licensePlate\": \"QXZ5E24\",\n" +
//                    "                    \"make\": \"Ford\",\n" +
//                    "                    \"mapImageUrl\": \"https://d1a3f4spazzrp4.cloudfront.net/car-types/map70px/map-uberx.png\",\n" +
//                    "                    \"model\": \"Ka\",\n" +
//                    "                    \"__typename\": \"RVWebCommonVehicle\"\n" +
//                    "                },\n" +
//                    "                \"waypoints\": [\n" +
//                    "                    {\n" +
//                    "                        \"coordinate\": {\n" +
//                    "                            \"latitude\": -19.97523,\n" +
//                    "                            \"longitude\": -43.974915,\n" +
//                    "                            \"__typename\": \"RVWebCommonCoordinate\"\n" +
//                    "                        },\n" +
//                    "                        \"displayMarker\": true,\n" +
//                    "                        \"id\": \"f8381514-6680-4664-9ffd-90e34c628da8\",\n" +
//                    "                        \"nickname\": \"\",\n" +
//                    "                        \"subtitle\": \"R. Eli Seabra Filho, 100 - Buritis, Belo Horizonte - MG, 30575-740\",\n" +
//                    "                        \"title\": \"Residencial Spazio Eco Vitta\",\n" +
//                    "                        \"type\": \"Pickup\",\n" +
//                    "                        \"__typename\": \"RVWebCommonWaypoint\"\n" +
//                    "                    },\n" +
//                    "                    {\n" +
//                    "                        \"coordinate\": {\n" +
//                    "                            \"latitude\": -19.957737,\n" +
//                    "                            \"longitude\": -43.96227,\n" +
//                    "                            \"__typename\": \"RVWebCommonCoordinate\"\n" +
//                    "                        },\n" +
//                    "                        \"displayMarker\": true,\n" +
//                    "                        \"id\": \"b3b4771c-56f8-4b45-b18b-54c5bc6ddce6\",\n" +
//                    "                        \"nickname\": \"\",\n" +
//                    "                        \"subtitle\": \"R. João de Almeida, 160, Belo Horizonte - MG\",\n" +
//                    "                        \"title\": \"Beach Square\",\n" +
//                    "                        \"type\": \"Dropoff\",\n" +
//                    "                        \"__typename\": \"RVWebCommonWaypoint\"\n" +
//                    "                    }\n" +
//                    "                ],\n" +
//                    "                \"__typename\": \"RVWebCommonTrip\"\n" +
//                    "            },\n" +
//                    "            \"__typename\": \"RVWebCommonStatus\"\n" +
//                    "        }\n" +
//                    "    }\n" +
//                    "}";
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
//                    "        \"etaStringShort\": \"2 min\",\n" +
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

                driverPicture = findViewById(R.id.profilePicture);
                Picasso.get()
                        .load(Jdriver.getString("pictureUrl"))
//                        .load("https://d1w2poirtb3as9.cloudfront.net/f11372ee51eeace5d8e5.jpeg?Expires=1695876062&Signature=Xzmh5e78LIqyBf-7N7Nx30EO7abqdsALupMfPIPmwbHxR8UHCuBXwf2g0srpO90rpXiVqJxEatYWuM8y-rM4eIgdIxZvHZ2CKrR7NCLN3D05rp5hN~tem8ZCKC1yWeTkqVO0annDyjEuqnrodKtivcBt9ViTwtcj-ilzI~6kGXtUfSYqpWlzvIjb9iC5yTDxDN~pHHHR2xL4QMKn6ILU7Xhd~bsxe-J~K1rjy78F7wprTXQo78zIUPuTZmUlyULIf0QlP~v1zGjC5G0o~q9NhdXNAHidIssE6ZBC0vfRhIREiPINeEcF4w5HmtlsmU4ySdr01LsZPRV50YZnZwiwmw__&Key-Pair-Id=K36LFL06Z5BT10")
                        .into(driverPicture);

                carPicture = findViewById(R.id.carPicture);
                Picasso.get()
                        .load(Jtrip.getString("productImageUrl"))
//                        .load("https://d3a74cgiihgn4m.cloudfront.net/2018/ford/ka/1123042255946.png")
                        .into(carPicture);
//                binding.profilePicture.setImageURI(Uri.parse("https://d1w2poirtb3as9.cloudfront.net/f11372ee51eeace5d8e5.jpeg?Expires=1695876062&Signature=Xzmh5e78LIqyBf-7N7Nx30EO7abqdsALupMfPIPmwbHxR8UHCuBXwf2g0srpO90rpXiVqJxEatYWuM8y-rM4eIgdIxZvHZ2CKrR7NCLN3D05rp5hN~tem8ZCKC1yWeTkqVO0annDyjEuqnrodKtivcBt9ViTwtcj-ilzI~6kGXtUfSYqpWlzvIjb9iC5yTDxDN~pHHHR2xL4QMKn6ILU7Xhd~bsxe-J~K1rjy78F7wprTXQo78zIUPuTZmUlyULIf0QlP~v1zGjC5G0o~q9NhdXNAHidIssE6ZBC0vfRhIREiPINeEcF4w5HmtlsmU4ySdr01LsZPRV50YZnZwiwmw__&Key-Pair-Id=K36LFL06Z5BT10"));

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
                int timeToArrivalInSeconds = Jtrip.getInt("etaToDestination");

                JSONObject jTripStatusMessage = (JSONObject) Jtrip.get("statusMessage");
                String onTripLowerMessage = jTripStatusMessage.getString("title");

                String estimatedTimeOfArrival = "";
                LocalTime nowTime = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    nowTime = LocalTime.now();
                    nowTime = nowTime.plusSeconds(timeToArrivalInSeconds);
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                    estimatedTimeOfArrival = nowTime.format(formatter);
                }

                binding.upperMessage.setText(languageMaps.get("estimatedTripTime"));
                mUpperMessage = binding.upperMessage;

                binding.lowerMessage.setText(onTripLowerMessage);
                mLowerMessage = binding.lowerMessage;

                binding.main.setText(Integer.toString(time));
                mMain = binding.main;

//                binding.timeUnit.setText(timeUnit);
                binding.timeUnit.setText(timeUnit + (estimatedTimeOfArrival.equals("") ? "" : "\n(" + estimatedTimeOfArrival + ")"));
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
//        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            ActivityCompat.requestPermissions((Activity) this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
//            return;
//        }
//
//        LocationListener locationListener = new MyLocationListener();
//
//        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, locationListener);
//        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
//        if(location == null)
//            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
//
//        if (location != null) {
//            lat = location.getLatitude();
//            lon = location.getLongitude();
//        }

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

// Check for location permissions
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
            return;
        }

// Check if location providers are enabled
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            // Handle case where neither GPS nor NETWORK providers are enabled
            // You can prompt the user to enable location services
        }

        LocationListener locationListener = new MyLocationListener();

// Request location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, locationListener);

// Retrieve the last known location from GPS_PROVIDER
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

// If GPS_PROVIDER didn't provide a location, try NETWORK_PROVIDER
        if (location == null) {
            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        if (location != null) {
            lat = location.getLatitude();
            lon = location.getLongitude();
        } else {
            // Handle the case where no last known location is available
            // You might want to show a message or prompt the user to enable location services
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
