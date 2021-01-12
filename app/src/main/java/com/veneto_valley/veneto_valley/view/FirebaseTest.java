package com.veneto_valley.veneto_valley.view;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.veneto_valley.veneto_valley.model.entities.Ordine;
import com.veneto_valley.veneto_valley.model.entities.Tavolo;

public class FirebaseTest {

//	//ArrayList<Ristorante> ristorantiArrayList = new ArrayList<>();
//	//ArrayList<Categoria> categorieArrayList = new ArrayList<>();
//	Button loadRes = null;
//	Button loadCat = null;
//	Spinner spinnerRes = null;
//	Spinner spinnerCat = null;
//	LinearLayout linLay = null;
//
//	int resElemSelected = 0;
//	int menElemSelected = 0;
//
//	@Override
//	protected void onCreate(Bundle savedInstanceState) {
//		super.onCreate(savedInstanceState);
//		setContentView(R.layout.activity_firebase_test);
//
//		loadRes = (Button) findViewById(R.id.loadResBtn);
//		loadRes.setEnabled(false);
//		loadCat = (Button) findViewById(R.id.loadMenBtn);
//		loadCat.setEnabled(false);
//
//		linLay = (LinearLayout) findViewById(R.id.linLay);
//
//		spinnerRes = (Spinner) findViewById(R.id.spinnerRes);
//
//		//LiveData<List<Ristorante>> ristoranti = rr.getRistoranti();
//		MenuViewModel m = new MenuViewModel(this.getApplication());
//		List<Ristorante> ristoranti = m.getRistoranti();
//		List<Categoria> categorie = m.getCategoria();
//		RepositoryRistorante rr = m.getRepoRistorante();
//		RepositoryMenu rm = m.getRepoMenu();
//		//Creating the ArrayAdapter instance having the country list
//		ristoranti.add(new Ristorante("", "Nessun Ristorante Selezionato", "", ""));
//		ArrayAdapter aa = new ArrayAdapter(this, android.R.layout.simple_spinner_item, ristoranti);
//		//dovrebbe funzionare
//		aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		//Setting the ArrayAdapter data on the Spinner
//		spinnerRes.setAdapter(aa);
//
//		spinnerCat = (Spinner) findViewById(R.id.spinnerMen);
//		spinnerCat.setEnabled(false);
//		//RepositoryMenu rm = new RepositoryMenu(linLay);
//		//LiveData<List<Categoria>> categorie = rm.getCategoria();
//
//		//Creating the ArrayAdapter instance having the country list
//		categorie.add(new Categoria("Nessuna Categoria Selezionata"));
//		ArrayAdapter aaMenu = new ArrayAdapter(this, android.R.layout.simple_spinner_item, categorie);
//		aaMenu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		//Setting the ArrayAdapter data on the Spinner
//		spinnerCat.setAdapter(aaMenu);
//
//		// Ottengo un istanza del db
//		FirebaseDatabase database = FirebaseDatabase.getInstance();
//		// Punto la reference al "Branch" del json che mi interessa
//		DatabaseReference mDatabase = database.getReference("ristoranti");
//
//		// Creo una repositoryRistorante
//
//		// Applico il listener della repo alla mia reference nel db
//
////		mDatabase.addListenerForSingleValueEvent(rr.RistoranteFirebaseListener);
//
//		spinnerRes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//				Log.w("FBTest", ristoranti.get(position).toString());
//				resElemSelected = position;
//				if (position > 0) {
//					loadRes.setEnabled(true);
//					spinnerCat.setEnabled(true);
//				} else {
//					loadRes.setEnabled(false);
//					spinnerCat.setEnabled(false);
//				}
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> parent) {
//				// TODO Auto-generated method stub
//			}
//		});
//
//		spinnerCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//			@Override
//			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//				Log.w("FBTest", categorie.get(position).toString());
//				menElemSelected = position;
//				if (position > 0) {
//					loadCat.setEnabled(true);
//				} else {
//					loadCat.setEnabled(false);
//				}
//			}
//
//			@Override
//			public void onNothingSelected(AdapterView<?> parent) {
//				// TODO Auto-generated method stub
//			}
//		});
//
//		((Button) findViewById(R.id.loadResBtn)).setOnClickListener(v -> {
//			Log.w("FBTest", "Loading menu for item " + resElemSelected + " (" + ristoranti.get(resElemSelected).idRistorante + ")");
//			//TODO: Caricare menu
//			DatabaseReference mMenu = database.getReference("menu").child(ristoranti.get(resElemSelected).idRistorante);
//
//			mMenu.addListenerForSingleValueEvent(rm.MenuFirebaseListener);
//		});
//
//		((Button) findViewById(R.id.loadMenBtn)).setOnClickListener(v -> {
//			Log.w("FBTest", "Loading category for item " + menElemSelected + " (" + categorie.get(menElemSelected).nomeCategoria + ")");
//			linLay.removeAllViews();
//			for (Piatto p : categorie.get(menElemSelected).piatti) {
//				TextView tv = new TextView(linLay.getContext());
//				tv.setText(p.toString());
//				linLay.addView(tv);
//			}
//		});
//
//	}
	
	public void sendOrdineToFirebase(Tavolo tavolo, Ordine ordine) {
		// Ottengo un istanza del db
		FirebaseDatabase database = FirebaseDatabase.getInstance();
		// Punto la reference al "Branch" del json che mi interessa
		DatabaseReference mDatabase = database.getReference("ordini");
		mDatabase.child(tavolo.ristorante).child(String.valueOf(ordine.idOrdine)).setValue(ordine);
	}
}