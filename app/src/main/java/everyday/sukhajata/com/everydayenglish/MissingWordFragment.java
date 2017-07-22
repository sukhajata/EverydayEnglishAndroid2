package everyday.sukhajata.com.everydayenglish;


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
import android.widget.Toast;

import java.util.Collections;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import everyday.sukhajata.com.everydayenglish.interfaces.SlideCompletedListener;
import everyday.sukhajata.com.everydayenglish.model.Slide;
import everyday.sukhajata.com.everydayenglish.model.SlideMedia;
import everyday.sukhajata.com.everydayenglish.model.SlideMedia;
import everyday.sukhajata.com.everydayenglish.utility.ContentManager;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MissingWordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MissingWordFragment extends Fragment {

    private Slide mSlide;
    private SlideMedia mTarget;
    private SlideCompletedListener mListener;
    private int errorCount;
    private String mSentence;
    private String mImageUrl;

    public MissingWordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param slide Parameter 1.
     * @return A new instance of fragment MissingWordFragment.
     */
    public static MissingWordFragment newInstance(Slide slide, String imageUrl) {
        MissingWordFragment fragment = new MissingWordFragment();
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
            mSlide = getArguments().getParcelable(LessonActivity.ARG_NAME_SLIDE);
            mImageUrl = getArguments().getString(LessonActivity.ARG_NAME_IMAGE_URL);

            for(SlideMedia slideMedia : mSlide.MediaList) {
                if (slideMedia.IsTarget) {
                    mTarget = slideMedia;
                    break;
                }
            }

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_missing_word, container, false);


        TextView txtContent = (TextView)layout.findViewById(R.id.missingWord_textContent);
        //remove answer
        /*
        Matcher m = Pattern.compile("(.+)(\\(.+\\))").matcher(mSlide.Content);
        if (m.matches()) {
            String word = m.group(2);
            word = word.substring()
        }
        */


        if (mSlide.ImageFileName != null) {
            ImageView img = (ImageView)layout.findViewById(R.id.missingWord_image);
            ContentManager.fetchImage(getActivity(), img, mSlide.ImageFileName,
                    mImageUrl);
        }

        final String answer = mSlide.Content.substring(mSlide.Content.indexOf('(') + 1, mSlide.Content.lastIndexOf(')'));
        //mTarget = answer;
        final String content = mSlide.Content.substring(0, mSlide.Content.indexOf('('));
        txtContent.setText(content);
        final String sentence = content.substring(0, content.indexOf('_')) +
                answer + content.substring(content.lastIndexOf('_') + 1);
        mSentence = sentence;
        txtContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentManager.playAudio(sentence, null);
            }
        });

        ImageView speaker = (ImageView)layout.findViewById(R.id.missingWord_speaker);
        speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentManager.playAudio(sentence, null);
            }
        });

        TextView txtContentThai = (TextView)layout.findViewById(R.id.missingWord_textContentThai);
        txtContentThai.setText(mSlide.ContentThai);

        //shuffle list
        long seed = System.nanoTime();
        Collections.shuffle(mSlide.MediaList, new Random(seed));

        Button button1 = (Button)layout.findViewById(R.id.missingWord_buttonChoice1);
        FrameLayout frame1 = (FrameLayout)layout.findViewById(R.id.missingWord_frame1);
        setupTextButton(button1, frame1, mSlide.MediaList.get(0));

        Button button2 = (Button)layout.findViewById(R.id.missingWord_buttonChoice2);
        FrameLayout frame2 = (FrameLayout)layout.findViewById(R.id.missingWord_frame2);
        setupTextButton(button2, frame2, mSlide.MediaList.get(1));

        Button button3 = (Button)layout.findViewById(R.id.missingWord_buttonChoice3);
        FrameLayout frame3 = (FrameLayout)layout.findViewById(R.id.missingWord_frame3);
        setupTextButton(button3, frame3, mSlide.MediaList.get(2));

        Button button4 = (Button)layout.findViewById(R.id.missingWord_buttonChoice4);
        FrameLayout frame4 = (FrameLayout)layout.findViewById(R.id.missingWord_frame4);
        setupTextButton(button4, frame4, mSlide.MediaList.get(3));


        return layout;

    }

    @Override
    public void onResume() {
        super.onResume();

        ContentManager.playAudio(mSentence, null);
    }

    private void setupTextButton (Button button, final FrameLayout frame,
                                  final SlideMedia word) {
        button.setText(word.English.toLowerCase());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonClicked(frame, word);
            }
        });
    }

    private void onButtonClicked(FrameLayout frame, SlideMedia word) {
        if(word == mTarget) {
            frame.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorBorderCorrect, null));
            mListener.onSlideCompleted(mSlide.Id, errorCount);
        } else {
            errorCount++;
            frame.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorBorderWrong, null));
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SlideCompletedListener) {
            mListener = (SlideCompletedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnLessonFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
