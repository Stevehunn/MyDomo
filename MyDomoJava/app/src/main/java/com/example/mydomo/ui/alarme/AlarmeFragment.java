package com.example.mydomo.ui.alarme;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.ViewModelProvider;

import android.app.Notification;
import android.app.NotificationChannel;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import com.example.mydomo.MainActivity;
import com.example.mydomo.R;
import com.example.mydomo.databinding.FragmentAlarmeBinding;


public class AlarmeFragment extends Fragment {

    private FragmentAlarmeBinding binding;

    Boolean etatAlarme =false;

    Button btnActAlarme;
    Button btnDesAlarme;

    TextView afficheEtat;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        AlarmeViewModel alarmeViewModel =
                new ViewModelProvider(this).get(AlarmeViewModel.class);


        binding = com.example.mydomo.databinding.FragmentAlarmeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        afficheEtat = (TextView) root.findViewById(R.id.afficheEtatAlarme);
        btnActAlarme = (Button) root.findViewById(R.id.btnActiveAlarme);
        btnDesAlarme = (Button) root.findViewById(R.id.btnDesactiveAlarme);

        btnActAlarme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            etatAlarme =true;
            afficheEtat.setText("L'alarme est activé");
            mListener.sendEtatAlarme(etatAlarme);
            }
        });

        btnDesAlarme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etatAlarme =false;
                afficheEtat.setText("L'alarme n'est pas activé");
                mListener.sendEtatAlarme(etatAlarme);
            }
        });

        if (etatAlarme){
            afficheEtat.setText("L'alarme est activé");
        } else {
            afficheEtat.setText("L'alarme n'est pas activé");
        }

        return root;
    }

    AlarmeListener mListener;
    
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        mListener = (AlarmeListener) context;
    }

    public interface AlarmeListener{
        void sendEtatAlarme(Boolean alarme);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}