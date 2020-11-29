package com.veneto_valley.veneto_valley.qr;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.veneto_valley.veneto_valley.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class GeneraQR extends AppCompatActivity {
	private final UUID uuid = UUID.randomUUID();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_genera_qr);
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
		ImageView imageView = findViewById(R.id.qr_code);
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = qrCodeWriter.encode(uuid.toString(), BarcodeFormat.QR_CODE, 200, 200);
		Bitmap bitmap = Bitmap.createBitmap(200, 200, Bitmap.Config.RGB_565);
		
		for (int x = 0; x < 200; ++x) {
			for (int y = 0; y < 200; ++y) {
				bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
			}
		}
		imageView.setImageBitmap(bitmap);
		
//		Salvo il QR su memoria interna
		File path = new File(getBaseContext().getFilesDir(), "Sushi" + File.separator + "Images");
		if (!path.exists())
			path.mkdirs();
		File outFile = new File(path, uuid + ".jpeg");

		FileOutputStream outputStream = new FileOutputStream(outFile);
		bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
		outputStream.close();
	}
	
	public void onClickBtn(View v) {
//		TODO Transizione
	}
}