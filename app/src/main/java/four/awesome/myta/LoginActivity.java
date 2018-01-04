package four.awesome.myta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import four.awesome.myta.models.User;
import four.awesome.myta.services.APIClient;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity
        implements View.OnClickListener,
        Observer<Response<User>> {

    static public String PREF_NAME = "data";

    private SharedPreferences sharedPreferences;
    private EditText editLoginEmail;
    private EditText editLoginPassword;
    private Button buttonForget;
    private Button buttonLogin;
    private Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initVariables();

        int id = sharedPreferences.getInt("id", -1);
        if (id != -1) {
            String username = sharedPreferences.getString("username", null);
            String name = sharedPreferences.getString("name", null);
            String campusID = sharedPreferences.getString("campus_id", null);
            String phone = sharedPreferences.getString("phone", null);
            String type = sharedPreferences.getString("type", null);
            String email = sharedPreferences.getString("email", null);
            String apiKey = sharedPreferences.getString("api_key", null);
            Set<String> courses = sharedPreferences.getStringSet("courses", null);
            Set<String> assignments = sharedPreferences.getStringSet(
                    "assignments", null);
            User user = new User();
            user.setID(id);
            user.setUsername(username);
            user.setName(name);
            user.setCampusID(campusID);
            user.setPhone(phone);
            user.setType(type);
            user.setEmail(email);
            user.setApiKey(apiKey);
            if (courses == null)
                user.setCourses(null);
            else
                user.setCourses(new ArrayList<>(courses));
            if (assignments == null)
                user.setAssignments(null);
            else
                user.setAssignments(new ArrayList<>(assignments));
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }

        buttonRegister.setOnClickListener(this);
        buttonForget.setOnClickListener(this);
        buttonLogin.setOnClickListener(this);
    }

    /**
     * Implement View.OnClickListener
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_login_forget:
                // TODO Forget password
                break;
            case R.id.button_login_submit:
                String username = editLoginEmail.getText().toString();
                String password = editLoginPassword.getText().toString();

                // For development
                if (username.equals("admin")) {
                    startActivity(new Intent(this, MainActivity.class));
                    return;
                }

                (new APIClient()).subscribeAuthorize(this, username, password);
                break;
            case R.id.button_login_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }

    /**
     * Implement Observer<Response<User>>
     */
    @Override
    public void onSubscribe(Disposable d) {}
    @Override
    public void onNext(Response<User> res) {
        if (res.code() == 200) {
            User user = res.body();
            if (user == null)
                return;
            makeToast(this, "登录成功");
            int userID = user.getID();
            String username = user.getUsername();
            String name = user.getName();
            String campusID = user.getCampusID();
            String phone = user.getPhone();
            String type = user.getType();
            String email = user.getEmail();
            String apiKey = user.getApiKey();
            List<String> courses = user.getCourses();
            List<String> assignments = user.getAssignments();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt("id", userID);
            editor.putString("username", username);
            editor.putString("name", name);
            editor.putString("campus_id", campusID);
            editor.putString("phone", phone);
            editor.putString("type", type);
            editor.putString("email", email);
            editor.putString("api_key", apiKey);
            editor.putStringSet("courses", new HashSet<>(courses));
            editor.putStringSet("assignments", new HashSet<>(assignments));
            editor.apply();

            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        } else if (res.code() == 400) {
            makeToast(this, "密码错误");
        } else {
            makeToast(this, "此邮箱不存在");
        }
    }
    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }
    @Override
    public void onComplete() {}

    /**
     * Initialize variables
     */
    private void initVariables() {
        sharedPreferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        editLoginEmail = (EditText) findViewById(R.id.edit_login_email);
        editLoginPassword = (EditText) findViewById(R.id.edit_login_psw);
        buttonForget = (Button) findViewById(R.id.button_login_forget);
        buttonLogin = (Button) findViewById(R.id.button_login_submit);
        buttonRegister = (Button) findViewById(R.id.button_login_register);
    }

    static public void makeToast(Context context, String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }

}
