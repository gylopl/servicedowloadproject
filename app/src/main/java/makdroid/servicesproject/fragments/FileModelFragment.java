package makdroid.servicesproject.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import makdroid.servicesproject.adapters.FileModelAdapter;
import makdroid.servicesproject.model.FileModel;
import makdroid.servicesproject.utils.FileUtil;


/**
 * Created by Grzecho on 22.11.2015.
 */
public class FileModelFragment extends ListFragment {
    public static final String FRAGMENT_TAG = FileModelFragment.class.getSimpleName();
    private static final boolean DEBUG = true;


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getListView().setDivider(null);
        populateListView();
    }

    private void populateListView() {
        File[] listFiles = FileUtil.getAppDir(getContext()).listFiles();
        List<FileModel>  mItems = new ArrayList<>();
        for (File f : listFiles) {
            FileModel fileModel = new FileModel();
            fileModel.setName(f.getName());
            mItems.add(fileModel);
        }
        setListAdapter(new FileModelAdapter(getActivity(), mItems));
    }

    @Override
    public void onStart() {
        if (DEBUG) Log.i(FRAGMENT_TAG, "onStart()");
        super.onStart();
    }

    @Override
    public void onResume() {
        if (DEBUG) Log.i(FRAGMENT_TAG, "onResume()");
        super.onResume();
    }

    @Override
    public void onPause() {
        if (DEBUG) Log.i(FRAGMENT_TAG, "onPause()");
        super.onPause();
    }

    @Override
    public void onStop() {
        if (DEBUG) Log.i(FRAGMENT_TAG, "onStop()");
        super.onStop();
    }

    @Override
    public void onAttach(Context context) {
        if (DEBUG) Log.i(FRAGMENT_TAG, "onAttach()");
        super.onAttach(context);
    }

    @Override
    public void onDestroy() {
        if (DEBUG) Log.i(FRAGMENT_TAG, "onDestroy()");
        super.onDestroy();
    }
}
