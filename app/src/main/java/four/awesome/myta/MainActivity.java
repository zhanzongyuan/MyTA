package four.awesome.myta;

import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.List;

import four.awesome.myta.fragments.AssignmentFragment;
import four.awesome.myta.fragments.CourseFragment;
import four.awesome.myta.fragments.UserFragment;
import four.awesome.myta.models.Course;
import four.awesome.myta.models.User;

/**
 * Main activity for navigation between three fragment.
 */
public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    private AssignmentFragment fragmentAssignment;
    private CourseFragment fragmentCourse;
    private UserFragment fragmentUser;

    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialData();
        initialNavigationView();
    }



    private void initialData() {
        fragmentAssignment = AssignmentFragment.newInstance();
        fragmentCourse = CourseFragment.newInstance();
        fragmentUser = UserFragment.newInstance();
        viewPager = (ViewPager) findViewById(R.id.frame_layout);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
    }
    /**
     * Initial navigation item click event.
     */
    private void initialNavigationView() {
        viewPager.addOnPageChangeListener(this);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return fragmentAssignment;
                    case 1:
                        return fragmentCourse;
                    case 2:
                        return fragmentUser;
                }
                return null;
            }

            @Override
            public int getCount() {
                return 3;
            }
        });
    }


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            //点击BottomNavigationView的Item项，切换ViewPager页面
            //menu/navigation.xml里加的android:orderInCategory属性就是下面item.getOrder()取的值
            switch (item.getItemId()) {
                case R.id.icon_assignment:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.icon_course:
                    viewPager.setCurrentItem(1);
                    break;
                case R.id.icon_user_info:
                    viewPager.setCurrentItem(2);
                    break;

            }
            return true;
        }

    };

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //页面滑动的时候，改变BottomNavigationView的Item高亮
        bottomNavigationView.getMenu().getItem(position).setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }



}
