package whatsappclone.proyecto_javier_juan_uceda.youtubeclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import whatsappclone.proyecto_javier_juan_uceda.youtubeclone.Adapter.PlayListAdapter;
import whatsappclone.proyecto_javier_juan_uceda.youtubeclone.Constants.FieldsConstants;
import whatsappclone.proyecto_javier_juan_uceda.youtubeclone.Constants.IntentConstants;
import whatsappclone.proyecto_javier_juan_uceda.youtubeclone.Models.PlaylistModel;

public class PublishContentActivity extends AppCompatActivity {

    private VideoView videoView;
    private String type, selectedPlaylist;
    private int videosCount;
    private Uri videoUri;
    private MediaController mediaController;
    private EditText chip;
    private TextView txtUploadVideo, txtChoosePlaylist, progressText;

    private EditText inputVideoTitle, inputVideoDescription;
    private LinearLayout progressLayout;
    private ProgressBar progressBar;

    private FirebaseUser user;
    private DatabaseReference reference;
    private StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_content);

        setUI();
    }

    private void setUI() {
        videoView = findViewById(R.id.videoView);
        inputVideoTitle = findViewById(R.id.inputVideoTitle);
        inputVideoDescription = findViewById(R.id.inputVideoDescription);
        progressLayout = findViewById(R.id.progressLyT);
        progressText = findViewById(R.id.progress_text);
        progressBar = findViewById(R.id.progressBar);
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

        user = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference().child(FieldsConstants.CONTENT_FIELD);
        storageReference = FirebaseStorage.getInstance().getReference().child(FieldsConstants.CONTENT_FIELD);


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

        ArrayList<PlaylistModel> list = new ArrayList<>();
        PlayListAdapter adapter;

        RecyclerView recyclerView = dialog.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);

        checkUserAlreadyHavePlaylist(recyclerView);




        adapter = new PlayListAdapter(PublishContentActivity.this, list, new PlayListAdapter.OnItemClick() {
            @Override
            public void onItemClick(PlaylistModel model, PlayListAdapter.OnItemClick listener) {
                dialog.dismiss();
                selectedPlaylist = model.getPlaylist_name();
                videosCount = model.getVideos();
                txtChoosePlaylist.setText("Playlist: " + model.getPlaylist_name());
            }
        });

        showAllPlayLists(adapter, list);

        recyclerView.setAdapter(adapter);




        txt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value = input_playlist_name.getText().toString();
                if (value.isEmpty()){
                    Toast.makeText(PublishContentActivity.this, getString(R.string.toastEnterPlaylist), Toast.LENGTH_SHORT).show();
                }
                else {
                    createPlayList(value);
                }
                    
            }
        });
        
        txtUploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = inputVideoTitle.getText().toString();
                String description = inputVideoDescription.getText().toString();
                //String tags = inputVideoTitle.getText().toString();
            
                if (title.isEmpty() || description.isEmpty()){
                    if (title.isEmpty()){
                        Toast.makeText(PublishContentActivity.this, getString(R.string.toastFillTitle), Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(PublishContentActivity.this, getString(R.string.toastFillDescription), Toast.LENGTH_SHORT).show();
                    }
                }
                else if(txtChoosePlaylist.getText().toString().equals(getString(R.string.tvChoosePlaylist))){
                    Toast.makeText(PublishContentActivity.this, getString(R.string.toastSelectPlaylist), Toast.LENGTH_SHORT).show();
                }
                else {
                    uploadVideoToStorage(title, description);
                }
            }
        });
        
        dialog.show();
    }

    private void uploadVideoToStorage(String title, String description) {
        progressLayout.setVisibility(View.VISIBLE);
        final StorageReference sRef = storageReference.child(user.getUid()).child(System.currentTimeMillis() + ","+getFileExtension(videoUri));
        sRef.putFile(videoUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                sRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String videoUrl = uri.toString();

                        saveDataToFirebase(title, description, videoUrl);
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                double progress = 108.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount();
                progressBar.setProgress((int) progress);
                progressText.setText("Uploading " + (int) progress + "%");

            }
        });
    }

    private void saveDataToFirebase(String title, String description, String videoUrl) {
        String currentDate = DateFormat.getDateInstance().format(new Date());
        String id = reference.push().getKey();
        HashMap<String, Object> map = new HashMap<>();

        map.put(FieldsConstants.ID_FIELD, id);
        map.put(FieldsConstants.VIDEO_TITLE_FIELD, title);
        //map.put("video_tag",tag);
        map.put(FieldsConstants.PLAYLIST_FIELD, selectedPlaylist);
        map.put(IntentConstants.VIDEO_URL_FIELD, videoUrl);
        map.put(FieldsConstants.PUBLISHER_FIELD, user.getUid());
        map.put(IntentConstants.TYPE_INTENT_KEY, IntentConstants.VIDEO_INTENT_KEY);
        map.put(FieldsConstants.VIEWS_FIELD, 0);
        map.put(FieldsConstants.DATE_FIELD, currentDate);

        reference.child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    progressLayout.setVisibility(View.GONE);
                    Toast.makeText(PublishContentActivity.this, getString(R.string.toastVideoUploaded), Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(PublishContentActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                    finish();

                    updateVideoCount();

                }
                else {
                    progressLayout.setVisibility(View.GONE);
                    Toast.makeText(PublishContentActivity.this, getString(R.string.toastFailedUpload), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void updateVideoCount() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(FieldsConstants.PLAYLISTS_FIELD);

        int update = videosCount + 1;
        HashMap<String, Object> map = new HashMap<>();
        map.put(FieldsConstants.VIDEOS_FIELD, update);

        reference.child(user.getUid()).child(selectedPlaylist).updateChildren(map);
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void showAllPlayLists(PlayListAdapter adapter2, ArrayList<PlaylistModel> list2) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(FieldsConstants.PLAYLISTS_FIELD);
            reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()){
                        list2.clear();
                        for (DataSnapshot dataSnapshot :
                                snapshot.getChildren()) {
                            PlaylistModel model = dataSnapshot.getValue(PlaylistModel.class);
                            Log.i("playlistitem",model.getPlaylist_name());
                            list2.add(model);
                        }
                        adapter2.notifyDataSetChanged();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(PublishContentActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void createPlayList(String value) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(FieldsConstants.PLAYLISTS_FIELD);

        HashMap<String, Object> map = new HashMap<>();
        map.put(FieldsConstants.PLAYLIST_NAME_FIELD, value);
        map.put(FieldsConstants.VIDEOS_FIELD, 0);
        map.put(FieldsConstants.UID_FIELD,user.getUid());
        
        reference.child(user.getUid()).child(value).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(PublishContentActivity.this, getString(R.string.toastNewPlaylistCreated), Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(PublishContentActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void checkUserAlreadyHavePlaylist(RecyclerView recyclerView) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(FieldsConstants.PLAYLISTS_FIELD);
        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Toast.makeText(PublishContentActivity.this, getString(R.string.toastUserHavePlaylists), Toast.LENGTH_SHORT).show();
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