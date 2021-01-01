package com.veneto_valley.veneto_valley.util;

import android.app.Activity;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.veneto_valley.veneto_valley.model.entities.Ordine;
import com.veneto_valley.veneto_valley.viewmodel.ConfirmedViewModel;
import com.veneto_valley.veneto_valley.viewmodel.DeliveredViewModel;
import com.veneto_valley.veneto_valley.viewmodel.MyViewModelFactory;

import java.io.IOException;

public class Connessione {
	public static final Strategy STRATEGY = Strategy.P2P_STAR;
	public long semaforo = 0;
	public byte[] ricevuto = null;    //qui puoi prendere le richieste che ti arrivano
	//public static final String SERVICE_ID="120001";
	String SERVICE_ID;
	String strendPointId;
	Activity cont;
	boolean client = false;
	boolean connesso = false;
	private ConfirmedViewModel viewModel;
	private final PayloadCallback mPayloadCallback = new PayloadCallback() {
		@Override
		public void onPayloadReceived(@NonNull String s, @NonNull Payload payload) {
			final byte[] receivedBytes = payload.asBytes();
			cont.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Ordine risposta;
					semaforo = 1;
					ricevuto = receivedBytes;
					semaforo = 0;
					MyViewModelFactory factory = new MyViewModelFactory(cont.getApplication(), SERVICE_ID);
					viewModel = new ViewModelProvider((ViewModelStoreOwner) cont, factory).get(ConfirmedViewModel.class);
					try {
						risposta = Ordine.getFromBytes(ricevuto);
						if (risposta.status.equals("confirmed")) {
							viewModel.insert(risposta);
						} else if (risposta.status.equals("pending")) {
							viewModel.delete(risposta);
						} else {
							viewModel.update(risposta);
						}
					} catch (IOException e) {
						e.printStackTrace();
					} catch (ClassNotFoundException e) {
						e.printStackTrace();
					}
				}
			});
		}
		
		@Override
		public void onPayloadTransferUpdate(@NonNull String s,
		                                    @NonNull PayloadTransferUpdate payloadTransferUpdate) {
			if (payloadTransferUpdate.getStatus() == PayloadTransferUpdate.Status.SUCCESS) {
				// Do something with is here...
			}
		}
	};
	private DeliveredViewModel viewModelDelivered;
	
	public Connessione(boolean client, Activity cont, String SERVICE_ID) {
		this.cont = cont;
		this.SERVICE_ID = SERVICE_ID; //service id deve essere il numero del qr code generato
		this.client = client;
		if (client) {
			startDiscovery();
		} else {
			startAdvertising();
		}
	}
	
	public void invia(byte[] oggetto) {
		if (connesso) {
			sendPayLoad(strendPointId, oggetto);
		} else {
			if (client) {
				startDiscovery();
			} else {
				startAdvertising();
			}
		}
	}
	
	private void startAdvertising() {
		AdvertisingOptions advertisingOptions = new AdvertisingOptions.Builder().setStrategy(STRATEGY).build();
		Nearby.getConnectionsClient(cont).startAdvertising("Device A", SERVICE_ID, new ConnectionLifecycleCallback() {
			@Override
			public void onConnectionInitiated(@NonNull String endPointId, @NonNull ConnectionInfo connectionInfo) {
				Nearby.getConnectionsClient(cont).acceptConnection(endPointId, mPayloadCallback);
			}
			
			@Override
			public void onConnectionResult(@NonNull String endPointId, @NonNull ConnectionResolution connectionResolution) {
				switch (connectionResolution.getStatus().getStatusCode()) {
					case ConnectionsStatusCodes.STATUS_OK:
						//siamo connessi possiamo iniziare a prendere i dati
						strendPointId = endPointId;
						Toast.makeText(cont, "siamo connessi",
								Toast.LENGTH_LONG).show();
						connesso = true;
						break;
					case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
						Toast.makeText(cont, "cn rif",
								Toast.LENGTH_LONG).show();
						break;
					case ConnectionsStatusCodes.STATUS_ERROR:
						Toast.makeText(cont, "cn err",
								Toast.LENGTH_LONG).show();
						break;
					default:
						//non si sa non facciamo nulla
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
		Nearby.getConnectionsClient(cont).sendPayload(endPointId, bytesPayload).addOnSuccessListener(new OnSuccessListener<Void>() {
			@Override
			public void onSuccess(Void aVoid) {
				Toast.makeText(cont, "inviato",
						Toast.LENGTH_LONG).show();
			}
		}).addOnFailureListener(new OnFailureListener() {
			@Override
			public void onFailure(@NonNull Exception e) {
				Toast.makeText(cont, "errore",
						Toast.LENGTH_LONG).show();
			}
		});
	}
	
	private void startDiscovery() {
		DiscoveryOptions discoveryOptions = new DiscoveryOptions.Builder().setStrategy(STRATEGY).build();
		Nearby.getConnectionsClient(cont).
				startDiscovery(SERVICE_ID, new EndpointDiscoveryCallback() {
					@Override
					public void onEndpointFound(@NonNull String endpointId, @NonNull DiscoveredEndpointInfo discoveredEndpointInfo) {
						Nearby.getConnectionsClient(cont).
								requestConnection("Device B", endpointId, new ConnectionLifecycleCallback() {
									@Override
									public void onConnectionInitiated(@NonNull String endpointId, @NonNull ConnectionInfo connectionInfo) {
										Nearby.getConnectionsClient(cont).acceptConnection(endpointId, mPayloadCallback);
										strendPointId = endpointId;
									}
									
									@Override
									public void onConnectionResult(@NonNull String s, @NonNull ConnectionResolution connectionResolution) {
										switch (connectionResolution.getStatus().getStatusCode()) {
											case ConnectionsStatusCodes.STATUS_OK:
												Toast.makeText(cont, "connessi",
														Toast.LENGTH_LONG).show();
												connesso = true;
												break;
											case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
												Toast.makeText(cont, "cn rif",
														Toast.LENGTH_LONG).show();
												break;
											case ConnectionsStatusCodes.STATUS_ERROR:
												Toast.makeText(cont, "cn err",
														Toast.LENGTH_LONG).show();
												break;
											default:
												//non si sa non facciamo nulla
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
		Nearby.getConnectionsClient(cont).stopAllEndpoints();
	}
	
}
