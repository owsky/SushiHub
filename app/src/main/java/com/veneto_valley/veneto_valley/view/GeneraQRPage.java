package com.veneto_valley.veneto_valley.view;

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
import com.veneto_valley.veneto_valley.R;
import com.veneto_valley.veneto_valley.util.ViewModelUtil;
import com.veneto_valley.veneto_valley.viewmodel.CreaTavoloViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class GeneraQRPage extends Fragment {
	private String dati;
	
	public GeneraQRPage() {
		super(R.layout.fragment_genera_qr);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		CreaTavoloViewModel viewModel = ViewModelUtil.getViewModel(requireActivity(), CreaTavoloViewModel.class);
		List<String> infoTavolo = viewModel.getInfoTavolo();
		
		dati = String.format("%s;%s;%s", infoTavolo.get(0), infoTavolo.get(1), infoTavolo.get(2));
		GeneraQRPageArgs args = GeneraQRPageArgs.fromBundle(requireArguments());
		if (args.getUnisciti()){
			view.findViewById(R.id.doneqr).setVisibility(View.INVISIBLE);
		} else {
			Button btn = view.findViewById(R.id.doneqr);
			btn.setOnClickListener(v -> NavHostFragment.findNavController(GeneraQRPage.this)
					.navigate(R.id.action_generaQRNav_to_impostaUtentePage));
		}

		try {
			generaQR();
		} catch (WriterException e) {
			e.printStackTrace();
		}
	}
	
	private void generaQR() throws WriterException {
		int qr_size = 177;
		ImageView imageView = requireView().findViewById(R.id.qr_code);
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		
		BitMatrix bitMatrix = qrCodeWriter.encode(dati, BarcodeFormat.QR_CODE, qr_size, qr_size);
		Bitmap bitmap = Bitmap.createBitmap(qr_size, qr_size, Bitmap.Config.RGB_565);
		
		for (int x = 0; x < qr_size; ++x) {
			for (int y = 0; y < qr_size; ++y) {
				bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
			}
		}
		imageView.setImageBitmap(bitmap);
	}
}