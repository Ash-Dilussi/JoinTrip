package com.example.myrest1.mapper;

import com.example.myrest1.dto.AuthorityDTO;
import com.example.myrest1.entity.Authority;

public class AuthorityMapper {

    public static AuthorityDTO mapToAuthorityDTO(Authority authority){

        return new AuthorityDTO(
            
            authority.getAname(),
            authority.getAclearance(),
            authority.getEmps(),
            authority.getAid()
        );

    }


    public static Authority mapToAuthority(AuthorityDTO authorityDTO){

        return new Authority(
            
            authorityDTO.getAname(),
            authorityDTO.getAclearance(),
            authorityDTO.getEmps(),
            authorityDTO.getAid()
        );
    }
    
}
