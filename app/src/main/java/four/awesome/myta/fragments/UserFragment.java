package four.awesome.myta.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import de.hdodenhof.circleimageview.CircleImageView;
import four.awesome.myta.R;
import four.awesome.myta.models.User;

/**
 * Fragment for user list.
 */
public class UserFragment extends Fragment {
    private User user_data;
    
    private CircleImageView user_img;
    private TextView user_name;
    private TextView user_id;
    private TextView user_email;
    private TextView user_phone;
    //界面组件对象

    private static UserFragment fragment;
    View user_view;
    //用户界面布局对象

    public static UserFragment newInstance() {
        if (fragment == null)
            fragment = new UserFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        user_view = inflater.inflate(R.layout.fragment_user, container, false);
        setUserData();
        setView();
        return user_view;
    }
    
    private void setView() {
        if (user_view == null) {
            System.out.println("user fragment error!");
        }
        user_img = (CircleImageView) user_view.findViewById(R.id.user_image);
        user_name = (TextView) user_view.findViewById(R.id.user_name);
        user_id = (TextView) user_view.findViewById(R.id.user_id);
        user_email = (TextView) user_view.findViewById(R.id.user_email);
        user_phone = (TextView) user_view.findViewById(R.id.user_phone);
        
        user_img.setImageResource(R.mipmap.lenna_round);
        // TODO: 18-1-2 User类没有头像信息，请补充
        user_name.setText(user_data.getUsername());
        user_id.setText(("15xxxxxx"));
        // TODO: 18-1-2 User类没有学号信息，请补充
        user_email.setText(user_data.getEmail());
        user_phone.setText("110");
        // TODO: 18-1-2 User类没有电话信息，请补充
    }
    //设置界面控件对象的有关属性
    private void setUserData() {
        if (user_data == null) {
            user_data = new User(
                    "张涵玮",
                    "", "张涵玮", "17665310114","student", "123@qq.com");
            // TODO: 18-1-2 不知user数据在哪，请在这里设置
        }

    }
}
