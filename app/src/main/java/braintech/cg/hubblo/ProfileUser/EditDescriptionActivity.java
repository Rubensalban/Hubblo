package braintech.cg.hubblo.ProfileUser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

import braintech.cg.hubblo.R;

public class EditDescriptionActivity extends AppCompatActivity {

    private Button btnDescriptpdate;
    private EditText updateDescript;
    private DatabaseReference databaseReference;
    private FirebaseUser mCurrentUser;
    private ProgressDialog mProgress;
    private Button Ok, cancel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_description);

        String status_value = getIntent().getStringExtra("status_value");

        btnDescriptpdate = (Button) findViewById(R.id.descriptionUpdate);
        updateDescript =(EditText) findViewById(R.id.editDescription);
        cancel = (Button) findViewById(R.id.CancelDescript);

        updateDescript.setText(status_value);


        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        String current_uid = mCurrentUser.getUid();

        databaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

        mProgress = new ProgressDialog(EditDescriptionActivity.this);

        btnDescriptpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mProgress.setTitle(getString(R.string.saving));
                mProgress.setMessage(getString(R.string.savingWait));
                mProgress.show();

                String status = updateDescript.getText().toString();
                databaseReference.child("status").setValue(status).addOnCompleteListener(new OnCompleteListener<Void>() {
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

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EditDescriptionActivity.this, ProfilActivity.class);
                startActivity(i);
            }
        });
    }
}
