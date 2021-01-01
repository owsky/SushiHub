package com.veneto_valley.veneto_valley.view;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.veneto_valley.veneto_valley.R;

import java.util.UUID;

public class GeneraQRPage extends Fragment {
	private String codice;
	
	public GeneraQRPage() {
		super(R.layout.fragment_genera_qr);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
		codice = preferences.getString("codice_tavolo", null);
		
		if (codice != null) {
			view.findViewById(R.id.doneqr).setVisibility(View.INVISIBLE);
		} else {
			codice = UUID.randomUUID().toString();
			Button btn = view.findViewById(R.id.doneqr);
			
			GeneraQRPageDirections.ActionGeneraQRToImpostaTavolo action = GeneraQRPageDirections.actionGeneraQRToImpostaTavolo(codice);
			btn.setOnClickListener(v -> NavHostFragment.findNavController(GeneraQRPage.this).navigate(action));
		}
		
		try {
			generaQR();
		} catch (WriterException e) {
			e.printStackTrace();
		}
	}
	
	private void generaQR() throws WriterException {
		int qr_size = 250;
		ImageView imageView = requireView().findViewById(R.id.qr_code);
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		
		BitMatrix bitMatrix = qrCodeWriter.encode(codice, BarcodeFormat.QR_CODE, qr_size, qr_size);
		Bitmap bitmap = Bitmap.createBitmap(qr_size, qr_size, Bitmap.Config.RGB_565);
		
		for (int x = 0; x < qr_size; ++x) {
			for (int y = 0; y < qr_size; ++y) {
				bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
			}
		}
		imageView.setImageBitmap(bitmap);
	}
}