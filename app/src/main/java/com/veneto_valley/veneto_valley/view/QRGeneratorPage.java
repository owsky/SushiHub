package com.veneto_valley.veneto_valley.view;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.veneto_valley.veneto_valley.viewmodel.CreateTableViewModel;

import java.util.List;

public class QRGeneratorPage extends Fragment {
	private String data;
	
	public QRGeneratorPage() {
		super(R.layout.fragment_qr_generator);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		// gets the information required to create a table
		CreateTableViewModel viewModel = ViewModelUtil.getViewModel(requireActivity(), CreateTableViewModel.class);
		List<String> tableInfo = viewModel.getInfoTavolo();
		
		// joins the information into a single string
		data = TextUtils.join(";", tableInfo);
		// checks whether to hide the button depending upon the saferg, telling whence the user came from
		QRGeneratorPageArgs args = QRGeneratorPageArgs.fromBundle(requireArguments());
		if (args.getShare()) {
			view.findViewById(R.id.doneqr).setVisibility(View.INVISIBLE);
		} else {
			Button btn = view.findViewById(R.id.doneqr);
			btn.setOnClickListener(v -> NavHostFragment.findNavController(QRGeneratorPage.this)
					.navigate(R.id.action_QRGeneratorNav_to_configureUserNav));
		}
		
		try {
			generateQR();
		} catch (WriterException e) {
			e.printStackTrace();
		}
	}
	
	private void generateQR() throws WriterException {
		int qr_size = 177;
		ImageView imageView = requireView().findViewById(R.id.qr_code);
		QRCodeWriter qrCodeWriter = new QRCodeWriter();
		BitMatrix bitMatrix = qrCodeWriter.encode(data, BarcodeFormat.QR_CODE, qr_size, qr_size);
		Bitmap bitmap = Bitmap.createBitmap(qr_size, qr_size, Bitmap.Config.RGB_565);
		
		for (int x = 0; x < qr_size; ++x) {
			for (int y = 0; y < qr_size; ++y) {
				bitmap.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
			}
		}
		imageView.setImageBitmap(bitmap);
	}
}