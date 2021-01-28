package com.microseladherent.dto;

import org.springframework.stereotype.Component;

import com.microseladherent.entities.User;

@Component
public class UpdateUserMapperImpl{

    public UpdateUserDTO userToupdateUserDTO(User entity) {
        if ( entity == null ) {
            return null;
        }

        UpdateUserDTO updateUserDTO = new UpdateUserDTO();

        updateUserDTO.setPassword( entity.getPassword() );
        updateUserDTO.setAdresseMail( entity.getEmail() );
        updateUserDTO.setUsername( entity.getUsername() );
        updateUserDTO.setPasswordConfirm( entity.getPasswordConfirm() );
        
        return updateUserDTO;
    }

    public User updateUserDTOToUser(UpdateUserDTO dto) {
        if ( dto == null ) {
            return null;
        }

        User user = new User();

        user.setUsername( dto.getUsername() );
        user.setPassword( dto.getPassword() );
        user.setEmail( dto.getAdresseMail() );
        user.setPasswordConfirm( dto.getPasswordConfirm() );

        return user;
    }
}