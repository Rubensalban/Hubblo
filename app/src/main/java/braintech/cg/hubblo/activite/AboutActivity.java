package braintech.cg.hubblo.activite;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import braintech.cg.hubblo.HomeActivity;
import braintech.cg.hubblo.R;
import braintech.cg.hubblo.license.PrivacyActivity;
import braintech.cg.hubblo.license.TermsActivity;

public class AboutActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView terms, privacy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAbout);
        TextView termsAbout = (TextView) findViewById(R.id.terms);
        TextView privacyAbout =(TextView) findViewById(R.id.privacy);



        //License
        termsAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AboutActivity.this, TermsActivity.class));
            }
        });
        privacyAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AboutActivity.this, PrivacyActivity.class));
            }
        });

         //Toolbar
        toolbar.setNavigationIcon(R.drawable.ic_backbtn);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AboutActivity.this, HomeActivity.class);
                startActivity(intent);
            }
        });


    }
}
