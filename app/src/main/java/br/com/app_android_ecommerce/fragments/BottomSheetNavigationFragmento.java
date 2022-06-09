package br.com.app_android_ecommerce.fragments;

import android.os.Bundle;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetNavigationFragmento extends BottomSheetDialogFragment {

    public BottomSheetNavigationFragmento() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static BottomSheetNavigationFragmento newInstance() {

        Bundle args = new Bundle();
        BottomSheetNavigationFragmento fragment = new BottomSheetNavigationFragmento();
        fragment.setArguments(args);
        return fragment;
    }
}