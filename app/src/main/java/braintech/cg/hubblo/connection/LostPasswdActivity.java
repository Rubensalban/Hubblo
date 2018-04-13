package braintech.cg.hubblo.connection;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Slide;
import android.view.Gravity;

import braintech.cg.hubblo.R;

public class LostPasswdActivity extends AppCompatActivity {

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lost_passwd);

        Slide slide = new Slide(Gravity.LEFT);
        getWindow().setExitTransition(slide);
    }
}
