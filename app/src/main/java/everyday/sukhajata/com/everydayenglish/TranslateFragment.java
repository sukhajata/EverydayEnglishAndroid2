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
import android.widget.TextView;

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
    private TextView txtTranslate;
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



        //shuffle list
        long seed = System.nanoTime();
        Collections.shuffle(mSlide.MediaList, new Random(seed));

        TextView txtTarget = (TextView)layout.findViewById(R.id.translate_txtTarget);
        txtTarget.setText(mSlide.ContentThai);

        txtTranslate = (TextView)layout.findViewById(R.id.translate_txtTranslate);
        txtTranslate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentManager.playAudio(mSlide.Content, null);

            }
        });

        ImageView speaker = (ImageView)layout.findViewById(R.id.translate_speaker);
        speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentManager.playAudio(mSlide.Content, null);
            }
        });

        if (mSlide.ImageFileName != null) {
            ImageView img = (ImageView)layout.findViewById(R.id.translate_image);
            ContentManager.fetchImage(getActivity(), img,
                    mSlide.ImageFileName, mImageUrl);
        }

        int count = mSlide.MediaList.size();

        FrameLayout frame1 = (FrameLayout)layout.findViewById(R.id.translate_frame1);
        visibleFrames.add(frame1);
        Button btn1 = (Button)layout.findViewById(R.id.translate_button1);
        SlideMedia word = mSlide.MediaList.get(0);
        setupButton(frame1, btn1, word);

        FrameLayout frame2 = (FrameLayout)layout.findViewById(R.id.translate_frame2);
        visibleFrames.add(frame2);
        Button btn2 = (Button)layout.findViewById(R.id.translate_button2);
        SlideMedia word2 = mSlide.MediaList.get(1);
        setupButton(frame2, btn2, word2);

        FrameLayout frame3 = (FrameLayout)layout.findViewById(R.id.translate_frame3);
        visibleFrames.add(frame3);
        Button btn3 = (Button)layout.findViewById(R.id.translate_button3);
        SlideMedia word3 = mSlide.MediaList.get(2);
        setupButton(frame3, btn3, word3);

        FrameLayout frame4 = (FrameLayout) layout.findViewById(R.id.translate_frame4);
        Button btn4 = (Button) layout.findViewById(R.id.translate_button4);
        if (count > 3) {
            SlideMedia word4 = mSlide.MediaList.get(3);
            setupButton(frame4, btn4, word4);
            visibleFrames.add(frame4);
        } else {
            frame4.setVisibility(View.INVISIBLE);
        }

        FrameLayout frame5 = (FrameLayout)layout.findViewById(R.id.translate_frame5);
        Button btn5 = (Button)layout.findViewById(R.id.translate_button5);
        if (count > 4) {
            SlideMedia word5 = mSlide.MediaList.get(4);
            setupButton(frame5, btn5, word5);
            visibleFrames.add(frame5);
        } else {
            frame5.setVisibility(View.INVISIBLE);
        }

        FrameLayout frame6 = (FrameLayout)layout.findViewById(R.id.translate_frame6);
        Button btn6 = (Button)layout.findViewById(R.id.translate_button6);
        if (count > 5) {
            SlideMedia word6 = mSlide.MediaList.get(5);
            setupButton(frame6, btn6, word6);
            visibleFrames.add(frame6);
        } else {
            frame6.setVisibility(View.INVISIBLE);
        }

        FrameLayout frame7 = (FrameLayout)layout.findViewById(R.id.translate_frame7);
        Button btn7 = (Button)layout.findViewById(R.id.translate_button7);
        if (count > 6) {
            SlideMedia word7 = mSlide.MediaList.get(6);
            setupButton(frame7, btn7, word7);
            visibleFrames.add(frame7);
        } else {
            frame7.setVisibility(View.INVISIBLE);
        }

        FrameLayout frame8 = (FrameLayout)layout.findViewById(R.id.translate_frame8);
        Button btn8 = (Button)layout.findViewById(R.id.translate_button8);
        if (count > 7) {
            SlideMedia word8 = mSlide.MediaList.get(7);
            setupButton(frame8, btn8, word8);
            visibleFrames.add(frame8);
        } else {
            frame8.setVisibility(View.INVISIBLE);
        }

        FrameLayout frame9 = (FrameLayout)layout.findViewById(R.id.translate_frame9);
        Button btn9 = (Button)layout.findViewById(R.id.translate_button9);
        if (count > 8) {
            SlideMedia word9 = mSlide.MediaList.get(8);
            setupButton(frame9, btn9, word9);
            visibleFrames.add(frame9);
        } else {
            frame9.setVisibility(View.INVISIBLE);
        }

        FrameLayout frame10 = (FrameLayout)layout.findViewById(R.id.translate_frame10);
        Button btn10 = (Button)layout.findViewById(R.id.translate_button10);
        if (count > 9) {
            SlideMedia word10 = mSlide.MediaList.get(9);
            setupButton(frame10, btn10, word10);
            visibleFrames.add(frame10);
        } else {
            frame10.setVisibility(View.INVISIBLE);
        }

        FrameLayout frame11 = (FrameLayout)layout.findViewById(R.id.translate_frame11);
        Button btn11 = (Button)layout.findViewById(R.id.translate_button11);
        if (count > 10) {
            SlideMedia word11 = mSlide.MediaList.get(10);
            setupButton(frame11, btn11, word11);
            visibleFrames.add(frame11);
        } else {
            frame11.setVisibility(View.INVISIBLE);
        }

        FrameLayout frame12 = (FrameLayout)layout.findViewById(R.id.translate_frame12);
        Button btn12 = (Button)layout.findViewById(R.id.translate_button12);
        if (count > 11) {
            SlideMedia word12 = mSlide.MediaList.get(11);
            setupButton(frame12, btn12, word12);
            visibleFrames.add(frame12);
        } else {
            frame12.setVisibility(View.INVISIBLE);
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
        ContentManager.playAudio(mTarget.English, mTarget.AudioFileName);
    }

    private void setupButton(final FrameLayout frame, Button button,
                                 final SlideMedia word) {
        button.setText(word.English);
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
                    R.color.colorPrimaryDark, null));
        }
        selectedFrame = frame;

        if (word.English.toLowerCase().equals(mTarget.English.toLowerCase())) {
            //frame.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorBorderSelected, null));
            //mListener.onSlideCompleted(mSlide.Id, errorCount);
            //audioQueue.add(word.English);
            //ContentManager.playAudio(word.English, word.AudioFileName);
            removeFrame(frame);
            visibleFrames.remove(frame);
            txtTranslate.setText(txtTranslate.getText() + " " + word.English.toLowerCase());
            index++;

            if (mQueue.isEmpty()) {
                //audioQueue.add(mSlide.Content);
                ContentManager.playAudio(mSlide.Content, null);
                ((Button)mLayout.findViewById(R.id.translate_btnNext)).setEnabled(true);
                for (FrameLayout visibleFrame : visibleFrames) {
                    removeFrame(visibleFrame);
                    //visibleFrames.remove(frame);
                }
            } else {
                mTarget = mQueue.poll();
                ContentManager.playAudio(mTarget.English, mTarget.AudioFileName);
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
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}
