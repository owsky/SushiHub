package com.veneto_valley.veneto_valley;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;

import java.util.Objects;

public class ScanQRFragment extends Fragment {
	
	public ScanQRFragment() {
		super(R.layout.fragment_scan_qr);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		scanCode();
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		Objects.requireNonNull(((MainActivity) requireActivity()).getSupportActionBar()).setTitle("Unisciti a un tavolo");
	}
	
	private void scanCode() {
		IntentIntegrator integrator = new IntentIntegrator(getActivity());
		integrator.setCaptureActivity(CaptureActivity.class);
		integrator.setBeepEnabled(false);
		integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
		integrator.setPrompt("Scanning QR Code");
		IntentIntegrator.forSupportFragment(this).initiateScan();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
		if (result != null) {
			String contents = result.getContents();
			if (contents != null) {
				//TODO crea sessione con codice ottenuto
				NavHostFragment.findNavController(this).navigate(R.id.action_scanQR_to_listPiattiFragment);
			} else {
				NavHostFragment.findNavController(this).navigate(R.id.action_scanQR_to_homepageFragment);
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
}