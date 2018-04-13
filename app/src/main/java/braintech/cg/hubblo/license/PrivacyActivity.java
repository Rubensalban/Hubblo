package braintech.cg.hubblo.license;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import braintech.cg.hubblo.HomeActivity;
import braintech.cg.hubblo.R;
import braintech.cg.hubblo.activite.AboutActivity;

public class PrivacyActivity extends AppCompatActivity {

    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarPrivacy);

        toolbar.setNavigationIcon(R.drawable.ic_backbtn);
        toolbar.setTitle("Privacy");
        toolbar.setTitleTextColor(Integer.parseInt("#fffd"));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PrivacyActivity.this, AboutActivity.class));
            }
        });
    }
}
