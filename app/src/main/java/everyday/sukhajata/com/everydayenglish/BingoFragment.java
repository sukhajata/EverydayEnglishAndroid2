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

import java.util.Collections;
import java.util.LinkedList;
import java.util.Random;

import everyday.sukhajata.com.everydayenglish.interfaces.SlideCompletedListener;
import everyday.sukhajata.com.everydayenglish.model.Slide;
import everyday.sukhajata.com.everydayenglish.model.SlideMedia;
import everyday.sukhajata.com.everydayenglish.utility.ContentManager;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BingoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BingoFragment extends Fragment {

    private Slide mSlide;
    private SlideCompletedListener mListener;
    private LinkedList<SlideMedia> mQueue;

    private FrameLayout selectedFrame;
    private SlideMedia selectedWord;
    private SlideMedia targetWord;
    private View mLayout;
    private TextView txtTarget;
    private ImageView imgTarget;
    private String  mImageUrl;

    private int errorCount;

    public BingoFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param slide Parameter 1.
     * @return A new instance of fragment BingoFragment.
     */
    public static BingoFragment newInstance(Slide slide, String imgUrl) {
        BingoFragment fragment = new BingoFragment();
        Bundle args = new Bundle();
        args.putParcelable(LessonActivity.ARG_NAME_SLIDE, slide);
        args.putString(LessonActivity.ARG_NAME_IMAGE_URL, imgUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSlide = getArguments().getParcelable(LessonActivity.ARG_NAME_SLIDE);
            mImageUrl = getArguments().getString(LessonActivity.ARG_NAME_IMAGE_URL);
            mQueue = new LinkedList<>(mSlide.MediaList);
            targetWord = mQueue.pop();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout =  inflater.inflate(R.layout.fragment_bingo, container, false);
        mLayout = layout;

        txtTarget = (TextView)layout.findViewById(R.id.bingo_txtTarget);
        imgTarget = (ImageView)layout.findViewById(R.id.bingo_image);

        //shuffle list
        long seed = System.nanoTime();
        Collections.shuffle(mSlide.MediaList, new Random(seed));

        ImageView speaker = (ImageView)layout.findViewById(R.id.bingo_speaker);
        speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentManager.playAudio(getActivity(), targetWord.English);
            }
        });


        FrameLayout frame1 = (FrameLayout)layout.findViewById(R.id.bingo_frame1);
        Button btn1 = (Button)layout.findViewById(R.id.bingo_button1);
        SlideMedia word = mSlide.MediaList.get(0);
        setupButton(frame1, btn1, word);

        FrameLayout frame2 = (FrameLayout)layout.findViewById(R.id.bingo_frame2);
        Button btn2 = (Button)layout.findViewById(R.id.bingo_button2);
        SlideMedia word2 = mSlide.MediaList.get(1);
        setupButton(frame2, btn2, word2);

        FrameLayout frame3 = (FrameLayout)layout.findViewById(R.id.bingo_frame3);
        Button btn3 = (Button)layout.findViewById(R.id.bingo_button3);
        SlideMedia word3 = mSlide.MediaList.get(2);
        setupButton(frame3, btn3, word3);

        FrameLayout frame4 = (FrameLayout)layout.findViewById(R.id.bingo_frame4);
        Button btn4 = (Button)layout.findViewById(R.id.bingo_button4);
        SlideMedia word4 = mSlide.MediaList.get(3);
        setupButton(frame4, btn4, word4);

        FrameLayout frame5 = (FrameLayout)layout.findViewById(R.id.bingo_frame5);
        Button btn5 = (Button)layout.findViewById(R.id.bingo_button5);
        SlideMedia word5 = mSlide.MediaList.get(4);
        setupButton(frame5, btn5, word5);

        FrameLayout frame6 = (FrameLayout)layout.findViewById(R.id.bingo_frame6);
        Button btn6 = (Button)layout.findViewById(R.id.bingo_button6);
        SlideMedia word6 = mSlide.MediaList.get(5);
        setupButton(frame6, btn6, word6);

        FrameLayout frame7 = (FrameLayout)layout.findViewById(R.id.bingo_frame7);
        Button btn7 = (Button)layout.findViewById(R.id.bingo_button7);
        SlideMedia word7 = mSlide.MediaList.get(6);
        setupButton(frame7, btn7, word7);

        FrameLayout frame8 = (FrameLayout)layout.findViewById(R.id.bingo_frame8);
        Button btn8 = (Button)layout.findViewById(R.id.bingo_button8);
        SlideMedia word8 = mSlide.MediaList.get(7);
        setupButton(frame8, btn8, word8);


        FrameLayout frame9 = (FrameLayout)layout.findViewById(R.id.bingo_frame9);
        Button btn9 = (Button)layout.findViewById(R.id.bingo_button9);
        SlideMedia word9 = mSlide.MediaList.get(8);
        setupButton(frame9, btn9, word9);

        /*
        FrameLayout frame10 = (FrameLayout)layout.findViewById(R.id.bingo_frame10);
        Button btn10 = (Button)layout.findViewById(R.id.bingo_button10);
        SlideMedia word10 = mSlide.MediaList.get(9);
        setupButton(frame10, btn10, word10);

        FrameLayout frame11 = (FrameLayout)layout.findViewById(R.id.bingo_frame11);
        Button btn11 = (Button)layout.findViewById(R.id.bingo_button11);
        SlideMedia word11 = mSlide.MediaList.get(10);
        setupButton(frame11, btn11, word11);

        FrameLayout frame12 = (FrameLayout)layout.findViewById(R.id.bingo_frame12);
        Button btn12 = (Button)layout.findViewById(R.id.bingo_button12);
        SlideMedia word12 = mSlide.MediaList.get(11);
        setupButton(frame12, btn12, word12);
        */

        return layout;
    }

    private void setupButton(final FrameLayout frame, final Button btn,
                             final SlideMedia word) {
        btn.setText(word.English);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClick(frame, btn, word);
            }
        });
    }

    private void onButtonClick(FrameLayout frame, Button btn, SlideMedia word) {
        if (selectedFrame != null) {
            selectedFrame.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null));
        }

        selectedFrame = frame;

        if (word == targetWord) {
            if (mQueue.isEmpty()) {
                mListener.onSlideCompleted(mSlide.Id, errorCount);
            } else {
                frame.setBackgroundColor(ResourcesCompat.getColor(mLayout.getResources(), R.color.colorBorderCorrect, null));
                removeFrame(frame);
                targetWord = mQueue.pop();
                txtTarget.setText(targetWord.Thai);
                if (targetWord.ImageFileName.length() > 0) {
                    ContentManager.fetchImage(getActivity(), imgTarget,
                            targetWord.ImageFileName, mImageUrl );
                } else {
                    imgTarget.setImageResource(android.R.color.transparent);
                }
                ContentManager.playAudio(getActivity(), targetWord.English);

            }
        } else {
            errorCount++;
            frame.setBackgroundColor(ResourcesCompat.getColor(mLayout.getResources(), R.color.colorBorderWrong, null));
            ContentManager.playAudio(getActivity(), targetWord.English);
        }
    }


    @Override
    public void onResume() {
        super.onResume();

        txtTarget.setText(targetWord.Thai);
        if (targetWord.ImageFileName.length() > 0) {
            ContentManager.fetchImage(getActivity(), imgTarget,
                    targetWord.ImageFileName, mImageUrl );
        } else {
            imgTarget.setImageResource(android.R.color.transparent);
        }
        ContentManager.playAudio(getActivity(), targetWord.English);
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
