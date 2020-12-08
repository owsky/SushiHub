package com.veneto_valley.veneto_valley;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class DeliveredOrdersFragment extends Fragment {
	
	public DeliveredOrdersFragment() {
		super(R.layout.fragment_delivered_orders);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		RecyclerView recyclerView = view.findViewById(R.id.recyclerViewDelivered);
		List<Ordine> listaOrdini = new ArrayList<>();
	}
}