package com.veneto_valley.veneto_valley.util;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcelableUtil {
	// compressione parcelable in array di byte
	public static byte[] marshall(Parcelable parceable) {
		Parcel parcel = Parcel.obtain();
		parceable.writeToParcel(parcel, 0);
		byte[] bytes = parcel.marshall();
		parcel.recycle();
		return bytes;
	}
	
	// conversione array di byte in parcelable
	public static Parcel unmarshall(byte[] bytes) {
		Parcel parcel = Parcel.obtain();
		parcel.unmarshall(bytes, 0, bytes.length);
		parcel.setDataPosition(0);
		return parcel;
	}
}

