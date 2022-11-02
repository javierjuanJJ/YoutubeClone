package whatsappclone.proyecto_javier_juan_uceda.youtubeclone.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.Collection;

import whatsappclone.proyecto_javier_juan_uceda.youtubeclone.Dashboard.AboutDashboard;
import whatsappclone.proyecto_javier_juan_uceda.youtubeclone.Dashboard.HomeDashboard;
import whatsappclone.proyecto_javier_juan_uceda.youtubeclone.Dashboard.PlaylistsDashboard;
import whatsappclone.proyecto_javier_juan_uceda.youtubeclone.Dashboard.SubscriptionDashboard;
import whatsappclone.proyecto_javier_juan_uceda.youtubeclone.Dashboard.VideosDashboard;
import whatsappclone.proyecto_javier_juan_uceda.youtubeclone.fragments.HomeFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {


   ArrayList<Fragment> fragments;
   ArrayList<Fragment> fragmentsUsed = new ArrayList<>();
   ArrayList<String> strings = new ArrayList<>();
   /**
    * Constructor for {@link FragmentPagerAdapter} that sets the fragment manager for the adapter.
    * This is the equivalent of calling {@link #FragmentPagerAdapter(FragmentManager, int)} and
    * passing in {@link #BEHAVIOR_SET_USER_VISIBLE_HINT}.
    *
    * <p>Fragments will have {@link Fragment#setUserVisibleHint(boolean)} called whenever the
    * current Fragment changes.</p>
    *
    * @param fm fragment manager that will interact with this adapter
    * @deprecated use {@link #FragmentPagerAdapter(FragmentManager, int)} with
    * {@link #BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT}
    */
   public ViewPagerAdapter(@NonNull FragmentManager fm) {
      super(fm);
      fragments = new ArrayList<>();
      fragments.add(new HomeFragment());
      fragments.add(new AboutDashboard());
      fragments.add(new VideosDashboard());
      fragments.add(new PlaylistsDashboard());
      fragments.add(new SubscriptionDashboard());
   }

   /**
    * Return the Fragment associated with a specified position.
    *
    * @param position
    */
   @NonNull
   @Override
   public Fragment getItem(int position) {
      return fragments.get(position);
   }

   public void add(Fragment fr, String sr){
      fragmentsUsed.add(fr);
      strings.add(sr);
   }

   @Nullable
   @Override
   public CharSequence getPageTitle(int position) {
      return strings.get(position);
   }

   /**
    * Return the number of views available.
    */
   @Override
   public int getCount() {
      return fragments.size();
   }
}
