package four.awesome.myta;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Field;
import java.util.List;

import four.awesome.myta.fragments.AssignmentFragment;
import four.awesome.myta.fragments.AssignmentListFragment;
import four.awesome.myta.fragments.CourseFragment;
import four.awesome.myta.fragments.UserFragment;
import four.awesome.myta.message.MyMessage;
import four.awesome.myta.models.Assignment;
import four.awesome.myta.models.Course;
import four.awesome.myta.models.User;
import four.awesome.myta.services.APIClient;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

/**
 * Main activity for navigation between three fragment.
 */
public class MainActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{

    private AssignmentListFragment assignmentListFragment;
    private AssignmentFragment assignmentFragment;
    private CourseFragment fragmentCourse;
    private UserFragment fragmentUser;
    private SharedPreferences data;
    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    private User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadIntent();
        setContentView(R.layout.activity_main);

        initialData();
        EventBus.getDefault().register(this);
        initialNavigationView();
    }

    public SharedPreferences getData() {
        return data;
    }

    public void loadIntent() {
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        if (user == null) user = new User();

        // Change color when different user.
        if (user.getType().equals("teacher")) {
            setTheme(R.style.AppThemeTA);
        }

    }

    public void getAllAsign() {
        (new APIClient()).subscribeGetAssign(new Observer<Response<List<Assignment>>>() {
            @Override
            public void onSubscribe(Disposable d) {}
            @Override
            public void onNext(Response<List<Assignment>> assignResponse) {
                if (assignResponse.code() == 200) {
                    Log.d("success", "获取所有assign成功");

                    assignmentListFragment.addAllAssignment(assignResponse.body(), user.getType());
                    assignmentListFragment.setApiKey(user.getApiKey());
                } else if (assignResponse.code() == 400) {
                    Log.d("error", "获取所有assign网络失败");
                } else {
                    Log.d("error", "获取所有assign其他原因失败");
                }
            }
            @Override
            public void onError(Throwable e) {
                Log.d("error", "获取所有assign其他原因失败");
                e.printStackTrace();
            }
            @Override
            public void onComplete() {}
        }, user.getApiKey(), user.getID());
    }

    private void initialData() {
        data = getSharedPreferences("data", MODE_PRIVATE);
        viewPager = (ViewPager) findViewById(R.id.frame_layout);
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        if (user == null) user = new User();
        // TODO: Put data to three different fragments.
        fragmentCourse = CourseFragment.newInstance(user.getApiKey(), user.getID(), user.getType(), user.getName());
        assignmentListFragment = AssignmentListFragment.newInstance(this);
        getAllAsign();
        assignmentFragment = AssignmentFragment.newInstance();
        fragmentUser = UserFragment.newInstance();
        fragmentUser.setUserData(user);
        SharedPreferences temp_data = getSharedPreferences("data", MODE_PRIVATE);
        fragmentUser.setSharedPerferences(temp_data);
        fragmentUser.setMain(this);
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
                        return fragmentCourse;
                    case 1:
                        return assignmentListFragment;
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
                case R.id.icon_course:
                    viewPager.setCurrentItem(0);
                    break;
                case R.id.icon_assignment:
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
        switch (position) {
            case 0:
                getSupportActionBar().setTitle(R.string.course);
                break;
            case 1:
                getSupportActionBar().setTitle(R.string.homework);
                getAllAsign();
                break;
            case 2:
                getSupportActionBar().setTitle(R.string.user_info);
                break;
        }
    }

    @Override
    public void onPageSelected(int position) {
        //页面滑动的时候，改变BottomNavigationView的Item高亮
        bottomNavigationView.getMenu().getItem(position).setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    // 监听eventBus
    @Subscribe
    public void onEventMainThread(Bundle bundle) {
        Intent intent = new Intent(MainActivity.this, AssignActivity.class);
        bundle.putString("type", user.getType());
        intent.putExtras(bundle);
        startActivity(intent);
    }
    @Subscribe
    public void onEventMainThread(Assignment assignment) {
        Log.d("assign", assignment.getName());
        assignmentListFragment.addAssignment(assignment);
    }
    @Subscribe
    public void onEventMainThread(MyMessage myMessage) {
        Intent intent = this.getIntent();
        (new APIClient()).subscribeGetAssign(new Observer<Response<List<Assignment>>>() {
            @Override
            public void onSubscribe(Disposable d) {}
            @Override
            public void onNext(Response<List<Assignment>> assignResponse) {
                if (assignResponse.code() == 200) {
                    Log.d("success", "获取所有assign成功");
                    assignmentListFragment.addAllAssignment(assignResponse.body(), user.getType());
                } else if (assignResponse.code() == 400) {
                    Log.d("error", "获取所有assign网络失败");
                } else {
                    Log.d("error", "获取所有assign其他原因失败");
                }
            }
            @Override
            public void onError(Throwable e) {
                Log.d("error", "获取所有assign其他原因失败");
                e.printStackTrace();
            }
            @Override
            public void onComplete() {}
        }, user.getApiKey(), user.getID());
    }
    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}
