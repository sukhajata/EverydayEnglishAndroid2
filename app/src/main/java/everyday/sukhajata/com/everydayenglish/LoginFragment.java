package everyday.sukhajata.com.everydayenglish;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import everyday.sukhajata.com.everydayenglish.interfaces.DownloadCallback;
import everyday.sukhajata.com.everydayenglish.interfaces.SlideCompletedListener;
import everyday.sukhajata.com.everydayenglish.model.User;
import everyday.sukhajata.com.everydayenglish.utility.ContentManager;
import everyday.sukhajata.com.everydayenglish.utility.EverydayLanguageDbHelper;


/**
 * Created by Tim on 3/03/2017.
 */

public class LoginFragment extends Fragment implements DownloadCallback {

    private ProgressDialog progressDialog;
    private View layout;
    private LoginFragmentInteractionListener mListener;

    public LoginFragment () {
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        //args.putBoolean(SetupActivity.ARG_WRONG_PASSWORD, wrongPassword);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    private void checkConnection() {
        ConnectivityManager cm =
                (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();


        if (!isConnected) {
            // 1. Instantiate an AlertDialog.Builder with its constructor
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

            // 2. Chain together various setter methods to set the dialog characteristics
            builder.setMessage(R.string.connect_to_network)
                    .setTitle(R.string.no_internet)
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            checkConnection();
                        }
                    });
            // 3. Get the AlertDialog from create()
            AlertDialog dialog = builder.create();
            dialog.show();
            //Toast.makeText(getActivity(), R.string.connect_to_network, Toast.LENGTH_LONG).show();
        } else {


        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        layout = (ScrollView)inflater.inflate(
                R.layout.fragment_login, container, false);

        final EditText password = (EditText)layout.findViewById(R.id.login_password);
        final Button btnSubmit = (Button)layout.findViewById(R.id.login_submit);

        progressDialog = new ProgressDialog(getActivity());
        Button btnFind = (Button)layout.findViewById(R.id.login_btnFind);
        final LoginFragment loginFragment = this;

        btnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnFind_click();
            }
        });

        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 2) {
                    btnSubmit.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = EverydayLanguageDbHelper
                        .getInstance(getActivity())
                        .getActiveUser();

                if (user != null) {
                    String p = password.getText().toString();
                    if (user.Password != null && user.Password.length() > 1) {
                        if (p.equals(user.Password)) {
                            mListener.onLoginFragmentInteraction(user);
                        } else {
                            password.setBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorBorderWrong, null));
                        }
                    } else {
                        EverydayLanguageDbHelper
                                .getInstance(getActivity())
                                .updateUserPassword(user.Id, p);

                        ContentManager.uploadUserPassword(getActivity(), user.Id, p, loginFragment);

                        mListener.onLoginFragmentInteraction(user);
                    }

                }
            }
        });

        return layout;
    }

    public void btnFind_click() {
        EditText txtClassCode = (EditText) layout.findViewById(R.id.login_classCode);
        String classCode = txtClassCode.getText().toString().toUpperCase();
        EditText txtStudentPosition = (EditText)layout.findViewById(R.id.login_studentPosition);
        int studentPosition = 0;
        try {
            studentPosition = Integer.valueOf(txtStudentPosition.getText().toString());
        } catch (NumberFormatException ex) {

        }

        if (studentPosition > 0) {
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("Loading...");
            progressDialog.show();

            User user = EverydayLanguageDbHelper
                    .getInstance(getActivity())
                    .getUser(classCode, studentPosition);
            if (user != null) {
                EverydayLanguageDbHelper
                        .getInstance(getActivity())
                        .setActiveUser(user);

                EditText txtFirstName = (EditText) layout.findViewById(R.id.login_firstName);
                txtFirstName.setText(user.FirstName);

                EditText txtLastName = (EditText) layout.findViewById(R.id.login_lastName);
                txtLastName.setText(user.LastName);

                EditText txtPassword = (EditText)layout.findViewById(R.id.login_password);
                txtPassword.setEnabled(true);

                progressDialog.dismiss();
            } else {
                ContentManager.getUser(getActivity(), classCode, studentPosition, this);
            }
        }
    }


    public void onDownloadError(String error) {

    }

    public void onDownloadFinished(String code, String type) {

    }

    public void onDownloadResult(String code, String type, String[] args) {

    }

    public void onDownloadResult(String code, String type, Object result) {
        if (code == DownloadCallback.RESPONSE_OK) {
            User user = (User) result;
            EverydayLanguageDbHelper
                    .getInstance(getActivity())
                    .setActiveUser(user);

            EditText txtFirstName = (EditText) layout.findViewById(R.id.login_firstName);
            txtFirstName.setText(user.FirstName);

            EditText txtLastName = (EditText) layout.findViewById(R.id.login_lastName);
            txtLastName.setText(user.LastName);

            EditText txtPassword = (EditText) layout.findViewById(R.id.login_password);
            txtPassword.setEnabled(true);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof LoginFragmentInteractionListener) {
            mListener = (LoginFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement LoginFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface LoginFragmentInteractionListener {
        void onLoginFragmentInteraction(User user);
    }
}
