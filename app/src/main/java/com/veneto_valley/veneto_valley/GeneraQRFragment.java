package com.veneto_valley.veneto_valley;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class GeneraQRFragment extends Fragment {
	private final UUID uuid = UUID.randomUUID();
	private final int qr_size = 250;
	
	public GeneraQRFragment() {
		super(R.layout.fragment_genera_qr);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		((MainActivity) getActivity()).getSupportActionBar().setTitle("Crea tavolo");
		try {
			generaQR();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (WriterException e) {
			e.printStackTrace();
		}
	}
	
	private void generaQR() throws IOException, WriterException {
//		Genero il QR
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

//		Salvo il QR su memoria interna
		File path = new File(requireActivity().getFilesDir(), "Sushi" + File.separator + "Images");
		if (!path.exists())
			path.mkdirs();
		File outFile = new File(path, uuid + ".jpeg");
		
		FileOutputStream outputStream = new FileOutputStream(outFile);
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
		outputStream.close();
	}
}