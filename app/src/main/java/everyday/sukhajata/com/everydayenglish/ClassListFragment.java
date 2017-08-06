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
import android.widget.EditText;

import java.util.ArrayList;

import everyday.sukhajata.com.everydayenglish.dummy.DummyContent;
import everyday.sukhajata.com.everydayenglish.dummy.DummyContent.DummyItem;
import everyday.sukhajata.com.everydayenglish.interfaces.DownloadCallback;
import everyday.sukhajata.com.everydayenglish.model.User;
import everyday.sukhajata.com.everydayenglish.utility.EverydayLanguageDbHelper;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link ClassListFragmentInteractionListener}
 * interface.
 */
public class ClassListFragment extends Fragment implements DownloadCallback{

    public static final String ARG_CLASS_LIST = "ClassList";

    private ClassListFragmentInteractionListener mListener;
    private ArrayList<User> mUsers;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ClassListFragment() {
    }

    public static ClassListFragment newInstance(ArrayList<User> users) {
        ClassListFragment fragment = new ClassListFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_CLASS_LIST, users);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
           mUsers = getArguments().getParcelableArrayList(ARG_CLASS_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_class_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new ClassListRecyclerViewAdapter(mUsers, mListener));
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ClassListFragmentInteractionListener) {
            mListener = (ClassListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ClassListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void onDownloadError(String error) {

    }

    public void onDownloadFinished(String code, String type) {

    }

    public void onDownloadResult(String code, String type, String[] args) {

    }

    public void onDownloadResult(String code, String type, Object result) {
        if (code == DownloadCallback.RESPONSE_OK && type == DownloadCallback.TYPE_CLASS) {
            ArrayList<User> users = (ArrayList<User>)result;

        }
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
    public interface ClassListFragmentInteractionListener {

        void onClassListFragmentInteraction();
    }
}
