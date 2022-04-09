package com.spekuli.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.joda.time.DateTime;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.spekuli.model.dto.PontoDV;

import eu.verdelhan.ta4j.Decimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document("priceGap")
public class Gap implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id 
	private Long id;
	private String acao;
	private Date data;
	private BigDecimal valor;  	
	
	public Gap() {}
	
	public Gap(String acao, DateTime data, Decimal valor) {
		this.acao = acao;
		this.data = data.toDate();
		this.valor = valor.getDelegate();
	}
	
	public PontoDV toPontoDV() {
		return new PontoDV(data, valor.doubleValue());
	}
	
}
