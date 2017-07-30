package everyday.sukhajata.com.everydayenglish;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;

import java.util.ArrayList;

import everyday.sukhajata.com.everydayenglish.interfaces.AudioFinishedCallback;
import everyday.sukhajata.com.everydayenglish.interfaces.AudioSetupCallback;
import everyday.sukhajata.com.everydayenglish.interfaces.SlideCompletedListener;
import everyday.sukhajata.com.everydayenglish.model.Lesson;
import everyday.sukhajata.com.everydayenglish.model.Slide;
import everyday.sukhajata.com.everydayenglish.utility.EverydayLanguageDbHelper;

public class LessonActivity extends AppCompatActivity
        implements SlideCompletedListener, AudioFinishedCallback, AudioSetupCallback {

    public static final String ARG_NAME_LESSON = "Lesson";
    public static final String ARG_NAME_IMAGE_URL = "ImageUrl";
    public static final String ARG_NAME_SLIDE = "Slide";
    public static final String ARG_NAME_INSTRUCTIONS = "Instructions";
    public static final String ARG_NAME_USER_ID = "UserID";
    public static final String ARG_NAME_MODULE_ID = "ModuleId";
    public static final String ARG_NAME_FINISH_TYPE = "FinishType";

    public static final int FINISH_TYPE_LESSON_COMPLETED = 1;
    public static final int FINISH_TYPE_SWITCH_USER = 2;
    public static final int FINISH_TYPE_REFRESH_LESSONS = 3;
    public static final int FINISH_TYPE_START_OVER = 4;
    public static final int CHECK_DATA_CODE = 10;


    private int numPages;
    private NonSwipeableViewPager mPager;
    private LessonPagerAdapter mPagerAdapter;
    private int mUserId;
    private Lesson mLesson;
    private String mImageUrl;
    private int mCurrentSlide;
    private int mModuleId;
    private Fragment mCurrentFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lesson2);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.lesson_toolbar);
        //setSupportActionBar(toolbar);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        Bundle bundle = getIntent().getBundleExtra("bundle");
        //bundle.setClassLoader(Lesson.class.getClassLoader());
        mLesson = bundle.getParcelable(ARG_NAME_LESSON);
        mImageUrl = bundle.getString(ARG_NAME_IMAGE_URL);
        mUserId = bundle.getInt(ARG_NAME_USER_ID);
        mModuleId = bundle.getInt(ARG_NAME_MODULE_ID);

        //check if user has already started this lesson
        mCurrentSlide = EverydayLanguageDbHelper
                .getInstance(this)
                .getSlideCompleted(mUserId, mLesson.Id) - 1;

        if (mCurrentSlide >= mLesson.Pages.size()) {
            mCurrentSlide = 0;
        }

        //show stars
        for (int i = 0; i < mCurrentSlide; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_star_gold, null));
            
        }

        //if this lesson has already been completed, remove values from slideCompleted table
        EverydayLanguageDbHelper
                .getInstance(this)
                .checkClearSlideCompleted(mUserId, mModuleId, mLesson.Id, mLesson.LessonOrder);

        //initialize text to speech
        //check english-US tts loaded
        //ContentManager.setupAudio(getApplicationContext(), this, this);

        /*
        for (Slide slide: mLesson.Pages) {
            Log.d("SUKH", "lesson content slide: " + String.valueOf(slide.Id) + ", cat: " +
                String.valueOf(slide.CatId));
        }
        */


        //Log.d("LESSON", "Lesson " + String.valueOf(lesson.Id) + " received by lesson activity");

        moveNext();

    }

    private void moveNext() {
        /*
        if (mLesson.Id == 1) {
            while (mLesson.Pages.get(mCurrentSlide).SlideOrder < 41) {
                mCurrentSlide++;
            }
        }
        */
        Slide slide = mLesson.Pages.get(mCurrentSlide);
        EverydayLanguageDbHelper dbHelper = EverydayLanguageDbHelper
                .getInstance(getApplicationContext());
        //get slide with all content
        //Slide slide =
        String instructions = dbHelper.getSlideInstructions(slide.CatId);
        Log.d("SLIDE", "cat = " + String.valueOf(slide.CatId));
        mCurrentFragment = null;
        switch (slide.CatId) {
            case 1:
                mCurrentFragment = MultipleChoiceImageFragment.newInstance(slide, mImageUrl);
                break;
            case 2:
                mCurrentFragment = MultipleChoiceTextFragment.newInstance(slide, mImageUrl);
                break;
            case 3:
                mCurrentFragment = MissingWordFragment.newInstance(slide, mImageUrl);
                break;
            case 4:
                mCurrentFragment = TeachingFragment.newInstance(slide);
                break;
            case 5:
                mCurrentFragment = PhoneticsFragment.newInstance(slide);
                break;
            case 6:
                mCurrentFragment = MatchingPairsTextFragment.newInstance(slide, mImageUrl);
                break;
            case 9:
                mCurrentFragment = TranslateFragment.newInstance(slide, instructions, mImageUrl);
                break;
            case 11:
                mCurrentFragment = MatchingPairsImageFragment.newInstance(slide, mImageUrl);
                break;
            case 12:
                mCurrentFragment = MatchingPairsImageTextFragment.newInstance(slide, mImageUrl);
                break;
            case 14:
                mCurrentFragment = WritingFragment.newInstance(slide);
                break;
            case 15:
                mCurrentFragment = BingoFragment.newInstance(slide, mImageUrl);
                break;
            case 16:
                mCurrentFragment = ConversationFragment.newInstance(slide, mImageUrl);
                break;
            case 17:
                mCurrentFragment = QuestionFragment.newInstance(slide, mImageUrl);
                break;
            case 18:
                mCurrentFragment = ListeningFragment.newInstance(slide, mImageUrl);
                break;
        }

        mCurrentFragment.onAttach(this);


        getSupportActionBar().setTitle(instructions);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.activity_lesson2, mCurrentFragment)
                .commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu_lesson, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();

        switch (item.getItemId()) {

            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_switchUser:
                intent.putExtra(ARG_NAME_FINISH_TYPE, FINISH_TYPE_SWITCH_USER);
                setResult(Activity.RESULT_OK, intent);
                finish();
                return true;

            case R.id.action_start_over:
                intent.putExtra(ARG_NAME_FINISH_TYPE, FINISH_TYPE_START_OVER);
                setResult(Activity.RESULT_OK, intent);
                finish();
                return true;

            case R.id.action_refresh_lessons:
                intent.putExtra(ARG_NAME_FINISH_TYPE, FINISH_TYPE_REFRESH_LESSONS);
                setResult(Activity.RESULT_OK, intent);
                finish();
                return true;

            case R.id.action_next_lesson:
                /*
                EverydayLanguageDbHelper
                        .getInstance(getApplicationContext())
                        .updateLessonCompleted(mUserId, mLesson.Id);
                        */
                intent.putExtra(ARG_NAME_FINISH_TYPE, FINISH_TYPE_LESSON_COMPLETED);
                setResult(Activity.RESULT_OK, intent);
                finish();
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public void onSlideCompleted(int slideId, int errors) {
        EverydayLanguageDbHelper
                .getInstance(this)
                .updateSlideCompleted(mUserId, mLesson.Id, mCurrentSlide, errors);

        if (errors > 1) {
            moveNext();
        } else {
            mCurrentSlide++;
            if (mCurrentSlide < mLesson.Pages.size()) {
                moveNext();
            } else {
                EverydayLanguageDbHelper
                        .getInstance(getApplicationContext())
                        .updateLessonCompleted(mUserId, mModuleId, mLesson.Id, mLesson.LessonOrder);

                Intent intent = new Intent();
                intent.putExtra(ARG_NAME_FINISH_TYPE, FINISH_TYPE_LESSON_COMPLETED);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        }

        /*
        try {
            //int currentItem = mPager.getCurrentItem();
            //mPager.setCurrentItem(currentItem + 1);
        } catch (Exception ex) {
            EverydayLanguageDbHelper.getInstance(getApplicationContext())
                    .updateLessonCompleted(mUserId, mLesson.Id);

            setResult(RESULT_OK);
            finish();
        }
        */
    }

    public void onAudioFinished(String key) {
        if (mCurrentFragment != null) {
            if (mCurrentFragment instanceof AudioFinishedCallback) {
                ((AudioFinishedCallback)mCurrentFragment).onAudioFinished(key);
            }
        }
    }


    public void onAudioSetupComplete(int code) {
        if (code == AUDIO_SETUP_MISSING_LANGUAGE) {
            Intent installTTSIntent = new Intent();
            installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
            ArrayList<String> languages = new ArrayList<String>();
            languages.add("en-US");
            installTTSIntent.putStringArrayListExtra(
                    TextToSpeech.Engine.EXTRA_CHECK_VOICE_DATA_FOR, languages);
            startActivityForResult(installTTSIntent, CHECK_DATA_CODE);

        } else {
            moveNext();
        }
    }

    @Override
    public void onStart(){
        super.onStart();
        //ContentManager.setupAudio(getApplicationContext(), this, this);
    }

    @Override
    public void onStop() {
        super.onStop();
        //ContentManager.releaseResources();
    }


}
