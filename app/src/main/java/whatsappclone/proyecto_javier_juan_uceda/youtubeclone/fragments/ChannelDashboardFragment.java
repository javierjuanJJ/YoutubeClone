package whatsappclone.proyecto_javier_juan_uceda.youtubeclone.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import whatsappclone.proyecto_javier_juan_uceda.youtubeclone.Constants.FieldsConstants;
import whatsappclone.proyecto_javier_juan_uceda.youtubeclone.R;

public class ChannelDashboardFragment extends Fragment {

    private TextView userChannelName;

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
}