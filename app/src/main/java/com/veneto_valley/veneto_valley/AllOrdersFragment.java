package com.veneto_valley.veneto_valley;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.veneto_valley.veneto_valley.db.AppDatabase;
import com.veneto_valley.veneto_valley.db.entities.Ordine;

import java.util.ArrayList;
import java.util.List;

public class AllOrdersFragment extends Fragment {
//	private List<Ordine> dataList = new ArrayList<>();
//	private final AppDatabase database = AppDatabase.getInstance(requireContext());
//
//	public AllOrdersFragment() {
//		super(R.layout.fragment_all_orders);
//	}
//
//	@Override
//	public void onCreate(@Nullable Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		dataList = database.ordineDao().getAll();
//	}
//
//	@Override
//	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//		super.onViewCreated(view, savedInstanceState);
//		RecyclerView recyclerView = view.findViewById(R.id.recyclerViewAll);
//		recyclerView.setHasFixedSize(true);
//		recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
//		MainAdapter adapter = new MainAdapter(requireActivity(), dataList);
//		recyclerView.setAdapter(adapter);
//	}
}