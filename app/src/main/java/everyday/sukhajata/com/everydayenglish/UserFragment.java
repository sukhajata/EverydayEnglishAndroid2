package everyday.sukhajata.com.everydayenglish;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;

import java.util.ArrayList;

import everyday.sukhajata.com.everydayenglish.interfaces.OnUserFragmentInteractionListener;
import everyday.sukhajata.com.everydayenglish.utility.EverydayLanguageDbHelper;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnUserFragmentInteractionListener}
 * interface.
 */
public class UserFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final String ARG_USER_LIST = "user-list";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private ArrayList<String> mEmails;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public UserFragment() {
    }

    // TODO: Customize parameter initialization
    /*
    @SuppressWarnings("unused")
    public static UserFragment newInstance(int columnCount, ArrayList<String> emails) {
        UserFragment fragment = new UserFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        args.putStringArrayList(ARG_USER_LIST, emails);
        fragment.setArguments(args);
        return fragment;
    }
    */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //mEmails = EverydayLanguageDbHelper.getInstance(getActivity()).getUsers();
        /*
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
        */
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ScrollView view = (ScrollView) inflater.inflate(R.layout.fragment_user, container, false);

        // Set the adapter
        ListView listView = (ListView)view.findViewById(R.id.user_list);
        ArrayAdapter adapter = new ArrayAdapter(getActivity().getApplicationContext(),
                R.layout.fragment_user, mEmails);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String email =  (String)parent.getItemAtPosition(position);
                Intent intent = new Intent();
                intent.putExtra("email", email);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        });

        /*
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new UserRecyclerViewAdapter(mEmails, mListener));
        }*/
        return view;
    }





}
