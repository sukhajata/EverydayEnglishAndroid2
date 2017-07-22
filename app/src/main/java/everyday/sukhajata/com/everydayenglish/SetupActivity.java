package everyday.sukhajata.com.everydayenglish;

import android.content.Intent;
import android.hardware.camera2.params.Face;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.facebook.FacebookSdk;

import java.util.ArrayList;

import everyday.sukhajata.com.everydayenglish.utility.EverydayLanguageDbHelper;

public class SetupActivity extends FragmentActivity {

    public static final String ARG_USER_COUNT = "UserCount";
    public static final String ARG_WRONG_PASSWORD = "WrongPassword";
    public static final String ARG_EMAIL = "Email";
    public static final String ARG_PASSWORD = "Password";

    private NonSwipeableViewPager mViewPager;
    private SetupPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        Bundle bundle = getIntent().getExtras();
        int numUsers = bundle.getInt(ARG_USER_COUNT);
        boolean wrongPassword = false;
        if (bundle.containsKey(ARG_WRONG_PASSWORD)) {
            wrongPassword = true;
        }

        mViewPager = (NonSwipeableViewPager) findViewById(R.id.setup_pager);
        mPagerAdapter = new SetupPagerAdapter(this,
                getSupportFragmentManager(), numUsers, wrongPassword);
        mViewPager.setAdapter(mPagerAdapter);


        /*
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
    }

}
