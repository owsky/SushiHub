package com.veneto_valley.veneto_valley.model.entities;


import androidx.annotation.NonNull;

import java.util.ArrayList;

public class Category {
	public final String name;
	public final ArrayList<Dish> dishes;
	
	public Category(String name) {
		this.name = name;
		dishes = new ArrayList<>();
	}
	
	@NonNull
	@Override
	public String toString() {
		return this.name;
	}
}
