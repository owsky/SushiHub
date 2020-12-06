package com.veneto_valley.veneto_valley;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.veneto_valley.veneto_valley.adapters.OrdiniAdapter;

import java.util.List;

public class ModificaPiattoDialog extends DialogFragment {
	private EditText editTextCodice, editTextDesc;
	int position;
	List<Ordine> lista;
	OrdiniAdapter ordiniAdapter;
	
	public ModificaPiattoDialog(List<Ordine> lista, OrdiniAdapter ordiniAdapter, int position) {
		this.position = position;
		this.lista = lista;
		this.ordiniAdapter = ordiniAdapter;
	}
	
	@Override
	@NonNull
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
		LayoutInflater inflater = requireActivity().getLayoutInflater();
		View view = inflater.inflate(R.layout.dialog_modifica_piatto, null);
		
		builder.setView(view)
				.setTitle("Modifica piatto")
				.setNegativeButton("Annulla", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					
					}
				})
				.setPositiveButton("Conferma", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						lista.get(position).codice = editTextCodice.getText().toString();;
						lista.get(position).descrizione = editTextDesc.getText().toString();
						ordiniAdapter.notifyItemChanged(position);
					}
				});
		
		editTextCodice = view.findViewById(R.id.editCodice);
		editTextDesc = view.findViewById(R.id.editDesc);
		editTextCodice.setText(lista.get(position).codice);
		editTextDesc.setText(lista.get(position).descrizione);
		
		return builder.create();
	}
}