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
import android.widget.TextView;

import com.example.mydomo.MainActivity;
import com.example.mydomo.R;

public class Luminosite extends Activity {
    private WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.luminosite);

        //Definition des boutons
        Button buttonHome = (Button) findViewById(R.id.buttonHome);
        Button buttonVersPol = (Button) findViewById(R.id.buttonVersMouv);
        Button buttonRefresh = (Button) findViewById(R.id.buttonRefresh);

        //Definition des paramètres de la webview
        webView = (WebView) findViewById(R.id.afficheFieldLum);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://thingspeak.com/channels/1686204/charts/3?bgcolor=%23ffffff&color=%23d62020&dynamic=true&results=60&title=Mesure+Luminosit%C3%A9&type=line");

        //Orientation du téléphone
        int orientation = getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape
            webView.setInitialScale(400);
        } else {
            // In portrait
            webView.setInitialScale(220);
        }

        // Definition de la luminosité actuelle
        TextView textView = (TextView) findViewById(R.id.afficheLumiActuel);
        int lumiActual = 20;
        textView.setText(lumiActual);

        //Bouton pour aller vers la page d'accueil

        buttonHome.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("Luminosite", "Home");
                Intent i = new Intent(Luminosite.this, MainActivity.class);
                startActivity(i);
                onPause();
            }
        });
        /*

        //Bouton pour aller vers le capteur d'humidité
        buttonVersPol.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("Luminosite", "versPol");

                Intent i = new Intent(Luminosite.this, Pollution.class);
                startActivity(i);
                onPause();
            }
        });

         */

        //Bouton pour refresh
        buttonRefresh.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.i("refresh", "refresh");
                Luminosite.this.webView.loadUrl("https://thingspeak.com/channels/2102609/charts/6?bgcolor=%23ffffff&color=%23d62020&dynamic=true&results=60&type=line&update=15");
            }
        });
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