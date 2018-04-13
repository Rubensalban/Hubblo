package braintech.cg.hubblo.connection;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import braintech.cg.hubblo.HomeActivity;
import braintech.cg.hubblo.R;

public class LoginActivity extends AppCompatActivity {

    private final static int REQUEST_CODE = 999;
    private static final int SIGN_IN_CODE = 777;

    private FirebaseAuth mAuth;
    private Button btnlogin;
    private ImageView btnPhone;
    private TextView create, btnRestPwd;
    private TextInputEditText inputmail;
    private TextInputEditText  inputpassword;
    private ProgressDialog mLoginprogress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        //tous les appel
        btnPhone = (ImageView) findViewById(R.id.LoginPhone);
        btnlogin = (Button) findViewById(R.id.loginUser);
        create = (TextView) findViewById(R.id.createAccount);
        btnRestPwd = (TextView) findViewById(R.id.lostpassword);
        inputmail = (TextInputEditText) findViewById(R.id.LoginUserEmail);
        inputpassword = (TextInputEditText) findViewById(R.id.LoginUserPassword);

        mLoginprogress = new ProgressDialog(this);


        //Creation du compte
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
            }
        });

        //Connection Email
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
               // startActivity(intent);

                String email = inputmail.getText().toString();
                String password = inputpassword.getText().toString();
                if (TextUtils.isEmpty(email)) {
                    Snackbar.make(v, getString(R.string.empty_mail), Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Snackbar.make(v, getString(R.string.empty_password), Snackbar.LENGTH_SHORT).show();
                    return;
                }

                if (!TextUtils.isEmpty(email) || !TextUtils.isEmpty(password)) {
                    mLoginprogress.setTitle(getString(R.string.progress_titleIn));
                    mLoginprogress.setMessage(getString(R.string.progress_msgIn));
                    mLoginprogress.setCanceledOnTouchOutside(false);
                    mLoginprogress.show();

                    mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                mLoginprogress.dismiss();
                                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                mLoginprogress.hide();
                                Toast.makeText(LoginActivity.this, getString(R.string.auth_errorIn), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    loginUser(email, password);
                }


            }
        });

        //Connection Phone
        btnPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //Password oublier
        btnRestPwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, LostPasswdActivity.class));
            }
        });

    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mLoginprogress.dismiss();
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                   // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else {
                    mLoginprogress.hide();
                    Toast.makeText(LoginActivity.this, getString(R.string.auth_errorIn), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
