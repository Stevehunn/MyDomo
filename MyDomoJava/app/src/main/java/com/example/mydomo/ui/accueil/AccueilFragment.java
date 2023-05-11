package com.example.mydomo.ui.accueil;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.mydomo.R;
import com.example.mydomo.databinding.FragmentHomeBinding;

import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;


public class AccueilFragment extends Fragment {


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



    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AccueilViewModel accueilViewModel =
                new ViewModelProvider(this).get(AccueilViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}