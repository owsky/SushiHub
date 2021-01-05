package com.veneto_valley.veneto_valley.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.veneto_valley.veneto_valley.R;
import com.veneto_valley.veneto_valley.model.entities.Tavolo;
import com.veneto_valley.veneto_valley.util.ViewModelUtil;
import com.veneto_valley.veneto_valley.viewmodel.StoricoViewModel;

public class StoricoDettagliPage extends Fragment {
	private StoricoViewModel viewModel;
	private Tavolo tavolo;
	
	public StoricoDettagliPage() {
		super(R.layout.fragment_recyclerview);
	}
	
	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setHasOptionsMenu(true);
		RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
		recyclerView.setHasFixedSize(true);
		recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
		OrdiniAdapter adapter = new OrdiniAdapter(OrdiniAdapter.TipoAdapter.storico);
		recyclerView.setAdapter(adapter);
		
		// estraggo dal viewmodel il livedata da osservare
		tavolo = StoricoDettagliPageArgs.fromBundle(requireArguments()).getTavolo();
		viewModel = ViewModelUtil.getViewModel(requireActivity(), StoricoViewModel.class);
		viewModel.getOrdini(tavolo).observe(getViewLifecycleOwner(), adapter::submitList);
	}
	
	@Override
	public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.storico_overflow, menu);
		menu.findItem(R.id.eliminaTuttiTavoli).setVisible(false);
	}
	
	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		// chiedo conferma all'utente per l'eliminazione del tavolo dallo storico
		if (item.getItemId() == R.id.eliminaTavolo) {
			ConfirmDialog dialog = new ConfirmDialog(() -> {
				viewModel.deleteTable(tavolo);
				NavHostFragment.findNavController(StoricoDettagliPage.this).navigateUp();
			});
			dialog.show(getParentFragmentManager(), null);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	public static class ConfirmDialog extends DialogFragment {
		private final Runnable runnable;
		
		public ConfirmDialog(Runnable runnable) {
			this.runnable = runnable;
		}
		
		@NonNull
		@Override
		public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
			AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity());
			builder.setTitle("Eliminare il tavolo?");
			builder.setPositiveButton("OK", (dialog, which) -> runnable.run());
			builder.setNegativeButton("Annulla", (dialog, which) -> dismiss());
			return builder.create();
		}
	}
}