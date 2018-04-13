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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import braintech.cg.hubblo.HomeActivity;
import braintech.cg.hubblo.R;

public class EditContactActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private Button OK, cancel;
    private EditText inputCantact;

    private DatabaseReference databaseReference;
    private FirebaseUser mCurrentUser;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        toolbar = (Toolbar) findViewById(R.id.toolbarContact);
        OK = (Button) findViewById(R.id.OkContact);
        cancel = (Button) findViewById(R.id.CancelContact);



        toolbar.setNavigationIcon(R.drawable.ic_backbtn);
        toolbar.setTitle(getString(R.string.profil));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentP = new Intent(EditContactActivity.this, HomeActivity.class);
                startActivity(intentP);
            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent exitInent = new Intent(EditContactActivity.this, ProfilActivity.class);
                startActivity(exitInent);
            }
        });
    }
}
