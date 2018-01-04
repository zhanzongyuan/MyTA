package four.awesome.myta.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import de.hdodenhof.circleimageview.CircleImageView;
import four.awesome.myta.MainActivity;
import four.awesome.myta.R;
import four.awesome.myta.models.User;
import four.awesome.myta.services.APIClient;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

/**
 * Fragment for user list.
 */
public class UserFragment extends Fragment implements Observer<Response<User>> {
    private User user_data;
    
    private CircleImageView user_img;
    private TextView user_name;
    private TextView user_id;
    private TextView user_email;
    private TextView user_phone;
    private Button change_user_data;
    private Button course_data;
    //界面组件对象

    private static UserFragment fragment;
    private Context context;
    View user_view;
    //用户界面布局对象

    public static UserFragment newInstance() {
        if (fragment == null) {
            fragment = new UserFragment();
        }
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
        context = user_view.getContext();
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
        change_user_data = (Button) user_view.findViewById(R.id.chang_user_data);
        course_data = (Button) user_view.findViewById(R.id.course_data);

        if (user_data == null) {
            user_data = new User();
            user_data.setCampusID("15xxxxxx");
            user_data.setEmail("None");
            user_data.setPhone("None");
            user_data.setName("None");
        }

        user_img.setImageResource(R.mipmap.lenna_round);
        // TODO: 18-1-3 User信息中没有头像
        user_name.setText(user_data.getUsername());
        user_id.setText(user_data.getCampusID());
        // TODO: 18-1-3 User没有学号信息
        user_email.setText(user_data.getEmail());
        user_phone.setText(user_data.getPhone());

        change_user_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = getLayoutInflater();
                View dialog = inflater.inflate(R.layout.dialog, (ViewGroup) user_view.findViewById(R.id.dialog), false);
                final EditText change_name = (EditText) dialog.findViewById(R.id.change_user_name);
                final EditText change_id = (EditText) dialog.findViewById(R.id.change_id);
                final EditText change_phone = (EditText) dialog.findViewById(R.id.change_phone);
                final EditText change_password = (EditText) dialog.findViewById(R.id.change_password);
                final EditText confirm_password = (EditText) dialog.findViewById(R.id.confirm_password);

                change_name.setText(user_data.getName());
                change_id.setText(user_data.getCampusID());
                change_phone.setText(user_data.getPhone());
                change_password.setText(user_data.getPassword());
                change_password.setText(user_data.getPassword());

                new AlertDialog.Builder(context).setTitle("修改个人信息")
                        .setView(dialog)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String name = change_name.getText().toString();
                                String id =  change_id.getText().toString();
                                String phone = change_phone.getText().toString();
                                String password = change_password.getText().toString();
                                String c_password = confirm_password.getText().toString();
                                    // TODO: 18-1-3 补充更多的合法性检测
                                if (name.isEmpty()) {
                                    Toast.makeText(context, "姓名不能为空", Toast.LENGTH_LONG).show();
                                } else if (id.isEmpty()) {
                                    Toast.makeText(context, "学号不能为空", Toast.LENGTH_LONG).show();
                                } else if (!password.isEmpty() && (password.length() < 8 ||
                                        password.matches("[0-9]+") ||
                                        password.matches("[A-Za-z]+"))) {
                                    Toast.makeText(context, "密码至少为8位，必须是字母和数字的组合", Toast.LENGTH_LONG).show();
                                } else if (!password.equals(c_password)) {
                                    Toast.makeText(context, "两次输入的密码不一致", Toast.LENGTH_LONG).show();
                                } else {
                                    commitChange(name, id, phone, password);
                                }
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
            }
        });
    }
    //设置界面控件对象的有关属性
    public void setUserData(User user) {
        user_data = user;
    }

    private void commitChange(String name, String id, String phone, String password) {
        if (password.isEmpty())
            password = user_data.getPassword();
        (new APIClient()).subscribeUpdateUser(
                this, user_data.getApiKey(), user_data.getID(),
                user_data.getUsername(), password, name, id, phone, user_data.getEmail(),
                user_data.getType()
        );
    }

    /**
     * Implement Observer<Response<User>>
     */
    @Override
    public void onSubscribe(Disposable d) {}
    @Override
    public void onNext(Response<User> res) {
        if (res.code() == 201) {
            User user = res.body();
            if (user == null) {
                return;
            }
            user_name.setText(user.getUsername());
            user_id.setText(user.getCampusID());
            user_phone.setText(user.getPhone());
        }
    }
    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }
    @Override
    public void onComplete() {}
}
