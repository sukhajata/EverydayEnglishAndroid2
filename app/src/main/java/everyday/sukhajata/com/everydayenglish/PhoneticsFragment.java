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
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Collections;

import everyday.sukhajata.com.everydayenglish.interfaces.AudioFinishedCallback;
import everyday.sukhajata.com.everydayenglish.interfaces.SlideCompletedListener;
import everyday.sukhajata.com.everydayenglish.model.Phonetic;
import everyday.sukhajata.com.everydayenglish.model.Slide;
import everyday.sukhajata.com.everydayenglish.model.SlideMedia;
import everyday.sukhajata.com.everydayenglish.utility.ContentManager;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PhoneticsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PhoneticsFragment extends Fragment {
    private Slide mSlide;

    private View mLayout;
    private FrameLayout selectedFrame;
    private TextView txtNotes;

    private SlideCompletedListener mListener;

    public PhoneticsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param slide Parameter 1.
     * @return A new instance of fragment PhoneticsFragment.
     */
    public static PhoneticsFragment newInstance(Slide slide) {
        PhoneticsFragment fragment = new PhoneticsFragment();
        Bundle args = new Bundle();
        args.putParcelable(LessonActivity.ARG_NAME_SLIDE, slide);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSlide = getArguments().getParcelable(LessonActivity.ARG_NAME_SLIDE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.fragment_phonetics, container, false);
        final SlideMedia slideMedia = mSlide.MediaList.get(0);

        TextView txtWord = ((TextView)layout.findViewById(R.id.phonetics_textTarget));
        txtWord.setText(slideMedia.English);
        txtWord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MyApplication)getActivity().getApplication()).playAudio(slideMedia.English);
            }
        });

        ImageView speaker = (ImageView)layout.findViewById(R.id.phonetics_speaker);
        speaker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MyApplication)getActivity().getApplication()).playAudio(slideMedia.English);
            }
        });

        //load audio files
       // ContentManager.loadAudioFiles(getActivity(), mSlide.MediaList.get(0).PhoneticList);

        LinearLayout wordLayout = (LinearLayout) layout.findViewById(R.id.phonetics_layoutWord);

        Collections.sort(slideMedia.PhoneticList);
        for (final Phonetic phonetic : slideMedia.PhoneticList) {
            final FrameLayout frame = new FrameLayout(getActivity());
            frame.setPadding(5,5,5,5);
            frame.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null));

            LinearLayout innerLayout = new LinearLayout(getActivity());
            innerLayout.setOrientation(LinearLayout.VERTICAL);

            txtNotes = (TextView)layout.findViewById(R.id.phonetics_txtNotes);


            final Button btn = new Button(getActivity());
            btn.setText(phonetic.Letters);
            btn.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryLight, null));
            btn.setPadding(10,10,10,10);
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnPhoneticClick(frame, phonetic);

                }
            });
            innerLayout.addView(btn);

            final Button btn2 = new Button(getActivity());
            btn2.setText(phonetic.Thai);
            btn2.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorLabelBackground, null));
            btn2.setPadding(10,10,10,10);
            btn2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    btnPhoneticClick(frame, phonetic);

                }
            });
            innerLayout.addView(btn2);

            frame.addView(innerLayout);
            wordLayout.addView(frame);
        }

        Button btnNext = (Button)layout.findViewById(R.id.phonetics_btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onSlideCompleted(mSlide.Id, 0);
            }
        });


        return layout;
    }

    private void btnPhoneticClick(FrameLayout frame, Phonetic phonetic) {


        if (phonetic.Notes.length() > 0) {
            txtNotes.setText(phonetic.Notes);
        } else {
            txtNotes.setText("");
        }
        if (selectedFrame != null) {
            selectedFrame.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorPrimaryDark, null));
        }
        selectedFrame = frame;
        frame.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
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
