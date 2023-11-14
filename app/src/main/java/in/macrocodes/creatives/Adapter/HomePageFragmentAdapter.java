package in.macrocodes.creatives.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import in.macrocodes.creatives.Fragments.HomeFragment;
import in.macrocodes.creatives.Fragments.ProfileSettingsFragment;
import in.macrocodes.creatives.Fragments.SearchFragment;


public class HomePageFragmentAdapter extends FragmentPagerAdapter {


    public HomePageFragmentAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch(position) {

            case 0:
                return new HomeFragment();
            case 1:
                return new ProfileSettingsFragment();
//            case 2:
//                return new ProfileSettingsFragment();
            default:
                return  null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }


}
