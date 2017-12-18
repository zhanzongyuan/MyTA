package four.awesome.myta;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import four.awesome.myta.models.User;
import four.awesome.myta.services.APIAccessService;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                (new APIAccessService()).subcribeAuthorize(this, username, password);
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

    }
    @Override
    public void onNext(User user) {
        Toast.makeText(this, user.getToken(), Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }
    @Override
    public void onComplete() {

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
}
