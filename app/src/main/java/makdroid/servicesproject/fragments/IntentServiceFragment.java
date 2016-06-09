package makdroid.servicesproject.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import makdroid.servicesproject.R;
import makdroid.servicesproject.receivers.MyReceiver;
import makdroid.servicesproject.services.intentService.DownloadIntentService;
import makdroid.servicesproject.utils.AlertDialogBuilder;
import makdroid.servicesproject.utils.Constants;
import makdroid.servicesproject.utils.FileUtil;
import makdroid.servicesproject.utils.NetworkUtils;


public class IntentServiceFragment extends Fragment implements MyReceiver.Receiver {
    public static final String FRAGMENT_TAG = "IntentServiceFragment";

    private View rootView;
    private Button mButtonDownload;
    private EditText editTextURL;
    private TextView mTextViewProgress;
    private ProgressBar mProgressBar;
    private MyReceiver mReceiver;

    public IntentServiceFragment() {
        // Required empty public constructor
    }

    public static IntentServiceFragment newInstance() {
        return new IntentServiceFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            mReceiver = savedInstanceState.getParcelable(Constants.RECEIVER);
        } else
            mReceiver = new MyReceiver(new Handler());
        mReceiver.setReceiver(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(Constants.RECEIVER, mReceiver);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_intent, container, false);
        setUpFragment();

        return rootView;
    }

    private void setUpFragment() {
        editTextURL = (EditText) rootView.findViewById(R.id.editText_url);
        editTextURL.setText(R.string.example_url);
        mButtonDownload = (Button) rootView.findViewById(R.id.btn_download);
        mButtonDownload.setOnClickListener(btnDownloadListener);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        mTextViewProgress = (TextView) rootView.findViewById(R.id.progressText);
    }

    private final View.OnClickListener btnDownloadListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            startDownload();
        }

    };

    private void startDownload() {
        String error = null;
        if (!FileUtil.isMimeTypeValid(editTextURL.getText().toString()))
            error = getString(R.string.error_mime_type_invalid);

        if (!NetworkUtils.checkNetworkConnection(getContext()))
            error = getString(R.string.error_no_internet);

        if (error != null) {
            AlertDialog alertDialog = AlertDialogBuilder.createErrorDialog(error, getContext());
            alertDialog.show();
        } else {
            Intent intent = new Intent(getActivity().getApplicationContext(), DownloadIntentService.class);
            intent.putExtra(Constants.DOWNLOAD_URL, editTextURL.getText().toString());
            intent.putExtra(Constants.RECEIVER, mReceiver);
            getActivity().startService(intent);
            showDownload();
        }
    }

    private void showDownload() {
        showProgressView();
        hideDownloadButton();
    }

    private void hideDownload() {
        hideProgressView();
        showDownloadButton();
    }

    private void showProgressView() {
        mProgressBar.setVisibility(View.VISIBLE);
        mTextViewProgress.setVisibility(View.VISIBLE);
        mProgressBar.setProgress(0);
        mTextViewProgress.setText("0%");
    }

    private void hideProgressView() {
        mProgressBar.setVisibility(View.INVISIBLE);
        mTextViewProgress.setVisibility(View.INVISIBLE);
    }

    private void showDownloadButton() {
        mButtonDownload.setVisibility(View.VISIBLE);
    }

    private void hideDownloadButton() {
        mButtonDownload.setVisibility(View.INVISIBLE);
    }

    private void downloadSuccess() {
        Toast.makeText(getContext(), R.string.result_download_success, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onReceiveResult(int resultCode, Bundle resultData) {
        if (resultCode == Constants.UPDATE_PROGRESS_CODE) {
            if (mProgressBar.getVisibility() == View.INVISIBLE)
                showDownload();
            int progress = resultData.getInt(Constants.UPDATE_PROGRESS);
            mProgressBar.setProgress(progress);
            mTextViewProgress.setText(progress + "%");
            if (progress == 100) {
                hideDownload();
                downloadSuccess();
            }
        }
    }
}
