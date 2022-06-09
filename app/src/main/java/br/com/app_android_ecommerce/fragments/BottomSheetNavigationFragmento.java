package br.com.app_android_ecommerce.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.navigation.NavigationView;

import br.com.app_android_ecommerce.R;

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
    // Retorno de chamada da folha inferior
    private BottomSheetBehavior.BottomSheetCallback bottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN);
            dismiss();
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            // verifique o deslocamento do slide e altere a visibilidade do botão fechar
            if (slideOffset > 0.5) {
                fecharBotao.setVisibility(View.VISIBLE);
            } else {
                fecharBotao.setVisibility(View.GONE);
            }
        }
    };
    private ImageView fecharBotao;

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        //buscar content view
        View contentView = View.inflate(getContext(), R.layout.bottom_navigation_drawer, null);
        dialog.setContentView(contentView);

        NavigationView navigationView = contentView.findViewById(R.id.navigation_view);

        // implementar o evento de clique do item do menu de navegação
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav01:
                        break;

                    case R.id.nav02:
                        break;

                    case R.id.nav03:
                        break;

                    case R.id.nav04:
                        break;

                    case R.id.nav05:
                        break;

                    case R.id.nav06:
                        break;

                    case R.id.nav07:
                        break;

                    case R.id.nav08:
                        break;
                }
                return false;
            }
        });
        fecharBotao = contentView.findViewById(R.id.close_image_view);
        fecharBotao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }
}