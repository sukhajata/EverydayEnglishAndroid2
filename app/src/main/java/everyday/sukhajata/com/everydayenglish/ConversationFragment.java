package everyday.sukhajata.com.everydayenglish;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
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
import android.widget.RelativeLayout;
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

        if (mSlide.ImageFileName != null && mSlide.ImageFileName.length() > 0) {
            ImageView imageView = (ImageView)view.findViewById(R.id.conversation_image);
            ContentManager.fetchImage(getActivity(), imageView, mSlide.ImageFileName, mImageUrl);
        }

        LinearLayout textContainer = (LinearLayout)view.findViewById(R.id.conversation_textContainer);

        String speech1 = mSlide.MediaList.get(0).English + "\n" + mSlide.MediaList.get(0).Thai;
        TextView txt = (TextView)view.findViewById(R.id.conversation_text1);
        txt.setText(speech1);
        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentManager.playAudio(getActivity(), mSlide.MediaList.get(0).English);
            }
        });

        String speech2 = mSlide.MediaList.get(1).English + "\n" + mSlide.MediaList.get(1).Thai;
        TextView txt2 = (TextView)view.findViewById(R.id.conversation_text2);
        txt2.setText(speech2);
        txt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentManager.playAudio(getActivity(), mSlide.MediaList.get(1).English);
            }
        });

        String speech3 = mSlide.MediaList.get(2).English + "\n" + mSlide.MediaList.get(2).Thai;
        TextView txt3 = (TextView)view.findViewById(R.id.conversation_text3);
        txt3.setText(speech3);
        txt3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentManager.playAudio(getActivity(), mSlide.MediaList.get(2).English);
            }
        });


        if (mSlide.MediaList.size() > 3) {
            String speech4 = mSlide.MediaList.get(3).English + "\n" + mSlide.MediaList.get(3).Thai;
            TextView txt4 = (TextView)view.findViewById(R.id.conversation_text4);
            txt4.setText(speech4);
            txt4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ContentManager.playAudio(getActivity(), mSlide.MediaList.get(3).English);
                }
            });

        } else {
            RelativeLayout relativeLayout = (RelativeLayout)view.findViewById(R.id.conversation_speech4);
            textContainer.removeView(relativeLayout);
        }

        if (mSlide.MediaList.size() > 4) {
            String speech5 = mSlide.MediaList.get(4).English + "\n" + mSlide.MediaList.get(4).Thai;
            TextView txt5 = (TextView)view.findViewById(R.id.conversation_text5);
            txt5.setText(speech5);
            txt5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ContentManager.playAudio(getActivity(), mSlide.MediaList.get(4).English);
                }
            });

        } else {
            RelativeLayout relativeLayout = (RelativeLayout)view.findViewById(R.id.conversation_speech5);
            textContainer.removeView(relativeLayout);
        }

        if (mSlide.MediaList.size() > 5) {
            String speech6 = mSlide.MediaList.get(5).English + "\n" + mSlide.MediaList.get(5).Thai;
            TextView txt6 = (TextView)view.findViewById(R.id.conversation_text6);
            txt6.setText(speech6);
            txt6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ContentManager.playAudio(getActivity(), mSlide.MediaList.get(5).English);
                }
            });

        } else {
            RelativeLayout relativeLayout = (RelativeLayout)view.findViewById(R.id.conversation_speech6);
            textContainer.removeView(relativeLayout);
        }

        if (mSlide.MediaList.size() > 6) {
            String speech7 = mSlide.MediaList.get(6).English + "\n" + mSlide.MediaList.get(6).Thai;
            TextView txt7 = (TextView)view.findViewById(R.id.conversation_text7);
            txt7.setText(speech7);
            txt7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ContentManager.playAudio(getActivity(), mSlide.MediaList.get(6).English);
                }
            });

        } else {
            RelativeLayout relativeLayout = (RelativeLayout)view.findViewById(R.id.conversation_speech7);
            textContainer.removeView(relativeLayout);
        }

        if (mSlide.MediaList.size() > 7) {
            String speech8 = mSlide.MediaList.get(7).English + "\n" + mSlide.MediaList.get(7).Thai;
            TextView txt8 = (TextView)view.findViewById(R.id.conversation_text8);
            txt8.setText(speech8);
            txt8.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ContentManager.playAudio(getActivity(), mSlide.MediaList.get(7).English);
                }
            });

        } else {
            RelativeLayout relativeLayout = (RelativeLayout)view.findViewById(R.id.conversation_speech8);
            textContainer.removeView(relativeLayout);
        }


        Button btnNext = (Button)view.findViewById(R.id.conversation_btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onSlideCompleted(mSlide.Id, 0);
            }
        });


        /*
        boolean left = true;
        LinearLayout textContainer = (LinearLayout)view.findViewById(R.id.conversation_textContainer);

        for (final SlideMedia slideMedia : mSlide.MediaList) {
            RelativeLayout.LayoutParams relativeLayoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            relativeLayoutParams.setMargins(0, 20, 0 , 0);

            RelativeLayout relativeLayout = new RelativeLayout(getActivity());
            relativeLayout.setLayoutParams(relativeLayoutParams);
            //relativeLayout.setOrientation(LinearLayout.HORIZONTAL);

            //text
            RelativeLayout.LayoutParams textViewLayoutParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            textViewLayoutParams.setMargins(0,10,0,0);
            TextView txt = new TextView(getActivity());

            txt.setPadding(10,10,10,10);
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

            //arrow
            RelativeLayout.LayoutParams arrowParams = new RelativeLayout.LayoutParams(30,30);
            ImageView arrow = new ImageView(getActivity());
            if (left) {
                arrow.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.arrow_bg2, null));
                arrowParams.setMargins(0,10,10-20,0);
            } else {
                arrowParams.setMargins(10-20,10,0,0);
                arrow.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.arrow_bg1, null));
            }


            //avatar
            RelativeLayout.LayoutParams avatarParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            ImageView avatar = new ImageView(getActivity());

            if (left) {
                avatar.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_giraffe, null));
            } else {
                avatar.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.mipmap.ic_panda, null));
            }

            if (left){
                arrowParams.addRule(RelativeLayout.RIGHT_OF, avatar.getId());
                textViewLayoutParams.addRule(RelativeLayout.RIGHT_OF, arrow.getId());

                avatar.setLayoutParams(avatarParams);
                arrow.setLayoutParams(arrowParams);
                txt.setLayoutParams(textViewLayoutParams);

                relativeLayout.addView(avatar);
                relativeLayout.addView(arrow);
                relativeLayout.addView(txt);

            } else {
                arrowParams.addRule(RelativeLayout.LEFT_OF, txt.getId());
                avatarParams.addRule(RelativeLayout.LEFT_OF, arrow.getId());

                avatar.setLayoutParams(avatarParams);
                arrow.setLayoutParams(arrowParams);
                txt.setLayoutParams(textViewLayoutParams);

                relativeLayout.addView(txt);
                relativeLayout.addView(arrow);
                relativeLayout.addView(avatar);

            }
            textContainer.addView(relativeLayout);

            left = !left;
        }
        */


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
