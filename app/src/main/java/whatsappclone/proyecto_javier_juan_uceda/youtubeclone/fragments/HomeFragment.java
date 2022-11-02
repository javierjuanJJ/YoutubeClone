package whatsappclone.proyecto_javier_juan_uceda.youtubeclone.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import whatsappclone.proyecto_javier_juan_uceda.youtubeclone.R;

public class HomeFragment extends Fragment {

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment channelDashboardFragment = new HomeFragment();
        Bundle args = new Bundle();
        channelDashboardFragment.setArguments(args);
        return channelDashboardFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}