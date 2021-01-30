package com.veneto_valley.veneto_valley.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.veneto_valley.veneto_valley.R;
import com.veneto_valley.veneto_valley.util.ViewModelUtil;
import com.veneto_valley.veneto_valley.viewmodel.CreateTableViewModel;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

public class ScanQRPage extends Fragment {

    private final ActivityResultLauncher<Intent> scanIntent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                IntentResult res = IntentIntegrator.parseActivityResult(REQUEST_CODE,
                        result.getResultCode(), result.getData());
                String contents = res.getContents();
                // if the scan ended without errors it creates an array of strings to create a table with
                if (contents != null) {
                    String[] info = TextUtils.split(contents, ";");
                    String tableCode = info[0];
                    float menuPrice = Float.parseFloat(info[1]);
                    String restCode = info[2];
                    String restName = info[3];
                    ViewModelUtil.getViewModel(requireActivity(), CreateTableViewModel.class)
                            .createTable(restCode, restName, tableCode, menuPrice);
                    NavHostFragment.findNavController(ScanQRPage.this)
                            .navigate(R.id.action_scanQRNav_to_configureUserNav);
                } else {
                    NavHostFragment.findNavController(ScanQRPage.this).navigateUp();
                }
            }
    );

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
        integrator.setOrientationLocked(true);
        integrator.setBeepEnabled(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
        integrator.setPrompt("Scanning QR Code");
        scanIntent.launch(integrator.createScanIntent());
    }
}