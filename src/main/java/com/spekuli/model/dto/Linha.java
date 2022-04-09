package com.spekuli.model.dto;

import java.io.Serializable;

public class Linha implements Serializable {
	private static final long serialVersionUID = 1L;

	private PontoDV p1;
	private PontoDV p2;
	
	public Linha(PontoDV p1, PontoDV p2) {
		this.p1 = p1;
		this.p2 = p2;
	}
	public PontoDV getP1() {
		return p1;
	}
	public void setP1(PontoDV p1) {
		this.p1 = p1;
	}
	public PontoDV getP2() {
		return p2;
	}
	public void setP2(PontoDV p2) {
		this.p2 = p2;
	}
	
	
}
