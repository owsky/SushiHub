package com.veneto_valley.veneto_valley.model;

import com.veneto_valley.veneto_valley.model.entities.Tavolo;

import java.util.ArrayList;
import java.util.Random;

public class TestUtil {
	public static ArrayList<Tavolo> creaTavoli(int nTavoli) {
		ArrayList<Tavolo> tavoloArrayList = new ArrayList<>();
		Random rand = new Random();
		
		for (int i = 0; i < nTavoli; i++) {
			tavoloArrayList.add(new Tavolo(Integer.toString(rand.nextInt(1000)) + (char) (rand.nextInt(25) + 65), rand.nextInt(10), rand.nextFloat() * 10));
		}
		
		return tavoloArrayList;
	}
}
