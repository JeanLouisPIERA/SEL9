package com.microseladherent.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "roles")
public class Role implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", length = '5')
    private Long id;
    @Column(name = "name", length='5')
    private RoleEnum name;
	/*
	 * @JsonIgnore
	 * 
	 * @OneToMany(mappedBy="role", fetch=FetchType.LAZY) private Collection<User>
	 * users;
	 */
    @ManyToMany(mappedBy = "roles")
    private List<User> users = new ArrayList<User>();
    
	
    public Role() {
		super();
	}


	public Role(Long id, RoleEnum name) {
		super();
		this.id = id;
		this.name = name;
	}

	

	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public RoleEnum getName() {
		return name;
	}

	public void setName(RoleEnum name) {
		this.name = name;
	}


	public List<User> getUsers() {
		return users;
	}


	public void setUsers(List<User> users) {
		this.users = users;
	}


	@Override
	public String toString() {
		return "Role [id=" + id + ", name=" + name + ", users=" + users + "]";
	}


	


	

	

	
	
    
    
}
    
