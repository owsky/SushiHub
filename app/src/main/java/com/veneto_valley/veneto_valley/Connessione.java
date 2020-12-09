package com.veneto_valley.veneto_valley;

import android.util.Log;

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
import java.util.*;

import static android.content.ContentValues.TAG;

public class Connessione {
    public static final Strategy STRATEGY = Strategy.P2P_POINT_TO_POINT;
    public static final String SERVICE_ID="120001";
    MainActivity base;
    String strendPointId;
    public Connessione(boolean client, boolean host, MainActivity actv){
        base=actv;
        if(client){
            startDiscovery();
        }else{
            startAdvertising();
        }
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
                        // We're connected! Can now start sending and receiving data.
                        strendPointId = endPointId;
                        sendPayLoad(strendPointId);
                        break;
                    case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
                        // The connection was rejected by one or both sides.
                        break;
                    case ConnectionsStatusCodes.STATUS_ERROR:
                        // The connection broke before it was able to be accepted.
                        break;
                    default:
                        // Unknown status code
                }
            }
            @Override
            public void onDisconnected(@NonNull String s) {
                strendPointId = null;
            }
        }, advertisingOptions);
    }
    private void sendPayLoad(final String endPointId) {
        Payload bytesPayload = Payload.fromBytes("ciao bro come va?".getBytes());
        Nearby.getConnectionsClient(base).sendPayload(endPointId, bytesPayload).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.println(Log.INFO,"payload_inviato", "fatto");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.println(Log.INFO,"payload_inviato", "tuto mae");
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
                                    }
                                    @Override
                                    public void onConnectionResult(@NonNull String s, @NonNull ConnectionResolution connectionResolution) {
                                        switch (connectionResolution.getStatus().getStatusCode()) {
                                            case ConnectionsStatusCodes.STATUS_OK:
                                                // We're connected! Can now start sending and receiving data.
                                                break;
                                            case ConnectionsStatusCodes.STATUS_CONNECTION_REJECTED:
                                                // The connection was rejected by one or both sides.
                                                break;
                                            case ConnectionsStatusCodes.STATUS_ERROR:
                                                // The connection broke before it was able to be accepted.
                                                break;
                                            default:
                                                // Unknown status code
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
                    String risultato = new String(receivedBytes);
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
