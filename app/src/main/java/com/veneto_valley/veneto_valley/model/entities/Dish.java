package com.veneto_valley.veneto_valley.model.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Dish implements Parcelable {
	@Exclude
	public static final Parcelable.Creator<Dish> CREATOR = new Parcelable.Creator<Dish>() {
		
		@Override
		public Dish createFromParcel(Parcel source) {
			return new Dish(source);
		}
		
		@Override
		public Dish[] newArray(int size) {
			return new Dish[size];
		}
	};
	
	@Exclude
	public String id;
	public String name;
	public float price;
	
	public Dish() {
		// Default constructor required for calls to DataSnapshot.getValue(Piatto.class)
	}
	
	protected Dish(Parcel in) {
		id = in.readString();
		name = in.readString();
		price = in.readFloat();
	}
	
	@NonNull
	@Override
	public String toString() {
		return this.id + " - " + this.name;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	@Exclude
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(name);
		dest.writeFloat(price);
	}
	
}