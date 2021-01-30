package com.veneto_valley.veneto_valley.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.veneto_valley.veneto_valley.util.ParcelableUtil;

@Entity
@IgnoreExtraProperties
public class Order implements Parcelable {
    @Exclude
    public static final Parcelable.Creator<Order> CREATOR = new Parcelable.Creator<Order>() {
        @Override
        public Order createFromParcel(Parcel in) {
            return new Order(in);
        }

        @Override
        public Order[] newArray(int size) {
            return new Order[size];
        }
    };
    @PrimaryKey(autoGenerate = true)
    @Exclude
    public long id;
    @Exclude
    public OrderStatus status;
    public String desc;
    @Exclude
    public float price;
    @Exclude
    public boolean receivedFromSlave;

    //1-N Relations
    @Exclude
    public String tableCode;
    public String dish;
    @Exclude
    public String user;

    @Ignore
    public Order() {
    }

    public Order(String tableCode, String dish, OrderStatus status, String user, boolean receivedFromSlave) {
        this.tableCode = tableCode;
        this.dish = dish;
        this.status = status;
        this.user = user;
        this.receivedFromSlave = receivedFromSlave;
    }

    @Ignore
    protected Order(Parcel in) {
        status = OrderStatus.valueOf(in.readString());
        desc = in.readString();
        tableCode = in.readString();
        dish = in.readString();
        user = in.readString();
    }

    @Exclude
    public static Order getFromBytes(byte[] order) {
        Parcel parcel = ParcelableUtil.unmarshal(order);
        return new Order(parcel);
    }

    @Exclude
    public byte[] getBytes() {
        return ParcelableUtil.marshall(this);
    }

    @Override
    @Exclude
    public int describeContents() {
        return 0;
    }

    @Override
    @Exclude
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(status.toString());
        dest.writeString(desc);
        dest.writeString(tableCode);
        dest.writeString(dish);
        dest.writeString(user);
    }

    public enum OrderStatus {
        pending,
        confirmed,
        delivered,
        insertOrder,
        deliverOrder,
        undoDeliverOrder,
        deleteOrder
    }
}