package whatsappclone.proyecto_javier_juan_uceda.youtubeclone;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import whatsappclone.proyecto_javier_juan_uceda.youtubeclone.Constants.FieldsConstants;
import whatsappclone.proyecto_javier_juan_uceda.youtubeclone.fragments.ChannelDashboardFragment;
import whatsappclone.proyecto_javier_juan_uceda.youtubeclone.fragments.ExploreFragment;
import whatsappclone.proyecto_javier_juan_uceda.youtubeclone.fragments.HomeFragment;
import whatsappclone.proyecto_javier_juan_uceda.youtubeclone.fragments.LibraryFragment;
import whatsappclone.proyecto_javier_juan_uceda.youtubeclone.fragments.SubcriptionsFragment;

public class MainActivity extends AppCompatActivity implements View.OnApplyWindowInsetsListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;
    private ImageView userProfileImage;
    private FirebaseAuth auth;
    private FirebaseUser user;
    Fragment fragment;

    private static final int PERMISSION = 101;
    private static final int PICK_VIDEO = 102;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUI();

    }

    private void setUI() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        frameLayout = findViewById(R.id.frame_layout);

        userProfileImage = findViewById(R.id.user_profile_image);
        appBarLayout = findViewById(R.id.appBar);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        checkPermission();

        userProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user != null) {
                    // Toast.makeText(MainActivity.this, "User alreasy sign in", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, AccountActivity.class));
                    getProfileImage();
                }
                else {
                    userProfileImage.setImageResource(R.drawable.ic_launcher_background);
                    showDialogue();
                }
            }
        });

        getSupportActionBar().setTitle("");

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);

        if (user != null) {
            getProfileImage();
        }

    }

    private void showPublishContentDialogue() {
        Dialog dialog = new Dialog(MainActivity.this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.upload_dialogue);
        dialog.setCanceledOnTouchOutside(true);

        TextView txt_upload_video = dialog.findViewById(R.id.txt_upload_video);
        TextView txt_make_post = dialog.findViewById(R.id.txt_make_post);
        TextView txt_poll = dialog.findViewById(R.id.txt_poll);

        txt_upload_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(MainActivity.this, PublishContentActivity.class);
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("video/*");
                startActivityForResult(Intent.createChooser(intent,"Select video"), PICK_VIDEO);
                //intent.putExtra("type","video");
                startActivity(intent);
            }
        });
        dialog.show();
    }



    private void showDialogue() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(true);
        ViewGroup viewGroup = findViewById(androidx.appcompat.R.id.content);
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_signin_dialogue, viewGroup, false);
        builder.setView(view);

        TextView txtGoogleSignIn = view.findViewById(R.id.txt_google_signIn);
        txtGoogleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        builder.create().show();

    }

    private static final int RC_SIGN_IN = 100;
    private AppBarLayout appBarLayout;

    private void signIn() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        showDialogue();

        Intent intent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case RC_SIGN_IN :

                if (resultCode == RESULT_OK && data != null) {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);


                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
                        auth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()){
                                    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

                                    HashMap<String, Object> map = new HashMap<>();
                                    map.put(FieldsConstants.USERNAME_FIELD, account.getDisplayName());
                                    map.put(FieldsConstants.EMAIL_FIELD, account.getEmail());
                                    map.put(FieldsConstants.PROFILE_FIELD, String.valueOf(account.getPhotoUrl()));
                                    map.put(FieldsConstants.UID_FIELD, firebaseUser.getUid());
                                    map.put(FieldsConstants.SEARCH_FIELD, account.getDisplayName().toLowerCase());

                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
                                    reference.child(firebaseUser.getUid()).setValue(map);

                                }
                                else {
                                    Toast.makeText(MainActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } catch (ApiException e) {
                        Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
                break;
            case PICK_VIDEO:
                if (resultCode == RESULT_OK && data != null) {
                    videoUri = data.getData();
                    Intent intent = new Intent(MainActivity.this, PublishContentActivity.class);
                    intent.putExtra("type", "video");
                    intent.setData(videoUri);
                    startActivity(intent);
                }
                break;
        }



    }

    Uri videoUri;
    MediaController mediaController;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.notification:
                Toast.makeText(this, "notification", Toast.LENGTH_SHORT).show();
                break;
            case R.id.search:
                Toast.makeText(this, FieldsConstants.SEARCH_FIELD, Toast.LENGTH_SHORT).show();break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }

    private void selectedFragment(Fragment fragment){
        setStatusColor("#FFFFFF");
        appBarLayout.setVisibility(View.VISIBLE);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
        return null;
    }

    /**
     * Called when an item in the navigation menu is selected.
     *
     * @param item The selected item
     * @return true to display the item as the selected item and false if the item should not be
     * selected. Consider setting non-selectable items as disabled preemptively to make them
     * appear non-interactive.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.home:
                HomeFragment homeFragment = new HomeFragment();
                selectedFragment(homeFragment);
                break;
            case R.id.publish:
                showPublishContentDialogue();
                break;
            case R.id.explore:
                ExploreFragment exploreFragment = new ExploreFragment();
                selectedFragment(exploreFragment);
                break;
            case R.id.subscriptions:
                SubcriptionsFragment subscriptionsFragment = new SubcriptionsFragment();
                selectedFragment(subscriptionsFragment);
                break;

            case R.id.library:
                LibraryFragment libraryFragment = new LibraryFragment();
                selectedFragment(libraryFragment);
                break;

        }
        return false;
    }

    private void getProfileImage(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users");
        reference.child(user.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String profilePhotoUrl = snapshot.child(FieldsConstants.PROFILE_FIELD).getValue().toString();

                    Picasso.get().load(profilePhotoUrl).placeholder(R.drawable.ic_launcher_background).into(userProfileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showFragment(){
        String type = getIntent().getStringExtra("type");
        if (type != null) {
            switch (type){
                case "channel":
                    setStatusColor("#99FF0080");
                    appBarLayout.setVisibility(View.GONE);
                    fragment = ChannelDashboardFragment.newInstance();
                break;
            }
            if (fragment != null){
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.frame_layout,fragment).commit();
            }
            else {
                Toast.makeText(this, "Something went bad", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setStatusColor(String color){
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor(color));
    }


    private void checkPermission(){
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PERMISSION
        ) {

            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION);

            } else {
            Log.d(MainActivity.class.getSimpleName() + " permissions", "checkPermission(): Permision granted");
        }
    }

}