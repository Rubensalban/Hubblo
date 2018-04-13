package braintech.cg.hubblo.ProfileUser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import braintech.cg.hubblo.HomeActivity;
import braintech.cg.hubblo.R;

public class ViewImgActivity extends AppCompatActivity {


    private DatabaseReference mUserdatabase;
    private FirebaseUser mCurrentUser;
    private StorageReference mImageStorage;

    private Toolbar toolbar;

    private ProgressDialog mViewprogress;
    private ProgressDialog mProgressStorag;
    private ImageView imageViewProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_img);

        toolbar = (Toolbar) findViewById(R.id.toolbarView);
        imageViewProfile = (ImageView) findViewById(R.id.ViewImgProfile);

        mImageStorage = FirebaseStorage.getInstance().getReference();

        mViewprogress = new ProgressDialog(this);
        mViewprogress.setMessage(getString(R.string.progress));
        mViewprogress.setCanceledOnTouchOutside(false);
        //mViewprogress.show();


        toolbar.setNavigationIcon(R.drawable.ic_backbtn);
        toolbar.setTitle(getString(R.string.profil));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentP = new Intent(ViewImgActivity.this, ProfilActivity.class);
                startActivity(intentP);
            }
        });


        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        String current_uid = mCurrentUser.getUid();

        mUserdatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

        mUserdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String upimage = dataSnapshot.child("image").getValue().toString();
                String upthrum_img = dataSnapshot.child("trumb_image").getValue().toString();

                Picasso.get().load(upimage).into(imageViewProfile);
                mViewprogress.dismiss();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



    }
}
