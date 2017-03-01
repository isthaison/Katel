package nson.katel;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Nson on 2/22/2017.
 */

public class AdapterIndex extends FragmentPagerAdapter {

    ArrayList<Fragment> fragments = new ArrayList<Fragment>();
    ArrayList<String> tabtitle = new ArrayList<String>();


    public void addFragments(Fragment fragment, String title) {
        this.fragments.add(fragment);
        this.tabtitle.add(title);

    }


    public AdapterIndex(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    /**
     * Return the number of views available.
     */
    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabtitle.get(position);
    }
}