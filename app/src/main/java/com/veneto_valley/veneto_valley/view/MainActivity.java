package com.veneto_valley.veneto_valley.view;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.MenuItem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;
import com.veneto_valley.veneto_valley.R;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
	private static final BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	private final ActivityResultLauncher<String[]> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), Map::values);
	private final ActivityResultLauncher<Intent> requestBluetoothLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
		if (!bluetoothAdapter.isEnabled()) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setMessage("SushiHub necessita del Bluetooth per funzionare. Vuoi abilitarlo?")
					.setPositiveButton("OK", (dialog, which) -> bluetoothAdapter.enable())
					.setNegativeButton("Annulla", (dialog, which) -> dialog.dismiss())
					.create().show();
		}
	});
	private final ActivityResultLauncher<Intent> requestWifiLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
	});
	private final String[] permissionCodes = {"android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"};
	private DrawerLayout drawer;
	private NavController navController;
	private AppBarConfiguration appBarConfiguration;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
		// Setup Navigation component
		navController = Navigation.findNavController(this, R.id.nav_host_fragment);
		drawer = findViewById(R.id.drawer_layout);
		NavigationView navigationView = findViewById(R.id.navigation_view);
		Set<Integer> topLevelDestinations = new HashSet<>();
		topLevelDestinations.add(R.id.homepageNav);
		topLevelDestinations.add(R.id.listeTabNav);
		topLevelDestinations.add(R.id.storicoNav);
		appBarConfiguration = new AppBarConfiguration.Builder(topLevelDestinations).setOpenableLayout(drawer).build();
		NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
		navigationView.setNavigationItemSelectedListener(this);
		
		// Controllo permessi
		for (String permesso : permissionCodes) {
			if (!(ContextCompat.checkSelfPermission(this, permesso) == PackageManager.PERMISSION_GRANTED)) {
				requestPermissionLauncher.launch(permissionCodes);
			}
		}
		
		// Controllo se Bluetooth è abilitato
		requestBluetoothLauncher.launch(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
		// Controllo se WiFi è abilitato
		WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			Intent enableWifiIntent = new Intent(Settings.Panel.ACTION_WIFI);
			if (!wifiManager.isWifiEnabled())
				requestWifiLauncher.launch(enableWifiIntent);
		} else {
			wifiManager.setWifiEnabled(true);
		}
	}
	
	@Override
	public boolean onSupportNavigateUp() {
		int destination = Objects.requireNonNull(navController.getCurrentDestination()).getId();
		if (destination == R.id.userInputNav) {
			CancelDialog dialog = new CancelDialog();
			dialog.show(getSupportFragmentManager(), null);
			return true;
		}
		return NavigationUI.navigateUp(navController, appBarConfiguration) || super.onSupportNavigateUp();
	}
	
	@Override
	public void onBackPressed() {
		if (drawer.isDrawerOpen(GravityCompat.START)) {
			drawer.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
	}
	
	@Override
	public boolean onNavigationItemSelected(@NonNull MenuItem item) {
		if (item.getItemId() == R.id.listeTabNav) {
			// Se vi è una sessione non terminata naviga verso listeTab, altrimenti homepage
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
			if (preferences.contains("codice_tavolo"))
				navController.navigate(R.id.listeTabNav);
			else
				navController.navigate(R.id.homepageNav);
		} else if (item.getItemId() == R.id.storicoNav) {
			navController.navigate(R.id.storicoNav);
		}
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}
	
	
}