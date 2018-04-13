package braintech.cg.hubblo.connection;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

import braintech.cg.hubblo.HomeActivity;
import braintech.cg.hubblo.R;

public class SignUpActivity extends AppCompatActivity {

    private final static int REQUEST_CODE = 999;
    private FirebaseAuth mAuth;
    private ImageView btnPhone;
    private TextInputEditText username, userpassword, usermail;
    private Button btnSignUp;
    private ProgressDialog mRegProgress;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        btnPhone = (ImageView) findViewById(R.id.SignPhone);
        username = (TextInputEditText) findViewById(R.id.UserName);
        usermail = (TextInputEditText) findViewById(R.id.UserEmail);
        userpassword = (TextInputEditText) findViewById(R.id.UserPassword);
        btnSignUp = (Button) findViewById(R.id.UserSignUp);


        mRegProgress = new ProgressDialog(this);


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* String getemail = usermail.getText().toString().trim();
                String getPassword = userpassword.getText().toString().trim();
                callsignup(getemail,getPassword);
                */

               String name = username.getText().toString().trim();
               String email = usermail.getText().toString().trim();
               String password = userpassword.getText().toString().trim();

                if (TextUtils.isEmpty(name)) {
                    Snackbar.make(v, getString(R.string.empty_name), Snackbar.LENGTH_SHORT).show();
                    return;
                }
               if (TextUtils.isEmpty(email)) {
                   Snackbar.make(v, getString(R.string.empty_mail), Snackbar.LENGTH_SHORT).show();
                   return;
               }
               if (TextUtils.isEmpty(password)) {
                   Snackbar.make(v, getString(R.string.empty_password), Snackbar.LENGTH_SHORT).show();
                   return;
               }
               if (password.length() < 6) {
                   Snackbar.make(v, getString(R.string.empty_min), Snackbar.LENGTH_SHORT).show();
                   return;
               }

                    mRegProgress.setTitle(getString(R.string.progress_title));
                    mRegProgress.setMessage(getString(R.string.progress_msg));
                    mRegProgress.setCanceledOnTouchOutside(false);
                    mRegProgress.show();

                    signup_user(name, email, password);

            }
        });

    }


    //Enregistrement
    private void signup_user(final String name, String email, String password) {
        //Creation du compte
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                            FirebaseUser current_user = FirebaseAuth.getInstance().getCurrentUser();
                            String uid =  current_user.getUid();

                            mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

                            HashMap<String, String> userMap = new HashMap<>();
                            userMap.put("name", name);
                            userMap.put("image", "default");
                            userMap.put("Status", "Mon Status");
                            userMap.put("trumb_image", "default");

                            mDatabase.setValue(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        mRegProgress.dismiss();
                                        Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                                       // intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });

                        } else {
                            mRegProgress.hide();
                            Toast.makeText(SignUpActivity.this, getString(R.string.auth_error) + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }



    @Override
    protected void onResume() {
        super.onResume();

    }




    /*
    private void userProfil() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null)
        {
            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                    .setDisplayName(username.getText().toString().trim())
                    .build();

            user.updateProfile(profileChangeRequest)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("TESTING", "User profile update");
                            }
                        }
                    });
        }
    }
    */
}
