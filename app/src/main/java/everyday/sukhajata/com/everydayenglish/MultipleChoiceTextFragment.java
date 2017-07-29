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
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.Random;

import everyday.sukhajata.com.everydayenglish.interfaces.SlideCompletedListener;
import everyday.sukhajata.com.everydayenglish.model.Lesson;
import everyday.sukhajata.com.everydayenglish.model.Slide;
import everyday.sukhajata.com.everydayenglish.model.SlideMedia;
import everyday.sukhajata.com.everydayenglish.utility.ContentManager;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MultipleChoiceTextFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MultipleChoiceTextFragment extends Fragment {

    private Slide mSlide;
    private String mImageUrl;
    private SlideMedia mTarget;
    private FrameLayout mSelectedTextFrame;
    private SlideCompletedListener mListener;
    private View mLayout;
    private int errorCount;


    public MultipleChoiceTextFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param slide Parameter 1.
     * @return A new instance of fragment MultipleChoiceTextFragment.
     */
    public static MultipleChoiceTextFragment newInstance(Slide slide, String imgUrl) {
        MultipleChoiceTextFragment fragment = new MultipleChoiceTextFragment();
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

            for (SlideMedia media : mSlide.MediaList) {
                if (media.IsTarget) {
                    mTarget = media;
                    break;
                }
            }

            if (mTarget == null) {

            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_multiple_choice_text, container, false);
        mLayout = view;


        TextView txtContent = ((TextView)view.findViewById(R.id.multipleChoiceText_txtContent));
        txtContent.setText(mTarget.English);
        txtContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentManager.playAudio(getActivity(), mTarget.English);
            }
        });

        ImageView speaker = (ImageView)view.findViewById(R.id.multipleChoiceText_speaker);
        speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentManager.playAudio(getActivity(), mTarget.English);
            }
        });


        ImageView img = (ImageView)view.findViewById(R.id.multipleChoiceText_image);;
        if (mTarget.ImageFileName.length() > 0) {
            ContentManager.fetchImage(getActivity(), img, mTarget.ImageFileName,
                    mImageUrl);
        }
        //shuffle list
        long seed = System.nanoTime();
        Collections.shuffle(mSlide.MediaList, new Random(seed));

        Button btn1 = (Button) view.findViewById(R.id.multipleChoiceText_txtChoice1);
        FrameLayout frame1 = (FrameLayout)view.findViewById(R.id.multipleChoiceText_frame1);
        setupTextFrame(frame1, btn1, mSlide.MediaList.get(0));

        Button btn2 = (Button) view.findViewById(R.id.multipleChoiceText_txtChoice2);
        FrameLayout frame2 = (FrameLayout)view.findViewById(R.id.multipleChoiceText_frame2);
        setupTextFrame(frame2, btn2, mSlide.MediaList.get(1));

        Button btn3 = (Button) view.findViewById(R.id.multipleChoiceText_txtChoice3);
        FrameLayout frame3 = (FrameLayout)view.findViewById(R.id.multipleChoiceText_frame3);
        setupTextFrame(frame3, btn3, mSlide.MediaList.get(2));

        Button btn4 = (Button) view.findViewById(R.id.multipleChoiceText_txtChoice4);
        FrameLayout frame4 = (FrameLayout)view.findViewById(R.id.multipleChoiceText_frame4);
        setupTextFrame(frame4, btn4, mSlide.MediaList.get(3));


        return view;
    }

    private void setupTextFrame(final FrameLayout frame, final Button button,
                                final SlideMedia media) {
        button.setText(media.Thai);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (media == mTarget) {
                    frame.setBackgroundColor(ResourcesCompat.getColor(mLayout.getResources(), R.color.colorBorderCorrect, null));
                    mListener.onSlideCompleted(mSlide.Id, errorCount);

                } else {
                    errorCount++;
                    frame.setBackgroundColor(ResourcesCompat.getColor(mLayout.getResources(), R.color.colorBorderWrong, null));

                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        ContentManager.playAudio(getActivity(), mTarget.English);
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
