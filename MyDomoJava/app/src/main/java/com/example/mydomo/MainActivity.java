package com.example.mydomo;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.WindowManager;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.example.mydomo.ui.alarme.AlarmeFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import com.example.mydomo.databinding.ActivityMainBinding;
import org.json.JSONObject;
import java.util.Calendar;
import java.util.Date;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity implements AlarmeFragment.AlarmeListener {


    String urlMouvSalon = "https://api.thingspeak.com/channels/2102609/fields/5.json?results=1";
    String urlMouvChambre = "https://api.thingspeak.com/channels/2102609/fields/1.json?results=1";
    String urlTempChambre = "https://api.thingspeak.com/channels/2102609/fields/3.json?results=1";


    Date currentTime = Calendar.getInstance().getTime();

    TextView data2;
    TextView data;
    String url;
    String url2;

    String parseValue="";

    Boolean alarmeActive = false;

    TextView salon;
    TextView mouvSalon;
    TextView mouvChambre;

    Button notifBtn;

    private AppBarConfiguration mAppBarConfiguration;
    private WebView mSuperSafeWebView;

    private boolean mSafeBrowsingIsInitialized;

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        salon =findViewById(R.id.afficheSalonActuel);

        mouvSalon =findViewById(R.id.afficheMouvementSalonActuel);
        mouvChambre =findViewById(R.id.afficheMouvementChambreActuel);
        String url = "https://worldtimeapi.org/api/timezone/Asia/Kolkata";


        // Pleine écran
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        mSuperSafeWebView = new WebView(this);
        mSuperSafeWebView.setWebViewClient(new WebViewClient());
        mSafeBrowsingIsInitialized = false;


        mSuperSafeWebView.startSafeBrowsing(this, new ValueCallback<Boolean>() {
            @Override
            public void onReceiveValue(Boolean success) {
                mSafeBrowsingIsInitialized = true;
                if (!success) {
                    Log.e("MY_APP_TAG", "Unable to initialize Safe Browsing!");
                }
            }
        });

        //Notif Test
        notifBtn = (Button) findViewById(R.id.buttonNotif);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /*
            NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);


             */

            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

// The id of the channel.
            String id = "1";

// The user-visible name of the channel.
            CharSequence name = getString(R.string.channel_name);

// The user-visible description of the channel.
            String description = getString(R.string.channel_description);

            int importance = NotificationManager.IMPORTANCE_LOW;

            NotificationChannel mChannel = new NotificationChannel(id, name,importance);

// Configure the notification channel.
            mChannel.setDescription(description);

            mChannel.enableLights(true);
// Sets the notification light color for notifications posted to this
// channel, if the device supports this feature.
            mChannel.setLightColor(Color.RED);

            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});

            mNotificationManager.createNotificationChannel(mChannel);



        }

        setSupportActionBar(binding.appBarMain.toolbar);
        binding.appBarMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if (alarmeActive){
                    Snackbar.make(view, "Alarme = false", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    alarmeActive =false;
                } else if (!alarmeActive){

                    Snackbar.make(view, "Alarme = true", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    alarmeActive =true;
                }

            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_salon, R.id.nav_chambre, R.id.nav_clock)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);



        //Recupère les données de thingspeak

        data = findViewById(R.id.afficheMouvementSalonActuel);
        data2 = findViewById(R.id.afficheMouvementChambreActuel);
        url = "https://worldtimeapi.org/api/timezone/Asia/Kolkata";
        url2 = "https://api.thingspeak.com/channels/2102609/fields/3.json?results=1";


        notifBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateSalon();
                updateChambre();
            }


            public void updateSalon() {

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlMouvSalon, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    String datetime = response.getString("feeds");

                                    parseValue = datetime;
                                    String lastThree = parseValue.substring(parseValue.length() - 4);
                                    Log.e("alarme", lastThree);

                                    lastThree = lastThree.substring(0, 1);
                                    Log.e("alarme", lastThree);
                                    String dateTimeMouvSalon = datetime;
                                    dateTimeMouvSalon = dateTimeMouvSalon.substring(16,35);
                                    String mois = dateTimeMouvSalon.substring(5,7);
                                    String jour = dateTimeMouvSalon.substring(8,10);
                                    String houreReel =dateTimeMouvSalon.substring(11,13);
                                    houreReel =convertHeure(houreReel);
                                    String min =dateTimeMouvSalon.substring(14,16);
                                    if (!lastThree.equals("0")){
                                        data.setText("Mouvement détecté le "+ jour+"/"+mois+" à "+ houreReel+"h"+min );
                                        if (alarmeActive){
                                            notifSalon();
                                        }
                                    }else {
                                        data.setText("Aucun mouvement détecté");
                                    }

                                } catch (Exception e) {

                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                Volley.newRequestQueue(MainActivity.this).add(request);

                Log.e("alarme", "bouton clicker notif salon");

            }


            public void updateChambre() {

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlMouvChambre, null,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                try {

                                    String datetime = response.getString("feeds");

                                    parseValue = datetime;
                                    String result = parseValue.substring(parseValue.length() - 4);
                                    Log.e("chambre", result);
                                    result = result.substring(0, 1);
                                    Log.e("chambre", result);
                                    String dateTimeMouvChambre = datetime;
                                    dateTimeMouvChambre = dateTimeMouvChambre.substring(16,35);
                                    String moisChambre = dateTimeMouvChambre.substring(5,7);
                                    String jourChambre = dateTimeMouvChambre.substring(8,10);
                                    String houreReelChambre =dateTimeMouvChambre.substring(3,5);
                                    houreReelChambre =convertHeure(houreReelChambre);
                                    String minChambre =dateTimeMouvChambre.substring(14,16);
                                    if (!result.equals("0")){
                                        data2.setText("Mouvement détecté le "+ jourChambre+"/"+moisChambre+" à "+ houreReelChambre+"h"+minChambre );
                                        if (alarmeActive){
                                            notifChambre();
                                        }

                                    }else {
                                        data2.setText("Aucun mouvement détecté");
                                    }
                                } catch (Exception e) {

                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                Volley.newRequestQueue(MainActivity.this).add(request);

                Log.e("alarme", "bouton clicker notif salon");

            }


        });
    }

    public String convertHeure(String heureInit){
        String vraiHeure="";

        if (heureInit.equals("01")){
            vraiHeure = "03";
        }
        if (heureInit.equals("02")){
            vraiHeure = "04";
        }
        if (heureInit.equals("03")){
            vraiHeure = "05";
        }
        if (heureInit.equals("04")){
            vraiHeure = "06";
        }
        if (heureInit.equals("05")){
            vraiHeure = "07";
        }
        if (heureInit.equals("06")){
            vraiHeure = "08";
        }
        if (heureInit.equals("07")){
            vraiHeure = "09";
        }
        if (heureInit.equals("08")){
            vraiHeure = "10";
        }
        if (heureInit.equals("09")){
            vraiHeure = "11";
        }
        if (heureInit.equals("10")){
            vraiHeure = "12";
        }
        if (heureInit.equals("11")){
            vraiHeure = "13";
        }
        if (heureInit.equals("12")){
            vraiHeure = "14";
        }
        if (heureInit.equals("13")){
            vraiHeure = "15";
        }
        if (heureInit.equals("14")){
            vraiHeure = "16";
        }
        if (heureInit.equals("15")){
            vraiHeure = "17";
        }
        if (heureInit.equals("16")){
            vraiHeure = "18";
        }
        if (heureInit.equals("17")){
            vraiHeure = "19";
        }
        if (heureInit.equals("18")){
            vraiHeure = "20";
        }
        if (heureInit.equals("19")){
            vraiHeure = "21";
        }
        if (heureInit.equals("20")){
            vraiHeure = "22";
        }
        if (heureInit.equals("21")){
            vraiHeure = "23";
        }
        if (heureInit.equals("22")){
            vraiHeure = "00";
        }
        if (heureInit.equals("23")){
            vraiHeure = "01";
        }
        if (heureInit.equals("24")){
            vraiHeure = "02";
        }


        return vraiHeure;
    }

    public void notifSalon(){
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlMouvSalon, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String datetime = response.getString("feeds");

                            parseValue = datetime;
                            String lastThree = parseValue.substring(parseValue.length() - 4);
                            Log.e("alarme", lastThree);

                            lastThree = lastThree.substring(0, 1);
                            Log.e("alarme", lastThree);
                            String dateTimeMouvSalon = datetime;
                            dateTimeMouvSalon = dateTimeMouvSalon.substring(16,35);
                            String mois = dateTimeMouvSalon.substring(5,7);
                            String jour = dateTimeMouvSalon.substring(8,10);
                            String houreReel =dateTimeMouvSalon.substring(11,13);
                            houreReel =convertHeure(houreReel);
                            String min =dateTimeMouvSalon.substring(14,16);


                            String text = "Mouvement détecté le "+ jour+"/"+mois+" à "+ houreReel+"h"+min ;

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "My notification");
                            builder.setContentTitle("Mouvement detecté: Salon");
                            builder.setChannelId("1");
                            builder.setContentText(text);
                            builder.setSmallIcon(R.drawable.ic_launcher_background);
                            builder.setAutoCancel(true);
                            Log.e("main", "notif salon");

                            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
                            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }

                            managerCompat.notify(1, builder.build());


                        } catch (Exception e) {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(MainActivity.this).add(request);

        Log.e("notif", "notif envoyé");
    }

    public void notifChambre(){

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, urlMouvChambre, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            String datetime = response.getString("feeds");

                            parseValue = datetime;
                            String result = parseValue.substring(parseValue.length() - 4);
                            Log.e("chambre", result);
                            result = result.substring(0, 1);
                            Log.e("chambre", result);
                            String dateTimeMouvChambre = datetime;
                            dateTimeMouvChambre = dateTimeMouvChambre.substring(16,35);
                            String moisChambre = dateTimeMouvChambre.substring(5,7);
                            String jourChambre = dateTimeMouvChambre.substring(8,10);
                            String houreReelChambre =dateTimeMouvChambre.substring(3,5);
                            houreReelChambre =convertHeure(houreReelChambre);
                            String minChambre =dateTimeMouvChambre.substring(14,16);

                            String text = "Mouvement détecté le "+ jourChambre+"/"+moisChambre+" à "+ houreReelChambre+"h"+minChambre ;

                            NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, "My notification");
                            builder.setContentTitle("Mouvement detecté: Chambre");
                            builder.setChannelId("1");
                            builder.setContentText(text);
                            builder.setSmallIcon(R.drawable.ic_launcher_background);
                            builder.setAutoCancel(true);
                            Log.e("main", "bouton clicker notif accueil");

                            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(MainActivity.this);
                            if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }

                            managerCompat.notify(1, builder.build());


                        } catch (Exception e) {

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(MainActivity.this).add(request);

        Log.e("alarme", "bouton clicker notif salon");



    }

    private void replaceFragment(Fragment fragment) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment_content_main,fragment);
        fragmentTransaction.commit();

    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

 */

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public Boolean getAlarmeActive() {
        return alarmeActive;
    }

    public void setAlarmeActive(Boolean alarmeActive) {
        this.alarmeActive = alarmeActive;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    public void sendEtatAlarme(Boolean alarme) {

    }
}