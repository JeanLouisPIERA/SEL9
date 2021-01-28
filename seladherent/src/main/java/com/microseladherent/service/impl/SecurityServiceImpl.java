package com.microseladherent.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
/*import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;*/
import org.springframework.stereotype.Service;

import com.microseladherent.dao.IUserRepository;
import com.microseladherent.dto.UpdateUserDTO;
import com.microseladherent.dto.UserDTO;
import com.microseladherent.entities.User;
import com.microseladherent.exceptions.DeniedAccessException;
import com.microseladherent.exceptions.EntityNotFoundException;
import com.microseladherent.exceptions.WrongParametersException;
import com.microseladherent.service.ISecurityService;

@Service
public class SecurityServiceImpl implements ISecurityService {

	/*
	 * @Autowired private IUserRepository userRepository;
	 * 
	 * @Override public User autologin(UserDTO userDTO) throws
	 * EntityNotFoundException, DeniedAccessException {
	 * 
	 * Optional<User> userToLog =
	 * userRepository.findByUsername(userDTO.getUsername()); if
	 * (!userToLog.isPresent()) throw new
	 * EntityNotFoundException("Ce nom n'est pas enregistré"); if
	 * (!userToLog.get().getPassword().equals(userDTO.getUsername())) throw new
	 * DeniedAccessException("Le mot de passe n'est pas correct");
	 * 
	 * }
	 * 
	 * 
	 * @Override public User login(UpdateUserDTO updateUserDTO) throws
	 * EntityNotFoundException, DeniedAccessException {
	 * 
	 * Optional<User> userToLog =
	 * userRepository.findByUsername(updateUserDTO.getUsername()); if
	 * (!userToLog.isPresent()) throw new
	 * EntityNotFoundException("Ce nom n'est pas enregistré en tant qu'utilisateur"
	 * );
	 * 
	 * 
	 * Optional<User> userToAuthenticate =
	 * userRepository.findByUsernameAndPassword(updateUserDTO.getUsername(),
	 * updateUserDTO.getPassword()); if (!userToAuthenticate.isPresent()) throw new
	 * DeniedAccessException("Le mot de passe n'est pas correct");
	 * 
	 * return userToAuthenticate.get(); }
	 */
	/*
	 * @Autowired private AuthenticationManager authenticationManager;
	 * 
	 * @Autowired private UserDetailsService userDetailsService;
	 * 
	 * @Autowired private IUserRepository userRepository;
	 * 
	 * private static final Logger logger =
	 * LoggerFactory.getLogger(SecurityServiceImpl.class);
	 * 
	 * 
	 * Cette méthode permet d'identifier le nom de l'utilisateur loggé après
	 * authentification
	 * 
	 * @Override public String findLoggedInUsername() { Object userDetails =
	 * SecurityContextHolder.getContext().getAuthentication().getDetails(); if
	 * (userDetails instanceof UserDetails) { return
	 * ((UserDetails)userDetails).getUsername(); } return null; }
	 * 
	 * 
	 * Cette méthode permet d'identifier l'utilisateur loggé après authentification
	 * 
	 * @Override public User findLoggedInUser() { Object userDetails =
	 * SecurityContextHolder.getContext().getAuthentication().getDetails(); if
	 * (userDetails instanceof UserDetails) { Optional<User> user =
	 * userRepository.findByUsername(((UserDetails)userDetails).getUsername());
	 * return user.get(); } return null; }
	 * 
	 * 
	 * 
	 * Cette méthode permet à un visiteur de se logger automatiquement avec le role
	 * USER
	 * 
	 * @Override public Optional<User> autologin(String username, String password) {
	 * UserDetails userDetails = userDetailsService.loadUserByUsername(username);
	 * UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new
	 * UsernamePasswordAuthenticationToken(userDetails, password,
	 * userDetails.getAuthorities());
	 * 
	 * Authentication authenticated =
	 * authenticationManager.authenticate(usernamePasswordAuthenticationToken);
	 * 
	 * if (authenticated.isAuthenticated()){
	 * SecurityContextHolder.getContext().setAuthentication(
	 * usernamePasswordAuthenticationToken);
	 * logger.debug(String.format("Auto login %s successfully!", username));
	 * Optional<User> userAuthenticated = userRepository.findByUsername(username);
	 * return userAuthenticated; } return null; }
	 */

}
