package com.example.mydomo.ui.salonAct;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import com.example.mydomo.MainActivity;
import com.example.mydomo.R;

public class Salon extends Activity {
    private WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temperature);

        //Definition des boutons
        Button buttonHome = (Button) findViewById(R.id.buttonHome);
        Button buttonVersHum = (Button) findViewById(R.id.buttonVersHum);
        Button buttonRefresh = (Button) findViewById(R.id.buttonRefresh);



        //Definition des paramètres de la webview
        webView = (WebView) findViewById(R.id.afficheFieldTmp);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://thingspeak.com/channels/2102609/charts/1?bgcolor=%23ffffff&color=%23d62020&dynamic=true&results=60&type=line&update=15");

        //Orientation du téléphone
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
            webView.setInitialScale(400);
        } else {
            // In portrait
            webView.setInitialScale(220);
        }

        //Bouton pour aller vers la page d'accueil

        buttonHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("Mouvement", "Home");
                Intent i = new Intent(Salon.this, MainActivity.class);
                startActivity(i);
                onPause();
            }
        });

        /*
        //Bouton pour aller vers le capteur d'humidité
        buttonVersHum.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("Mouvement", "versHum");

                Intent i = new Intent(Salon.this, Humidite.class);
                startActivity(i);
                onPause();
            }
        });

         */

    }


    /*------------------------Cycle de vie de l'activité-----------------------*/
    protected void onResume() {
        super.onResume();
    }

    protected void onPause() {
        super.onPause();
    }

    protected void onStop() {
        super.onStop();
    }

    protected void onRestart() {
        super.onRestart();
    }

    protected void onDestroy() {
        super.onDestroy();
    }
}