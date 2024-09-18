package com.example.myrest1.service;

import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.example.myrest1.dto.AuthorityDTO;
import com.example.myrest1.entity.Authority;
import com.example.myrest1.mapper.AuthorityMapper;
import com.example.myrest1.repo.AuthorityRepo;

import lombok.AllArgsConstructor;



@Service
@AllArgsConstructor
public class AuthorityService {

    private AuthorityRepo authorityRepo;

    
    private ModelMapper modelMapper;

    public List<AuthorityDTO> getAllAuthorites(){

      //  return AuthorityMapper.mapToAuthorityDTO(authorityRepo.getAuthorities());
       List<Authority> authoritiesList= authorityRepo.findAll();
       return modelMapper.map(authoritiesList, new TypeToken<List<AuthorityDTO>>(){}.getType());
    
    }


    public AuthorityDTO createAuthority(AuthorityDTO authorityDTO){

        
        Authority savedAuthority = authorityRepo.save(AuthorityMapper.mapToAuthority(authorityDTO));
        return AuthorityMapper.mapToAuthorityDTO(savedAuthority);
    }

    public AuthorityDTO getAutherbyid(Integer id){

        Authority theAuthority=  authorityRepo.getAuthorityByAuthID(id);
        return AuthorityMapper.mapToAuthorityDTO(theAuthority);


    }

    public List<AuthorityDTO> getAutherbypost( String thepost){

        List<Authority> authoritiesList = authorityRepo.getAuthorityByAuthPOST(thepost);
        return modelMapper.map(authoritiesList, new TypeToken<List<AuthorityDTO>>(){}.getType());
    }

    public List<AuthorityDTO> getAutherbypostURL( String thepost){

        List<Authority> authoritiesList = authorityRepo.getAuthorityByAuthPOSTURL(thepost);
        return modelMapper.map(authoritiesList, new TypeToken<List<AuthorityDTO>>(){}.getType());
    }

    public List<AuthorityDTO> getAutherbyemsp(List<Integer> emps){

        List<Authority> authoritiesList = authorityRepo.getAuthorityByAuthemps(emps);
        return modelMapper.map(authoritiesList, new TypeToken<List<AuthorityDTO>>(){}.getType());
    }

    public List<AuthorityDTO> search2(String name, String emps,String clearance){

        int iemps= Integer.parseInt(emps);
        List<Authority> authlist= authorityRepo.getsearch2(name, iemps, clearance);
        return modelMapper.map(authlist,  new TypeToken<List<AuthorityDTO>>(){}.getType());

    }
    
}
