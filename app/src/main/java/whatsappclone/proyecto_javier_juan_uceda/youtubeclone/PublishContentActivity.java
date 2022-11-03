package whatsappclone.proyecto_javier_juan_uceda.youtubeclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.material.chip.Chip;

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
        dialog.show();
    }
}