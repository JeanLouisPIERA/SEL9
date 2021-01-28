package com.microseladherent.service.impl;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.microseladherent.dao.IRoleRepository;
import com.microseladherent.dao.IUserRepository;
import com.microseladherent.dto.UpdateUserDTO;
import com.microseladherent.dto.UpdateUserMapperImpl;
import com.microseladherent.dto.UserDTO;
import com.microseladherent.dto.UserMapperImpl;
import com.microseladherent.entities.Role;
import com.microseladherent.entities.RoleEnum;
import com.microseladherent.entities.User;
import com.microseladherent.exceptions.DeniedAccessException;
import com.microseladherent.exceptions.EntityAlreadyExistsException;
import com.microseladherent.exceptions.EntityNotFoundException;
import com.microseladherent.exceptions.WrongParametersException;
import com.microseladherent.service.ISecurityService;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceImplTest {
	
	@Mock
	private IRoleRepository roleRepository;
	
	@Mock
	private IUserRepository userRepository;
	
	@Mock
	private UserMapperImpl userMapper;
	
	@Mock
	private UpdateUserMapperImpl updateUserMapper;
	
	@Mock 
	private ISecurityService securityService;
	 
	
	/*
	 * @Spy private SecurityServiceImpl securityService;
	 */
	
	@InjectMocks
	private UserServiceImpl  userService;
	
	/*
	 * @Before public void setup() {
	 * 
	 * 
	 * }
	 */
	
	//ROLE ADHERENT //////////////////////////
	
	//Tests CREATE ADHERENT ************************************************************************************************

	@Test
	public void testCreateAccount_whenWrongParametersException() {
		
		UserDTO adherentDTO = new UserDTO("adherent","adherentA@gmail.com","adherent", "adherent" );
		
		User adherent = new User((long)1,"adherent", "adherentA@gmail.com","adherent",LocalDate.now());
		
		
		when(userRepository.findByUsername("adherent")).thenReturn(Optional.of(adherent));
		
		try {
			User userTest = userService.createAccount(adherentDTO);
		} catch (Exception e) {
			assertThat(e).isInstanceOf(WrongParametersException.class)
						 .hasMessage("Ce nom d'adhérent est déjà utilisé");
		}	
		
	}
	
	
	@Test
	public void testCreateAccount_whenEntityAlreadyExistsException() {
		
		UserDTO adherentDTO = new UserDTO();
		adherentDTO.setUsername("adherent");
		adherentDTO.setAdresseMail("adherent@gmail.com");
		adherentDTO.setPassword("adherent");
		adherentDTO.setPasswordConfirm("adherent");
		
		User adherent = new User();
		adherent.setUsername("adherent1");
		adherent.setEmail("adherent@gmail.com");
		adherent.setPassword("adherent");
		adherent.setPasswordConfirm("adherent");
		
		when(userRepository.findByEmail("adherent@gmail.com")).thenReturn(Optional.of(adherent));
		
		try {
			User userTest = userService.createAccount(adherentDTO);
		} catch (Exception e) {
			assertThat(e).isInstanceOf(EntityAlreadyExistsException.class)
						 .hasMessage("Un compte d'adhérent existe déjà pour cette adresse mail");
		}	
	}
		
		
	@Test
	public void testCreateAccount_withoutException() throws Exception {
		
		Role roleAdherent = new Role();
		roleAdherent.setName(RoleEnum.ADHERENT);
		
		UserDTO adherentDTO = new UserDTO();
		adherentDTO.setUsername("adherent");
		adherentDTO.setAdresseMail("adherent@gmail.com");
		adherentDTO.setPassword("adherent");
		adherentDTO.setPasswordConfirm("adherent");
		
		User adherent = new User();
		adherent.setUsername("adherent");
		adherent.setEmail("adherent@gmail.com");
		adherent.setPassword("adherent");
		adherent.setPasswordConfirm("adherent");
		
		when(userMapper.userDTOToUser(adherentDTO)).thenReturn(adherent);
		
		adherent.setDateAdhesionDebut(LocalDate.now());
		adherent.getRoles().add(roleAdherent);
		
		when(userRepository.save(any(User.class))).thenReturn(adherent);
		when(roleRepository.findByName(RoleEnum.ADHERENT)).thenReturn(roleAdherent);
		
		User adherentTest = userService.createAccount(adherentDTO);
		verify(userRepository, times(1)).save(any(User.class));
		Assert.assertTrue(adherentTest.equals(adherent)); }
	

	// Tests READ ADHERENT **********************************************************************************
	
	@Test
	public void testReadAccount_whenEntityNotFoundException() {
		
		UserDTO adherentDTO = new UserDTO("adherent","adherentA@gmail.com","adherent", "adherent" );
		
		when(userRepository.findById((long) 1)).thenReturn(Optional.empty());
		
		try {
			User userTest = userService.readAccount((long)1, adherentDTO);
		} catch (Exception e) {
			assertThat(e).isInstanceOf(EntityNotFoundException.class)
						 .hasMessage("Ce compte n'existe pas");
		}	
	}
	
	
	@Test
	public void testReadAccount_whenWrongParametersException_withWrongUsername() {
		
		UserDTO adherentDTO = new UserDTO("adherent1","adherentA@gmail.com","adherent", "adherent" );
		
		User adherent = new User((long)1,"adherent", "adherent","adherentA@gmail.com",LocalDate.now());
		
		when(userRepository.findById((long) 1)).thenReturn(Optional.of(adherent));
		
		try {
			User userTest = userService.readAccount((long)1, adherentDTO);
		} catch (Exception e) {
			assertThat(e).isInstanceOf(WrongParametersException.class)
						 .hasMessage(" Consultation interdite : un adhérent ne peut consulter que son propre compte");
		}	
	}
	
	@Test
	public void testReadAccount_whenWrongParametersException_withWrongPassword() {
		
		UserDTO adherentDTO = new UserDTO("adherent","adherentA@gmail.com","adherent1", "adherent1" );
		
		User adherent = new User((long)1,"adherent", "adherent","adherentA@gmail.com",LocalDate.now());
		
		when(userRepository.findById((long) 1)).thenReturn(Optional.of(adherent));
		
		try {
			User userTest = userService.readAccount((long)1, adherentDTO);
		} catch (Exception e) {
			assertThat(e).isInstanceOf(WrongParametersException.class)
						 .hasMessage(" Consultation interdite : un adhérent ne peut consulter que son propre compte");
		}	
	}
		
	@Test
	public void testReadAccount_whenDeniedAccessException_whenLockedAccount() {
		
		UserDTO adherentDTO = new UserDTO("adherent","adherentA@gmail.com","adherent", "adherent" );
		
		User adherent = new User();
		adherent.setId((long)1);
		adherent.setUsername("bureau/lockedAccount/adherent");
		adherent.setEmail("adherent@gmail.com");
		adherent.setPassword("123456/adherent");
		adherent.setPasswordConfirm("123456/adherent");
		adherent.setDateAdhesionDebut(LocalDate.now());
		
		when(userRepository.findById((long) 1)).thenReturn(Optional.of(adherent));
		
		try {
			User userTest = userService.readAccount((long)1, adherentDTO);
		} catch (Exception e) {
			assertThat(e).isInstanceOf(DeniedAccessException.class)
						 .hasMessage("Consultation impossible : ce compte est bloqué ou clôturé");
		}	
		
	}
	
	@Test
	public void testReadAccount_whenDeniedAccessException_whenClosedAccount() {
		
		UserDTO adherentDTO = new UserDTO("adherent","adherentA@gmail.com","adherent", "adherent" );
		
		User adherent = new User();
		adherent.setId((long)1);
		adherent.setUsername("closedAccount/12020-10-05");
		adherent.setEmail("closedAccount/12020-10-05");
		adherent.setPassword("closedAccount/12020-10-05");
		adherent.setPasswordConfirm("closedAccount/12020-10-05");
		adherent.setDateAdhesionDebut(LocalDate.now());
		
		when(userRepository.findById((long) 1)).thenReturn(Optional.of(adherent));
		
		try {
			User userTest = userService.readAccount((long)1, adherentDTO);
		} catch (Exception e) {
			assertThat(e).isInstanceOf(DeniedAccessException.class)
						 .hasMessage("Consultation impossible : ce compte est bloqué ou clôturé");
			
		}	
		
	}
	
	@Test
	public void testReadAccount_withoutException() throws Exception {
		
		UserDTO adherentADTO = new UserDTO("adherentA","adherentA@gmail.com","adherentA", "adherentA" );
			
		User adherentA = new User((long)1,"adherentA", "adherentA","adherentA@gmail.com",LocalDate.now());
		
		when(userRepository.findById((long)1)).thenReturn(Optional.of(adherentA));
		
		//when(userRepository.findByUsername("adherentA")).thenReturn(Optional.of(adherentA));
		
		User adherentATest = userService.readAccount((long)1,adherentADTO);
		
		Assert.assertTrue(adherentATest.getUsername().equals(adherentA.getUsername())); 
		Assert.assertTrue(adherentATest.getPassword().equals(adherentA.getPassword())); 
		Assert.assertTrue(adherentATest.getEmail().equals(adherentA.getEmail())); 
		
	}
	
	//TESTS UPDATE ADHERENT***************************************************************************
	
	
	@Test
	public void testUpdateAccount_whenEntityNotFoundException() {
		
		UpdateUserDTO adherentDTO = new UpdateUserDTO("adherent","adherentA@gmail.com","adherent", "adherent", null, null, null );
		
		when(userRepository.findById((long) 1)).thenReturn(Optional.empty());
		
		try {
			User userTest = userService.updateAccount((long)1, adherentDTO);
		} catch (Exception e) {
			assertThat(e).isInstanceOf(EntityNotFoundException.class)
						 .hasMessage("Ce compte n'existe pas");
		}	
	}
	
	
	@Test
	public void testUpdateAccount_whenWrongParametersException_withWrongUsername() {
		
		UpdateUserDTO adherentDTO = new UpdateUserDTO("adherent1","adherentA@gmail.com","adherent", "adherent", null, null, null );
		
		User adherent = new User((long)1,"adherent", "adherent","adherentA@gmail.com",LocalDate.now());
		
		when(userRepository.findById((long) 1)).thenReturn(Optional.of(adherent));
		
		try {
			User userTest = userService.updateAccount((long)1, adherentDTO);
		} catch (Exception e) {
			assertThat(e).isInstanceOf(WrongParametersException.class)
						 .hasMessage(" Consultation interdite : un adhérent ne peut consulter que son propre compte");
		}	
	}
	
	@Test
	public void testUpdateAccount_whenWrongParametersException_withWrongPassword() {
		
		UpdateUserDTO adherentDTO = new UpdateUserDTO("adherent","adherentA@gmail.com","adherent1", "adherent1", null, null, null );
		
		User adherent = new User((long)1,"adherent", "adherent", "adherentA@gmail.com",LocalDate.now());
		
		when(userRepository.findById((long) 1)).thenReturn(Optional.of(adherent));
		
		try {
			User userTest = userService.updateAccount((long)1, adherentDTO);
		} catch (Exception e) {
			assertThat(e).isInstanceOf(WrongParametersException.class)
						 .hasMessage(" Consultation interdite : un adhérent ne peut consulter que son propre compte");
		}	
	}
		
	@Test
	public void testUpdateAccount_whenDeniedAccessException_whenLockedAccount() {
		
		UpdateUserDTO adherentDTO = new UpdateUserDTO("adherent","adherentA@gmail.com","adherent", "adherent", null, null, null);
		
		User adherent = new User();
		adherent.setId((long)1);
		adherent.setUsername("bureau/lockedAccount/adherent");
		adherent.setEmail("adherent@gmail.com");
		adherent.setPassword("123456/adherent");
		adherent.setPasswordConfirm("123456/adherent");
		adherent.setDateAdhesionDebut(LocalDate.now());
		
		when(userRepository.findById((long) 1)).thenReturn(Optional.of(adherent));
		
		try {
			User userTest = userService.updateAccount((long)1, adherentDTO);
		} catch (Exception e) {
			assertThat(e).isInstanceOf(DeniedAccessException.class)
						 .hasMessage("Consultation impossible : ce compte est bloqué ou clôturé");
		}	
		
	}
	
	@Test
	public void testUpdateAccount_whenDeniedAccessException_whenClosedAccount() {
		
		UpdateUserDTO adherentDTO = new UpdateUserDTO("adherent","adherentA@gmail.com","adherent", "adherent", null, null, null );
		
		User adherent = new User();
		adherent.setId((long)1);
		adherent.setUsername("closedAccount/12020-10-05");
		adherent.setEmail("closedAccount/12020-10-05");
		adherent.setPassword("closedAccount/12020-10-05");
		adherent.setPasswordConfirm("closedAccount/12020-10-05");
		adherent.setDateAdhesionDebut(LocalDate.now());
		
		when(userRepository.findById((long) 1)).thenReturn(Optional.of(adherent));
		
		try {
			User userTest = userService.updateAccount((long)1, adherentDTO);
		} catch (Exception e) {
			assertThat(e).isInstanceOf(DeniedAccessException.class)
						 .hasMessage("Consultation impossible : ce compte est bloqué ou clôturé");
			
		}	
		
	}
	
		
	@Test
	public void testUpdateAccount_withoutException() throws Exception {
			
		/*
		 * UpdateUserDTO adherentDTO = new
		 * UpdateUserDTO("adherentA","adherentA@gmail.com","adherentA", "adherentA",
		 * "AdherentB", "AdherentB@gmail.com", "AdherentB" );
		 * 
		 * User adherentA = new User((long)1,"adherentA",
		 * "adherentA","adherentA@gmail.com",LocalDate.now());
		 * 
		 * when(userRepository.findById((long)1)).thenReturn(Optional.of(adherentA));
		 * 
		 * User adherentATest = userService.updateAccount((long)1,adherentDTO);
		 * 
		 * Assert.assertTrue(adherentATest.getUsername().equals(adherentDTO.
		 * getUsernameToChange()));
		 * Assert.assertTrue(adherentATest.getPassword().equals(adherentDTO.
		 * getPasswordToChange()));
		 * Assert.assertTrue(adherentATest.getEmail().equals(adherentDTO.
		 * getAdresseMailToChange()));
		 */
		 
	}
	
	
	
	
	
	
	
	
	
	
	
		
		
	}
	
	
	
	
	
	
	
	
	
	

	



	


