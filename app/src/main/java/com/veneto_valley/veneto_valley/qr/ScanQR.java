package com.veneto_valley.veneto_valley.qr;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.CaptureActivity;
import com.veneto_valley.veneto_valley.MainActivity;
import com.veneto_valley.veneto_valley.R;


public class ScanQR extends AppCompatActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_scan_qr);
		scanCode();
	}
	
	private void scanCode() {
		IntentIntegrator integrator = new IntentIntegrator(this);
		integrator.setCaptureActivity(CaptureActivity.class);
		integrator.setBeepEnabled(false);
		integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
		integrator.setPrompt("Scanning QR Code");
		integrator.initiateScan();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
		if (result != null) {
			String contents = result.getContents();
			if (contents != null) {
				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setMessage(contents);
				builder.setTitle("Scanning result");
				builder.setNegativeButton("Finish", (dialog, which) -> finish());
				AlertDialog dialog = builder.create();
				dialog.show();
			} else {
				Intent intent = new Intent(this, MainActivity.class);
				startActivity(intent);
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}
	}
}