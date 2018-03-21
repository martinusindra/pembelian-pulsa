package bbw.pembelianpulsa.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import bbw.pembelianpulsa.R;
import bbw.pembelianpulsa.activity.fragment.LoginFragment;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener, LoginFragment.TaskCallbacks {

    private EditText eUsername;
    private EditText ePassword;
    private Button bLogin;
    private ProgressDialog pd;
    private LoginFragment loginFragment;
    private static final String LOGIN_FRAGMENT = "LoginFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("Login User");

        eUsername = findViewById(R.id.eUsername);
        ePassword = findViewById(R.id.ePassword);
        bLogin = findViewById(R.id.bLogin);

        bLogin.setOnClickListener(this);

        loginFragment = (LoginFragment) getFragmentManager().findFragmentByTag(LOGIN_FRAGMENT);
        if (loginFragment == null) {
            loginFragment = new LoginFragment();
            getFragmentManager().beginTransaction().add(loginFragment, LOGIN_FRAGMENT).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        eUsername.setText("");
        ePassword.setText("");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bLogin:
                if(eUsername.getText().toString().equals("") || ePassword.getText().toString().equals("")){
                    Toast.makeText(this, "Please input username / password first", Toast.LENGTH_SHORT).show();
                    return;
                }
                onPreExecute();
                loginFragment.Login(eUsername.getText().toString(), ePassword.getText().toString(), this);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPreExecute() {
        initDialog();
        pd.show();
    }

    @Override
    public void onPostExecute() {
        if (pd.isShowing()) {
            pd.dismiss();
        }

        Intent SettingScreen = new Intent(this, TransactionActivity.class);
        startActivity(SettingScreen);
    }

    @Override
    public void onError() {
        if (pd.isShowing()) {
            pd.dismiss();
        }
    }

    public void initDialog() {
        pd = new ProgressDialog(this);
        pd.setTitle(R.string.app_name);
        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pd.setMessage("Login ...");
        pd.setCancelable(false);
    }
}
