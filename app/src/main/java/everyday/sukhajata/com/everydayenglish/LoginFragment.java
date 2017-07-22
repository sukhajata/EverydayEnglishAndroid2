package everyday.sukhajata.com.everydayenglish;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.regex.Pattern;

import everyday.sukhajata.com.everydayenglish.model.Slide;
import everyday.sukhajata.com.everydayenglish.model.User;
import everyday.sukhajata.com.everydayenglish.utility.EverydayLanguageDbHelper;


/**
 * Created by Tim on 3/03/2017.
 */

public class LoginFragment extends Fragment {

    public LoginButton mLoginButton;
    public CallbackManager mCallbackManager;

    private SetupActivity mContext;
    private boolean mEmailValid;
    private ArrayList<String> mEmails;
    private boolean mWrongPassword;


    public LoginFragment () {
    }

    public static LoginFragment newInstance(boolean wrongPassword) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putBoolean(SetupActivity.ARG_WRONG_PASSWORD, wrongPassword);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mWrongPassword = getArguments().getBoolean(SetupActivity.ARG_WRONG_PASSWORD);
        }
        //FacebookSdk.setApplicationId(getResources().getString(R.string.facebook_app_id));

        //FacebookSdk.sdkInitialize(getActivity().getApplicationContext());
        //mCallbackManager = CallbackManager.Factory.create();
        mEmails = EverydayLanguageDbHelper.getInstance(getActivity()).getUsers();

        Pattern emailPattern = Patterns.EMAIL_ADDRESS; // API level 8+
        Account[] accounts = AccountManager.get(getActivity()).getAccounts();
        for (Account account : accounts) {
            if (emailPattern.matcher(account.name).matches()) {
                if (!mEmails.contains(account.name)) {
                    mEmails.add(account.name);
                }
            }
        }
        checkConnection();

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

            //check if logged in already
            /*
            AccessToken token = AccessToken.getCurrentAccessToken();
            if (token != null) {
                getEmailFromFacebook(token);
            }
            */
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout layout = (LinearLayout)inflater.inflate(
                R.layout.fragment_login, container, false);

        TextView txtErrorMessage = (TextView)layout.findViewById(R.id.login_error);
        if (!mWrongPassword) {
            txtErrorMessage.setVisibility(View.GONE);
        }

        final EditText txtEmail = (EditText)layout.findViewById(R.id.login_email);

        ListView listView = (ListView)layout.findViewById(R.id.user_list);
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.list_user, mEmails);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String email =  (String)parent.getItemAtPosition(position);
                txtEmail.setText(email);
            }
        });

        /*
        mLoginButton = (LoginButton)layout.findViewById(R.id.facebook_login_button);
        mLoginButton.setReadPermissions("email");
        mLoginButton.setFragment(this);
        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                getEmailFromFacebook(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
            }
        });
        */


        txtEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Patterns.EMAIL_ADDRESS.matcher(s).matches()) {
                    mEmailValid = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final Button btn = (Button)layout.findViewById(R.id.login_submit);


        final EditText password = (EditText)layout.findViewById(R.id.login_password);
        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 3 && mEmailValid) {
                    btn.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String e = txtEmail.getText().toString();
                String p = password.getText().toString();
                Intent intent = new Intent();
                intent.putExtra(SetupActivity.ARG_EMAIL, e);
                intent.putExtra(SetupActivity.ARG_PASSWORD, p);
                getActivity().setResult(Activity.RESULT_OK, intent);
                getActivity().finish();
            }
        });

        return layout;
    }

    /*
    private void getEmailFromFacebook(AccessToken token) {
        GraphRequest request = GraphRequest.newMeRequest(
                token,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.v("LoginActivity", response.toString());

                        // Application code
                        try {
                            String email = object.getString("email");
                            Log.d("FACEBOOK", "on success: " + email);
                            Intent intent = new Intent();
                            intent.putExtra(SetupActivity.ARG_EMAIL, email);
                            getActivity().setResult(Activity.RESULT_OK, intent);
                            getActivity().finish();

                        } catch (JSONException ex) {
                            Toast.makeText(getContext(), "Json exception", Toast.LENGTH_SHORT);
                            Log.e("FACEBOOK", ex.getMessage());
                        } catch (NullPointerException ex) {
                            //could not get email
                            return;
                        }
                    }
                });
        Bundle parameters = new Bundle();
        parameters.putString("fields", "email");
        request.setParameters(parameters);
        request.executeAsync();
    }
    */

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }


}
