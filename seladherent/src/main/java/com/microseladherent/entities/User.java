package com.microseladherent.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;

@Entity
@Table(name="users")
public class User implements Serializable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(notes = "ID de l'utilisateur generee dans la base de donnees")
	@Column(name = "user_id", length=5)
	private Long id;
	
	/*
	 * @ApiModelProperty(notes= "ID du SEL d'adhésion")
	 * 
	 * @Column(name = "sel_id", length = 5, nullable=false, unique=true) private
	 * Long sel;
	 */
	
	@ApiModelProperty(notes= "Nom de l'utilisateur")
	@Column(name = "username", length = 100, nullable=false, unique=true)
	private String username;
	
	@Column(name = "password", length= 25, nullable=false)
	private String password;
	
	@Transient
	@JsonIgnore
	private String passwordConfirm;
	
	@ApiModelProperty(notes= "Adresse mail de l'utilisateur")
	@Column(name = "email", length = 100, nullable=false, unique=true)
	private String email;
	
	@ApiModelProperty(notes= "Date Adhesion")
	@Column(name = "date_adhesion_debut", nullable=false)
	private LocalDate dateAdhesionDebut;
	
	@ApiModelProperty(notes= "Date Fin Adhesion")
	@Column(name = "date_adhesion_fin", nullable=true)
	private LocalDate dateAdhesionFin;
	
	@ApiModelProperty(notes= "Date Début Blocage")
	@Column(name="date_blocage_debut", nullable=true)
	private LocalDate dateBlocageDebut;
	
	@ApiModelProperty(notes= "Date Fin Blocage")
	@Column(name="date_blocage_fin", nullable=true)
	private LocalDate dateBlocageFin;
	
	/*
	 * @ManyToOne
	 * 
	 * @JoinColumn(name="role_id") private Role role;
	 * 
	 * private Role role
	 */
	 @JsonIgnore 
	@ManyToMany(cascade = { CascadeType.ALL })
    @JoinTable(
        name = "users_roles", 
        joinColumns = { @JoinColumn(name = "user_id") }, 
        inverseJoinColumns = { @JoinColumn(name = "role_id") }
    )
    private List<Role> roles = new ArrayList<Role>();
	
	
	public User() {
		super();
	}
	

	public User(Long id, String username, String password, String email, LocalDate dateAdhesionDebut) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.email = email;
		this.dateAdhesionDebut = dateAdhesionDebut;
	}


	public User(Long id, String username, String password, String passwordConfirm, String email,
			LocalDate dateAdhesionDebut, LocalDate dateAdhesionFin, LocalDate dateBlocageDebut,
			LocalDate dateBlocageFin) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.passwordConfirm = passwordConfirm;
		this.email = email;
		this.dateAdhesionDebut = dateAdhesionDebut;
		this.dateAdhesionFin = dateAdhesionFin;
		this.dateBlocageDebut = dateBlocageDebut;
		this.dateBlocageFin = dateBlocageFin;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
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

	

	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}
	
	public LocalDate getDateAdhesionDebut() {
		return dateAdhesionDebut;
	}

	public void setDateAdhesionDebut(LocalDate dateAdhesionDebut) {
		this.dateAdhesionDebut = dateAdhesionDebut;
	}

	public LocalDate getDateAdhesionFin() {
		return dateAdhesionFin;
	}

	public void setDateAdhesionFin(LocalDate dateAdhesionFin) {
		this.dateAdhesionFin = dateAdhesionFin;
	}
	
	public LocalDate getDateBlocageDebut() {
		return dateBlocageDebut;
	}

	public void setDateBlocageDebut(LocalDate dateBlocageDebut) {
		this.dateBlocageDebut = dateBlocageDebut;
	}

	public LocalDate getDateBlocageFin() {
		return dateBlocageFin;
	}

	public void setDateBlocageFin(LocalDate dateBlocageFin) {
		this.dateBlocageFin = dateBlocageFin;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + ", password=" + password + ", passwordConfirm="
				+ passwordConfirm + ", email=" + email + ", dateAdhesionDebut=" + dateAdhesionDebut
				+ ", dateAdhesionFin=" + dateAdhesionFin + ", dateBlocageDebut=" + dateBlocageDebut
				+ ", dateBlocageFin=" + dateBlocageFin + ", roles=" + roles + "]";
	}
	
	
	
	

	

	
	
	
    
	
}
