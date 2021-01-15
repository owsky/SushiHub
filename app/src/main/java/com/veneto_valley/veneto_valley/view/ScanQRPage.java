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
import com.veneto_valley.veneto_valley.viewmodel.CreaTavoloViewModel;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

public class ScanQRPage extends Fragment {
	
	private final ActivityResultLauncher<Intent> scanIntent = registerForActivityResult(
			new ActivityResultContracts.StartActivityForResult(), result -> {
				IntentResult res = IntentIntegrator.parseActivityResult(REQUEST_CODE,
						result.getResultCode(), result.getData());
				String contents = res.getContents();
				// se la lettura è andata a buon fine splitta la stringa in array di stringhe per
				// creare il tavolo tramite viewmodel
				if (contents != null) {
					String[] info = TextUtils.split(contents, ";");
					String codiceTavolo = info[0];
					float costoMenu = Float.parseFloat(info[1]);
					String codiceRistorante = info[2];
					String nomeRistorante = info[3];
					ViewModelUtil.getViewModel(requireActivity(), CreaTavoloViewModel.class)
							.creaTavolo(codiceRistorante, nomeRistorante, codiceTavolo, costoMenu);
					NavHostFragment.findNavController(ScanQRPage.this)
							.navigate(R.id.action_scanQRNav_to_impostaUtentePage);
				} else {
					NavHostFragment.findNavController(ScanQRPage.this).navigateUp();
				}
			}
	);
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// impostazioni zxing scanner e avvio
		IntentIntegrator integrator = IntentIntegrator.forSupportFragment(this);
		integrator.setOrientationLocked(true);
		integrator.setBeepEnabled(false);
		integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
		integrator.setPrompt("Scanning QR Code");
		scanIntent.launch(integrator.createScanIntent());
	}
}