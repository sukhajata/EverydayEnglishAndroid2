package everyday.sukhajata.com.everydayenglish;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.net.Uri;
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
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Random;

import everyday.sukhajata.com.everydayenglish.interfaces.SlideCompletedListener;
import everyday.sukhajata.com.everydayenglish.model.Slide;
import everyday.sukhajata.com.everydayenglish.model.SlideMedia;
import everyday.sukhajata.com.everydayenglish.utility.ContentManager;

import static everyday.sukhajata.com.everydayenglish.LessonActivity.ARG_NAME_IMAGE_URL;
import static everyday.sukhajata.com.everydayenglish.LessonActivity.ARG_NAME_INSTRUCTIONS;
import static everyday.sukhajata.com.everydayenglish.LessonActivity.ARG_NAME_SLIDE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SlideCompletedListener} interface
 * to handle interaction events.
 * Use the {@link MatchingPairsImageTextFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MatchingPairsImageTextFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private Slide mPage;
    private String mImageUrl;
    private int correctCount;
    private int errorCount;
    private View mLayout;

    private SlideCompletedListener mListener;

    private SlideMedia mSelectedTextPhrase;
    private FrameLayout mSelectedTextFrame;
    private SlideMedia mSelectedImagePhrase;
    private FrameLayout mSelectedImageFrame;

    public MatchingPairsImageTextFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param page the Slide object for this fragment.
     * @return A new instance of fragment MatchingPairsImageTextFragment.
     */
    public static MatchingPairsImageTextFragment newInstance(
            Slide page, String imageUrl) {
        MatchingPairsImageTextFragment fragment = new MatchingPairsImageTextFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_NAME_SLIDE, page);
        args.putString(ARG_NAME_IMAGE_URL, imageUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPage = getArguments().getParcelable(ARG_NAME_SLIDE);
            mImageUrl = getArguments().getString(ARG_NAME_IMAGE_URL);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ScrollView layout = (ScrollView) inflater.inflate(
                R.layout.fragment_matching_pairs_image_text, container, false);
        mLayout = layout;


        final Button btn1 = (Button) layout.findViewById(R.id.buttonText1);
        final FrameLayout frame1 = (FrameLayout)layout.findViewById(R.id.matchingPairsImageText_frame1);
        setupButton(btn1, mPage.MediaList.get(0), frame1);

        final Button btn2 = (Button)layout.findViewById(R.id.buttonText2);
        final FrameLayout frame2 = (FrameLayout)layout.findViewById(R.id.matchingPairsImageText_frame2);
        setupButton(btn2, mPage.MediaList.get(1), frame2);


        //shuffle list
        long seed = System.nanoTime();
        Collections.shuffle(mPage.MediaList, new Random(seed));

        //thai text
        final ImageView imgBtn1 = (ImageView)layout.findViewById(R.id.matchingPairsImageText_imageView1);
        TextView txtView1 = (TextView)layout.findViewById(R.id.matchingPairsImageText_textView1);
        final FrameLayout imgFrame1 = (FrameLayout)layout.findViewById(R.id.matchingPairsImageText_frame5);
        setupImageView(imgBtn1, txtView1, mPage.MediaList.get(0), imgFrame1);

        final ImageView imgBtn2 = (ImageView)layout.findViewById(R.id.matchingPairsImageText_imageView2);
        TextView txtView2 = (TextView)layout.findViewById(R.id.matchingPairsImageText_textView2);
        final FrameLayout imgFrame2 = (FrameLayout)layout.findViewById(R.id.matchingPairsImageText_frame6);
        setupImageView(imgBtn2, txtView2, mPage.MediaList.get(1), imgFrame2);

        final ImageView imgBtn3 = (ImageView)layout.findViewById(R.id.matchingPairsImageText_imageView3);
        TextView txtView3 = (TextView)layout.findViewById(R.id.matchingPairsImageText_textView3);
        final FrameLayout imgFrame3 = (FrameLayout)layout.findViewById(R.id.matchingPairsImageText_frame7);
        setupImageView(imgBtn3, txtView3, mPage.MediaList.get(2), imgFrame3);

        final ImageView imgBtn4 = (ImageView)layout.findViewById(R.id.matchingPairsImageText_imageView4);
        TextView txtView4 = (TextView)layout.findViewById(R.id.matchingPairsImageText_textView4);
        final FrameLayout imgFrame4 = (FrameLayout)layout.findViewById(R.id.matchingPairsImageText_frame8);
        setupImageView(imgBtn4, txtView4, mPage.MediaList.get(3), imgFrame4);

        return  layout;
    }

    private void setupButton(final Button button, SlideMedia phrase, final FrameLayout frame) {
        button.setText(phrase.Thai);
        button.setTag(phrase);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                onTextButtonPressed(button, frame);
            }
        });

    }

    private void setupImageView(final ImageView imageView, TextView txt,
                                  SlideMedia media, final FrameLayout frame) {

        //imageView.setImageResource(mLayout.getResources().getIdentifier(
        //        media.ImageFileName, "drawable", getActivity().getPackageName()));
        ContentManager.fetchImage(getActivity().getApplicationContext(), imageView,
                media.ImageFileName, mImageUrl);
        imageView.setTag(media);
        imageView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                onImageViewPressed(imageView, frame);
            }
        });

        txt.setText(media.English);
    }


    public void onTextButtonPressed(Button button, FrameLayout frame) {
        if (mSelectedTextFrame != null) {
            mSelectedTextFrame.setBackgroundColor(ResourcesCompat.getColor(mLayout.getResources(), R.color.colorBorderInactive, null));
        }

        mSelectedTextFrame = frame;
        mSelectedTextPhrase = (SlideMedia)button.getTag();
        ContentManager.playAudio(getActivity(), mSelectedTextPhrase.English);

        frame.setBackgroundColor(ResourcesCompat.getColor(mLayout.getResources(), R.color.colorBorderSelected, null));


        if (mSelectedImagePhrase != null) {

            //is it a match?
            if (mSelectedImagePhrase.Id == mSelectedTextPhrase.Id) {
                //yes, remove both
                removeFrames(mSelectedImageFrame, mSelectedTextFrame);
                correctCount++;
                if (correctCount == 4) {
                    mListener.onSlideCompleted(mPage.Id, errorCount);
                }
            } else {
                //no, deselect both
                errorCount++;
                mSelectedTextFrame.setBackgroundColor(ResourcesCompat.getColor(mLayout.getResources(), R.color.colorBorderInactive, null));
                mSelectedImageFrame.setBackgroundColor(ResourcesCompat.getColor(mLayout.getResources(), R.color.colorBorderInactive, null));

            }

            //reset
            mSelectedImageFrame = null;
            mSelectedTextFrame = null;
            mSelectedImagePhrase = null;
            mSelectedTextPhrase = null;
        }

    }

    private void onImageViewPressed(ImageView imgButton, FrameLayout frame) {

        if (mSelectedImageFrame != null) {
            //deselect
            mSelectedImageFrame.setBackgroundColor(ResourcesCompat.getColor(mLayout.getResources(), R.color.colorBorderInactive, null));
        }

        mSelectedImageFrame = frame;
        mSelectedImagePhrase = (SlideMedia)imgButton.getTag();
        mSelectedImageFrame.setBackgroundColor(ResourcesCompat.getColor(mLayout.getResources(), R.color.colorBorderSelected, null));

        //play audio
        //ContentManager.playAudio(getActivity(), mSelectedImagePhrase.English, mSelectedImagePhrase.AudioFileName);

        if (mSelectedTextPhrase != null) {
            //is it a match?
            if (mSelectedImagePhrase.Id == mSelectedTextPhrase.Id) {
                //yes, remove both
                removeFrames(mSelectedImageFrame, mSelectedTextFrame);
                correctCount++;
                if (correctCount == 4) {
                    mListener.onSlideCompleted(mPage.Id, errorCount);
                }
            } else {
                //no, deselect both
                errorCount++;
                mSelectedTextFrame.setBackgroundColor(ResourcesCompat.getColor(mLayout.getResources(), R.color.colorBorderInactive, null));
                mSelectedImageFrame.setBackgroundColor(ResourcesCompat.getColor(mLayout.getResources(), R.color.colorBorderInactive, null));
            }

            //reset
            mSelectedImageFrame = null;
            mSelectedTextFrame = null;
            mSelectedImagePhrase = null;
            mSelectedTextPhrase = null;
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
