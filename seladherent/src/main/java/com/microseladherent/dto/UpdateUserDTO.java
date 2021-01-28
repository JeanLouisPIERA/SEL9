package com.microseladherent.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.microseladherent.errors.IPasswordConfirmation;



@IPasswordConfirmation.List({
    @IPasswordConfirmation(
            password = "password",
            passwordConfirm = "passwordConfirm",
            message = "La confirmation de votre mot de passe n'est pas correcte"
    )
})
public class UpdateUserDTO {
	
	@NotEmpty(message="Merci de saisir votre identifiant actuel")
	@Size(min = 5, max = 25, message = "{user.name.invalid}")
	private String username;
	
	@NotEmpty(message="Merci de saisir votre e-mail actuel")
	@Email(message="{user.email.invalid}")
	private String adresseMail;
	
	
	@NotEmpty(message="Merci de saisir votre mot de passe actuel")
	private String password;
	
	
	@NotEmpty(message="Merci de confirmer votre mot de passe actuel")
	private String passwordConfirm;
	
	@NotEmpty(message=" Saisir votre nouveau identifiant ou maintenir ici votre ancien identifiant")
	private String usernameToChange;
	@NotEmpty(message=" Saisir votre nouvel email ou maintenir ici votre ancien email")
	private String adresseMailToChange;
	@NotEmpty(message=" Saisir votre nouveau mot de passe ou maintenir ici votre ancien mot de passe")
	private String passwordToChange;
	
		
	public UpdateUserDTO() {
		super();
	
	}
	

	public UpdateUserDTO(
			@NotEmpty(message = "Merci de saisir votre identifiant actuel") @Size(min = 5, max = 25, message = "{user.name.invalid}") String username,
			@NotEmpty(message = "Merci de saisir votre e-mail actuel") @Email(message = "{user.email.invalid}") String adresseMail,
			@NotEmpty(message = "Merci de saisir votre mot de passe actuel") String password,
			@NotEmpty(message = "Merci de confirmer votre mot de passe actuel") String passwordConfirm,
			@NotEmpty(message = " Saisir votre nouveau identifiant ou maintenir ici votre ancien identifiant") String usernameToChange,
			@NotEmpty(message = " Saisir votre nouvel email ou maintenir ici votre ancien email") String adresseMailToChange,
			@NotEmpty(message = " Saisir votre nouveau mot de passe ou maintenir ici votre ancien mot de passe") String passwordToChange) {
		super();
		this.username = username;
		this.adresseMail = adresseMail;
		this.password = password;
		this.passwordConfirm = passwordConfirm;
		this.usernameToChange = usernameToChange;
		this.adresseMailToChange = adresseMailToChange;
		this.passwordToChange = passwordToChange;
	}


	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getAdresseMail() {
		return adresseMail;
	}
	public void setAdresseMail(String adresseMail) {
		this.adresseMail = adresseMail;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPasswordConfirm() {
		return passwordConfirm;
	}
	public void setPasswordConfirm(String passwordConfirm) {
		this.passwordConfirm = passwordConfirm;
	}
	public String getUsernameToChange() {
		return usernameToChange;
	}
	public void setUsernameToChange(String usernameToChange) {
		this.usernameToChange = usernameToChange;
	}
	public String getAdresseMailToChange() {
		return adresseMailToChange;
	}
	public void setAdresseMailToChange(String adresseMailToChange) {
		this.adresseMailToChange = adresseMailToChange;
	}
	public String getPasswordToChange() {
		return passwordToChange;
	}
	public void setPasswordToChange(String passwordToChange) {
		this.passwordToChange = passwordToChange;
	}
	
	
	
	
}
