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

        String username = sharedPreferences.getString("username", null);
        if (username != null) {
            String apiKey = sharedPreferences.getString("api_key", null);
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("api_key", apiKey);
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
            String username = user.getUsername();
            String name = user.getName();
            String campusID = user.getCampusID();
            String phone = user.getPhone();
            String type = user.getType();
            String email = user.getEmail();
            String apiKey = user.getApiKey();
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("username", username);
            editor.putString("name", name);
            editor.putString("campus_id", campusID);
            editor.putString("phone", phone);
            editor.putString("type", type);
            editor.putString("email", email);
            editor.putString("api_key", apiKey);
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
