package com.muon.arbitrage.security.model;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.security.core.GrantedAuthority;

import com.arangodb.springframework.annotation.Document;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(value = Include.NON_NULL)
@Document("User" )
public class User  {

	@Id
	private String id;
    
    private String username;
	
    private String password;
    
    private String name;
    
    private List<GrantedAuthority> authoritys;

    @DateTimeFormat(pattern = "dd/MM/yyyy", iso = ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate birthDate;
    
    private String cellphone;
    private String cellphonePrefix;
    
    private String identity;
    
    private IdentityType identityType;
    
    private boolean googleAuthGenerated;
    
    private boolean googleAuthEnable;
    
    private boolean active;

	private String verificationCode;
	
	//private GoogleCredentials googleAuthCredentials;
	
	private String tokenCredentials;
	
	private LocalDate registerDate;
	
	private String balance;
	

}
