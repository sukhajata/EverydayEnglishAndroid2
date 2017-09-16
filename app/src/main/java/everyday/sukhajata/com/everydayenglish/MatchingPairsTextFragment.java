package everyday.sukhajata.com.everydayenglish;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Bundle;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Random;

import everyday.sukhajata.com.everydayenglish.interfaces.SlideCompletedListener;
import everyday.sukhajata.com.everydayenglish.model.Slide;
import everyday.sukhajata.com.everydayenglish.model.SlideMedia;
import everyday.sukhajata.com.everydayenglish.utility.ContentManager;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MatchingPairsTextFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MatchingPairsTextFragment extends Fragment {
    private Slide mPage;
    private String mImageUrl;

    private SlideMedia mSelectedEnglishPhrase;
    private FrameLayout mSelectedEnglishFrame;
    private SlideMedia mSelectedThaiPhrase;
    private FrameLayout mSelectedThaiFrame;
    private View mLayout;
    private ImageView mImageView;

    private int correctCount;
    private int errorCount;

    private SlideCompletedListener mListener;


    public MatchingPairsTextFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param slide Parameter 1.
     * @return A new instance of fragment MatchingPairsTextFragment.
     */
    public static MatchingPairsTextFragment newInstance(Slide slide, String imageUrl) {
        MatchingPairsTextFragment fragment = new MatchingPairsTextFragment();
        Bundle args = new Bundle();
        args.putParcelable(LessonActivity.ARG_NAME_SLIDE, slide);
        args.putString(LessonActivity.ARG_NAME_IMAGE_URL, imageUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPage = getArguments().getParcelable(LessonActivity.ARG_NAME_SLIDE);
            mImageUrl = getArguments().getString(LessonActivity.ARG_NAME_IMAGE_URL);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout =  inflater.inflate(R.layout.fragment_matching_pairs_text, container, false);
        mLayout = layout;


        FrameLayout frame1 = (FrameLayout)layout.findViewById(R.id.matchingPairsText_frame1);
        Button btn1 = (Button)layout.findViewById(R.id.matchingPairsText_buttonText1);
        setupEnglishButton(btn1, mPage.MediaList.get(0), frame1);

        FrameLayout frame2 = (FrameLayout)layout.findViewById(R.id.matchingPairsText_frame2);
        Button btn2 = (Button)layout.findViewById(R.id.matchingPairsText_buttonText2);
        setupEnglishButton(btn2, mPage.MediaList.get(1), frame2);


        //shuffle list
        long seed = System.nanoTime();
        Collections.shuffle(mPage.MediaList, new Random(seed));

        FrameLayout frame3 = (FrameLayout)layout.findViewById(R.id.matchingPairsText_frame3);
        Button btn5 = (Button)layout.findViewById(R.id.matchingPairsText_buttonThai1);
        setupThaiButton(btn5, mPage.MediaList.get(0), frame3);

        FrameLayout frame4 = (FrameLayout)layout.findViewById(R.id.matchingPairsText_frame4);
        Button btn6 = (Button)layout.findViewById(R.id.matchingPairsText_buttonThai2);
        setupThaiButton(btn6, mPage.MediaList.get(1), frame4);

        mImageView  = (ImageView)mLayout.findViewById(R.id.matchingPairsText_image);


        return layout;
    }

    private  void setupEnglishButton(final Button button, SlideMedia slideMedia,
                                     final FrameLayout frame) {
        String firstLetterCapitalized = slideMedia.English.substring(0,1).toUpperCase() + slideMedia.English.substring(1);
        button.setText(firstLetterCapitalized);
        //button.setText(slideMedia.English);
        button.setTag(slideMedia);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                onEnglishButtonClicked(button, frame);
            }
        });
    }

    private void setupThaiButton(final Button button, SlideMedia slideMedia,
                                 final FrameLayout frame) {
        button.setText(slideMedia.Thai);
        button.setTag(slideMedia);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                onThaiButtonClicked(button, frame);
            }
        });
    }

    private void onEnglishButtonClicked(Button button, FrameLayout frame){
        if (mSelectedEnglishFrame != null) {
            mSelectedEnglishFrame.setBackgroundColor(ResourcesCompat.getColor(mLayout.getResources(), R.color.colorBorderInactive, null));
        }

        mSelectedEnglishFrame = frame;
        mSelectedEnglishPhrase = (SlideMedia)button.getTag();

        if (mSelectedEnglishPhrase.ImageFileName != null && mSelectedEnglishPhrase.ImageFileName.length() > 1) {
            ContentManager.fetchImage(getActivity(), mImageView,
                    mSelectedEnglishPhrase.ImageFileName, mImageUrl);
            mImageView.setBackgroundColor(ResourcesCompat.getColor(mLayout.getResources(),
                    R.color.colorLabelBackground, null));
        }
        ((MyApplication)getActivity().getApplication()).playAudio( mSelectedEnglishPhrase.English);
        mSelectedEnglishFrame.setBackgroundColor(ResourcesCompat.getColor(mLayout.getResources(), R.color.colorBorderSelected, null));

        if (mSelectedThaiFrame != null) {
            //is it a match?
            if (mSelectedEnglishPhrase == mSelectedThaiPhrase) {
                //match. remove both
                removeFrames(mSelectedEnglishFrame, mSelectedThaiFrame);
                correctCount++;
                if (correctCount == 2) {
                    mListener.onSlideCompleted(mPage.Id, errorCount);
                }
            } else {
                //not a match. deselect both
                errorCount++;
                mSelectedEnglishFrame.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorBorderInactive, null));
                mSelectedThaiFrame.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorBorderInactive, null));

            }

            mSelectedEnglishFrame = null;
            mSelectedThaiFrame = null;
            mSelectedEnglishPhrase = null;
            mSelectedThaiPhrase = null;
        }
    }

    private void onThaiButtonClicked(Button button, FrameLayout frame) {

        if (mSelectedThaiFrame != null) {
            mSelectedThaiFrame.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorBorderInactive, null));
        }

        mSelectedThaiFrame = frame;
        mSelectedThaiPhrase = (SlideMedia)button.getTag();
        mSelectedThaiFrame.setBackgroundColor(ResourcesCompat.getColor(mLayout.getResources(), R.color.colorBorderSelected, null));

        if (mSelectedEnglishFrame != null) {
            //is it a match?
            if (mSelectedThaiPhrase == mSelectedEnglishPhrase) {
                //yes. remove both
                removeFrames(mSelectedEnglishFrame, mSelectedThaiFrame);
                correctCount++;
                if (correctCount == 2) {
                    mListener.onSlideCompleted(mPage.Id, errorCount);
                }
            } else {
                //not a match. deselect
                errorCount++;
                mSelectedEnglishFrame.setBackgroundColor(ResourcesCompat.getColor(mLayout.getResources(), R.color.colorBorderInactive, null));
                mSelectedThaiFrame.setBackgroundColor(ResourcesCompat.getColor(mLayout.getResources(), R.color.colorBorderInactive, null));
            }

            mSelectedEnglishFrame = null;
            mSelectedThaiFrame = null;
            mSelectedEnglishPhrase = null;
            mSelectedThaiPhrase = null;

        }

    }

    private void removeFrames(final FrameLayout frame1, final FrameLayout frame2) {
        if (frame1 != null && frame2 != null) {
            frame1.animate()
                    .alpha(0.0f)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            frame1.setBackgroundColor(ResourcesCompat.getColor(mLayout.getResources(), R.color.colorBorderInactive, null));
                        }
                    });

            frame2.animate()
                    .alpha(0.0f)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            frame2.setBackgroundColor(ResourcesCompat.getColor(mLayout.getResources(), R.color.colorBorderInactive, null));
                        }
                    });

            mImageView.setImageResource(android.R.color.transparent);
            mImageView.setBackgroundColor(ResourcesCompat.getColor(mLayout.getResources(),
                    R.color.colorLabelBackground, null));
        }

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SlideCompletedListener) {
            mListener = (SlideCompletedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnTotalsFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}
