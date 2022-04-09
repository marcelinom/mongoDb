package com.spekuli.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.spekuli.model.entity.Scalper;

public class ScalperSerie {
	private List<Scalper> serie;
	private int maxSize;
	
	public ScalperSerie(int size) {
		this.maxSize = size;
		this.serie = new ArrayList<Scalper>();
	}

	public List<Scalper> getSerie() {
		return serie;
	}

	public int getMaxSize() {
		return maxSize;
	}
	
	public void add(Scalper scalper) {
		if (serie.size()==maxSize) serie.remove(0);
		serie.add(scalper);
	}
	
	public Date data(int index) {
		return serie.get(index).getData();
	}
	
	public String codigo() {
		return serie.size()>0?serie.get(0).getCodigo():null;
	}
	
	public int total() {
		return serie.size();
	}
}
