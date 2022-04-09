package com.spekuli.model.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.spekuli.util.Symbol;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@Document("historicCrypto")
public class Cripto implements Serializable {
	private static final long serialVersionUID = 1L;
    public static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    public static final SimpleDateFormat sdfDia = new SimpleDateFormat("yyyyMMdd");
		
	@Id 
	private String chave;				// codigo do papel + _ + timestamp
	private LocalDate periodo;
	private String codigo;
	private String tipo;
	private String nome;
	private String especies;
	private Integer prazo;
	private BigDecimal abertura;  
	private BigDecimal maximo;  
	private BigDecimal minimo;  
	private BigDecimal ultimo;  
	private Long negocios;
	private Long titulos;
	private BigDecimal volume;
	private BigDecimal opcao;
	private Date vencimento;
		
    public static Cripto fromArray(List<Object> fields, Symbol symbol){
        return Cripto.builder()
        		.chave(symbol.getCode()+"_"+fields.get(0))
                .codigo(symbol.getCode())
                .nome(symbol.getNome())
                .periodo(Instant.ofEpochMilli((Long)fields.get(0)).atZone(ZoneId.systemDefault()).toLocalDate())
                .abertura(new BigDecimal(fields.get(1).toString()))
                .maximo(new BigDecimal(fields.get(2).toString()))
                .minimo(new BigDecimal(fields.get(3).toString()))
                .ultimo(new BigDecimal(fields.get(4).toString()))
                .volume(new BigDecimal(fields.get(5).toString()))
                .negocios(Long.parseLong(fields.get(8).toString()))
                .build();
    }
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((periodo == null) ? 0 : periodo.hashCode());
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cripto other = (Cripto) obj;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (periodo == null) {
			if (other.periodo != null)
				return false;
		} else if (!periodo.equals(other.periodo))
			return false;
		return true;
	}


}
