package everyday.sukhajata.com.everydayenglish;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import everyday.sukhajata.com.everydayenglish.interfaces.SlideCompletedListener;
import everyday.sukhajata.com.everydayenglish.model.Slide;

import java.util.List;

import static everyday.sukhajata.com.everydayenglish.LessonActivity.ARG_NAME_SLIDE;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link SlideCompletedListener}
 * interface.
 */
public class TeachingFragment extends Fragment {

    private Slide mSlide;
    private SlideCompletedListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TeachingFragment() {
    }

    @SuppressWarnings("unused")
    public static TeachingFragment newInstance(Slide slide) {
        TeachingFragment fragment = new TeachingFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_NAME_SLIDE, slide);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mSlide = getArguments().getParcelable(ARG_NAME_SLIDE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_teaching_list, container, false);

        // Set the adapter
        Context context = view.getContext();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.teaching_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        SlideMediaRecyclerViewAdapter adapter =
                new SlideMediaRecyclerViewAdapter(getActivity(), mSlide.MediaList, mListener);
        recyclerView.setAdapter(adapter);

        TextView content = (TextView)view.findViewById(R.id.teaching_txtContent);
        content.setText(mSlide.ContentThai);

        //next button
        Button button = (Button) view.findViewById(R.id.teaching_btn_continue);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    + " must implement OnLessonFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

}
