package everyday.sukhajata.com.everydayenglish;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

import everyday.sukhajata.com.everydayenglish.utility.EverydayLanguageDbHelper;

/**
 * Created by Tim on 3/03/2017.
 */

public class SetupPagerAdapter extends FragmentPagerAdapter {

    private SetupActivity mContext;
    private ArrayList<String> mUserList;
    private int mUserCount;
    private boolean mWrongPassword;

    public SetupPagerAdapter(SetupActivity context, FragmentManager fm,
                             int userCount, boolean wrongPassword) {
        super(fm);
        mContext = context;
        mUserCount = userCount;
        mWrongPassword = wrongPassword;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Fragment getItem(int position) {

            return LoginFragment.newInstance();
    }

}
