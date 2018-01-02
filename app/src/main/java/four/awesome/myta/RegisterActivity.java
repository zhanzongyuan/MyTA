package four.awesome.myta;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import four.awesome.myta.models.User;
import four.awesome.myta.services.APIClient;
import io.reactivex.CompletableObserver;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class RegisterActivity extends AppCompatActivity
        implements View.OnClickListener, Observer<User> {

    private EditText editUsername;
    private EditText editEmail;
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
                // TODO Validation check
                String pswHash = LoginActivity.md5Hash(editPsw.getText().toString());
                String type = radioGroupType.getCheckedRadioButtonId() ==
                        R.id.radio_register_student ? "student" : "teacher";
                APIClient client = new APIClient();
                client.subscribeRegister(
                        this,
                        editUsername.getText().toString(),
                        pswHash,
                        editEmail.getText().toString(),
                        type
                );

        }
    }

    private void initVariables() {
        editUsername = (EditText) findViewById(R.id.edit_register_username);
        editEmail = (EditText) findViewById(R.id.edit_register_email);
        editPsw = (EditText) findViewById(R.id.edit_register_password);
        editConfirmPsw = (EditText) findViewById(R.id.edit_register_confirm_password);
        radioGroupType = (RadioGroup) findViewById(R.id.radio_group_register_type);
        buttonRegister = (Button) findViewById(R.id.button_register_submit);
    }

    /**
     * Implement Observer<User>
     */
    @Override
    public void onSubscribe(Disposable d) {

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
}
