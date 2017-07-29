package everyday.sukhajata.com.everydayenglish;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import everyday.sukhajata.com.everydayenglish.interfaces.SlideCompletedListener;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ConversationFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ConversationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ConversationFragment extends Fragment {


    private SlideCompletedListener mListener;

    public ConversationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ConversationFragment.
     */
    public static ConversationFragment newInstance(String param1, String param2) {
        ConversationFragment fragment = new ConversationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_conversation, container, false);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof SlideCompletedListener) {
            mListener = (SlideCompletedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


}
