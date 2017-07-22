package everyday.sukhajata.com.everydayenglish;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import everyday.sukhajata.com.everydayenglish.model.Lesson;
import everyday.sukhajata.com.everydayenglish.model.Slide;
import everyday.sukhajata.com.everydayenglish.utility.EverydayLanguageDbHelper;

/**
 * Created by Tim on 17/02/2017.
 */

public class LessonPagerAdapter extends FragmentStatePagerAdapter {


    private LessonActivity mActivity;
    private Lesson mLesson;
    private String mImageUrl;

    public LessonPagerAdapter(LessonActivity activity, FragmentManager fm, Lesson lesson,
                              String imageUrl) {
        super(fm);
        mActivity = activity;
        mLesson = lesson;
        mImageUrl = imageUrl;
    }

    @Override
    public int getCount() {
        return mLesson.Pages.size();
    }

    @Override
    public Fragment getItem(int position) {

        Slide slide = mLesson.Pages.get(position);
        EverydayLanguageDbHelper dbHelper = EverydayLanguageDbHelper
                .getInstance(mActivity.getApplicationContext());
        //get slide with all content
        //Slide slide =
        String instructions = dbHelper.getSlideInstructions(slide.CatId);
        Log.d("SLIDE", "cat = " + String.valueOf(slide.CatId));
        Fragment fragment = null;
        switch (slide.CatId) {
            case 1:
                fragment = MultipleChoiceImageFragment.newInstance(slide, mImageUrl);
                break;
            case 2:
                fragment = MultipleChoiceTextFragment.newInstance(slide, mImageUrl);
                break;
            case 3:
                fragment = MissingWordFragment.newInstance(slide, mImageUrl);
                break;
            case 4:
                fragment = TeachingFragment.newInstance(slide);
                break;
            case 5:
                fragment = PhoneticsFragment.newInstance(slide);
                break;
            case 6:
                fragment = MatchingPairsTextFragment.newInstance(slide, mImageUrl);
                break;
            case 9:
                fragment = TranslateFragment.newInstance(slide, instructions, mImageUrl);
                break;
            case 11:
                fragment = MatchingPairsImageFragment.newInstance(slide, mImageUrl);
                break;
            case 12:
                fragment = MatchingPairsImageTextFragment.newInstance(slide, mImageUrl);
                break;
            case 14:
                fragment = WritingFragment.newInstance(slide);
                break;
        }

        fragment.onAttach(mActivity);


        return fragment;

    }

    /*
    public LessonPagerAdapter(Context context, List<Slide> pages) {
        mContext = context;
        mPages = pages;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        Slide page = mPages.get(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewGroup layout = (ViewGroup) inflater.inflate(page.getLayoutResId(), collection, false);
        collection.addView(layout);
        return layout;
    }



    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }
    */
}
