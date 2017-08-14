package everyday.sukhajata.com.everydayenglish;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

import everyday.sukhajata.com.everydayenglish.interfaces.AudioFinishedCallback;
import everyday.sukhajata.com.everydayenglish.interfaces.AudioSetupCallback;
import everyday.sukhajata.com.everydayenglish.interfaces.DownloadCallback;
import everyday.sukhajata.com.everydayenglish.interfaces.NextLessonListener;
import everyday.sukhajata.com.everydayenglish.model.Lesson;
import everyday.sukhajata.com.everydayenglish.model.LessonCompleted;
import everyday.sukhajata.com.everydayenglish.model.User;
import everyday.sukhajata.com.everydayenglish.utility.ContentManager;
import everyday.sukhajata.com.everydayenglish.utility.EverydayLanguageDbHelper;

public class    MainActivity extends AppCompatActivity implements DownloadCallback,
        AudioSetupCallback, AudioFinishedCallback, NextLessonListener, LessonFragment.OnLessonFragmentInteractionListener,
        LoginFragment.LoginFragmentInteractionListener, TotalsFragment.OnTotalsFragmentInteractionListener, ClassListFragment.ClassListFragmentInteractionListener{

    public String imageUrl;
    public static final int USER_REQUEST_CODE = 1;
    public static final int LESSON_REQUEST_CODE = 2;
    public static final int TOTALS_REQUEST_CODE = 3;
    //private Lesson mLesson;

    //private User mCurrentUser;
    //private boolean mWaitingForDownload;
    private boolean mWaitingForLessons;
    private boolean mWaitingForSlides;
    private boolean mWaitingForLessonsCompleted;
    private boolean mAudioSetupComplete;
    private String licenseKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAk6cvdOFTd92nAa9flm2PxMzKgF0swiYw2qnVPe6CCyr7O+QoB6yTb16Sv7grQwwK46/zRdoFt+Ot0YqoSXaaJUpEMGJxHemN";
    private String licenseKey2 = "+g0WO/DUuWopdpoGVLOGTctZbwaYFKRphFUSGXIRLVjP/74jpqhTqgRsTqoot9Ta1E1gculZtUgFjRII3IJO+OIeNea9UdiLKf2ZJBYod7qpyPumCAy0HFIcylsO7PR8v8VlqAOpDyvqvJL+/qD3TATVmwzZM66xm/Q/5LCujKuS5JhZIcOWmVaWs05t2mYDspOSH5+fme2A9ak+zm3NEyRuoNuFCgUZuXK4w6ARFbZMT3c7nPCR7wIDAQAB";
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(myToolbar);

        showProgressDialog();
        mAudioSetupComplete = false;
        ContentManager.setupAudio(getApplicationContext(), this, this);

        //calculate the size of images for this device and set url for image download
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        imageUrl = ContentManager.getImageUrl(metrics);

        //get the last user that logged in. If there are no users launch setup
        EverydayLanguageDbHelper dbHelper = EverydayLanguageDbHelper.getInstance(getApplicationContext());
        User user = dbHelper.getActiveUser();
        if (user != null) {
           syncUser(user);
        } else  {
            //select user or create new
            LoginFragment loginFragment = LoginFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_content, loginFragment)
                    .commitAllowingStateLoss();

            //Log.d("SUKH", "Starting setup");
            //Intent intent = new Intent(this, SetupActivity.class);
            //intent.putExtra("UserCount", numUsers);
            //startActivityForResult(intent, USER_REQUEST_CODE);

        }


    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }
    }

    private void hideProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private void syncUser(User user) {

        boolean nextLessonCached = EverydayLanguageDbHelper
                .getInstance(this)
                .checkForLesson(user.LessonCompletedOrder + 1, user.ModuleId);

        if (!nextLessonCached) {
            showProgressDialog();
            int lastLessonId = EverydayLanguageDbHelper
                    .getInstance(this)
                    .getLastLessonId();

            mWaitingForLessons = true;
            mWaitingForSlides = true;
            ContentManager.downloadLessons(this, lastLessonId, this);
            ContentManager.downloadSlides(this, lastLessonId, this);
        }

        LessonFragment lessonFragment = LessonFragment.newInstance(user.Id, user.ModuleId);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_content, lessonFragment)
                .commitAllowingStateLoss();

        /*
        boolean upToDate = EverydayLanguageDbHelper
                .getInstance(this)
                .lessonsCompletedUpToDate(user);
        if (!upToDate) {
            showProgressDialog();
            mWaitingForLessonsCompleted = true;
            ContentManager.pullLessonsCompleted(this, user.Id, user.ModuleId, 0, this);
        } else {
            LessonFragment lessonFragment = LessonFragment.newInstance(user.Id, user.ModuleId);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_content, lessonFragment)
                    .commitAllowingStateLoss();
        }
        */

    }

    @Override
    public void onPause(){
        super.onPause();
        ContentManager.shutDownAudio();
    }

    private void launchLesson(Lesson lesson) {
        Log.d("SUKH", "launching lesson " + lesson.Id);

        //fetch images
        ArrayList<String> imageNames = EverydayLanguageDbHelper
                .getInstance(this)
                .getLessonImageNames(lesson.Id);

        ContentManager.cacheImages(this, imageNames);

        User user = EverydayLanguageDbHelper
                .getInstance(this)
                .getActiveUser();

        if (user != null) {
            Intent intent = new Intent(this, LessonActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable(LessonActivity.ARG_NAME_LESSON, lesson);
            //bundle.putString(LessonActivity.ARG_NAME_LESSON, lesson);
            bundle.putString(LessonActivity.ARG_NAME_IMAGE_URL, imageUrl);
            bundle.putInt(LessonActivity.ARG_NAME_USER_ID, user.Id);
            bundle.putInt(LessonActivity.ARG_NAME_MODULE_ID, user.ModuleId);
            intent.putExtra("bundle", bundle);
            startActivityForResult(intent, LESSON_REQUEST_CODE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        User user = EverydayLanguageDbHelper
                .getInstance(this)
                .getActiveUser();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_refresh) {
            showProgressDialog();
            mWaitingForLessonsCompleted = true;
            ContentManager.pullLessonsCompleted(this, user.Id, user.ModuleId, 0, this);

        }else if (id == R.id.action_class) {

            if (user != null) {
                showProgressDialog();
                ContentManager.getClass(this, user.ClassId, this);
            }

            return true;
        } else if (id == R.id.action_switchUser) {
            LoginFragment loginFragment = LoginFragment.newInstance();

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_content, loginFragment)
                    .commitAllowingStateLoss();
        } else if (id == R.id.action_home) {

            if (user != null) {
                LessonFragment lessonFragment = LessonFragment.newInstance(user.Id, user.ModuleId);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_content, lessonFragment)
                        .commitAllowingStateLoss();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDownloadError(String msg) {

        //Toast.makeText(this, msg, Toast.LENGTH_LONG);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(msg)
                .setTitle(R.string.error)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        // 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
        dialog.show();
        Log.e("SUKH", "Download error: " + msg);
    }

    @Override
    public void onDownloadFinished(String code, String type) {
        if (code.equals(DownloadCallback.RESPONSE_OK)) {
            if (type.equals(DownloadCallback.TYPE_LESSONS)) {
                mWaitingForLessons = false;

                if (!mWaitingForSlides && !mWaitingForLessonsCompleted) {
                    hideProgressDialog();

                    User user = EverydayLanguageDbHelper
                            .getInstance(this)
                            .getActiveUser();

                    if (user != null) {
                        LessonFragment lessonFragment = LessonFragment.newInstance(user.Id, user.ModuleId);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_content, lessonFragment)
                                .commitAllowingStateLoss();
                    }
                }
            } else if (type.equals(DownloadCallback.TYPE_PULL_LESSONS_COMPLETED)) {
                mWaitingForLessonsCompleted = false;

                if (!mWaitingForLessons && !mWaitingForSlides) {
                    hideProgressDialog();

                    User user = EverydayLanguageDbHelper
                            .getInstance(this)
                            .getActiveUser();

                    LessonFragment lessonFragment = LessonFragment.newInstance(user.Id, user.ModuleId);
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.main_content, lessonFragment)
                            .commitAllowingStateLoss();
                }
            } else if (type.equals(DownloadCallback.TYPE_SLIDES)) {
                mWaitingForSlides = false;

                if (!mWaitingForLessons && !mWaitingForLessonsCompleted) {
                    hideProgressDialog();

                    User user = EverydayLanguageDbHelper
                            .getInstance(this)
                            .getActiveUser();

                    if (user != null) {
                        LessonFragment lessonFragment = LessonFragment.newInstance(user.Id, user.ModuleId);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_content, lessonFragment)
                                .commitAllowingStateLoss();
                    }
                }
            }
        }
        /*
        Log.d("SUKH", "Download finished: " + code);
        if (mWaitingForLessons || mWaitingForSlides) {
            if(code.equals(DownloadCallback.TYPE_SLIDES)) {
                mWaitingForSlides = false;
            } else if (code.equals(DownloadCallback.TYPE_LESSONS)) {
                mWaitingForLessons = false;
            }

            if (!mWaitingForLessons && !mWaitingForSlides) {
                mLesson =
                        EverydayLanguageDbHelper
                                .getInstance(this.getApplicationContext())
                                .getLesson(mCurrentUser.Id, mCurrentUser.CurrentLessonId);
                if (mLesson != null && mLesson.Pages.size() > 0) {
                    if (launch) {
                        launchLesson(mLesson);
                    }
                } else {
                    onDownloadError("Next lesson null after download or no slides");
                }
            }
        }

*/
    }



    public void onDownloadResult(String code, String type, Object result) {
        if (code == DownloadCallback.RESPONSE_OK) {
            if (type == DownloadCallback.TYPE_CLASS) {
                hideProgressDialog();

                ArrayList<User> users = (ArrayList<User>)result;
                ClassListFragment classListFragment = ClassListFragment.newInstance(users);

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_content, classListFragment)
                        .commitAllowingStateLoss();
            }
        }
    }

    public void onDownloadResult(String code, String type, String[] args) {

            if (code.equals(DownloadCallback.RESPONSE_OK)) {
                if (type.equals(DownloadCallback.TYPE_PUSH_LESSON_COMPLETED)) {
                    try {
                        /*
                        int userId = Integer.valueOf(args[0]);
                        int currentLesson = Integer.valueOf(args[1]);
                        boolean launch = Boolean.valueOf(args[2]);
                        if (userId != mCurrentUser.Id) {
                            onDownloadError("User mismatch");
                        }

                        mCurrentUser.CurrentLessonId = currentLesson;
                        EverydayLanguageDbHelper
                                .getInstance(this)
                                .updateLessonCompleted(mCurrentUser.Id,
                                        mCurrentUser.CurrentLessonId);


                        startLessonSync(launch);
                        */
                    } catch (NumberFormatException ex) {
                        onDownloadError(args[0]);
                    }
                } else if (type.equals(DownloadCallback.TYPE_TOTALS)) {
                    int wordTotal = Integer.valueOf(args[0]);
                    double percentage = Double.valueOf(args[1]);
                    int lessonOrder = Integer.valueOf(args[2]);

                    hideProgressDialog();

                    User user = EverydayLanguageDbHelper
                            .getInstance(this)
                            .getActiveUser();

                    if (user != null) {
                        LessonCompleted lessonCompleted = EverydayLanguageDbHelper
                                .getInstance(this)
                                .getLessonCompleted(user.Id, user.ModuleId, lessonOrder);

                        TotalsFragment totalsFragment = TotalsFragment.newInstance(
                                lessonCompleted.Correct,
                                lessonCompleted.Errors,
                                wordTotal,
                                percentage);
                        getSupportFragmentManager()
                                .beginTransaction()
                                .replace(R.id.main_content, totalsFragment)
                                .commitAllowingStateLoss();
                    }
                } else if (type.equals(DownloadCallback.TYPE_USER)) {
                    try {

                        int id = Integer.valueOf(args[0]);
                        if (id == -1) {
                            //wrong password
                            int numUsers = EverydayLanguageDbHelper
                                    .getInstance(this)
                                    .getUserCount();
                            Intent intent = new Intent(this, SetupActivity.class);
                            intent.putExtra(SetupActivity.ARG_USER_COUNT, numUsers);
                            intent.putExtra(SetupActivity.ARG_WRONG_PASSWORD, true);
                            startActivityForResult(intent, USER_REQUEST_CODE);
                        } else {
                            /*
                            Log.d("SUKH", "user id: " + args[0]);
                            int currentLessonId = Integer.valueOf(args[1]);
                            String email = args[2];
                            String password = args[3];
                            mCurrentUser = new User(id, email, currentLessonId);
                            EverydayLanguageDbHelper
                                    .getInstance(getApplicationContext())
                                    .checkInsertUser(id, email, password, currentLessonId);
                            startLessonSync(true);
                            */
                            //ContentManager.syncUser(this, this, mCurrentUser.Id);

                            /*
                            mWaitingForLessons = true;
                            mWaitingForSlides = true;
                            int lastOrder = EverydayLanguageDbHelper
                                    .getInstance(this)
                                    .getLastLessonOrder(mCurrentUser.Id);

                            ContentManager.syncData(this, this, imageUrl, id, lastOrder);
                            */
                        }

                    } catch (NumberFormatException ex) {
                        //Toast.makeText(this, "Error: " + args[0], Toast.LENGTH_LONG);
                        onDownloadError(ex.getMessage());
                        Log.e("SUKH", "Download error: " + args[0]);
                    }

                }

            } else {

            }

    }

    public void onAudioFinished(String key) {

    }



    public void onAudioSetupComplete(int code) {
        if (code == AUDIO_SETUP_MISSING_LANGUAGE) {
            Intent installTTSIntent = new Intent();
            installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
            ArrayList<String> languages = new ArrayList<String>();
            languages.add("en-US");
            installTTSIntent.putStringArrayListExtra(
                    TextToSpeech.Engine.EXTRA_CHECK_VOICE_DATA_FOR, languages);
            startActivityForResult(installTTSIntent, LessonActivity.CHECK_DATA_CODE);

        } else {
            mAudioSetupComplete = true;
            if (!mWaitingForLessons && !mWaitingForSlides && !mWaitingForLessonsCompleted) {
                hideProgressDialog();
            }
        }
    }

    public void onLoginFragmentInteraction(User user) {

        if (user != null) {
           syncUser(user);
        }
    }

    private void startLessonSync () {
    /*c(boolean launch) {

        mLesson = EverydayLanguageDbHelper
                .getInstance(this)
                .getLesson(mCurrentUser.Id, mCurrentUser.CurrentLessonId);

        if (mLesson != null) {
            mWaitingForLessons = false;
            if ( mLesson.Pages.size() > 0) {
                mWaitingForSlides = false;
                if (launch) {
                    launchLesson(mLesson);
                }
            } else {
                mWaitingForSlides = true;
            }
        } else {
            Log.d("SUKH", "Waiting for data...");
            mWaitingForLessons = true;
            mWaitingForSlides = true;
        }

        /*
        int lastOrder = EverydayLanguageDbHelper
                .getInstance(this)
                .getLastLessonOrder(mCurrentUser.Id, mCurrentUser.CurrentLessonId);


        ContentManager.syncData(getApplicationContext(), this, imageUrl,
                mCurrentUser.Id, launch);
    */
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if (requestCode == USER_REQUEST_CODE) {


       } else if (requestCode == TOTALS_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                //startLessonSync(true);
                //ContentManager.syncUser(this, this, mCurrentUser.Id, 0, false);

                /*
                mLesson = EverydayLanguageDbHelper
                        .getInstance(getApplicationContext())
                        .getNextLesson(mCurrentUser.Id);
                if (mLesson != null) {
                    launchLesson(mLesson);
                } else {
                    mWaitingForLessons = true;
                    mWaitingForSlides = true;
                    int lastOrder = EverydayLanguageDbHelper
                            .getInstance(this)
                            .getLastLessonOrder(mCurrentUser.Id);

                    ContentManager.syncData(this, this, imageUrl,
                            mCurrentUser.Id, lastOrder);
                }
                */
            }
       } else if (requestCode == LESSON_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                int finishType = data.getIntExtra(LessonActivity.ARG_NAME_FINISH_TYPE,
                        LessonActivity.FINISH_TYPE_LESSON_COMPLETED);
                Intent intent = null;

                switch (finishType) {
                    case LessonActivity.FINISH_TYPE_LESSON_COMPLETED:
                        User user = EverydayLanguageDbHelper
                                .getInstance(this)
                                .getActiveUser();

                        if (user != null) {
                            Lesson lesson = data.getParcelableExtra(LessonActivity.ARG_NAME_LESSON);

                            LessonCompleted lessonCompleted = EverydayLanguageDbHelper
                                    .getInstance(this)
                                    .getLessonCompleted(user.Id, lesson.ModuleId, lesson.LessonOrder);
                            ContentManager.pushLessonCompleted(this, user.Id, lesson.ModuleId, lesson.LessonOrder,
                                    lessonCompleted.Correct, lessonCompleted.Errors, lessonCompleted.DateCompleted, this);

                            if (lesson.ReviewStartOrder > 0) {
                                showProgressDialog();
                                ContentManager.downloadTotals(this, lesson.LessonOrder, lesson.ModuleId, this);
                            } else {
                                TotalsFragment totalsFragment = TotalsFragment.newInstance(
                                        lessonCompleted.Correct,
                                        lessonCompleted.Errors,
                                        0,
                                        0);
                                getSupportFragmentManager()
                                        .beginTransaction()
                                        .replace(R.id.main_content, totalsFragment)
                                        .commitAllowingStateLoss();
                            }
                            //ContentManager.syncUser(this, user.Id, this);

                           // intent = new Intent(this, TotalsActivity.class);
                            //intent.putExtra(ARG_NAME_TOTAL, mLesson.ReviewStartOrder);
                            //startActivityForResult(intent, TOTALS_REQUEST_CODE);
                        }
                        break;

                    case LessonActivity.FINISH_TYPE_SWITCH_USER:
                       // FacebookSdk.sdkInitialize(getApplicationContext());
                        //LoginManager.getInstance().logOut();

                        int numUsers = EverydayLanguageDbHelper.getInstance(getApplicationContext())
                                .getUserCount();
                        intent = new Intent(this, SetupActivity.class);
                        intent.putExtra(SetupActivity.ARG_USER_COUNT, numUsers);
                        startActivityForResult(intent, USER_REQUEST_CODE);
                        break;

                    case LessonActivity.FINISH_TYPE_START_OVER:
                       // ContentManager.syncUser(this, this, mCurrentUser.Id, 0, true, true);

                        /*
                        mLesson = EverydayLanguageDbHelper
                                .getInstance(getApplicationContext())
                                .getNextLesson(mCurrentUser.Id);
                        if (mLesson != null) {
                            launchLesson(mLesson);
                        } else {
                            mWaitingForLessons = true;
                            mWaitingForSlides = true;
                            int lastOrder = EverydayLanguageDbHelper
                                    .getInstance(this)
                                    .getLastLessonOrder(mCurrentUser.Id);
                            ContentManager.syncData(this, this, imageUrl,
                                    mCurrentUser.Id, lastOrder);
                        }
                        */
                        break;

                    case LessonActivity.FINISH_TYPE_REFRESH_LESSONS:
                        EverydayLanguageDbHelper
                                .getInstance(getApplicationContext())
                                .clearLessons();
                        //ContentManager.syncUser(this, this, mCurrentUser.Id, 0, true, true);

                        /*
                        mWaitingForLessons = true;
                        mWaitingForSlides = true;
                        int lastOrder = EverydayLanguageDbHelper
                                .getInstance(this)
                                .getLastLessonOrder(mCurrentUser.Id);
                        ContentManager.syncData(this, this, imageUrl,
                                mCurrentUser.Id, lastOrder);
                                */
                        break;
                    case LessonActivity.FINISH_TYPE_CANCEL:
                        User user2 = EverydayLanguageDbHelper
                                .getInstance(this)
                                .getActiveUser();

                        if (user2 != null) {
                            LessonFragment lessonFragment = LessonFragment.newInstance(user2.Id, user2.ModuleId);
                            getSupportFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.main_content, lessonFragment)
                                    .commitAllowingStateLoss();
                        }

                        break;
                }

            }
        }

    }

    public void onNextLessonClick() {
        //startLessonSync(true);
        /*
        mLesson = EverydayLanguageDbHelper
                .getInstance(getApplicationContext())
                .getNextLesson(mCurrentUser.Id);
        if (mLesson != null) {
            launchLesson(mLesson);
        } else {
            mWaitingForLessons = true;
            mWaitingForSlides = true;
            int lastOrder = EverydayLanguageDbHelper
                    .getInstance(this)
                    .getLastLessonOrder(mCurrentUser.Id);

            ContentManager.syncData(this, this, imageUrl,
                    mCurrentUser.Id, lastOrder);
        }
        */
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //ContentManager.releaseResources();
    }

    @Override
    public void onListFragmentInteraction(Lesson item) {

        Lesson lesson = EverydayLanguageDbHelper
                .getInstance(this)
                .getLesson(item.Id);

        launchLesson(lesson);
    }

    public void onFragmentInteraction() {
        User user = EverydayLanguageDbHelper
                .getInstance(this)
                .getActiveUser();

        if (user != null) {
            LessonFragment lessonFragment = LessonFragment.newInstance(user.Id, user.ModuleId);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_content, lessonFragment)
                    .commitAllowingStateLoss();
        }
    }

    public void onClassListFragmentInteraction(){
        User user = EverydayLanguageDbHelper
                .getInstance(this)
                .getActiveUser();

        if (user != null) {
            LessonFragment lessonFragment = LessonFragment.newInstance(user.Id, user.ModuleId);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_content, lessonFragment)
                    .commitAllowingStateLoss();
        }
    }

}
