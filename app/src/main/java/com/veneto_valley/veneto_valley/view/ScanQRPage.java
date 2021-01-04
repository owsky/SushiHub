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
		// impostazioni activity zxing scanner e avvio
		IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
		integrator.setOrientationLocked(true);
		integrator.setBeepEnabled(false);
		integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
		integrator.setPrompt("Scanning QR Code");
		integrator.initiateScan();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
		if (result != null) {
			String contents = result.getContents();
			if (contents != null) {
				// se la lettura è andata a buon fine splitta la stringa in array di stringhe per creare il tavolo tramite viewmodel
				String[] info = contents.split(";");
				ViewModelUtil.getViewModel(requireActivity(), CreaTavoloViewModel.class).creaTavolo(info[0], Integer.parseInt(info[1]), Float.parseFloat(info[2]));
				NavHostFragment.findNavController(this).navigate(R.id.action_scanQRNav_to_impostaUtentePage);
			} else {
				NavHostFragment.findNavController(this).navigateUp();
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
}