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

import java.util.ArrayList;

import everyday.sukhajata.com.everydayenglish.dummy.DummyContent;
import everyday.sukhajata.com.everydayenglish.dummy.DummyContent.DummyItem;
import everyday.sukhajata.com.everydayenglish.model.Lesson;
import everyday.sukhajata.com.everydayenglish.utility.EverydayLanguageDbHelper;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnLessonFragmentInteractionListener}
 * interface.
 */
public class LessonFragment extends Fragment {

    public static final String ARG_USER_ID = "UserId";
    public static final String ARG_MODULE_ID = "ModuleId";

    private int mUserId;
    private int mModuleId;
    private OnLessonFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public LessonFragment() {
    }

    @SuppressWarnings("unused")
    public static LessonFragment newInstance(int userId, int moduleId) {
        LessonFragment fragment = new LessonFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_USER_ID, userId);
        args.putInt(ARG_MODULE_ID, moduleId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mUserId = getArguments().getInt(ARG_USER_ID);
            mModuleId = getArguments().getInt(ARG_MODULE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lesson_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            ArrayList<Lesson> lessons = EverydayLanguageDbHelper
                                        .getInstance(getActivity())
                                        .getLessonsForUser(mModuleId, mUserId);
            RecyclerView recyclerView = (RecyclerView) view;
            LinearLayoutManager layoutManager = new LinearLayoutManager(context);
            layoutManager.setStackFromEnd(true);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(new MyLessonRecyclerViewAdapter(getActivity(), mUserId, mModuleId, lessons, mListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnLessonFragmentInteractionListener) {
            mListener = (OnLessonFragmentInteractionListener) context;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnLessonFragmentInteractionListener {

        void onListFragmentInteraction(Lesson item);
    }
}
