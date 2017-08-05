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
import android.widget.LinearLayout;
import android.widget.TextView;

import everyday.sukhajata.com.everydayenglish.interfaces.SlideCompletedListener;
import everyday.sukhajata.com.everydayenglish.model.Slide;
import everyday.sukhajata.com.everydayenglish.model.SlideMedia;
import everyday.sukhajata.com.everydayenglish.utility.ContentManager;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SlideCompletedListener} interface
 * to handle interaction events.
 * Use the {@link ConversationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConversationFragment extends Fragment {

    private Slide mSlide;
    private String mImageUrl;
    private View mLayout;

    private SlideCompletedListener mListener;

    public ConversationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param slide Parameter 1.
     * @param imageUrl Parameter 2.
     * @return A new instance of fragment ConversationFragment.
     */
    public static ConversationFragment newInstance(Slide slide, String imageUrl) {
        ConversationFragment fragment = new ConversationFragment();
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
            mImageUrl = getArguments().getString(LessonActivity.ARG_NAME_IMAGE_URL);
            mSlide = getArguments().getParcelable(LessonActivity.ARG_NAME_SLIDE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_conversation, container, false);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 20, 0 , 0);

        LinearLayout.LayoutParams textViewLayoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textViewLayoutParams.setMargins(0,10,0,0);


        LinearLayout.LayoutParams avatarParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        boolean left = true;
        LinearLayout root = (LinearLayout)view.findViewById(R.id.conversation_root);

        for (final SlideMedia slideMedia : mSlide.MediaList) {
            LinearLayout linearLayout = new LinearLayout(getActivity());
            linearLayout.setLayoutParams(layoutParams);

            TextView txt = new TextView(getActivity());
            txt.setLayoutParams(textViewLayoutParams);
            txt.setText(slideMedia.English + "\n" + slideMedia.Thai);
            if (left) {
                txt.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.rounded_corner1, null));
            } else {
                txt.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.rounded_corner, null));
            }
            txt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ContentManager.playAudio(getActivity(), slideMedia.English);
                }
            });

            LinearLayout.LayoutParams arrowParams = new LinearLayout.LayoutParams(30, 30);
            ImageView arrow = new ImageView(getActivity());
            if (left) {
                arrow.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.arrow_bg2, null));
                arrowParams.setMargins(0,6,15-30,0);
            } else {
                arrowParams.setMargins(15-30,6,0,0);
                arrow.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.arrow_bg1, null));
            }
            arrow.setLayoutParams(arrowParams);

            ImageView avatar = new ImageView(getActivity());
            avatar.setLayoutParams(avatarParams);
            if (left) {
                avatar.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_giraffe, null));
            } else {
                avatar.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_panda, null));
            }

            if (left){
                linearLayout.addView(avatar);
                linearLayout.addView(arrow);
                linearLayout.addView(txt);
            } else {
                linearLayout.addView(txt);
                linearLayout.addView(arrow);
                linearLayout.addView(avatar);
            }
            root.addView(linearLayout);

            left = !left;
        }

        Button btnNext = (Button)view.findViewById(R.id.conversation_btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onSlideCompleted(mSlide.Id, 0);
            }
        });

        return view;
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
