package com.microseladherent.service;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.microseladherent.dto.UpdateUserDTO;
import com.microseladherent.dto.UserDTO;
import com.microseladherent.entities.User;
import com.microseladherent.exceptions.EntityAlreadyExistsException;
import com.microseladherent.exceptions.DeniedAccessException;
import com.microseladherent.exceptions.EntityNotFoundException;
import com.microseladherent.exceptions.WrongParametersException;

public interface IUserService {
	
	//ROLE USER
	
	/**
	 * Cette méthode permet à un adhérent de créer son compte avec le rôle adhérent 
	 * La date de création est renseignée automatiquement
	 * @param userDTO
	 * @return
	 * @throws EntityAlreadyExistsException
	 * @throws WrongParametersException
	 */
	User createAccount(UserDTO userDTO) 
			throws EntityAlreadyExistsException, WrongParametersException;
	/**
	 * Cette méthode permet à un adhérent de lire les données de son compte
	 * RGPD Compliant
	 * @param id
	 * @param userDTO
	 * @return
	 * @throws EntityNotFoundException
	 * @throws DeniedAccessException
	 * @throws WrongParametersException 
	 */
	User readAccount(Long id, UserDTO userDTO) 
			throws EntityNotFoundException, DeniedAccessException, WrongParametersException; 
	
	/**
	 * Cette méthode permet à un adhérent de mettre à jour toutes les données de son compte 
	 * RGPD Compliant
	 * @param id
	 * @param updateUserDTO
	 * @return
	 * @throws EntityNotFoundException
	 * @throws DeniedAccessException
	 * @throws WrongParametersException 
	 */
	User updateAccount(Long id, UpdateUserDTO updateUserDTO) 
			throws EntityNotFoundException, DeniedAccessException, WrongParametersException;
	
	/**
	 * Cette méthode permet à un adhérent de clôturer son compte sans le supprimer et de passer son rôle à ex-Adhérent
	 * La date de clôture est renseignée automatiquement
	 * Toutes les données sont anonymisées
	 * RGPD Compliant
	 * @param id
	 * @param userDTO
	 * @return
	 * @throws EntityNotFoundException
	 * @throws DeniedAccessException
	 * @throws WrongParametersException 
	 */
	User closeAccount( Long id, UserDTO userDTO) 
			throws EntityNotFoundException, DeniedAccessException, WrongParametersException;
	
	// ROLE BUREAU
	
	/**
	 * Cette méthode permet à un membre du Bureau de pouvoir afficher les comptes de tous les adhérents et leurs informations
	 * à l'exception du mot de passe affiché null
	 * @param userDTO
	 * @return
	 * @throws EntityNotFoundException
	 * @throws DeniedAccessException
	 * @throws WrongParametersException 
	 */
	List<User> showAllUsers(UserDTO userDTO) 
			throws EntityNotFoundException, DeniedAccessException, WrongParametersException;
	
	/**
	 * Cette méthode permet à un membre du Bureau de bloquer l'accès de son compte à un adhérent et passe son rôle à Locked
	 * Le nom du membre du bureau qui a bloqué est renseigné
	 * La date du blocage est renseignée
	 * Le mot de passe est modifié avec un préfixe numérique random 
	 * @param id
	 * @param userDTO
	 * @return
	 * @throws EntityNotFoundException
	 * @throws DeniedAccessException
	 * @throws WrongParametersException 
	 */
	User lockAccount(Long id, UserDTO userDTO) 
			throws EntityNotFoundException, DeniedAccessException, WrongParametersException;
	/**
	 * Cette méthode permet à un membre du Bureau de débloquer l'accès de son compte à un adhrent et repasse son rôle à Adhérent
	 * La date de déblocage est renseignée
	 * Le nom et le mot de passe utilisés par l'adhérent avant le blocage sont automatiqyement récupérés
	 * @param id
	 * @param userDTO
	 * @return
	 * @throws EntityNotFoundException
	 * @throws DeniedAccessException
	 * @throws WrongParametersException 
	 */
	User unlockAccount(Long id, UserDTO userDTO) 
			throws EntityNotFoundException, DeniedAccessException, WrongParametersException;

	// ROLE ADMIN
	
	/**
	 * Cette méthode permet à un administrateur de promouvoir un adhérent à un rôle de membre du Bureau
	 * @param id
	 * @param userDTO
	 * @return
	 * @throws EntityAlreadyExistsException
	 * @throws EntityNotFoundException
	 * @throws DeniedAccessException
	 * @throws WrongParametersException 
	 */
	User updateToBureau(Long id, UserDTO userDTO) 
			throws EntityAlreadyExistsException, EntityNotFoundException, DeniedAccessException, WrongParametersException;
	
	/**
	 * Cette méthode permet à un administrateur de rétrograder un membre du Bureau à un rôle d'Adhérent	
	 * @param id
	 * @param userDTO
	 * @return
	 * @throws EntityNotFoundException
	 * @throws DeniedAccessException
	 * @throws WrongParametersException 
	 */
	User closeBureau(Long id, UserDTO userDTO) 
			throws EntityNotFoundException, DeniedAccessException, WrongParametersException;
	
	/**
	 * Cette méthode permet à un administrateur de promouvoir un membre du Bureau à un rôle d'administrateur
	 * @param id
	 * @param userDTO
	 * @return
	 * @throws EntityNotFoundException
	 * @throws DeniedAccessException
	 * @throws EntityAlreadyExistsException
	 * @throws WrongParametersException 
	 */
	User updateToAdmin(Long id, UserDTO userDTO) 
			throws EntityNotFoundException, DeniedAccessException, EntityAlreadyExistsException, WrongParametersException ;
			
	/**
	 * Cette méthode permet à un administrateur de rétrograder un admistrateur à un rôle de membre du Bureau	
	 * @param id
	 * @param userDTO
	 * @return
	 * @throws EntityNotFoundException
	 * @throws DeniedAccessException
	 * @throws WrongParametersException 
	 */
	User closeAdmin(Long id, UserDTO userDTO) 
			throws EntityNotFoundException, DeniedAccessException, WrongParametersException;


	
	
	
}
