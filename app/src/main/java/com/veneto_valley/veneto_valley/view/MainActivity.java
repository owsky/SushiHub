package com.veneto_valley.veneto_valley.view;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
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
	private final ActivityResultLauncher<String[]> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), Map::values);
	private final ActivityResultLauncher<Intent> requestBluetoothLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
		//TODO: show dialog
	});
	private DrawerLayout drawer;
	private NavController navController;
	private AppBarConfiguration appBarConfiguration;
	private String[] permissionCodes;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		
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
		
		try {
			PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_PERMISSIONS);
			permissionCodes = info.requestedPermissions;
		} catch (PackageManager.NameNotFoundException e) {
			e.printStackTrace();
		}
		for (String permesso : permissionCodes) {
			if (!(ContextCompat.checkSelfPermission(this, permesso) == PackageManager.PERMISSION_GRANTED)) {
				requestPermissionLauncher.launch(permissionCodes);
			}
		}
		
		BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (!bluetoothAdapter.isEnabled()) {
			Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			requestBluetoothLauncher.launch(enableBtIntent);
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