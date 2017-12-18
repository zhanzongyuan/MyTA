package four.awesome.myta.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import four.awesome.myta.R;

/**
 * Fragment for homework list.
 */
public class AssignmentFragment extends Fragment {
    public static AssignmentFragment newInstance() {
        AssignmentFragment fragment = new AssignmentFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO: Complete page logic here.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_assignment, container, false);
    }

    // TODO: Complete related function here.
}
