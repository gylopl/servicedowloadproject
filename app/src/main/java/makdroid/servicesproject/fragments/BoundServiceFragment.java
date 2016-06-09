package makdroid.servicesproject.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import makdroid.servicesproject.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BoundServiceFragment extends Fragment {
    public static final String FRAGMENT_TAG = "BoundServiceFragment";

    public BoundServiceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bound_service, container, false);
    }

}
