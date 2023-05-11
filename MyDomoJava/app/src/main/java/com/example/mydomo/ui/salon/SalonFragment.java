package com.example.mydomo.ui.salon;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.mydomo.R;
import com.example.mydomo.databinding.FragmentSalonBinding;
import com.example.mydomo.ui.chambre.ChambreFragment;

public class SalonFragment extends Fragment {

    View view;
    private WebView webViewMouv;
    private WebView webViewTemp;
    private WebView webViewLumi;
    private WebView webViewHumi;

    private FragmentSalonBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        SalonViewModel salonViewModel =
                new ViewModelProvider(this).get(SalonViewModel.class);

        binding = com.example.mydomo.databinding.FragmentSalonBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //Definition des paramètres de la webview mouvement
        webViewMouv = (WebView) binding.afficheMouvementActuel;
        WebSettings webSettingsMouv = webViewMouv.getSettings();
        webSettingsMouv.setJavaScriptEnabled(true);
        webViewMouv.setWebViewClient(new WebViewClient());
        webViewMouv.loadUrl("https://thingspeak.com/channels/2102609/widgets/645408");


        //Definition des paramètres de la webview temperature
        webViewTemp = (WebView) binding.afficheTempActuel;
        WebSettings webSettingsTemp = webViewTemp.getSettings();
        webSettingsTemp.setJavaScriptEnabled(true);
        webViewTemp.setWebViewClient(new WebViewClient());
        webViewTemp.loadUrl("https://thingspeak.com/channels/2102609/widgets/645412");


        //Definition des paramètres de la webview luminosite
        webViewLumi = (WebView) binding.afficheLumiActuel;
        WebSettings webSettingsLumi = webViewLumi.getSettings();
        webSettingsLumi.setJavaScriptEnabled(true);
        webViewLumi.setWebViewClient(new WebViewClient());
        webViewLumi.loadUrl("https://thingspeak.com/channels/2102609/widgets/645411\n");

        //Definition des paramètres de la webview humidite
        webViewHumi = (WebView) binding.afficheHumiActuel;
        WebSettings webSettingsHumi = webViewHumi.getSettings();
        webSettingsHumi.setJavaScriptEnabled(true);
        webViewHumi.setWebViewClient(new WebViewClient());
        webViewHumi.loadUrl("https://thingspeak.com/channels/2102609/widgets/645414");

        return root;
    }





    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}