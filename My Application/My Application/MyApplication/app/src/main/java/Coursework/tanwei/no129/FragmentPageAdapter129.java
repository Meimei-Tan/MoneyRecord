package Coursework.tanwei.no129;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.PagerAdapter;

import java.util.List;


public class FragmentPageAdapter129 extends FragmentPagerAdapter {
    private List<Fragment> mviewlist129;
    private String[] mtitles129;
    public FragmentPageAdapter129(FragmentManager fragmentManager, List<Fragment> viewlist,String[] titles){
        super(fragmentManager);
        this.mviewlist129 = viewlist;
        this.mtitles129 = titles;
        Log.i("AddToBackStack","true");

    }

    
    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        return super.instantiateItem(container, position);
    }

    @Override
    public int getCount() {
        return mviewlist129.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mviewlist129.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mtitles129[position];
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.destroyItem(container, position, object);
    }
}
