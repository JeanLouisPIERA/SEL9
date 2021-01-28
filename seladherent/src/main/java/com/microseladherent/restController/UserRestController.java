package com.microseladherent.restController;

import java.util.List;


import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import javax.ws.rs.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microseladherent.dao.IUserRepository;
import com.microseladherent.dto.UpdateUserDTO;
import com.microseladherent.dto.UserDTO;
import com.microseladherent.entities.User;
import com.microseladherent.exceptions.EntityAlreadyExistsException;
import com.microseladherent.exceptions.DeniedAccessException;
import com.microseladherent.exceptions.EntityNotFoundException;
import com.microseladherent.exceptions.WrongParametersException;
import com.microseladherent.service.ISecurityService;
import com.microseladherent.service.IUserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/sel")
@Validated
public class UserRestController {
	
	@Autowired
	private IUserRepository userRepository;
	@Autowired
	private IUserService userService;
	@Autowired
	ISecurityService securityService;

	
	//ENDPOINTS ACCESSIBLES A TOUS LES ROLES *******************************************************************
	
	
	/**
	 * Ce endpoint permet à l'adhérent de créer son compte
	 * Il renseigne le UserDTO des informations du compte à créer
	 * La date de création du compte est enregistrée
	 * @param user the user
	 * @return the user
	 * @throws MissingRequiredInformationException 
	 * @throws WrongParametersException 
	 * @throws EntityAlreadyExistsException 
	   */
	  @ApiOperation(value = "Enregistrement de son compte par un adhérent)", response = User.class)
		@ApiResponses(value = {
		        @ApiResponse(code = 201, message = "Le compte a été créé"),
		        @ApiResponse(code = 400, message = "Les informations fournies ne sont pas correctes"),
		        @ApiResponse(code = 404, message = "Ressource inexistante"),
		        @ApiResponse(code = 409, message = "Un compte existe déjà avec cette adresse mail"),
		        @ApiResponse(code = 423, message = "L'adresse mail n'est pas disponible"),
		        @ApiResponse(code = 500, message = "Le mot de passe de confirmation n'est pas correct")
		})
	  @PostMapping("/users/accounts")
	  public ResponseEntity<User> createAccount(
			  @Valid @RequestBody UserDTO userDTO
			  ) throws EntityAlreadyExistsException, WrongParametersException{
	    return new ResponseEntity<User>(userService.createAccount(userDTO), HttpStatus.OK);
	  }
	  

	
	/**
	   * Ce endpoint permet à un adhérent de consulter son compte
	   * Il renseigne son password pour s'authentifier car seul un adhérent peut consulter son propre compte
	   * RGPD COMPLIANT
	   * @param id
	   * @param userDTO
	   * @return
	   * @throws EntityNotFoundException
	   * @throws DeniedAccessException
	 * @throws WrongParametersException 
	   */
	  @GetMapping("/users/accounts/{id}")
	  public ResponseEntity<User> displayAccount(
			  @PathVariable @Valid Long id, 
			  @RequestBody @Valid UserDTO userDTO
			  ) throws EntityNotFoundException, DeniedAccessException, WrongParametersException {
		User userFound = userService.readAccount(id, userDTO);
		return new ResponseEntity<User>(userFound, HttpStatus.OK);
		  
	  }
	  
	  
	  /**
	   * Ce endpoint permet à l'utilisateur de mettre à jour les infos de son compte (adresse mail, etc ...) 
	   * Il s'identifie avec le UpdateUserDTO
	   * Il renseigne les éléments à modifier dans le UpdateUserDTO
	   * Il renseigne tous les paramètres du DTO qui ont la validation constraints @NotEmpty 
	   * RGPD COMPLIANT
	   * @param id
	   * @param userDTO
	   * @return
	 * @throws DeniedAccessException 
	 * @throws EntityNotFoundException 
	 * @throws WrongParametersException 
	   */
	  @PutMapping(value = "/users/accounts/update/{id}")
	  	public ResponseEntity<User> updateAccount(
	  			@PathVariable @Valid Long id, 
	  			@Valid @RequestBody UpdateUserDTO updateUserDTO
	  			) throws EntityNotFoundException, DeniedAccessException, WrongParametersException {
		  
		  User userToUpdate = userService.updateAccount(id, updateUserDTO); 
		  return new ResponseEntity<User>(userToUpdate, HttpStatus.OK);
		  
	  }
	  
	  
	  /**
	   * Ce endpoint permet à utilisateur de cloturer son compte définitivement en supprimant toutes les informations le concernant
	   * L'adhérent s'authentifie avec le UserDTO
	   * Tous les paramètres sont anonymisés CONTRAINTE RGPD
	   * La date de clôture est enregistrée
	   * Le compte reste persisté en respectant la CONTRAINTE ACID
	   * RGPD COMPLIANT
	   * @param Id
	   * @param userDTO
	   * @return
	 * @throws DeniedAccessException 
	 * @throws EntityNotFoundException 
	 * @throws WrongParametersException 
	   */
	  @PutMapping(value="/users/accounts/close/{id}")
	  public ResponseEntity<User> closeAccount(
			  @PathVariable @Valid Long id, 
			  @Valid @RequestBody UserDTO userDTO)
					  throws EntityNotFoundException, DeniedAccessException, WrongParametersException{
		  
		  User userToClose = userService.closeAccount(id, userDTO); 
		  return new ResponseEntity<User>(userToClose, HttpStatus.OK);
		  
	  }
	
	  
	  //ENDPOINTS ACCESSIBLES AU ROLE MEMBRE DU BUREAU ******************************************************************* 
	
	  /**
	   * Ce endpoint permet à un membre du bureau d'obtenir la liste de tous les utilisateurs
	   * Le membre du bureau s'identifie avec le UserDTO
	   * @return the list
	 * @throws DeniedAccessException 
	 * @throws EntityNotFoundException 
	 * @throws WrongParametersException 
	   */
	  @GetMapping("/bureau/accounts")
	  public ResponseEntity<List<User>> showAllUsers(@Valid @RequestBody UserDTO userDTO
			  ) throws EntityNotFoundException, DeniedAccessException, WrongParametersException {
	    return new ResponseEntity<List<User>>(userService.showAllUsers(userDTO), HttpStatus.OK);
	  }
	  
	  /**
	   * Ce endpoint permet à un membre du Bureau de bloquer le compte d'un adhérent sans supprimer ses infos
	   * Le nom d'utilisateur est modifié en incluant le nom du membre du bureau à l'origine du blocage
	   * Le mot de passe de l'utilisateur est modifié avec un préfixe numérique aléatoire 
	   * @param Id
	   * @param userDTO
	   * @return
	 * @throws DeniedAccessException 
	 * @throws EntityNotFoundException 
	 * @throws WrongParametersException 
	   */
	  @PutMapping(value = "/bureau/accounts/lock/{id}")
	  	public ResponseEntity<User> lockAccount(
	  			@PathVariable @Valid Long id, 
	  			@Valid @RequestBody UserDTO userDTO) throws EntityNotFoundException, DeniedAccessException, WrongParametersException {
		  User userToLock = userService.lockAccount(id, userDTO); 
		  return new ResponseEntity<User>(userToLock, HttpStatus.OK);
		  
	  }
	  
	  /**
	   * Ce endpoint permet à un membre du Bureau de débloquer le compte d'un adhérent en retouvant ses infos d'origine
	   * @param Id
	   * @param userDTO
	   * @return
	 * @throws DeniedAccessException 
	 * @throws EntityNotFoundException 
	 * @throws WrongParametersException 
	   */
	  @PutMapping(value = "/bureau/accounts/unlock/{id}")
	  	public ResponseEntity<User> unlockAccount(
	  			@PathVariable @Valid Long id, 
	  			@Valid @RequestBody UserDTO userDTO) throws EntityNotFoundException, DeniedAccessException, WrongParametersException {
		  User userToLock = userService.unlockAccount(id, userDTO); 
		  return new ResponseEntity<User>(userToLock, HttpStatus.OK);
		  
	  }
	  
	  
	  //ENDPOINTS ACCESSIBLES AU ROLE ADMINISTRATEUR***********************************************************************
	  	  
	  /**
	   * Ce endpoint permet à un administrateur de promouvoir un adhérent à un rôle de membre du Bureau
	   * @param id
	   * @param userDTO
	   * @return
	   * @throws EntityAlreadyExistsException
	   * @throws WrongParametersException
	   * @throws EntityNotFoundException
	   * @throws DeniedAccessException
	   */
	  @PutMapping("/admin/accounts/bureau/{id}")
	  public ResponseEntity<User> createBureau(
			  @PathVariable @Valid Long id,
			  @Valid @RequestBody UserDTO userDTO)
			  throws EntityAlreadyExistsException, 
			  WrongParametersException,  
			  EntityNotFoundException, 
			  DeniedAccessException {
	    return new ResponseEntity<User>(userService.updateToBureau(id, userDTO), HttpStatus.OK);
	  }
	  
	  /**
	   * Ce endpoint permet à un administrateur de rétrograder un membre du Bureau à un rôle d'Adhérent
	   * @param Id
	   * @param userDTO
	   * @return
	 * @throws DeniedAccessException 
	 * @throws EntityNotFoundException 
	 * @throws WrongParametersException 
	   */
	  @PutMapping(value = "/admin/accounts/bureau/close/{id}")
	  	public ResponseEntity<User> closeBureau(
	  			@PathVariable @Valid Long id, 
	  			@Valid @RequestBody UserDTO userDTO) throws EntityNotFoundException, DeniedAccessException, WrongParametersException {
		  return new ResponseEntity<User>(userService.closeBureau(id,userDTO), HttpStatus.OK);
		  
	  }
	  
	  /**
	   * Ce endpoint permet à un administrateur de promouvoir un membre du Bureau à un rôle d'Administrateur
	   * @param id
	   * @param userDTO
	   * @return
	   * @throws EntityAlreadyExistsException
	   * @throws WrongParametersException
	   * @throws EntityNotFoundException
	   * @throws DeniedAccessException
	   */
	  @PutMapping("/admin/accounts/admin/{id}")
	  public ResponseEntity<User> createAdmin(
			  @PathVariable @Valid Long id,
			  @Valid @RequestBody UserDTO userDTO) 
			  throws EntityAlreadyExistsException, 
			  WrongParametersException,  
			  EntityNotFoundException, 
			  DeniedAccessException {
	    return new ResponseEntity<User>(userService.updateToAdmin(id, userDTO), HttpStatus.OK);
	  }
	  
	  /**
	   * Ce endpoint permet à un administrateur de rétrograder un administrateur au rôle de membre du Bureau
	   * @param Id
	   * @param userDTO
	   * @return
	 * @throws DeniedAccessException 
	 * @throws EntityNotFoundException 
	 * @throws WrongParametersException 
	   */
	  @PutMapping(value = "/admin/accounts/admin/close/{id}")
	  	public ResponseEntity<User> closeAdmin(
	  			@PathVariable @Valid Long id, 
	  			@Valid @RequestBody UserDTO userDTO) throws EntityNotFoundException, DeniedAccessException, WrongParametersException {
		  return new ResponseEntity<User>(userService.closeAdmin(id,userDTO), HttpStatus.OK);
		  
	  }
	  
	  

}
