package braintech.cg.hubblo.ProfileUser;

import android.accounts.Account;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import braintech.cg.hubblo.HomeActivity;
import braintech.cg.hubblo.R;
import braintech.cg.hubblo.connection.LoginActivity;
import braintech.cg.hubblo.connection.SignUpActivity;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

public class ProfilActivity extends AppCompatActivity {

    private DatabaseReference mUserdatabase;
    private FirebaseUser mCurrentUser;
    private StorageReference mImageStorage;
    private FirebaseAuth mAuth;

    private CircleImageView mDisplayImage;
    private TextView userName;
    private TextView userStatus;
    private TextView userContact;
    private Button btnlogout;
    private Toolbar toolbar;
    private ImageButton editName, editContact, editStatus;
    private FloatingActionButton changePicture;


    private ProgressDialog mLoginprogress;
    private ProgressDialog mProgressStorage;
    private static final int GALLERY_PICK= 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        mDisplayImage = (CircleImageView) findViewById(R.id.UserImgProfile);
        userName = (TextView) findViewById(R.id.UserNameProfiles);
        userStatus = (TextView) findViewById(R.id.UserStatusProfile);
        userContact = (TextView) findViewById(R.id.UserStatusEmails);
        btnlogout = (Button) findViewById(R.id.BtnUserLogout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarProfile);
        editName = (ImageButton) findViewById(R.id.EditUserName);
        editContact = (ImageButton) findViewById(R.id.EditUserContact);
        editStatus = (ImageButton) findViewById(R.id.EditUserStatus);
        changePicture = (FloatingActionButton) findViewById(R.id.ChangePicture);


        mAuth = FirebaseAuth.getInstance();

        mImageStorage = FirebaseStorage.getInstance().getReference();

        mLoginprogress = new ProgressDialog(this);
        mLoginprogress.setMessage(getString(R.string.progress));
        mLoginprogress.setCanceledOnTouchOutside(false);

        toolbar.setNavigationIcon(R.drawable.ic_backbtn);
        toolbar.setTitle(getString(R.string.profil));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentP = new Intent(ProfilActivity.this, HomeActivity.class);
                startActivity(intentP);
            }
        });

        mCurrentUser = FirebaseAuth.getInstance().getCurrentUser();

        String current_uid = mCurrentUser.getUid();

        mUserdatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(current_uid);

        mUserdatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String upname = dataSnapshot.child("name").getValue().toString();
                String upimage = dataSnapshot.child("image").getValue().toString();
                String upstatus = dataSnapshot.child("Status").getValue().toString();
                String upthrum_img = dataSnapshot.child("trumb_image").getValue().toString();

                userName.setText(upname);
                userStatus.setText(upstatus);

                Picasso.get().load(upimage).into(mDisplayImage);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //Deconnection
        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // mLoginprogress.show();
               // AccountKit.logOut();
               // finish();

                final AlertDialog.Builder builder = new AlertDialog.Builder(ProfilActivity.this);
                builder.setMessage("Deconnection");
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        builder.show();
                        Intent intent =  new Intent(ProfilActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                      //logout
                        mAuth.signOut();
                        finish();
                        startActivity(intent);
                    }
                });


                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        //Share name
        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name_value = userName.getText().toString();
                Intent name = new Intent(ProfilActivity.this, EditNameActivity.class);
                name.putExtra("name_status", name_value);
                startActivity(name);
            }
        });

        //Editer Contact
        editContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editCIntent = new Intent(ProfilActivity.this, EditContactActivity.class);
                startActivity(editCIntent);
            }
        });

        //Share Description
        editStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String status_value =userStatus.getText().toString();
                Intent status = new Intent(ProfilActivity.this, EditDescriptionActivity.class);
                status.putExtra("status_value",status_value);
                startActivity(status);
            }
        });

        //Afficher Image profile


        mDisplayImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProfilActivity.this, ViewImgActivity.class);
                Pair<View, String> pair1 = Pair.create(findViewById( R.id.UserImgProfile), "myImage");
                ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(ProfilActivity.this, pair1);
                startActivity(i, optionsCompat.toBundle());
            }
        });

        //Picture
        changePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "SELECT IMAGE"), GALLERY_PICK);

                /*
                CropImage.activity()
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(ProfilActivity.this);
                        */
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
            Uri imageUri = data.getData();
            CropImage.activity(imageUri)
                    .setAspectRatio(1,1)
                    .setMinCropWindowSize(200,200)
                    .start(ProfilActivity.this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                mProgressStorage = new ProgressDialog(ProfilActivity.this);
                mProgressStorage.setTitle(getString(R.string.uplode));
                mProgressStorage.setMessage(getString(R.string.uploding));
                mProgressStorage.setCanceledOnTouchOutside(false);
                mProgressStorage.show();

                Uri resultUri = result.getUri();

               // File trumb_filePath = new File(resultUri.getPath());

                String current_user_id = mCurrentUser.getUid();

                /*
                Bitmap trumb_bitmap = new Compressor(this)
                        .setMaxWidth(640)
                        .setMaxHeight(480)
                        .setQuality(75)
                        .setCompressFormat(Bitmap.CompressFormat.WEBP)
                        .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES).getAbsolutePath())
                        .compressToFile(trumb_filePath);
                        */

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
              //  trumb_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] trumb_byte = baos.toByteArray();

                StorageReference filepath = mImageStorage.child("profile_images").child(current_user_id + ".jpg");
                final StorageReference trumb_filepath = mImageStorage.child("profile_images").child("trumbs").child(current_user_id + ".jpg");

                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {

                            final String download_url = task.getResult().getDownloadUrl().toString();

                            UploadTask uploadTask = trumb_filepath.putBytes(trumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> trumb_task) {

                                    String trumb_downloadUrl = trumb_task.getResult().getDownloadUrl().toString();

                                    if (trumb_task.isSuccessful()) {
                                        Map update_hashMap = new HashMap<>();
                                        update_hashMap.put("image", download_url);
                                        update_hashMap.put("trumb_image", trumb_downloadUrl);

                                        mUserdatabase.updateChildren(update_hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    mProgressStorage.dismiss();
                                                    Toast.makeText(ProfilActivity.this, "Success", Toast.LENGTH_SHORT).show();
                                                }

                                            }
                                        });
                                    } else {
                                        Toast.makeText(ProfilActivity.this, "Error in uploading thumbnail", Toast.LENGTH_SHORT).show();
                                        mProgressStorage.dismiss();

                                    }
                                }
                            });


                        } else {
                            mProgressStorage.dismiss();
                        }
                    }
                });

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }

    }

    private static String  random() {
        Random generator = new Random();
        StringBuilder randomStringBuilder = new StringBuilder();
        int randomLength = generator.nextInt(10);
        char tempChar;
        for (int i = 0; i < randomLength; i++) {
            tempChar = (char) (generator.nextInt(96) + 32);
            randomStringBuilder.append(tempChar);
        }
        return randomStringBuilder.toString();
    }
}
