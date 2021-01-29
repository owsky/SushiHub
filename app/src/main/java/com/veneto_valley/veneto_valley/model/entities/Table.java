package com.veneto_valley.veneto_valley.model.entities;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Calendar;
import java.util.Date;

@Entity
public class Table implements Parcelable {
	public static final Parcelable.Creator<Table> CREATOR = new Parcelable.Creator<Table>() {
		@Override
		public Table createFromParcel(Parcel in) {
			return new Table(in);
		}
		
		@Override
		public Table[] newArray(int size) {
			return new Table[size];
		}
	};
	final static String TAG = "ETavoloLog";
	@PrimaryKey
	@NonNull
	public final String id;
	public String name;
	public Date date;
	public float menuPrice;
	public String restaurant;
	public boolean checkedOut;
	
	public Table(@NonNull String id) {
		this.id = id;
	}
	
	@Ignore
	public Table(@NonNull String id, float menuPrice) {
		this.id = id;
		this.menuPrice = menuPrice;
		this.date = Calendar.getInstance().getTime();
		checkedOut = false;
		Log.d(TAG, "Current time => " + this.date);
	}
	
	@Ignore
	public Table(String id, String name, float menuPrice) {
		this(id, menuPrice);
		this.name = name;
	}
	
	@Ignore
	public Table(String id, float menuPrice, String restaurant) {
		this(id, menuPrice);
		this.restaurant = restaurant;
	}
	
	@Ignore
	protected Table(Parcel in) {
		id = in.readString();
		name = in.readString();
		long tmpDate = in.readLong();
		date = tmpDate != -1 ? new Date(tmpDate) : null;
		menuPrice = in.readFloat();
		restaurant = in.readString();
	}
	
	@Override
	public boolean equals(@Nullable Object obj) {
		// If the object is compared with itself then return true
		if (obj == this) {
			return true;
		}

        /* Check if o is an instance of Complex or not
          "null instanceof [type]" also returns false */
		if (!(obj instanceof Table)) {
			return false;
		}
		
		// typecast o to Complex so that we can compare data members
		Table t = (Table) obj;
		
		// Compare the data members and return accordingly
		return this.id.equals(t.id);
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(name);
		dest.writeLong(date != null ? date.getTime() : -1L);
		dest.writeFloat(menuPrice);
		dest.writeString(restaurant);
	}
}
