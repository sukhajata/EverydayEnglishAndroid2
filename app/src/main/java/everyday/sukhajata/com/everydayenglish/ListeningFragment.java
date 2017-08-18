package everyday.sukhajata.com.everydayenglish;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.Collections;
import java.util.Random;

import everyday.sukhajata.com.everydayenglish.interfaces.SlideCompletedListener;
import everyday.sukhajata.com.everydayenglish.model.Slide;
import everyday.sukhajata.com.everydayenglish.model.SlideMedia;
import everyday.sukhajata.com.everydayenglish.utility.ContentManager;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SlideCompletedListener} interface
 * to handle interaction events.
 * Use the {@link ListeningFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ListeningFragment extends Fragment {

    private Slide mSlide;
    private String mImageUrl;
    private SlideMedia mTarget;
    private FrameLayout mSelectedTextFrame;
    private View mLayout;
    private int errorCount;

    private SlideCompletedListener mListener;

    public ListeningFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param slide Parameter 1.
     * @param imageUrl Parameter 2.
     * @return A new instance of fragment ListeningFragment.
     */
    public static ListeningFragment newInstance(Slide slide, String imageUrl) {
        ListeningFragment fragment = new ListeningFragment();
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

            for (SlideMedia media : mSlide.MediaList) {
                if (media.IsTarget) {
                    mTarget = media;
                    break;
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_listening, container, false);
        mLayout = view;

        ImageView speaker = (ImageView)view.findViewById(R.id.listening_speaker);
        speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MyApplication)getActivity().getApplication()).playAudio(mTarget.English);
            }
        });

        ImageView img = (ImageView)view.findViewById(R.id.listening_image);;
        if (mTarget.ImageFileName.length() > 0) {
            ContentManager.fetchImage(getActivity(), img, mTarget.ImageFileName,
                    mImageUrl);
        }
        //shuffle list
        long seed = System.nanoTime();
        Collections.shuffle(mSlide.MediaList, new Random(seed));

        Button btn1 = (Button) view.findViewById(R.id.listening_txtChoice1);
        FrameLayout frame1 = (FrameLayout)view.findViewById(R.id.listening_frame1);
        setupTextFrame(frame1, btn1, mSlide.MediaList.get(0));

        Button btn2 = (Button) view.findViewById(R.id.listening_txtChoice2);
        FrameLayout frame2 = (FrameLayout)view.findViewById(R.id.listening_frame2);
        setupTextFrame(frame2, btn2, mSlide.MediaList.get(1));

        Button btn3 = (Button) view.findViewById(R.id.listening_txtChoice3);
        FrameLayout frame3 = (FrameLayout)view.findViewById(R.id.listening_frame3);
        setupTextFrame(frame3, btn3, mSlide.MediaList.get(2));

        Button btn4 = (Button) view.findViewById(R.id.listening_txtChoice4);
        FrameLayout frame4 = (FrameLayout)view.findViewById(R.id.listening_frame4);
        setupTextFrame(frame4, btn4, mSlide.MediaList.get(3));


        return view;
    }

    private void setupTextFrame(final FrameLayout frame, final Button button,
                                final SlideMedia media) {
        button.setText(media.English);
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

        ((MyApplication)getActivity().getApplication()).playAudio(mTarget.English);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SlideCompletedListener) {
            mListener = (SlideCompletedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement SlideCompletedListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}
