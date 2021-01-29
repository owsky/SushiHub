package com.veneto_valley.veneto_valley.util;

import android.app.Application;

import androidx.annotation.NonNull;

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
import com.google.android.gms.nearby.connection.Strategy;

import java.util.concurrent.Executors;

public class Nearby {
	public static final Strategy STRATEGY = Strategy.P2P_STAR;
	private static Nearby nearby = null;
	private final Application application;
	private final String SERVICE_ID;
	private final PayloadCallback payloadCallback;
	private final Object lock = new Object();
	private String strendPointId;
	private volatile boolean isConnected = false;
	
	private Nearby(boolean client, Application application, String SERVICE_ID, PayloadCallback callback) {
		this.application = application;
		this.SERVICE_ID = SERVICE_ID; // table code
		payloadCallback = callback;
		if (client) {
			startDiscovery();
		} else {
			startAdvertising();
		}
	}
	
	public static Nearby getInstance(boolean client, Application application, String SERVICE_ID, PayloadCallback callback) {
		if (nearby == null) {
			nearby = new Nearby(client, application, SERVICE_ID, callback);
		}
		return nearby;
	}
	
	public void send(byte[] oggetto) {
		Executors.newSingleThreadExecutor().execute(() -> {
			synchronized (lock) {
				while (!isConnected) {
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
		com.google.android.gms.nearby.Nearby.getConnectionsClient(application).startAdvertising("Device A", SERVICE_ID, new ConnectionLifecycleCallback() {
			
			@Override
			public void onConnectionInitiated(@NonNull String endPointId, @NonNull ConnectionInfo connectionInfo) {
				com.google.android.gms.nearby.Nearby.getConnectionsClient(application).acceptConnection(endPointId, payloadCallback);
			}
			
			@Override
			public void onConnectionResult(@NonNull String endPointId, @NonNull ConnectionResolution connectionResolution) {
				if (connectionResolution.getStatus().getStatusCode() == ConnectionsStatusCodes.STATUS_OK) {
					strendPointId = endPointId;
					synchronized (lock) {
						isConnected = true;
						lock.notify();
					}
				}
			}
			
			@Override
			public void onDisconnected(@NonNull String s) {
				isConnected = false;
				strendPointId = null;
			}
		}, advertisingOptions);
	}
	
	private void sendPayLoad(final String endPointId, byte[] payload) {
		Payload bytesPayload = Payload.fromBytes(payload);
		com.google.android.gms.nearby.Nearby.getConnectionsClient(application).sendPayload(endPointId, bytesPayload);
	}
	
	private void startDiscovery() {
		DiscoveryOptions discoveryOptions = new DiscoveryOptions.Builder().setStrategy(STRATEGY).build();
		com.google.android.gms.nearby.Nearby.getConnectionsClient(application).
				startDiscovery(SERVICE_ID, new EndpointDiscoveryCallback() {
					
					@Override
					public void onEndpointFound(@NonNull String endpointId, @NonNull DiscoveredEndpointInfo discoveredEndpointInfo) {
						com.google.android.gms.nearby.Nearby.getConnectionsClient(application).
								requestConnection("Device B", endpointId, new ConnectionLifecycleCallback() {
									
									@Override
									public void onConnectionInitiated(@NonNull String endpointId, @NonNull ConnectionInfo connectionInfo) {
										com.google.android.gms.nearby.Nearby.getConnectionsClient(application).acceptConnection(endpointId, payloadCallback);
										strendPointId = endpointId;
									}
									
									@Override
									public void onConnectionResult(@NonNull String s, @NonNull ConnectionResolution connectionResolution) {
										if (connectionResolution.getStatus().getStatusCode() == ConnectionsStatusCodes.STATUS_OK) {
											synchronized (lock) {
												isConnected = true;
												lock.notify();
											}
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
		isConnected = false;
		nearby = null;
		com.google.android.gms.nearby.Nearby.getConnectionsClient(application).stopAllEndpoints();
	}
	
}
