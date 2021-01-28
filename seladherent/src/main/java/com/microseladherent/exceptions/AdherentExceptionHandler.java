package com.microseladherent.exceptions;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AdherentExceptionHandler {
	
	/**
	 * Cette méthode permet de gérer l'exception métier personnalisée "Entity Not Found"
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(EntityNotFoundException.class)
	@ResponseBody
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	public AdherentExceptionReponse handleCustomException(EntityNotFoundException ex1) {
		AdherentExceptionReponse exceptionReponse = new AdherentExceptionReponse();
		exceptionReponse.setLocalDate(LocalDate.now());
		exceptionReponse.setMessageErreur(ex1.getMessage());
		exceptionReponse.setStatusCode(HttpStatus.NOT_FOUND.value());
		return exceptionReponse;
	}
	
	/**
	 * Cette méthode permet de gérer l'exception métier personnalisée " nom déjà utilisé par un autre adhérent "
	 * Voir LivreMetier
	 * @param ex1
	 * @return
	 */
	@ExceptionHandler(WrongParametersException.class)
	@ResponseBody
	@ResponseStatus(code = HttpStatus.LOCKED)
	public AdherentExceptionReponse handleCustomException(WrongParametersException ex2) {
		AdherentExceptionReponse exceptionReponse = new AdherentExceptionReponse();
		exceptionReponse.setLocalDate(LocalDate.now());
		exceptionReponse.setMessageErreur(ex2.getMessage());
		exceptionReponse.setStatusCode(HttpStatus.LOCKED.value());
		return exceptionReponse;
	}
	
		
	/**
	 * Cette méthode permet de gérer l'exception personnalisée " Entity Already Exists "
	 * Voir CategorieMetier et LivreMetier
	 * @param ex2
	 * @return
	 */
	
	@ExceptionHandler (EntityAlreadyExistsException.class)
	@ResponseBody
	@ResponseStatus(code = HttpStatus.CONFLICT)
	public AdherentExceptionReponse handleCustomException(EntityAlreadyExistsException ex4) {
		AdherentExceptionReponse exceptionReponse = new AdherentExceptionReponse();
		exceptionReponse.setLocalDate(LocalDate.now());
		exceptionReponse.setMessageErreur(ex4.getMessage());
		exceptionReponse.setStatusCode(HttpStatus.CONFLICT.value());
		return exceptionReponse;
		
	}
	
	/**
	 * Cette méthode permet de gérer l'exception personnalisée " Entity Already Exists "
	 * Voir CategorieMetier et LivreMetier
	 * @param ex2
	 * @return
	 */
	
	@ExceptionHandler (DeniedAccessException.class)
	@ResponseBody
	@ResponseStatus(code = HttpStatus.FORBIDDEN)
	public AdherentExceptionReponse handleCustomException(DeniedAccessException ex5) {
		AdherentExceptionReponse exceptionReponse = new AdherentExceptionReponse();
		exceptionReponse.setLocalDate(LocalDate.now());
		exceptionReponse.setMessageErreur(ex5.getMessage());
		exceptionReponse.setStatusCode(HttpStatus.FORBIDDEN.value());
		return exceptionReponse;
		
	}
	

}
