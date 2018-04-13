package braintech.cg.hubblo;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

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

import braintech.cg.hubblo.activite.AboutActivity;
import braintech.cg.hubblo.activite.SettingsActivity;
import braintech.cg.hubblo.connection.LoginActivity;
import braintech.cg.hubblo.ProfileUser.ProfilActivity;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    private DatabaseReference mUserdatabase;
    private FirebaseUser mCurrentUser;
    private StorageReference mImageStorage;
    private CircleImageView Nav_ProfileImg;
    private TextView Nav_Name;
    private TextView Nav_Email;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Get Firebase auth instance
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        setDataToView(user);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Nav_ProfileImg = (CircleImageView) findViewById(R.id.DrwImgProfile);
        Nav_Name = (TextView) findViewById(R.id.DrwUserName);
        Nav_Email = (TextView) findViewById(R.id.DrwPhoneorEmail);



        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                Intent intent = new Intent(HomeActivity.this, ProfilActivity.class);
                startActivity(intent);
            }
        });

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        String current_uid = mCurrentUser.getUid();

        mUserdatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

        mUserdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String upname = dataSnapshot.child("name").getValue().toString();
                String upimage = dataSnapshot.child("image").getValue().toString();
                String upthrum_img = dataSnapshot.child("trumb_image").getValue().toString();

                Nav_Name.setText(upname);

                Picasso.get().load(upimage).into(Nav_ProfileImg);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*Modifier l'image de profile
        View headView = navigationView.getHeaderView(0);
        de.hdodenhof.circleimageview.CircleImageView imgProfile = headView.findViewById(R.id.imageProfil);
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        //LostPassword

        /*
        //Code de hashage
        printkeyHash();
        */


    }

    @SuppressLint("SetTextI18n")
    private void setDataToView(FirebaseUser user) {
        Nav_Email.setText("User Email: " + user.getEmail());
    }

    FirebaseAuth.AuthStateListener authStateListener = new FirebaseAuth.AuthStateListener() {
        @Override
        public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null){
                startActivity(new Intent(HomeActivity.this, LoginActivity.class));
                finish();
            }
        }
    };

    protected void onStart() {
        super.onStart();
        /*
        FirebaseUser firebaseUser = mAuth.getCurrentUser();

        if (firebaseUser == null) {
            Intent intent =  new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        */
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            mAuth.removeAuthStateListener(authStateListener);
        }
    }


    /*
    private void printkeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
            "braintech.cg.hubblo", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures)
            {
                MessageDigest messageDigest = MessageDigest.getInstance("SHA");
                messageDigest.update(signature.toByteArray());
                Log.d("KEYHASH", Base64.encodeToString(messageDigest.digest(),Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
    */


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
           return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_account) {
            /* Handle the profile
            //startActivity(new Intent(HomeActivity.this, ProfilActivity.class));
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.flmain, new ProfilFragment());
            ft.commit();
            */
            Intent intent = new Intent(HomeActivity.this, ProfilActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_chat) {

        } else if (id == R.id.nav_home) {

        } else if (id == R.id.nav_rate) {

        } else if (id == R.id.nav_settings) {
            startActivity(new Intent(HomeActivity.this, SettingsActivity.class));

        } else if (id == R.id.nav_feedback) {

        } else if (id == R.id.nav_send) {
                shareit();

        } else if (id == R.id.nav_about) {
                startActivity(new Intent(HomeActivity.this,
                        AboutActivity.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Partager l'app
    private void shareit() {
        Intent shareintent = new Intent(Intent.ACTION_SEND);
        shareintent.setType("text/plain");
        shareintent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
        shareintent.putExtra(Intent.EXTRA_TEXT, getString(R.string.sharedoc));
        startActivity(Intent.createChooser(shareintent, getString(R.string.shareit)));
    }
}
