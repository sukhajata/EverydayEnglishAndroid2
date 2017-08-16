package everyday.sukhajata.com.everydayenglish;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.toolbox.StringRequest;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnTotalsFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TotalsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TotalsFragment extends Fragment {

    public static final String ARG_NAME_CORRECT = "Correct";
    public static final String ARG_NAME_ERRORS = "Errors";
    public static final String ARG_NAME_WORD_TOTAL = "WordTotal";
    public static final String ARG_NAME_PERCENTAGE = "Percentage";

    private int mCorrect;
    private int mErrors;
    private int mWordTotal;
    private double mPercentage;

    private OnTotalsFragmentInteractionListener mListener;

    public TotalsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param wordTotal Parameter 1.
     * @param percentage Parameter 2.
     * @return A new instance of fragment TotalsFragment.
     */
    public static TotalsFragment newInstance(int correct, int errors, int wordTotal, double percentage){
        TotalsFragment fragment = new TotalsFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_NAME_CORRECT, correct);
        args.putInt(ARG_NAME_ERRORS, errors);
        args.putInt(ARG_NAME_WORD_TOTAL, wordTotal);
        args.putDouble(ARG_NAME_PERCENTAGE, percentage);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCorrect = getArguments().getInt(ARG_NAME_CORRECT);
            mErrors = getArguments().getInt(ARG_NAME_ERRORS);
            mWordTotal = getArguments().getInt(ARG_NAME_WORD_TOTAL);
            mPercentage = getArguments().getDouble(ARG_NAME_PERCENTAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        RelativeLayout view =  (RelativeLayout)inflater.inflate(R.layout.fragment_totals, container, false);

        TextView txtCorrect = (TextView)view.findViewById(R.id.totals_txtCorrect);
        txtCorrect.setText(String.valueOf(mCorrect));

        TextView txtErrors = (TextView)view.findViewById(R.id.totals_txtErrors);
        txtErrors.setText(String.valueOf(mErrors));

        TextView txtWordTotal = (TextView)view.findViewById(R.id.totals_txtTotalWords);
        if (mWordTotal > 0) {
            txtWordTotal.setText(String.valueOf(mWordTotal));
        } else {
            TextView lblWordTotal = (TextView)view.findViewById(R.id.totals_lblWordTotal);
            lblWordTotal.setVisibility(View.GONE);
            txtWordTotal.setVisibility(View.GONE);
        }

        TextView txtPercentage = (TextView)view.findViewById(R.id.totals_txtPercentage);
        if (mPercentage > 0) {
            txtPercentage.setText(String.valueOf(mPercentage));
        } else {
            TextView lblPercentage = (TextView)view.findViewById(R.id.totals_lblPercentage);
            lblPercentage.setVisibility(View.GONE);
            txtPercentage.setVisibility(View.GONE);
        }

        Button btnNext = (Button)view.findViewById(R.id.totals_btnContinue);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFragmentInteraction();
            }
        });

        return view;
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTotalsFragmentInteractionListener) {
            mListener = (OnTotalsFragmentInteractionListener) context;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnTotalsFragmentInteractionListener {
        void onFragmentInteraction();
    }
}
