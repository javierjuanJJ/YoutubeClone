package whatsappclone.proyecto_javier_juan_uceda.youtubeclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import whatsappclone.proyecto_javier_juan_uceda.youtubeclone.fragments.ExploreFragment;
import whatsappclone.proyecto_javier_juan_uceda.youtubeclone.fragments.HomeFragment;
import whatsappclone.proyecto_javier_juan_uceda.youtubeclone.fragments.LibraryFragment;
import whatsappclone.proyecto_javier_juan_uceda.youtubeclone.fragments.SubcriptionsFragment;

public class MainActivity extends AppCompatActivity implements View.OnApplyWindowInsetsListener, BottomNavigationView.OnNavigationItemSelectedListener {

    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;
    FrameLayout frameLayout;


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

        getSupportActionBar().setTitle("");

        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);
    }

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
                Toast.makeText(this, "search", Toast.LENGTH_SHORT).show();break;
            case R.id.account:
                Toast.makeText(this, "account", Toast.LENGTH_SHORT).show();break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }

    private void selectedFragment(Fragment fragment){
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
                Toast.makeText(this, getString(R.string.toastAddAVideo), Toast.LENGTH_SHORT).show();
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
}