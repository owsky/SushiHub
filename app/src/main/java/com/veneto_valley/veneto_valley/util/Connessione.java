package com.veneto_valley.veneto_valley.util;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.google.android.gms.nearby.Nearby;
import com.google.android.gms.nearby.connection.AdvertisingOptions;
import com.google.android.gms.nearby.connection.ConnectionInfo;
import com.google.android.gms.nearby.connection.ConnectionLifecycleCallback;
import com.google.android.gms.nearby.connection.ConnectionResolution;
import com.google.android.gms.nearby.connection.ConnectionsStatusCodes;
import com.google.android.gms.nearby.connection.DiscoveredEndpointInfo;
import com.google.android.gms.nearby.connection.DiscoveryOptions;
import com.google.android.gms.nearby.connection.EndpointDiscoveryCallback;
import com.google.android.gms.nearby.connection.Payload;
import com.google.android.gms.nearby.connection.PayloadCallback;
import com.google.android.gms.nearby.connection.PayloadTransferUpdate;
import com.google.android.gms.nearby.connection.Strategy;
import com.veneto_valley.veneto_valley.model.entities.Ordine;
import com.veneto_valley.veneto_valley.viewmodel.ConfirmedViewModel;
import com.veneto_valley.veneto_valley.viewmodel.MyViewModelFactory;

import java.util.concurrent.Executors;

public class Connessione {
	public static final Strategy STRATEGY = Strategy.P2P_STAR;
	private static Connessione connessione = null;
	private final Application application;
	private final String SERVICE_ID;
	private String strendPointId;
	private volatile boolean connesso = false;
	private final PayloadCallback mPayloadCallback;
	private final Object lock = new Object();
	
	private Connessione(boolean client, Application application, String SERVICE_ID, PayloadCallback callback) {
		this.application = application;
		this.SERVICE_ID = SERVICE_ID; //service id deve essere il numero del qr code generato
		mPayloadCallback = callback;
		if (client) {
			startDiscovery();
		} else {
			startAdvertising();
		}
	}
	
	public static Connessione getInstance(boolean client, Application application, String SERVICE_ID, PayloadCallback callback) {
		if (connessione == null) {
			connessione = new Connessione(client, application, SERVICE_ID, callback);
		}
		return connessione;
	}
	
	public void invia(byte[] oggetto) {
		Executors.newSingleThreadExecutor().execute(() -> {
			synchronized (lock) {
				while (!connesso) {
					try {
						lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			sendPayLoad(strendPointId, oggetto);
		});
	}
	
	private void startAdvertising() {
		AdvertisingOptions advertisingOptions = new AdvertisingOptions.Builder().setStrategy(STRATEGY).build();
		Nearby.getConnectionsClient(application).startAdvertising("Device A", SERVICE_ID, new ConnectionLifecycleCallback() {
			
			@Override
			public void onConnectionInitiated(@NonNull String endPointId, @NonNull ConnectionInfo connectionInfo) {
				Nearby.getConnectionsClient(application).acceptConnection(endPointId, mPayloadCallback);
			}
			
			@Override
			public void onConnectionResult(@NonNull String endPointId, @NonNull ConnectionResolution connectionResolution) {
				switch (connectionResolution.getStatus().getStatusCode()) {
					case ConnectionsStatusCodes.STATUS_OK:
						//siamo connessi possiamo iniziare a prendere i dati
						strendPointId = endPointId;
						Toast.makeText(application, "siamo connessi", Toast.LENGTH_LONG).show();
						synchronized (lock) {
							connesso = true;
							lock.notify();
						}
						break;
					case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
						Toast.makeText(application, "cn rif", Toast.LENGTH_LONG).show();
						break;
					case ConnectionsStatusCodes.STATUS_ERROR:
						Toast.makeText(application, "cn err", Toast.LENGTH_LONG).show();
						break;
				}
			}
			
			@Override
			public void onDisconnected(@NonNull String s) {
				connesso = false;
				strendPointId = null;
			}
		}, advertisingOptions);
	}
	
	private void sendPayLoad(final String endPointId, byte[] oggetto) {
		Payload bytesPayload = Payload.fromBytes(oggetto);
		Nearby.getConnectionsClient(application).sendPayload(endPointId, bytesPayload)
				.addOnSuccessListener(aVoid -> Toast.makeText(application, "inviato",
						Toast.LENGTH_LONG).show()).addOnFailureListener(e -> Toast.makeText(application,
				"errore", Toast.LENGTH_LONG).show());
	}
	
	private void startDiscovery() {
		DiscoveryOptions discoveryOptions = new DiscoveryOptions.Builder().setStrategy(STRATEGY).build();
		Nearby.getConnectionsClient(application).
				startDiscovery(SERVICE_ID, new EndpointDiscoveryCallback() {
					
					@Override
					public void onEndpointFound(@NonNull String endpointId, @NonNull DiscoveredEndpointInfo discoveredEndpointInfo) {
						Nearby.getConnectionsClient(application).
								requestConnection("Device B", endpointId, new ConnectionLifecycleCallback() {
									
									@Override
									public void onConnectionInitiated(@NonNull String endpointId, @NonNull ConnectionInfo connectionInfo) {
										Nearby.getConnectionsClient(application).acceptConnection(endpointId, mPayloadCallback);
										strendPointId = endpointId;
									}
									
									@Override
									public void onConnectionResult(@NonNull String s, @NonNull ConnectionResolution connectionResolution) {
										switch (connectionResolution.getStatus().getStatusCode()) {
											case ConnectionsStatusCodes.STATUS_OK:
												Toast.makeText(application, "connessi",
														Toast.LENGTH_LONG).show();
												synchronized (lock) {
													connesso = true;
													lock.notify();
												}
												break;
											case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
												Toast.makeText(application, "cn rif",
														Toast.LENGTH_LONG).show();
												break;
											case ConnectionsStatusCodes.STATUS_ERROR:
												Toast.makeText(application, "cn err",
														Toast.LENGTH_LONG).show();
												break;
										}
									}
									
									@Override
									public void onDisconnected(@NonNull String s) {
									}
								});
					}
					
					@Override
					public void onEndpointLost(@NonNull String s) {
						// disconnected
					}
				}, discoveryOptions);
	}
	
	public void closeConnection() {
		connesso = false;
		Nearby.getConnectionsClient(application).stopAllEndpoints();
	}
	
}
