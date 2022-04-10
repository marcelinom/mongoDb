package com.spekuli.model.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document("mutex")
public class Mutex implements Serializable {
	private static final long serialVersionUID = 1L;
	public enum Status {IDLE, BUSY}
	
	@Id 
	private String id;
	private LocalDateTime data;
	private String owner;  	
	private Status status;  	
	
	public Mutex() {}
	
}
