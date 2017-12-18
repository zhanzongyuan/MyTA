package four.awesome.myta;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import four.awesome.myta.fragments.AssignmentFragment;
import four.awesome.myta.fragments.CourseFragment;
import four.awesome.myta.fragments.UserFragment;

/**
 * Main activity for navigation between three fragment.
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialNavigationView();
    }

    /**
     * Initial navigation item click event.
     */
    private void initialNavigationView() {
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selectFragment = null;
                // Navigation to differnt fragment by clicking different navigation item.
                switch (item.getItemId()) {
                    case R.id.icon_course:
                        selectFragment = CourseFragment.newInstance();
                        break;
                    case R.id.icon_assignment:
                        selectFragment = AssignmentFragment.newInstance();
                        break;
                    case R.id.icon_user_info:
                        selectFragment = UserFragment.newInstance();
                        break;
                }
                // Change fragment.
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.frame_layout, selectFragment);
                transaction.commit();
                return true;
            }
        });
        // Initial first fragment.
        Fragment selectFragment = UserFragment.newInstance();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frame_layout, selectFragment);
        transaction.commit();
    }
}
