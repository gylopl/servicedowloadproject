package makdroid.servicesproject.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
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
import makdroid.servicesproject.services.DownloadService;
import makdroid.servicesproject.utils.AlertDialogBuilder;
import makdroid.servicesproject.utils.Constants;
import makdroid.servicesproject.utils.FileUtil;
import makdroid.servicesproject.utils.NetworkUtils;


public class ServiceFragment extends Fragment {
    public static final String FRAGMENT_TAG = "ServiceFragment";

    private View rootView;
    private Button mButtonDownload;
    private Button mButtonCancelDownload;
    private EditText editTextURL;
    private TextView mTextViewProgress;
    private ProgressBar mProgressBar;
    private boolean isServiceRunning = false;

    public ServiceFragment() {
    }

    public static ServiceFragment newInstance() {
        return new ServiceFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter mStatusIntentFilter = new IntentFilter(DownloadService.TAG);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mMessageReceiver, mStatusIntentFilter);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(Constants.IS_DOWNLOADING, isServiceRunning);
    }

    @Override
    public void onDestroy() {
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_service, container, false);
        setUpFragment();
        if (savedInstanceState != null)
            isServiceRunning = savedInstanceState.getBoolean(Constants.IS_DOWNLOADING);
        if (isServiceRunning)
            showDownload();
        return rootView;
    }

    private void setUpFragment() {
        editTextURL = (EditText) rootView.findViewById(R.id.editText_url);
        editTextURL.setText(R.string.example_url);
        mButtonDownload = (Button) rootView.findViewById(R.id.btn_download);
        mButtonDownload.setOnClickListener(btnDownloadListener);
        mButtonCancelDownload = (Button) rootView.findViewById(R.id.buttonCancel);
        mButtonCancelDownload.setOnClickListener(btnCancelDownloadListener);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        mTextViewProgress = (TextView) rootView.findViewById(R.id.progressText);
    }

    private View.OnClickListener btnDownloadListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            startDownload();
        }

    };

    private View.OnClickListener btnCancelDownloadListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            cancelDownload();
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
            Intent intent = new Intent(getActivity().getApplicationContext(), DownloadService.class);
            intent.putExtra(Constants.DOWNLOAD_URL, editTextURL.getText().toString());
            getActivity().startService(intent);
            showDownload();
        }
    }

    private void cancelDownload() {
        Intent intent = new Intent(getActivity().getApplicationContext(), DownloadService.class);
        getActivity().stopService(intent);
        hideDownload();
        downloadCanceled();
    }

    private void showDownload() {
        showProgressView();
        showCancelButton();
        hideDownloadButton();
        isServiceRunning = true;
    }

    private void hideDownload() {
        hideProgressView();
        hideCancelButton();
        showDownloadButton();
        isServiceRunning = false;
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

    private void showCancelButton() {
        mButtonCancelDownload.setVisibility(View.VISIBLE);
    }

    private void hideCancelButton() {
        mButtonCancelDownload.setVisibility(View.INVISIBLE);
    }

    private void showDownloadButton() {
        mButtonDownload.setVisibility(View.VISIBLE);
    }

    private void hideDownloadButton() {
        mButtonDownload.setVisibility(View.INVISIBLE);
    }

    private void downloadCanceled() {
        Toast.makeText(getContext(), R.string.result_download_canceled, Toast.LENGTH_LONG).show();
    }

    private void downloadSuccess() {
        Toast.makeText(getContext(), R.string.result_download_success, Toast.LENGTH_LONG).show();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int progress = intent.getIntExtra(Constants.UPDATE_PROGRESS, 0);
            mProgressBar.setProgress(progress);
            mTextViewProgress.setText(progress + "%");
            if (progress == 100) {
                hideDownload();
                downloadSuccess();
            }
            boolean isConnectionError = intent.getBooleanExtra(Constants.CONNECTION_ERROR, false);
            if (isConnectionError) {
                hideDownload();
                AlertDialog alertDialog = AlertDialogBuilder.createErrorDialog(getString(R.string.error_no_internet), getContext());
                alertDialog.show();
            }
        }
    };

}
