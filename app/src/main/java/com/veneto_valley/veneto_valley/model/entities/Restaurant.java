package com.veneto_valley.veneto_valley.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

// Firebase entity
@IgnoreExtraProperties
public class Restaurant implements Parcelable {
	@Exclude
	public static final Parcelable.Creator<Restaurant> CREATOR = new Parcelable.Creator<Restaurant>() {
		@Override
		public Restaurant createFromParcel(Parcel in) {
			return new Restaurant(in);
		}
		
		@Override
		public Restaurant[] newArray(int size) {
			return new Restaurant[size];
		}
	};
	@Exclude
	public String id;
	public String name;
	public String address;
	public String city;
	public float menuPrice;
	
	public Restaurant() {
		// Default constructor required for calls to DataSnapshot.getValue(User.class)
	}
	
	protected Restaurant(Parcel in) {
		id = in.readString();
		name = in.readString();
		address = in.readString();
		city = in.readString();
		menuPrice = in.readFloat();
	}
	
	@NonNull
	@Override
	public String toString() {
		return this.name + " - " + this.city;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(name);
		dest.writeString(address);
		dest.writeString(city);
		dest.writeFloat(menuPrice);
	}
}


