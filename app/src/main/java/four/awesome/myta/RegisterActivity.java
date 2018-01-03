package four.awesome.myta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import four.awesome.myta.models.User;
import four.awesome.myta.services.APIClient;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Response;

import static four.awesome.myta.LoginActivity.PREF_NAME;
import static four.awesome.myta.LoginActivity.makeToast;

public class RegisterActivity extends AppCompatActivity
        implements View.OnClickListener,
        Observer<Response<User>> {

    private EditText editUsername;
    private EditText editEmail;
    private EditText editName;
    private EditText editCampusID;
    private EditText editPhone;
    private EditText editPsw;
    private EditText editConfirmPsw;
    private RadioGroup radioGroupType;
    private Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initVariables();
        buttonRegister.setOnClickListener(this);
    }

    /**
     * Implement View.OnClickListener
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_register_submit:
                if (!checkValidation()) {
                    return;
                }
                String type = radioGroupType.getCheckedRadioButtonId() ==
                        R.id.radio_register_student ? "student" : "teacher";
                APIClient client = new APIClient();
                client.subscribeRegister(
                        this,
                        editUsername.getText().toString(),
                        editPsw.getText().toString(),
                        editName.getText().toString(),
                        editCampusID.getText().toString(),
                        editPhone.getText().toString(),
                        editEmail.getText().toString(),
                        type
                );

        }
    }

    private void initVariables() {
        editUsername = (EditText) findViewById(R.id.edit_register_username);
        editEmail = (EditText) findViewById(R.id.edit_register_email);
        editName = (EditText) findViewById(R.id.edit_register_name);
        editCampusID = (EditText) findViewById(R.id.edit_register_campus_id);
        editPhone = (EditText) findViewById(R.id.edit_register_phone);
        editPsw = (EditText) findViewById(R.id.edit_register_password);
        editConfirmPsw = (EditText) findViewById(R.id.edit_register_confirm_password);
        radioGroupType = (RadioGroup) findViewById(R.id.radio_group_register_type);
        buttonRegister = (Button) findViewById(R.id.button_register_submit);
    }

    private boolean checkValidation() {
        String username = editUsername.getText().toString();
        String password = editPsw.getText().toString();
        String confirmPsw = editConfirmPsw.getText().toString();
        String email = editEmail.getText().toString();
        String phone = editPhone.getText().toString();
        if (username.isEmpty()) {
            makeToast(this, "请输入用户名");
            return false;
        } else if (email.isEmpty()) {
            makeToast(this, "请输入邮箱");
            return false;
        } else if (password.isEmpty()) {
            makeToast(this, "请输入密码");
            return false;
        } else if (confirmPsw.isEmpty()) {
            makeToast(this, "请再次输入密码");
            return false;
        }
        if (!email.matches(
                "^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(.[a-zA-Z0-9-]+)*$")) {
            makeToast(this, "请输入正确的邮箱地址");
            return false;
        }
        if (!phone.matches("^[0-9]{11}$")) {
            makeToast(this, "请输入正确的手机号码");
            return false;
        }
        if (password.length() < 8 ||
            password.matches("[0-9]+") ||
            password.matches("[A-Za-z]+")) {
            makeToast(this, "密码至少为8位，必须是字母和数字的组合");
            return false;
        }
        if (!password.equals(confirmPsw)) {
            makeToast(this, "两次输入的密码不一致");
            return false;
        }
        return true;
    }

    /**
     * Implement Observer<Response<User>>
     */
    @Override
    public void onSubscribe(Disposable d) {

    }
    @Override
    public void onNext(Response<User> res) {
        if (res.code() == 201) {
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
            SharedPreferences data = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = data.edit();
            editor.putString("username", username);
            editor.putString("name", name);
            editor.putString("campus_id", campusID);
            editor.putString("phone", phone);
            editor.putString("type", type);
            editor.putString("email", email);
            editor.putString("api_key", apiKey);
            editor.apply();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        } else {
            makeToast(this, "用户名或邮箱已存在");
        }
    }
    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }
    @Override
    public void onComplete() {}
}
