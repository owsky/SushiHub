package com.veneto_valley.veneto_valley;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

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

import static android.content.ContentValues.TAG;

public class Connessione {
    public static final Strategy STRATEGY = Strategy.P2P_STAR;
    public static final String SERVICE_ID="120001";
    Activity base;
    String strendPointId;
    String risposta;
    NearbyTest cont;
    public Connessione(boolean client, boolean host, NearbyTest cont){

        this.cont=cont;
        base=cont.getActivity();

        if(client){
            startDiscovery();

        }else{
            startAdvertising();
        }
    }
    public void invia(String testo){
        sendPayLoad(strendPointId, testo);
    }

    private void startAdvertising () {
        AdvertisingOptions advertisingOptions = new AdvertisingOptions.Builder().setStrategy(STRATEGY).build();
        Nearby.getConnectionsClient(base).startAdvertising("Device A", SERVICE_ID, new ConnectionLifecycleCallback() {
            @Override
            public void onConnectionInitiated(@NonNull String endPointId, @NonNull ConnectionInfo connectionInfo) {
                Nearby.getConnectionsClient(base).acceptConnection(endPointId, mPayloadCallback);
            }
            @Override
            public void onConnectionResult(@NonNull String endPointId, @NonNull ConnectionResolution connectionResolution) {
                switch (connectionResolution.getStatus().getStatusCode()) {
                    case ConnectionsStatusCodes.STATUS_OK:
                        //siamo connessi possiamo iniziare a prendere i dati
                        strendPointId = endPointId;
                        Toast.makeText(cont.getActivity(), "siamo connessi",
                                Toast.LENGTH_LONG).show();
                        invia("mucche");
                        break;
                    case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
                        Toast.makeText(cont.getActivity(), "cn rif",
                            Toast.LENGTH_LONG).show();
                        break;
                    case ConnectionsStatusCodes.STATUS_ERROR:
                        Toast.makeText(cont.getActivity(), "cn err",
                                Toast.LENGTH_LONG).show();
                        break;
                    default:
                        //non si sa non facciamo nulla
                }
            }
            @Override
            public void onDisconnected(@NonNull String s) {
                strendPointId = null;
            }
        }, advertisingOptions);
    }
    private void sendPayLoad(final String endPointId, String testo) {
        Payload bytesPayload = Payload.fromBytes(testo.getBytes());
        Nearby.getConnectionsClient(base).sendPayload(endPointId, bytesPayload).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(cont.getActivity(), "inviato",
                        Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(cont.getActivity(), "errore",
                        Toast.LENGTH_LONG).show();
            }
        });
    }
    private void startDiscovery() {
        DiscoveryOptions discoveryOptions = new DiscoveryOptions.Builder().setStrategy(STRATEGY).build();
        Nearby.getConnectionsClient(base).
                startDiscovery(SERVICE_ID, new EndpointDiscoveryCallback() {
                    @Override
                    public void onEndpointFound(@NonNull String endpointId, @NonNull DiscoveredEndpointInfo discoveredEndpointInfo) {
                        Nearby.getConnectionsClient(base).
                                requestConnection("Device B", endpointId, new ConnectionLifecycleCallback() {
                                    @Override
                                    public void onConnectionInitiated(@NonNull String endpointId, @NonNull ConnectionInfo connectionInfo) {
                                        Nearby.getConnectionsClient(base).acceptConnection(endpointId, mPayloadCallback);
                                        strendPointId=endpointId;
                                    }
                                    @Override
                                    public void onConnectionResult(@NonNull String s, @NonNull ConnectionResolution connectionResolution) {
                                        switch (connectionResolution.getStatus().getStatusCode()) {
                                            case ConnectionsStatusCodes.STATUS_OK:
                                                Toast.makeText(cont.getActivity(), "connessi",
                                                        Toast.LENGTH_LONG).show();
                                                break;
                                            case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
                                                Toast.makeText(cont.getActivity(), "cn rif",
                                                        Toast.LENGTH_LONG).show();
                                                break;
                                            case ConnectionsStatusCodes.STATUS_ERROR:
                                                Toast.makeText(cont.getActivity(), "cn err",
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
    private final PayloadCallback mPayloadCallback = new PayloadCallback() {
        @Override
        public void onPayloadReceived(@NonNull String s, @NonNull Payload payload) {
            final   byte[] receivedBytes = payload.asBytes();
            base.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    risposta = new String(receivedBytes);
                    //Log.println(Log.INFO, "risposta", risposta);
                    Toast.makeText(cont.getActivity(), risposta,
                            Toast.LENGTH_LONG).show();
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
    public void closeConnection(){
        Nearby.getConnectionsClient(base).stopAllEndpoints();
    }

}
