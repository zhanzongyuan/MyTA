package four.awesome.myta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigInteger;
import java.security.MessageDigest;

import four.awesome.myta.models.User;
import four.awesome.myta.services.APIClient;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class LoginActivity extends AppCompatActivity
        implements View.OnClickListener,
        Observer<User> {
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
                // TODO: Forget password
                break;
            case R.id.button_login_submit:
                String username = editLoginEmail.getText().toString();
                String password = editLoginPassword.getText().toString();
                (new APIClient()).subscribeAuthorize(this, username, password);
                break;
            case R.id.button_login_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }

    /**
     * Implement Observer<User>
     */
    @Override
    public void onSubscribe(Disposable d) {
        Toast.makeText(this, "Logging", Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onNext(User user) {
        Toast.makeText(this, user.getApiKey(), Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }
    @Override
    public void onComplete() {
        // Log in successfully, jump to main activity.
        Toast.makeText(this, "Log in successfully", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivityForResult(intent, 0);
    }

    /**
     * Initialize variables
     */
    private void initVariables() {
        editLoginEmail = (EditText) findViewById(R.id.edit_login_email);
        editLoginPassword = (EditText) findViewById(R.id.edit_login_psw);
        buttonForget = (Button) findViewById(R.id.button_login_forget);
        buttonLogin = (Button) findViewById(R.id.button_login_submit);
        buttonRegister = (Button) findViewById(R.id.button_login_register);
    }

    static public String md5Hash(String data) {
        String result = "";
        try {
            byte[] strBytes = data.getBytes("UTF-8");
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md.digest(strBytes);
            BigInteger bigInt = new BigInteger(1, md5Bytes);
            result = bigInt.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
