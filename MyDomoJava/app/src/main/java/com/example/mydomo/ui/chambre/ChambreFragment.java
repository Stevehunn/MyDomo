package com.example.mydomo.ui.chambre;

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

import com.example.mydomo.databinding.FragmentChambreBinding;

public class ChambreFragment extends Fragment {

    View view;
    private WebView webViewMouv;
    private WebView webViewTemp;
    private WebView webViewLumi;
    private WebView webViewHumi;

    private FragmentChambreBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ChambreViewModel chambreViewModel =
                new ViewModelProvider(this).get(ChambreViewModel.class);

        binding = FragmentChambreBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        //Definition des paramètres de la webview mouvement
        webViewMouv = (WebView) binding.afficheMouvementActuel;
        WebSettings webSettingsMouv = webViewMouv.getSettings();
        webSettingsMouv.setJavaScriptEnabled(true);
        webViewMouv.setWebViewClient(new WebViewClient());
        webViewMouv.loadUrl("https://thingspeak.com/channels/2102609/widgets/644715");


        //Definition des paramètres de la webview temperature
        webViewTemp = (WebView) binding.afficheTempActuel;
        WebSettings webSettingsTemp = webViewTemp.getSettings();
        webSettingsTemp.setJavaScriptEnabled(true);
        webViewTemp.setWebViewClient(new WebViewClient());
        webViewTemp.loadUrl("https://thingspeak.com/channels/2102609/widgets/644712");


        //Definition des paramètres de la webview luminosite
        webViewLumi = (WebView) binding.afficheLumiActuel;
        WebSettings webSettingsLumi = webViewLumi.getSettings();
        webSettingsLumi.setJavaScriptEnabled(true);
        webViewLumi.setWebViewClient(new WebViewClient());
        webViewLumi.loadUrl("https://thingspeak.com/channels/2102609/widgets/645406");

        //Definition des paramètres de la webview humidite
        webViewHumi = (WebView) binding.afficheHumiActuel;
        WebSettings webSettingsHumi = webViewHumi.getSettings();
        webSettingsHumi.setJavaScriptEnabled(true);
        webViewHumi.setWebViewClient(new WebViewClient());
        webViewHumi.loadUrl("https://thingspeak.com/channels/2102609/widgets/645407");



        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}