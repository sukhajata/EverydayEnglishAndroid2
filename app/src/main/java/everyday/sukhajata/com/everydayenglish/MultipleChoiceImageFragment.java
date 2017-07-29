package everyday.sukhajata.com.everydayenglish;


import android.content.Context;
import android.os.Bundle;
import android.support.percent.PercentRelativeLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
 * Use the {@link MultipleChoiceImageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MultipleChoiceImageFragment extends Fragment {


    private Slide mPage;
    private String mImageUrl;
    private FrameLayout mSelectedImageFrame;
    private SlideMedia mSelectedImageMedia;
    private SlideMedia mTarget;
    private int errorCount;

    private SlideCompletedListener mListener;


    public MultipleChoiceImageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param page Parameter 1.
     * @param imageUrl Parameter 2.
     * @return A new instance of fragment MultipleChoiceImageFragment.
     */
    public static MultipleChoiceImageFragment newInstance(
            Slide page, String imageUrl) {
        MultipleChoiceImageFragment fragment = new MultipleChoiceImageFragment();
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

            for(SlideMedia slideMedia : mPage.MediaList) {
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
        PercentRelativeLayout layout =
                (PercentRelativeLayout)inflater.inflate(R.layout.fragment_multiple_choice_image, container, false);


        TextView txtTarget = (TextView)layout.findViewById(R.id.multipleChoiceImage_textTarget);
        txtTarget.setText(mTarget.English);
        txtTarget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentManager.playAudio(getActivity(), mTarget.English);
            }
        });

        ImageView speaker = (ImageView)layout.findViewById(R.id.multipleChoiceImage_speaker);
        speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentManager.playAudio(getActivity(), mTarget.English);
            }
        });

        //shuffle list
        long seed = System.nanoTime();
        Collections.shuffle(mPage.MediaList, new Random(seed));

        FrameLayout frame1 = (FrameLayout)layout.findViewById(R.id.multipleChoiceImage_frame1);
        ImageView img1 = (ImageView)layout.findViewById(R.id.multipleChoiceImage_imageView1);
        setupImageView(img1, mPage.MediaList.get(0), frame1);

        FrameLayout frame2 = (FrameLayout)layout.findViewById(R.id.multipleChoiceImage_frame2);
        ImageView img2 = (ImageView)layout.findViewById(R.id.multipleChoiceImage_imageView2);
        setupImageView(img2, mPage.MediaList.get(1), frame2);

        FrameLayout frame3 = (FrameLayout)layout.findViewById(R.id.multipleChoiceImage_frame3);
        ImageView img3 = (ImageView)layout.findViewById(R.id.multipleChoiceImage_imageView3);
        setupImageView(img3, mPage.MediaList.get(2), frame3);


        //play audio

        return layout;
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

    private void onImageViewClicked(ImageView imageView, FrameLayout frame) {

        mSelectedImageFrame = frame;
        mSelectedImageMedia = (SlideMedia)imageView.getTag();


        if (mSelectedImageMedia.Id == mTarget.Id) {
            mListener.onSlideCompleted(mPage.Id, errorCount);
            mSelectedImageFrame.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorBorderCorrect, null));
        } else {
            errorCount++;
            mSelectedImageFrame.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorBorderWrong, null));
        }
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
