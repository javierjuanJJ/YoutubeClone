package whatsappclone.proyecto_javier_juan_uceda.youtubeclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class PublishContentActivity extends AppCompatActivity {

    private VideoView videoView;
    private String type;
    private Uri videoUri;
    private MediaController mediaController;
    private EditText chip;
    private TextView txtUploadVideo, txtChoosePlaylist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_content);

        setUI();
    }

    private void setUI() {
        videoView = findViewById(R.id.videoView);
        mediaController = new MediaController(PublishContentActivity.this);
        chip = findViewById(R.id.inputVideoTag);
        txtUploadVideo = findViewById(R.id.txt_upload_video);
        txtChoosePlaylist = findViewById(R.id.choosePlayList);
        Intent intent = getIntent();

        if (intent != null) {
            videoUri = intent.getData();
            videoView.setVideoURI(videoUri);
            videoView.setMediaController(mediaController);
            videoView.start();
        }

        txtChoosePlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPlayListDialogue();
            }
        });
    }

    Dialog dialog;

    private void showPlayListDialogue() {
        dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.playlist_dialog);
        dialog.setCancelable(true);
        
        EditText input_playlist_name = dialog.findViewById(R.id.input_playlist_name);
        TextView txt_add = dialog.findViewById(R.id.txt_add);
        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);
        
        checkUserAlreadyHavePlaylist(recyclerView);
        
        txt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = input_playlist_name.getText().toString();
                if (value.isEmpty()){
                    Toast.makeText(PublishContentActivity.this, "Enter Playlist name", Toast.LENGTH_SHORT).show();
                }
                else {
                    createPlayList(value);
                }
                    
            }
        });
        
        dialog.show();
    }

    private void createPlayList(String value) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Playlists");

        HashMap<String, Object> map = new HashMap<>();
        map.put("playlist_name", value);
        map.put("videos",0);
        map.put("uid",user.getUid());
        
        reference.child(user.getUid()).child(value).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(PublishContentActivity.this, "New playlist created", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(PublishContentActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkUserAlreadyHavePlaylist(RecyclerView recyclerView) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Playlist");
        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Toast.makeText(PublishContentActivity.this, "User have playlists", Toast.LENGTH_SHORT).show();
                    recyclerView.setVisibility(View.VISIBLE);
                }
                else {
                    recyclerView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(PublishContentActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}