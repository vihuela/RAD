package worldgo.rad.vm;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.flyco.tablayout.listener.CustomTabEntity;

import java.util.ArrayList;
import java.util.List;

import worldgo.common.viewmodel.framework.AbstractViewModel;
import worldgo.rad.R;
import worldgo.rad.databinding.ActivityMainBinding;
import worldgo.rad.ui.ImageFragment;
import worldgo.rad.ui.MainActivity;
import worldgo.rad.ui.NewsFragment;

/**
 * @author ricky.yao on 2017/3/23.
 */

public class MainActivityVM extends AbstractViewModel<MainActivity> {
    private final ThreadLocal<List<Fragment>> mFragments = new ThreadLocal<List<Fragment>>() {
        @Override
        protected List<Fragment> initialValue() {
            return new ArrayList<>();
        }
    };
    private String[] mTitles = {"妹子", "新闻"};
    /**
     * CommonTabLayout  unUse
     */
    private int[] mIconUnselectIds = {
            R.mipmap.ic_home_normal, R.mipmap.ic_girl_normal, R.mipmap.ic_care_normal};
    private int[] mIconSelectIds = {
            R.mipmap.ic_home_selected, R.mipmap.ic_girl_selected, R.mipmap.ic_care_selected};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();

    private ActivityMainBinding binding;

    @Override
    public void onCreate(@Nullable Bundle arguments, @Nullable Bundle savedInstanceState) {
        super.onCreate(arguments, savedInstanceState);
    }

    @Override
    public void onBindView(@NonNull MainActivity view) {
        super.onBindView(view);

        binding = view.getBinding();

        mFragments.get().clear();
        mFragments.get().add(ImageFragment.getInstance(mTitles[0]));
        mFragments.get().add(NewsFragment.getInstance(mTitles[1]));

        binding.mViewPager.setAdapter(new FragmentPagerAdapter(view.getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mFragments.get().get(position);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mTitles[position];
            }

            @Override
            public int getCount() {
                return mTitles.length;
            }
        });


        binding.mTab.addItem(new BottomNavigationItem(R.mipmap.ic_girl_normal, "美图"))
                .addItem(new BottomNavigationItem(R.mipmap.ic_home_normal, "新闻"))
                .addItem(new BottomNavigationItem(R.mipmap.ic_care_normal, "木有"))
                .addItem(new BottomNavigationItem(R.mipmap.ic_care_normal, "木有"))
                .initialise();
        binding.mTab.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position) {
                binding.mViewPager.setCurrentItem(position, false);
            }

            @Override
            public void onTabUnselected(int position) {

            }

            @Override
            public void onTabReselected(int position) {

            }
        });
        binding.mViewPager.setOffscreenPageLimit(mTitles.length);
        binding.mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                binding.mTab.selectTab(position);
            }
        });


    }

}
