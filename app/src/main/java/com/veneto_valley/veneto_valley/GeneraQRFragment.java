package com.veneto_valley.veneto_valley;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
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

import java.util.Objects;
import java.util.UUID;

public class GeneraQRFragment extends Fragment {
	private final UUID uuid;
	
	public GeneraQRFragment() {
		super(R.layout.fragment_genera_qr);
		this.uuid = UUID.randomUUID();
	}
	
	public GeneraQRFragment(UUID uuid) {
		super(R.layout.fragment_genera_qr);
		this.uuid = uuid;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		try {
			generaQR();
		} catch (WriterException e) {
			e.printStackTrace();
		}
		
		Button btn = view.findViewById(R.id.doneqr);
		btn.setOnClickListener(v -> NavHostFragment.findNavController(GeneraQRFragment.this).navigate(R.id.action_generaQR_to_listPiattiFragment));
		
	}
	
	private void generaQR() throws WriterException {
		int qr_size = 250;
		ImageView imageView = requireView().findViewById(R.id.qr_code);
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		
		BitMatrix bitMatrix = qrCodeWriter.encode(uuid.toString(), BarcodeFormat.QR_CODE, qr_size, qr_size);
		Bitmap bitmap = Bitmap.createBitmap(qr_size, qr_size, Bitmap.Config.RGB_565);
		
		for (int x = 0; x < qr_size; ++x) {
			for (int y = 0; y < qr_size; ++y) {
				bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
			}
		}
		imageView.setImageBitmap(bitmap);
	}
}