package everyday.sukhajata.com.everydayenglish;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import everyday.sukhajata.com.everydayenglish.interfaces.AudioFinishedCallback;
import everyday.sukhajata.com.everydayenglish.interfaces.SlideCompletedListener;
import everyday.sukhajata.com.everydayenglish.model.SlideMedia;
import everyday.sukhajata.com.everydayenglish.model.Slide;
import everyday.sukhajata.com.everydayenglish.utility.ContentManager;

import static everyday.sukhajata.com.everydayenglish.LessonActivity.ARG_NAME_IMAGE_URL;
import static everyday.sukhajata.com.everydayenglish.LessonActivity.ARG_NAME_INSTRUCTIONS;
import static everyday.sukhajata.com.everydayenglish.LessonActivity.ARG_NAME_SLIDE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SlideCompletedListener} interface
 * to handle interaction events.
 * Use the {@link MatchingPairsImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MatchingPairsImageFragment extends Fragment implements AudioFinishedCallback {


    //parameters passed in bundle
    private Slide mPage;
    private String mImageUrl;
    private View mLayout;

    private SlideMedia mSelectedTextPhrase;
    private FrameLayout mSelectedTextFrame;
    private SlideMedia mSelectedImagePhrase;
    private FrameLayout mSelectedImageFrame;

    private int correctCount;
    private int errorCount;
    private Queue<String> audioQueue;
    private boolean mWatingToDie;

    private SlideCompletedListener mListener;

    public MatchingPairsImageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param page a Slide.
     * @param imageUrl
     * @return A new instance of fragment MatchingPairsImageFragment.
     */
    public static MatchingPairsImageFragment newInstance(
            Slide page, String imageUrl) {
        MatchingPairsImageFragment fragment = new MatchingPairsImageFragment();
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

        audioQueue = new LinkedList<>();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View layout = (View)inflater.inflate(
                R.layout.fragment_matching_pairs_image, container, false);
        mLayout = layout;

        //text buttons
        final Button btn1 = (Button) layout.findViewById(R.id.matchingPairsImage_buttonText1);
        final FrameLayout frame1 = (FrameLayout)layout.findViewById(R.id.matchingPairsImage_frame1);
        setupButton(btn1, mPage.MediaList.get(0), frame1);

        final Button btn2 = (Button)layout.findViewById(R.id.matchingPairsImage_buttonText2);
        final FrameLayout frame2 = (FrameLayout)layout.findViewById(R.id.matchingPairsImage_frame2);
        setupButton(btn2, mPage.MediaList.get(1), frame2);



        //shuffle list
        long seed = System.nanoTime();
        Collections.shuffle(mPage.MediaList, new Random(seed));

        //images
        final ImageView imgBtn1 = (ImageView)layout.findViewById(R.id.matchingPairsImage_imageView1);
        final FrameLayout imgFrame1 = (FrameLayout)layout.findViewById(R.id.matchingPairsImage_frame5);
        setupImageView(imgBtn1, mPage.MediaList.get(0), imgFrame1);

        final ImageView imgBtn2 = (ImageView)layout.findViewById(R.id.matchingPairsImage_imageView2);
        final FrameLayout imgFrame2 = (FrameLayout)layout.findViewById(R.id.matchingPairsImage_frame6);
        setupImageView(imgBtn2, mPage.MediaList.get(1), imgFrame2);



        return layout;
    }

    private void setupButton(final Button button, SlideMedia phrase, final FrameLayout frame) {
        button.setText(phrase.English);
        button.setTag(phrase);
        button.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                onTextButtonPressed(button, frame);
            }
        });

    }

    private void setupImageView(final ImageView imageView, SlideMedia media,
                                final FrameLayout frame) {

        //imageView.setImageResource(getResources().getIdentifier(
        //        media.ImageFileName, "drawable", getActivity().getPackageName()));
        Context context = getActivity().getApplicationContext();
        ContentManager.fetchImage(context, imageView, media.ImageFileName, mImageUrl);
        imageView.setTag(media);
        imageView.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                onImageViewClicked(imageView, frame);
            }
        });

    }

    public void onTextButtonPressed(Button button, FrameLayout frame) {
        if (mSelectedTextFrame != null) {
            mSelectedTextFrame.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorBorderInactive, null));
        }

        mSelectedTextFrame = frame;
        mSelectedTextPhrase = (SlideMedia)button.getTag();
        frame.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorBorderSelected, null));

        audioQueue.add(mSelectedTextPhrase.English);
        ContentManager.playAudio(
                getActivity(),
                mSelectedTextPhrase.English);

        if (mSelectedImagePhrase != null) {

            //is it a match?
            if (mSelectedImagePhrase.Id == mSelectedTextPhrase.Id) {
                //yes, remove both
                removeFrames(mSelectedTextFrame, mSelectedImageFrame);
                correctCount++;
                if (correctCount == 2) {

                        mListener.onSlideCompleted(mPage.Id, errorCount);
                }
            } else {
                //no, deselect both
                errorCount++;
                mSelectedTextFrame.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorBorderInactive, null));
                mSelectedImageFrame.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorBorderInactive, null));

            }

            //reset
            mSelectedImageFrame = null;
            mSelectedTextFrame = null;
            mSelectedImagePhrase = null;
            mSelectedTextPhrase = null;
        }

    }

    public void onAudioFinished(String key) {
        audioQueue.poll();
        if (mWatingToDie && audioQueue.size() == 0) {
            mListener.onSlideCompleted(mPage.Id, errorCount);
        }
    }

    private void onImageViewClicked(ImageView imageView, FrameLayout frame) {

        if (mSelectedImageFrame != null) {
            //deselect
            mSelectedImageFrame.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorBorderInactive, null));
        }

        mSelectedImageFrame = frame;
        mSelectedImagePhrase = (SlideMedia)imageView.getTag();
        mSelectedImageFrame.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorBorderSelected, null));

        if (mSelectedTextPhrase != null) {
            //is it a match?
            if (mSelectedImagePhrase.Id == mSelectedTextPhrase.Id) {
                //yes, remove both
                removeFrames(mSelectedTextFrame, mSelectedImageFrame);
                correctCount++;
                if (correctCount == 2) {

                        mListener.onSlideCompleted(mPage.Id, errorCount);
                }
            } else {
                //no, deselect both
                errorCount++;
                mSelectedTextFrame.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorBorderInactive, null));
                mSelectedImageFrame.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorBorderInactive, null));

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
