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
import android.widget.Toast;

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
	private final ActivityResultLauncher<String[]> requestPermissionLauncher = registerForActivityResult(
			new ActivityResultContracts.RequestMultiplePermissions(), Map::values);
	private final ActivityResultLauncher<Intent> requestBluetoothLauncher = registerForActivityResult(
			new ActivityResultContracts.StartActivityForResult(), result -> {
				if (!bluetoothAdapter.isEnabled()) {
					AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setMessage("SushiHub requires Bluetooth to work. Do you want to enable it?")
							.setPositiveButton("OK", (dialog, which) -> bluetoothAdapter.enable())
							.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
							.create().show();
				}
			});
	private final ActivityResultLauncher<Intent> requestWifiLauncher =
			registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
				Toast.makeText(this, "SushiHub requires WiFi to work", Toast.LENGTH_SHORT).show();
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
		topLevelDestinations.add(R.id.listsNav);
		topLevelDestinations.add(R.id.historyNav);
		appBarConfiguration = new AppBarConfiguration.Builder(topLevelDestinations).setOpenableLayout(drawer).build();
		NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
		navigationView.setNavigationItemSelectedListener(this);
		
		// permissions check
		for (String permesso : permissionCodes) {
			if (!(ContextCompat.checkSelfPermission(this, permesso) == PackageManager.PERMISSION_GRANTED)) {
				requestPermissionLauncher.launch(permissionCodes);
			}
		}
		
		// Bluetooth check
		requestBluetoothLauncher.launch(new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE));
		// Wifi check
		WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
		// Since Android Q Google removed the ability to enable WiFi programmatically
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
			// It opens a panel that shows that WiFi is disabled
			Intent enableWifiIntent = new Intent(Settings.Panel.ACTION_WIFI);
			if (!wifiManager.isWifiEnabled())
				requestWifiLauncher.launch(enableWifiIntent);
		} else {
			wifiManager.setWifiEnabled(true);
		}
	}
	
	@Override
	public boolean onSupportNavigateUp() {
		// it provides a custom implementation of the navigate up feature for selected fragments
		int destination = Objects.requireNonNull(navController.getCurrentDestination()).getId();
		if (destination == R.id.userInputNav || destination == R.id.userInputMenuNav) {
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
		// it handles navigation manually so that the actions can be customized depeding on the app's status
		if (item.getItemId() == R.id.listsNav) {
			SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
			if (preferences.contains("table_code")) {
				navController.navigate(R.id.listsNav);
			}
			else {
				navController.navigate(R.id.homepageNav);
			}
			drawer.closeDrawer(GravityCompat.START);
			return true;
		} else if (item.getItemId() == R.id.historyNav) {
			navController.navigate(R.id.historyNav);
			drawer.closeDrawer(GravityCompat.START);
			return true;
		}
		
		return false;
	}
	
	
}