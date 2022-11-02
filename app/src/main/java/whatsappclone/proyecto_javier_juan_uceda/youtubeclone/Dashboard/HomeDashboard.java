package whatsappclone.proyecto_javier_juan_uceda.youtubeclone.Dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import whatsappclone.proyecto_javier_juan_uceda.youtubeclone.R;
import whatsappclone.proyecto_javier_juan_uceda.youtubeclone.fragments.ChannelDashboardFragment;

public class HomeDashboard extends Fragment {

    public HomeDashboard() {
        // Required empty public constructor
    }

    public static HomeDashboard newInstance() {
        HomeDashboard fragment = new HomeDashboard();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }
}