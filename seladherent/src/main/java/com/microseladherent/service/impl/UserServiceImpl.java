package com.microseladherent.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.microseladherent.dao.IRoleRepository;
import com.microseladherent.dao.IUserRepository;
import com.microseladherent.dto.UpdateUserDTO;
import com.microseladherent.dto.UpdateUserMapperImpl;
import com.microseladherent.dto.UserDTO;
import com.microseladherent.dto.UserMapperImpl;
import com.microseladherent.entities.Role;
import com.microseladherent.entities.RoleEnum;
import com.microseladherent.entities.User;
import com.microseladherent.exceptions.EntityAlreadyExistsException;
import com.microseladherent.exceptions.DeniedAccessException;
import com.microseladherent.exceptions.EntityNotFoundException;
import com.microseladherent.exceptions.WrongParametersException;
import com.microseladherent.service.IUserService;

@Service
public class UserServiceImpl implements IUserService{
	
	@Autowired
	private IUserRepository userRepository;
	@Autowired
	private IRoleRepository roleRepository;
	/*
	 * @Autowired private BCryptPasswordEncoder bCryptPasswordEncoder;
	 */
	@Autowired private 
	SecurityServiceImpl securityService; 
	@Autowired
	private UserMapperImpl userMapper;
	@Autowired
	private UpdateUserMapperImpl updateUserMapper;
	
	//ROLE ADHERENT***********************************************************************************************
	
	/**
	 * Cette méthode permet à un adhérent de créer son compte avec le rôle adhérent 
	 * La date de création est renseignée automatiquement
	 * @param userDTO
	 * @return
	 * @throws EntityAlreadyExistsException
	 * @throws WrongParametersException
	 */
	@Override
	public User createAccount(UserDTO userDTO) 
			throws EntityAlreadyExistsException, WrongParametersException {
				
		Optional<User> usernameAlreadyExists = userRepository.findByUsername(userDTO.getUsername());
		if (usernameAlreadyExists.isPresent()) 
			throw new WrongParametersException("Ce nom d'adhérent est déjà utilisé"); 
		
		Optional<User> adresseMailAlreadyExists = userRepository.findByEmail(userDTO.getAdresseMail());
		if(adresseMailAlreadyExists.isPresent())
			throw new EntityAlreadyExistsException("Un compte d'adhérent existe déjà pour cette adresse mail");
			
		User userToCreate = userMapper.userDTOToUser(userDTO);
			
	    userToCreate.setDateAdhesionDebut(LocalDate.now());
	    userToCreate.getRoles().add(roleRepository.findByName(RoleEnum.ADHERENT));
	    return userRepository.save(userToCreate);
	}
	
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
	@Override
	public User readAccount(Long id, UserDTO userDTO) 
			throws EntityNotFoundException, DeniedAccessException, WrongParametersException {
		
		//User userAuthenticated = securityService.autologin(userDTO);
		
		Optional<User> userFound = userRepository.findById(id);
		if(!userFound.isPresent())
			throw new EntityNotFoundException("Ce compte n'existe pas");
		if(userFound.get().getUsername().contains("closedAccount/") || userFound.get().getUsername().contains("lockedAccount/"))
			throw new DeniedAccessException("Consultation impossible : ce compte est bloqué ou clôturé");	
		if(!userDTO.getPassword().equals(userFound.get().getPassword()) && !userDTO.getUsername().equals(userFound.get().getUsername()))
			throw new WrongParametersException(" Consultation interdite : un adhérent ne peut consulter que son propre compte");
		
		
		
		return userFound.get();
		
	}
		
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
	@Override
	public User updateAccount(Long id, UpdateUserDTO updateUserDTO) 
			throws EntityNotFoundException, DeniedAccessException, WrongParametersException {
		
		Optional<User> userFound = userRepository.findById(id);
		if(!userFound.isPresent())
			throw new EntityNotFoundException("Ce compte n'existe pas");
		if(userFound.get().getUsername().contains("closedAccount/") || userFound.get().getUsername().contains("lockedAccount/"))
			throw new DeniedAccessException("Consultation impossible : ce compte est bloqué ou clôturé");	
		if(!updateUserDTO.getPassword().equals(userFound.get().getPassword()) && !updateUserDTO.getUsername().equals(userFound.get().getUsername()))
			throw new WrongParametersException(" Consultation interdite : un adhérent ne peut consulter que son propre compte");
		
		userFound.get().setPassword(updateUserDTO.getPasswordToChange());
		userFound.get().setUsername(updateUserDTO.getUsernameToChange());
		userFound.get().setEmail(updateUserDTO.getAdresseMailToChange());
		
		return userRepository.save(userFound.get());
	}

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
	@Override
	public User closeAccount(Long id, UserDTO userDTO) throws EntityNotFoundException, DeniedAccessException, WrongParametersException {
		
		Optional<User> userFound = userRepository.findById(id);
		if(!userFound.isPresent())
			throw new EntityNotFoundException("Ce compte n'existe pas");
		if(userFound.get().getUsername().contains("closedAccount/") || userFound.get().getUsername().contains("lockedAccount/"))
			throw new DeniedAccessException("Consultation impossible : ce compte est bloqué ou clôturé");	
		if(!userDTO.getPassword().equals(userFound.get().getPassword()) && !userDTO.getUsername().equals(userFound.get().getUsername()))
			throw new WrongParametersException(" Consultation interdite : un adhérent ne peut consulter que son propre compte");
		
		
		LocalDate dateCloture = LocalDate.now();
		
		//Anonymisation des paramètres du compte clôturé - La composition de ce string est unique et respecte la contrainte ACID
		int number = 0;
		Optional<List<User>> listAccountClosedThisDateCloture = userRepository.findByDateAdhesionFin(dateCloture);
			if(listAccountClosedThisDateCloture.isEmpty()) {number=1;}
			else {number = listAccountClosedThisDateCloture.get().size()+1;};
		String usernameClosedAccount = new StringBuilder("closedAccount/").append(number).append(dateCloture.toString()).toString();
		
		
		userFound.get().setUsername(usernameClosedAccount);	
		userFound.get().setEmail(usernameClosedAccount);
		userFound.get().setPassword(usernameClosedAccount);
		userFound.get().setPasswordConfirm(usernameClosedAccount);
		userFound.get().setDateAdhesionFin(dateCloture);
		
		userFound.get().getRoles().clear();
		userFound.get().getRoles().add(roleRepository.findByName(RoleEnum.EXADHERENT));
		
		return userRepository.save(userFound.get());
	}
	
	
	
	// ROLE BUREAU**************************************************************************************************

	
	/**
	 * Cette méthode permet à un membre du Bureau de pouvoir afficher les comptes de tous les adhérents et leurs informations
	 * à l'exception du mot de passe affiché null
	 * @param userDTO
	 * @return
	 * @throws EntityNotFoundException
	 * @throws DeniedAccessException
	 */
	@Override
	public List<User> showAllUsers(UserDTO userDTO) throws EntityNotFoundException, DeniedAccessException {
		
		Optional<User> userBureau = userRepository.findByUsernameAndPassword(userDTO.getUsername(), userDTO.getPassword());
		if (!userBureau.isPresent())
			throw new EntityNotFoundException("Identifiants non reconnus");

		
		//Vérification que l'utilisateur a le rôle requis
		List<Role> adminRoles = userBureau.get().getRoles();
		if(!adminRoles.contains(roleRepository.findByName(RoleEnum.BUREAU))) 
    			throw new DeniedAccessException("Vous ne disposez pas du niveau d'habilitation suffisant pour réaliser cette opération");
		
		
		List<User> listUsers = userRepository.findAll();
		
		
		
		//Affichage du password impossible - Affichage null dans la liste
		for (User user : listUsers) {
			user.setPassword(null);
			user.setPasswordConfirm(null);
		}
		
		return listUsers;
	}
	
	
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
	 */
	@Override
	public User lockAccount(Long id, UserDTO userDTO) throws EntityNotFoundException, DeniedAccessException {
		
		Optional<User> userBureau = userRepository.findByUsernameAndPassword(userDTO.getUsername(), userDTO.getPassword());
		if (!userBureau.isPresent())
			throw new EntityNotFoundException("Identifiants non reconnus");

		
		//Vérification que l'utilisateur a le rôle requis
				List<Role> adminRoles = userBureau.get().getRoles();
				if(!adminRoles.contains(roleRepository.findByName(RoleEnum.BUREAU))) 
		    			throw new DeniedAccessException("Vous ne disposez pas du niveau d'habilitation suffisant pour réaliser cette opération");
				
		
		//Identification du compte à verrouiller
		Optional<User> userToBeLocked = userRepository.findById(id);
			if(!userToBeLocked.isPresent()) throw new EntityNotFoundException("Le compte que vous voulez bloquer n'existe pas");
			if(userToBeLocked.get().getRoles().contains(roleRepository.findByName(RoleEnum.BUREAU))
					|| userToBeLocked.get().getRoles().contains(roleRepository.findByName(RoleEnum.ADMIN))
					|| !userToBeLocked.get().getRoles().contains(roleRepository.findByName(RoleEnum.ADHERENT)))
				throw new DeniedAccessException("Vous ne pouvez pas bloquer ce compte");
			
		
		//Clé de Verrouillage en générant un nombre aléatoire dans le mot de passe
		Random rnd = new Random();
		int number = 100000 + rnd.nextInt(900000);// nombre aléatoire de 6 chiffres entre 100000 et 999999
		String numberString = String.valueOf(number);
 		
		//String passwordLocked = new StringBuilder(numberString).append("/").append(userToBeLocked.get().getPassword()).toString();
		
 		//Verrouillage du compte par modification du username en indiquant qui est le membre du bureau à l'origine du verrouillage
 				String usernameLocked = new StringBuilder(numberString).append("/").append(userBureau.get().getUsername()).append("/lockedAccount/").append(userToBeLocked.get().getUsername()).toString();
 				
 		
		User userLocked = userToBeLocked.get();
		userLocked.setUsername(usernameLocked);
		userLocked.setDateBlocageDebut(LocalDate.now());
		userLocked.getRoles().clear();
		userLocked.getRoles().add(roleRepository.findByName(RoleEnum.LOCKED));
		
		return userRepository.save(userLocked);
	}
	
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
	@Override
	public User unlockAccount(Long id, UserDTO userDTO) throws EntityNotFoundException, DeniedAccessException {
		
		Optional<User> userBureau = userRepository.findByUsernameAndPassword(userDTO.getUsername(), userDTO.getPassword());
		if (!userBureau.isPresent())
			throw new EntityNotFoundException("Identifiants non reconnus");
		
		//Vérification que l'utilisateur a le rôle requis
		List<Role> adminRoles = userBureau.get().getRoles();
		if(!adminRoles.contains(roleRepository.findByName(RoleEnum.BUREAU))) 
    			throw new DeniedAccessException("Vous ne disposez pas du niveau d'habilitation suffisant pour réaliser cette opération");
	
		
		//Identification du compte à déverrouiller
		Optional<User> userToUnlock = userRepository.findById(id);
			if(!userToUnlock.isPresent()) throw new EntityNotFoundException("Le compte que vous voulez bloquer n'existe pas");
			if(!userToUnlock.get().getRoles().contains(roleRepository.findByName(RoleEnum.LOCKED)))
				throw new DeniedAccessException("Ce compte n'est pas bloqué");
			
		//Déverrouillage du compte en indiquant qui est le membre du bureau à l'origine du verrouillage
		String usernameUnlocked = userToUnlock.get().getUsername().substring(userToUnlock.get().getUsername().lastIndexOf("/")+1);
		
		
		//2ème Verrouillage en générant un nombre aléatoire dans le mot de passe
		//String passwordUnlocked = userToUnlock.get().getPassword().substring(7);
		
		
		User userUnlocked = userToUnlock.get();
		userUnlocked.setUsername(usernameUnlocked);
		userUnlocked.setDateBlocageFin(LocalDate.now());
		userUnlocked.getRoles().clear();
		userUnlocked.getRoles().add(roleRepository.findByName(RoleEnum.ADHERENT));
					
		
		return userRepository.save(userUnlocked);
	}
	
	// ROLE ADMIN****************************************************************************************************
		
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
	@Override
	public User updateToBureau(Long id, UserDTO userDTO) 
			throws EntityAlreadyExistsException, 
			EntityNotFoundException, 
			DeniedAccessException, WrongParametersException {
		
		Optional<User> adminAuthenticated = userRepository.findByUsernameAndPassword(userDTO.getUsername(), userDTO.getPassword());
		if (!adminAuthenticated.isPresent())
			throw new EntityNotFoundException("Identifiants non reconnus");
			
		//On vérifie que le membre du Bureau qui update "Membre du Bureau" le compte Adhérent existe et a bien le rôle requis d'Administrateur		
		  List<Role> adminRoles = adminAuthenticated.get().getRoles();
		  if(!adminRoles.contains(roleRepository.findByName(RoleEnum.ADMIN))) 
			  throw new DeniedAccessException("Vous ne disposez pas du niveau d'habilitation suffisant pour réaliser cette opération"
		  );
		 
		
		//On vérifie que le compte à passer en rôle Bureau existe 
		Optional<User> userAuthenticated = userRepository.findById(id); 
		if (!userAuthenticated.isPresent())
			throw new EntityNotFoundException("Aucun compte ne correspond aucun compte que vous avez sélectionné");
		
		List<Role> userRoles = userAuthenticated.get().getRoles();
		
		//On vérifie que le compte à passer en rôle membre du Bureau n'est pas déjà membre du bureau (les admin sont aussi membre du bureau)
		if(userRoles.contains(roleRepository.findByName(RoleEnum.BUREAU))) 
    			throw new EntityAlreadyExistsException("Ce compte a déjà l'habilitation Membre du Bureau");
    
		//On vérifie que le compte à passer en rôle membre du Bureau est bien ADHERENT
		//Si oui, cela exclut les rôles LOCKED et EXADHERENT qui ne peuvent pas être promus membres du bureau 
		if(!userRoles.contains(roleRepository.findByName(RoleEnum.ADHERENT))) 
			throw new EntityNotFoundException("Ce compte ne peut pas recevoir l'habilitation Membre du Bureau");
		
		//Un membre du Bureau a 2 rôles : ADHERENT + MEMBRE DU BUREAU
		userRoles.add(roleRepository.findByName(RoleEnum.BUREAU));
		
	    return userRepository.save(userAuthenticated.get());
	}

	/**
	 * Cette méthode permet à un administrateur de rétrograder un membre du Bureau à un rôle d'Adhérent	
	 * @param id
	 * @param userDTO
	 * @return
	 * @throws EntityNotFoundException
	 * @throws DeniedAccessException
	 * @throws WrongParametersException 
	 */
	@Override
	public User closeBureau(Long id, UserDTO userDTO) throws EntityNotFoundException, DeniedAccessException, WrongParametersException {
		
		Optional<User> adminAuthenticated = userRepository.findByUsernameAndPassword(userDTO.getUsername(), userDTO.getPassword());
		if (!adminAuthenticated.isPresent())
		throw new EntityNotFoundException("Identifiants non reconnus");
	
		
		//On vérifie que le membre du Bureau qui update "Membre du Bureau" le compte Adhérent existe et a bien le rôle requis d'Administrateur
		List<Role> adminRoles = adminAuthenticated.get().getRoles();
			if(!adminRoles.contains(roleRepository.findByName(RoleEnum.ADMIN))) 
		    throw new DeniedAccessException("Vous ne disposez pas du niveau d'habilitation suffisant pour réaliser cette opération");
				

		//On vérifie que le compte Bureau à rétrograder en rôle Adhérent existe 
		Optional<User> userAuthenticated = userRepository.findById(id); 
		if (!userAuthenticated.isPresent())
			throw new EntityNotFoundException("Aucun compte ne correspond aucun compte que vous avez sélectionné");
		
		List<Role> userRoles = userAuthenticated.get().getRoles();
		
		//On vérifie que le compte à rétrograder en ADHERENT est bien MEMBRE DU BUREAU
		//Si oui, cela exclut les rôles LOCKED et EXADHERENT qui ne peuvent pas être promus membres du bureau 
		if(!userRoles.contains(roleRepository.findByName(RoleEnum.BUREAU))) 
			throw new EntityNotFoundException("Ce compte n'est un compte de membre du Bureau");
		
		//On ne laisse que le rôle ADHERENT
		userRoles.clear();
		userRoles.add(roleRepository.findByName(RoleEnum.ADHERENT));
		
		return userRepository.save(userAuthenticated.get());
		
	}
	
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
	@Override
	public User updateToAdmin(Long id, UserDTO userDTO) throws EntityNotFoundException, DeniedAccessException, EntityAlreadyExistsException, WrongParametersException {
		
		Optional<User> adminAuthenticated = userRepository.findByUsernameAndPassword(userDTO.getUsername(), userDTO.getPassword());
		if (!adminAuthenticated.isPresent())
		throw new EntityNotFoundException("Identifiants non reconnus");
		
		//On vérifie que le membre du Bureau qui update "Membre du Bureau" le compte Adhérent existe et a bien le rôle requis d'Administrateur
				
		List<Role> adminRoles = adminAuthenticated.get().getRoles();
			if(!adminRoles.contains(roleRepository.findByName(RoleEnum.ADMIN))) 
		    throw new DeniedAccessException("Vous ne disposez pas du niveau d'habilitation suffisant pour réaliser cette opération");
				
		//On vérifie que le compte à passer en rôle ADMIN existe 
		Optional<User> userAuthenticated = userRepository.findById(id); 
		if (!userAuthenticated.isPresent())
			throw new EntityNotFoundException("Aucun compte ne correspond aucun compte que vous avez sélectionné");
		
		List<Role> userRoles = userAuthenticated.get().getRoles();
		
		//On vérifie que le compte à passer en rôle Admin n'est pas déjà Admin (les admin sont aussi membre du bureau)
		if(userRoles.contains(roleRepository.findByName(RoleEnum.ADMIN))) 
				throw new EntityAlreadyExistsException("Ce compte a déjà l'habilitation Membre du Bureau");
		
		//On vérifie que le compte à passer en rôle ADMIN est bien Membre du Bureau
		//Si oui, cela exclut les rôles LOCKED et EXADHERENT qui ne peuvent pas être promus Admin
		//Cela n'exclut pas le rôle ADHERENT puisqu'un Membre du Bureau à 2 rôles celui d'ADHERENT et de Membre du Bureau
		if(!userRoles.contains(roleRepository.findByName(RoleEnum.BUREAU))) 
			throw new EntityNotFoundException("Ce compte ne peut pas recevoir l'habilitation Membre du Bureau");
		
		//Un ADMIN a 3 rôles : ADHERENT, MEMBRE DU BUREAU et ADMIN
		userRoles.add(roleRepository.findByName(RoleEnum.ADMIN));
		
		return userRepository.save(userAuthenticated.get());
					
	}
	
	/**
	 * Cette méthode permet à un administrateur de rétrograder un admistrateur à un rôle de membre du Bureau	
	 * @param id
	 * @param userDTO
	 * @return
	 * @throws EntityNotFoundException
	 * @throws DeniedAccessException
	 * @throws WrongParametersException 
	 */
	@Override
	public User closeAdmin(Long id, UserDTO userDTO) throws EntityNotFoundException, DeniedAccessException, WrongParametersException {
		
		Optional<User> adminAuthenticated = userRepository.findByUsernameAndPassword(userDTO.getUsername(), userDTO.getPassword());
		if (!adminAuthenticated.isPresent())
		throw new EntityNotFoundException("Identifiants non reconnus");
		
		//On vérifie que le membre du Bureau qui update "Membre du Bureau" le compte Adhérent existe et a bien le rôle requis d'Administrateur
				
		List<Role> adminRoles = adminAuthenticated.get().getRoles();
			if(!adminRoles.contains(roleRepository.findByName(RoleEnum.ADMIN))) 
		    throw new DeniedAccessException("Vous ne disposez pas du niveau d'habilitation suffisant pour réaliser cette opération");
				

		//On vérifie que le compte Admin à rétrograder en rôle membre du Bureau existe 
		Optional<User> userAuthenticated = userRepository.findById(id); 
		if (!userAuthenticated.isPresent())
			throw new EntityNotFoundException("Aucun compte ne correspond aucun compte que vous avez sélectionné");
		
		List<Role> userRoles = userAuthenticated.get().getRoles();
		
		//On vérifie que le compte à rétrograder en membre du Bureau est bien ADMIN
		//Si oui, cela exclut les rôles LOCKED et EXADHERENT qui ne peuvent pas être promus membres du bureau et donc Admin
		if(!userRoles.contains(roleRepository.findByName(RoleEnum.ADMIN))) 
			throw new EntityNotFoundException("Ce compte n'est pas un compte d'administrateur");
		
		//On laisse les rôles ADHERENT et Membre du Bureau
		userRoles.clear();
		userRoles.add(roleRepository.findByName(RoleEnum.ADHERENT));
		userRoles.add(roleRepository.findByName(RoleEnum.BUREAU));
		
		return userRepository.save(userAuthenticated.get());
	}

	
}
	
	

	



