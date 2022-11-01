package whatsappclone.proyecto_javier_juan_uceda.youtubeclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private CircleImageView userProfileImage;
    private TextView username, email, txtYourChannerl, txtSettings, txtHelp;

    private FirebaseAuth auth;
    private FirebaseUser user;
    private DatabaseReference reference;
    private String profileValue;

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
                    Toast.makeText(AccountActivity.this, getString(R.string.toastUserHaveChannel), Toast.LENGTH_SHORT).show();
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

        EditText input_channel_name = dialog.findViewById(R.id.input_channel_name);
        EditText input_description = dialog.findViewById(R.id.input_description);
        TextView txt_create_channel = dialog.findViewById(R.id.txt_create_channel);

        txt_create_channel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = input_channel_name.getText().toString();
                String description = input_description.getText().toString();

                if (name.isEmpty() || description.isEmpty()){

                    if (name.isEmpty()){
                        Toast.makeText(AccountActivity.this, getString(R.string.toastRequiredName), Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(AccountActivity.this, getString(R.string.toastRequiredDescription), Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    createNewChannel(name, description, dialog);
                }
            }
        });

        dialog.show();
    }

    private void createNewChannel(String name, String description, Dialog dialog) {
        ProgressDialog progressDialog = new ProgressDialog(AccountActivity.this);
        progressDialog.setTitle(getString(R.string.progressDialogTitle));
        progressDialog.setMessage(getString(R.string.progressDialogMessage));
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        String date = DateFormat.getDateInstance().format(new Date());

        HashMap<String, Object> map = new HashMap<>();
        map.put("channel_name", name);
        map.put("description", description);
        map.put("joined", date);
        map.put("uid", user.getUid());
        map.put("channel_logo", profileValue );

        reference.child("Channel").child(user.getUid()).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressDialog.dismiss();
                    dialog.dismiss();
                    Toast.makeText(AccountActivity.this, getString(R.string.toastChannelCreated, name), Toast.LENGTH_SHORT).show();
                }
                else{
                    progressDialog.dismiss();
                    dialog.dismiss();
                    Toast.makeText(AccountActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void getData() {
        reference.child("Users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String usernameValue = snapshot.child("username").getValue().toString();
                    String emailValue = snapshot.child("email").getValue().toString();
                    profileValue = snapshot.child("profile").getValue().toString();

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