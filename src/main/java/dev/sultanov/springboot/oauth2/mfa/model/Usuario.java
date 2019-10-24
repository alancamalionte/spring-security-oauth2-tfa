package dev.sultanov.springboot.oauth2.mfa.model;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Usuario  {

	@Id
	private String id;
    
    private String username;
	
    private String password;
    
    private String passwordConfirm;
    
    private String name;
    
    private List<GrantedAuthority> authoritys;

    @DateTimeFormat(pattern = "dd/MM/yyyy", iso = ISO.DATE_TIME)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dataNascimento;
    
    private String identity;
    
    private IdentityType identityType;
    
    private long celular;
    
    private boolean googleAuthGenerated;
    
    private boolean active;

	private String verificationCode;
	
	private GoogleCredentials googleAthCredentials;
    

}
