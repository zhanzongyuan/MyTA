package four.awesome.myta;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import four.awesome.myta.models.User;
import four.awesome.myta.services.APIAccessService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private TextInputLayout editLoginEmail;
    private TextInputLayout editLoginPassword;
    private Button buttonForget;
    private Button buttonLogin;
    private Button buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initVariables();
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
                String username = editLoginEmail.getEditText().getText().toString();
                String password = editLoginPassword.getEditText().getText().toString();
                Call<User> call = new APIAccessService().authorize(username, password);
                call.enqueue(new Callback<User>() {
                    @Override
                    public void onResponse(@NonNull Call<User> call,
                                           @NonNull Response<User> response) {
                        // TODO: Jump to main activity
                        Toast.makeText(LoginActivity.this, response.body().getToken(), Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onFailure(@NonNull Call<User> call, @NonNull Throwable t) {
                        t.printStackTrace();
                    }
                });
                break;
            case R.id.button_login_register:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
        }
    }

    /**
     * Initialize variables
     */
    private void initVariables() {
        editLoginEmail = (TextInputLayout) findViewById(R.id.edit_login_email);
        editLoginPassword = (TextInputLayout) findViewById(R.id.edit_login_psw);
        buttonForget = (Button) findViewById(R.id.button_login_forget);
        buttonLogin = (Button) findViewById(R.id.button_login_submit);
        buttonRegister = (Button) findViewById(R.id.button_login_register);
    }
}
