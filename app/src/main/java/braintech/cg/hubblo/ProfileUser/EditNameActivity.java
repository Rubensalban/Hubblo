package braintech.cg.hubblo.ProfileUser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import braintech.cg.hubblo.HomeActivity;
import braintech.cg.hubblo.R;

public class EditNameActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button OK, Cancel;
    private EditText inputName;

    private DatabaseReference databaseReference;
    private FirebaseUser mCurrentUser;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_name);

        toolbar = (Toolbar) findViewById(R.id.toolbarName);
        OK = (Button) findViewById(R.id.OkName);
        Cancel = (Button) findViewById(R.id.CancelName);
        inputName = (EditText) findViewById(R.id.inputNameUser);


        String status_value = getIntent().getStringExtra("name_value");
        inputName.setText(status_value);

        toolbar.setNavigationIcon(R.drawable.ic_backbtn);
        toolbar.setTitle(getString(R.string.profil));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentP = new Intent(EditNameActivity.this, HomeActivity.class);
                startActivity(intentP);
            }
        });


        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

        mProgress = new ProgressDialog(EditNameActivity.this);

        OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress.setTitle(getString(R.string.saving));
                mProgress.setMessage(getString(R.string.savingWait));
                mProgress.show();

                String status = inputName.getText().toString();
                databaseReference.child("name").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            mProgress.dismiss();
                        } else {
                            Toast.makeText(getApplicationContext(), getString(R.string.savingError), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}
