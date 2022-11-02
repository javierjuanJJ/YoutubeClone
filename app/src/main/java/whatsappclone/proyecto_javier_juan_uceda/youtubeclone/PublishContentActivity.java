package whatsappclone.proyecto_javier_juan_uceda.youtubeclone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class PublishContentActivity extends AppCompatActivity {

    private VideoView videoView;
    private String type;
    private Uri videoUri;
    private MediaController mediaController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_content);

        setUI();
    }

    private void setUI() {
        videoView = findViewById(R.id.videoView);
        mediaController = new MediaController(PublishContentActivity.this);

        Intent intent = getIntent();

        if (intent != null) {
            videoUri = intent.getData();
            videoView.setVideoURI(videoUri);
            videoView.setMediaController(mediaController);
            videoView.start();
        }
    }
}