package com.veneto_valley.veneto_valley;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.veneto_valley.veneto_valley.adapters.OrdiniAdapter;

import java.util.List;
import java.util.Locale;

public class ModificaPiattoDialog extends DialogFragment {
	private EditText editTextCodice, editTextDesc, editTextQuantita;
	Integer position = null;
	List<Ordine> listaOrdini;
	OrdiniAdapter ordiniAdapter;
	
	public ModificaPiattoDialog(List<Ordine> lista, OrdiniAdapter ordiniAdapter) {
		this.listaOrdini = lista;
		this.ordiniAdapter = ordiniAdapter;
	}
	
	public ModificaPiattoDialog(List<Ordine> lista, OrdiniAdapter ordiniAdapter, int position) {
		this.position = position;
		this.listaOrdini = lista;
		this.ordiniAdapter = ordiniAdapter;
	}
	
	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
		LayoutInflater inflater = requireActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_modifica_piatto, null);
		
		if (position != null) {
			builder.setView(view)
					.setTitle("Modifica piatto")
					.setPositiveButton("Conferma", (dialog, which) -> {
						if (editTextCodice.getText() != null)
							listaOrdini.get(position).codice = editTextCodice.getText().toString();
						if (editTextDesc.getText() != null)
							listaOrdini.get(position).descrizione = editTextDesc.getText().toString();
						if (editTextQuantita.getText() != null)
							listaOrdini.get(position).quantita = Integer.parseInt(editTextQuantita.getText().toString());
						ordiniAdapter.notifyItemChanged(position);
					})
					.setNegativeButton("Annulla", (dialog, which) -> {
					
					});
			editTextCodice = view.findViewById(R.id.editCodice);
			editTextDesc = view.findViewById(R.id.editDesc);
			editTextQuantita = view.findViewById(R.id.editQuantita);
			editTextCodice.setText(listaOrdini.get(position).codice);
			editTextDesc.setText(listaOrdini.get(position).descrizione);
			editTextQuantita.setText(String.format(Locale.ITALY, "%d", listaOrdini.get(position).quantita));
		}
//		} else {
//			builder.setView(view)
//					.setTitle("Aggiungi piatto")
//					.setPositiveButton("Conferma", (dialog, which) -> {
//						Ordine ordine = null;
//						if (editTextCodice.getText() != null) {
//							ordine = new Ordine(editTextCodice.getText().toString());
//						}
//
//						if (ordine != null) {
//							if (editTextDesc.getText() != null)
//								ordine.descrizione = editTextDesc.getText().toString();
//							if (editTextQuantita.getText() != null)
//								ordine.quantita = Integer.parseInt(editTextQuantita.getText().toString());
//							listaOrdini.add(ordine);
//							position = listaOrdini.size();
//							ordiniAdapter.notifyItemChanged(position);
//						}
//					}).
//					setNegativeButton("Annulla", (dialog, which) -> {
//
//					});
//			editTextCodice = view.findViewById(R.id.editCodice);
//			editTextDesc = view.findViewById(R.id.editDesc);
//			editTextQuantita = view.findViewById(R.id.editQuantita);
//		}
		return builder.create();
	}
}