package com.veneto_valley.veneto_valley;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.navigation.NavigationView;

import java.util.HashSet;
import java.util.Set;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
	private DrawerLayout drawer;
	private NavController navController;
	
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
		topLevelDestinations.add(R.id.homepageFragment);
		topLevelDestinations.add(R.id.listaPiattiFragment);
		AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(topLevelDestinations).setOpenableLayout(drawer).build();
		NavigationUI.setupActionBarWithNavController(this, navController, drawer);
		NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);
		
		navigationView.setNavigationItemSelectedListener(this);
	}
	
	@Override
	public boolean onSupportNavigateUp() {
		return NavigationUI.navigateUp(navController, drawer) || super.onSupportNavigateUp();
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
		switch (item.getItemId()) {
			case R.id.homepageFragment:
				navController.navigate(R.id.homepageFragment);
				break;
			case R.id.listaPiattiFragment:
				navController.navigate(R.id.listaPiattiFragment);
				break;
			default:
				NavigationUI.onNavDestinationSelected(item, navController);
		}
		
		drawer.closeDrawer(GravityCompat.START);
		return true;
	}
}