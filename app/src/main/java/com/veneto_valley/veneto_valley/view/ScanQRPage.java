package com.veneto_valley.veneto_valley.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.veneto_valley.veneto_valley.R;
import com.veneto_valley.veneto_valley.util.ViewModelUtil;
import com.veneto_valley.veneto_valley.viewmodel.CreaTavoloViewModel;

public class ScanQRPage extends Fragment {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		scanCode();
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
				ViewModelUtil.getViewModel(requireActivity(), CreaTavoloViewModel.class).creaTavolo(contents);
				NavHostFragment.findNavController(this).navigate(R.id.action_scanQRNav_to_impostaUtentePage);
			} else {
				NavHostFragment.findNavController(this).navigateUp();
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
}