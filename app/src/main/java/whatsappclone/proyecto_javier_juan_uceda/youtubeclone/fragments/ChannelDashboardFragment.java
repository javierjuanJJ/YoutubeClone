package whatsappclone.proyecto_javier_juan_uceda.youtubeclone.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import whatsappclone.proyecto_javier_juan_uceda.youtubeclone.Adapter.ViewPagerAdapter;
import whatsappclone.proyecto_javier_juan_uceda.youtubeclone.Constants.FieldsConstants;
import whatsappclone.proyecto_javier_juan_uceda.youtubeclone.Dashboard.AboutDashboard;
import whatsappclone.proyecto_javier_juan_uceda.youtubeclone.Dashboard.HomeDashboard;
import whatsappclone.proyecto_javier_juan_uceda.youtubeclone.Dashboard.PlaylistsDashboard;
import whatsappclone.proyecto_javier_juan_uceda.youtubeclone.Dashboard.SubscriptionDashboard;
import whatsappclone.proyecto_javier_juan_uceda.youtubeclone.Dashboard.VideosDashboard;
import whatsappclone.proyecto_javier_juan_uceda.youtubeclone.R;

public class ChannelDashboardFragment extends Fragment {

    private TextView userChannelName;


    private ViewPagerAdapter adapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;

    public ChannelDashboardFragment() {
        // Required empty public constructor
    }

    public static ChannelDashboardFragment newInstance() {
        return new ChannelDashboardFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_channel_dashboard, container, false);
        userChannelName = view.findViewById(R.id.user_channel_name);
        tabLayout = view.findViewById(R.id.tab);
        viewPager = view.findViewById(R.id.viewPager);

        initAdapter();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(FieldsConstants.CHANNEL_FIELD);

        reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String name = snapshot.child(FieldsConstants.CHANNEL_NAME_FIELD).getValue().toString();
                    userChannelName.setText(name);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    private void initAdapter() {
        adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.add(new HomeDashboard(), "Home");
        adapter.add(new VideosDashboard(), "Videos");
        adapter.add(new PlaylistsDashboard(), "Playlists");
        adapter.add(new AboutDashboard(), "About");
        adapter.add(new SubscriptionDashboard(), "Subscriptions");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}