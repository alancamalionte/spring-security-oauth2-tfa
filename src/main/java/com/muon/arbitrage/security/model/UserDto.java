package com.muon.arbitrage.security.model;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class UserDto {
	
	private String id;
    private String username;
    private String password;
    private String passwordConfirm;
    private String name;
    private String identity;
    private IdentityType identityType;
    private long celular;
    
    @DateTimeFormat(pattern = "dd/MM/yyyy", iso = ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate birthDate;
    
    
}
