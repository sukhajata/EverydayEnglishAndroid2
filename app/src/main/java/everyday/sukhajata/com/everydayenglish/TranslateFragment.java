package everyday.sukhajata.com.everydayenglish;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.os.Bundle;
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

import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

import everyday.sukhajata.com.everydayenglish.interfaces.AudioFinishedCallback;
import everyday.sukhajata.com.everydayenglish.interfaces.SlideCompletedListener;
import everyday.sukhajata.com.everydayenglish.model.Slide;
import everyday.sukhajata.com.everydayenglish.model.SlideMedia;
import everyday.sukhajata.com.everydayenglish.utility.ContentManager;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TranslateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TranslateFragment extends Fragment implements AudioFinishedCallback{

    private Slide mSlide;
    private String mInstructions;
    private SlideMedia mTarget;
    private SlideCompletedListener mListener;
    private String mImageUrl;
    private int errorCount;
    private View mLayout;
    private int index;
    private TextView txtEnglish;
    private FrameLayout selectedFrame;
    private LinkedList<SlideMedia> mQueue;
    //private LinkedList<String> audioQueue;
    private ArrayList<FrameLayout> visibleFrames;

    public TranslateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param slide Parameter 1.
     * @param instructions Parameter 2.
     * @return A new instance of fragment TranslateFragment.
     */
    public static TranslateFragment newInstance(Slide slide, String instructions,
                                                String imageUrl) {
        TranslateFragment fragment = new TranslateFragment();
        Bundle args = new Bundle();
        args.putParcelable(LessonActivity.ARG_NAME_SLIDE, slide);
        args.putString(LessonActivity.ARG_NAME_INSTRUCTIONS, instructions);
        args.putString(LessonActivity.ARG_NAME_IMAGE_URL, imageUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSlide = getArguments().getParcelable(LessonActivity.ARG_NAME_SLIDE);
            mInstructions = getArguments().getString(LessonActivity.ARG_NAME_INSTRUCTIONS);
            mImageUrl = getArguments().getString(LessonActivity.ARG_NAME_IMAGE_URL);

            visibleFrames = new ArrayList<>();

            //sort by WordOrder and queue
            Collections.sort(mSlide.MediaList);
            mQueue = new LinkedList<>();
            for (SlideMedia slideMedia : mSlide.MediaList) {
                if (slideMedia.MediaOrder < 100) {
                    mQueue.add(slideMedia);
                }
            }

            mTarget = mQueue.poll();
        }
        //audioQueue = new LinkedList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_translate, container, false);
        mLayout = layout;

        TextView txtTarget = (TextView)layout.findViewById(R.id.translate_txtTarget);
        txtTarget.setText(mSlide.ContentThai);

        txtEnglish = (TextView)layout.findViewById(R.id.translate_txtEnglish);
        txtEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentManager.playAudio(getActivity(), mSlide.Content);

            }
        });

        ImageView speaker = (ImageView)layout.findViewById(R.id.translate_speaker);
        speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentManager.playAudio(getActivity(), mSlide.Content);
            }
        });

        if (mSlide.ImageFileName != null && mSlide.ImageFileName.length() > 1) {
            ImageView img = (ImageView)layout.findViewById(R.id.translate_image);
            ContentManager.fetchImage(getActivity(), img,
                    mSlide.ImageFileName, mImageUrl);
        }

        //shuffle list
        long seed = System.nanoTime();
        Collections.shuffle(mSlide.MediaList, new Random(seed));

        FlexboxLayout flexboxLayout = (FlexboxLayout) layout.findViewById(R.id.translate_flexbox);

        for (SlideMedia slideMedia : mSlide.MediaList) {
            FrameLayout frame = new FrameLayout(getActivity());
            frame.setPadding(7,7,7,7);
            ViewGroup.LayoutParams frameLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            frame.setLayoutParams(frameLayoutParams);

            Button btn = new Button(getActivity(), null, R.style.SelectableButton);
            ViewGroup.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            btn.setLayoutParams(buttonLayoutParams);
            setupButton(frame, btn, slideMedia);

            frame.addView(btn);
            flexboxLayout.addView(frame);
        }

        Button btnNext = (Button)layout.findViewById(R.id.translate_btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSlideCompleted(mSlide.Id, errorCount-2);
            }
        });

        return layout;
    }


    @Override
    public void onResume() {
        super.onResume();
        ContentManager.playAudio(getActivity(), mTarget.English);
    }

    private void setupButton(final FrameLayout frame, Button button,
                                 final SlideMedia word) {
        button.setPadding(14,14,14,14);
        button.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null));
        if (word.English.toLowerCase().equals("i")) {
            button.setText(word.English);
        } else {
            button.setText(word.English.toLowerCase());
        }
        button.setTextSize(20);
        button.setTextColor(ResourcesCompat.getColor(getResources(),R.color.colorLabelBackground, null));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClicked(frame, word);
            }
        });
    }

    private  void onButtonClicked(FrameLayout frame, SlideMedia word) {
        if (selectedFrame != null) {
            selectedFrame.setBackgroundColor(ResourcesCompat.getColor(mLayout.getResources(),
                    R.color.colorLabelBackground, null));
        }
        selectedFrame = frame;

        if (word.English.toLowerCase().equals(mTarget.English.toLowerCase())) {
            //frame.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorBorderSelected, null));
            //mListener.onSlideCompleted(mSlide.Id, errorCount);
            //audioQueue.add(word.English);
            //ContentManager.playAudio(word.English, word.AudioFileName);
            removeFrame(frame);
            visibleFrames.remove(frame);
            if (index == 0) {
                String upperString = word.English.substring(0,1).toUpperCase() + word.English.substring(1);
                txtEnglish.setText(upperString);
            }else if (word.English.equals("I")) {
                txtEnglish.setText(txtEnglish.getText() + " " + word.English);
            } else {
                txtEnglish.setText(txtEnglish.getText() + " " + word.English.toLowerCase());
            }
            index++;

            if (mQueue.isEmpty()) {
                //audioQueue.add(mSlide.Content);
                ContentManager.playAudio(getActivity(), mSlide.Content);
                ((Button)mLayout.findViewById(R.id.translate_btnNext)).setEnabled(true);
                for (FrameLayout visibleFrame : visibleFrames) {
                    removeFrame(visibleFrame);
                    //visibleFrames.remove(frame);
                }
            } else {
                mTarget = mQueue.poll();
                ContentManager.playAudio(getActivity(), mTarget.English);
            }
        } else {
            errorCount++;
            frame.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorBorderWrong, null));
        }
    }

    private void removeFrame(final FrameLayout frame) {
        if (frame != null) {
            frame.animate()
                    .alpha(0.0f)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            frame.setBackgroundColor(ResourcesCompat.getColor(mLayout.getResources(), R.color.colorBorderInactive, null));
                        }
                    });


        }
    }


    public void onAudioFinished(String key) {
        //audioQueue.poll();

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
