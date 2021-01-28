package com.microseladherent.service.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.microseladherent.dao.IRoleRepository;
import com.microseladherent.dao.IUserRepository;
import com.microseladherent.dto.UserDTO;
import com.microseladherent.entities.User;
import com.microseladherent.exceptions.DeniedAccessException;
import com.microseladherent.exceptions.EntityNotFoundException;
import com.microseladherent.service.ISecurityService;
import com.microseladherent.service.IUserService;


/*@SpringBootTest
@RunWith(SpringRunner.class)*/
public class SecurityServiceImplTest {
	
	/*
	 * @Mock private IRoleRepository roleRepository;
	 * 
	 * @Mock private IUserRepository userRepository;
	 * 
	 * @InjectMocks private SecurityServiceImpl securityService;
	 * 
	 * @Mock private IUserService userService;
	 * 
	 * 
	 * @Test public void testAutologin_whenEntityNotFoundException() {
	 * 
	 * UserDTO adherentDTO = new UserDTO(); adherentDTO.setUsername("adherent");
	 * adherentDTO.setAdresseMail("adherent@gmail.com");
	 * adherentDTO.setPassword("adherent");
	 * adherentDTO.setPasswordConfirm("adherent");
	 * 
	 * User adherent = new User(); adherent.setId((long)1);
	 * adherent.setUsername("adherent"); adherent.setEmail("adherent@gmail.com");
	 * adherent.setPassword("adherent"); adherent.setPasswordConfirm("adherent");
	 * adherent.setDateAdhesionDebut(LocalDate.now());
	 * 
	 * when(userRepository.findByUsername("adherent")).thenReturn(Optional.empty());
	 * 
	 * try { User userTest = securityService.autologin(adherentDTO); } catch
	 * (Exception e) { assertThat(e).isInstanceOf(EntityNotFoundException.class)
	 * .hasMessage("Ce nom n'est pas enregistré en tant qu'utilisateur"); } }
	 * 
	 * 
	 * @Test public void testAutologin_whenDeniedAccessException() {
	 * 
	 * UserDTO adherentDTO = new UserDTO(); adherentDTO.setUsername("adherent");
	 * adherentDTO.setAdresseMail("adherent@gmail.com");
	 * adherentDTO.setPassword("adherent1");
	 * adherentDTO.setPasswordConfirm("adherent1");
	 * 
	 * User adherent = new User(); adherent.setId((long)1);
	 * adherent.setUsername("adherent"); adherent.setEmail("adherent@gmail.com");
	 * adherent.setPassword("adherent"); adherent.setPasswordConfirm("adherent");
	 * adherent.setDateAdhesionDebut(LocalDate.now());
	 * 
	 * when(userRepository.findByUsername("adherent")).thenReturn(Optional.of(
	 * adherent));
	 * 
	 * try { User userTest = securityService.autologin(adherentDTO); } catch
	 * (Exception e) { assertThat(e).isInstanceOf(DeniedAccessException.class)
	 * .hasMessage("Le mot de passe n'est pas correct"); } }
	 * 
	 * 
	 * @Test public void testAutologin_withoutException() throws Exception {
	 * 
	 * UserDTO adherentDTO = new UserDTO(); adherentDTO.setUsername("adherent");
	 * adherentDTO.setAdresseMail("adherent@gmail.com");
	 * adherentDTO.setPassword("adherent");
	 * adherentDTO.setPasswordConfirm("adherent");
	 * 
	 * User adherent = new User(); adherent.setId((long)1);
	 * adherent.setUsername("adherent"); adherent.setEmail("adherent@gmail.com");
	 * adherent.setPassword("adherent");
	 * adherent.setDateAdhesionDebut(LocalDate.of(2020, 10, 05));
	 * 
	 * when(userRepository.findByUsername("adherent")).thenReturn(Optional.of(
	 * adherent));
	 * 
	 * User adherentAuthenticated = securityService.autologin(adherentDTO);
	 * Assert.assertTrue(adherentAuthenticated.getUsername().equals(adherent.
	 * getUsername()));
	 * Assert.assertTrue(adherentAuthenticated.getPassword().equals(adherent.
	 * getPassword())); }
	 * 
	 */

}
