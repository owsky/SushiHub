package com.veneto_valley.veneto_valley.view;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class ListeTabAdapter extends FragmentStateAdapter {
	
	public ListeTabAdapter(@NonNull Fragment fragment) {
		super(fragment);
	}
	
	@NonNull
	@Override
	public Fragment createFragment(int position) {
		switch (position) {
			case 0:
				return new ListaOrdiniGenericaPage(ListaOrdiniGenericaPage.TipoLista.pending);
			case 1:
				return new ListaOrdiniGenericaPage(ListaOrdiniGenericaPage.TipoLista.confirmed);
			default:
				return new ListaOrdiniGenericaPage(ListaOrdiniGenericaPage.TipoLista.delivered);
		}
	}
	
	@Override
	public int getItemCount() {
		return 3;
	}
}
