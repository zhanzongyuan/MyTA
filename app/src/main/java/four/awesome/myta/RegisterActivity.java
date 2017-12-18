package four.awesome.myta;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

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
    }

    /**
     * Implement View.OnClickListener
     */
    @Override
    public void onClick(View v) {
        // TODO: Register
    }

    private void initVariables() {
        editEmail = (EditText) findViewById(R.id.edit_register_email);
        editPsw = (EditText) findViewById(R.id.edit_register_password);
        editConfirmPsw = (EditText) findViewById(R.id.edit_register_confirm_password);
        radioGroupType = (RadioGroup) findViewById(R.id.radio_group_register_type);
        buttonRegister = (Button) findViewById(R.id.button_register_submit);
    }
}
