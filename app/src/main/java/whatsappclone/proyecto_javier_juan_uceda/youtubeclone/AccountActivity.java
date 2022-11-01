package whatsappclone.proyecto_javier_juan_uceda.youtubeclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CircleImageView userProfileImage;
    private TextView username, email, txtYourChannerl, txtSettings, txtHelp;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        setUI();
    }

    private void setUI() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        userProfileImage = findViewById(R.id.user_profile_image);
        username = findViewById(R.id.user_channel_name);
        email = findViewById(R.id.email);
        txtYourChannerl = findViewById(R.id.txtYourChannerl);
        txtSettings = findViewById(R.id.txtSettings);
        txtHelp = findViewById(R.id.txtHelp);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference();

        getData();
        txtYourChannerl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkUserHaveChannel();
            }


        });

    }

    private void checkUserHaveChannel() {
        reference.child("Channel").child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Toast.makeText(AccountActivity.this, "User have a channel", Toast.LENGTH_SHORT).show();
                }
                else {
                    showDialogue();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AccountActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDialogue() {
        Dialog dialog = new Dialog(AccountActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.channel_dialog);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);

        dialog.show();
    }

    private void getData() {
        reference.child("Users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String usernameValue = snapshot.child("username").getValue().toString();
                    String emailValue = snapshot.child("email").getValue().toString();
                    String profileValue = snapshot.child("profile").getValue().toString();

                    username.setText(usernameValue);
                    email.setText(emailValue);

                    try {
                        Picasso.get().load(profileValue).placeholder(R.drawable.ic_launcher_background).into(userProfileImage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(AccountActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}