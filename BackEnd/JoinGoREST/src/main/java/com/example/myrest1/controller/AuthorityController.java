package com.example.myrest1.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.hibernate.id.IntegralDataTypeHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.myrest1.dto.AuthorityDTO;
import com.example.myrest1.service.AuthorityService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMethod;




@RestController
@RequestMapping(value= "api/v1/authority" )
@CrossOrigin
public class AuthorityController {

    @Autowired
    private AuthorityService authorityService;

   
   
    @GetMapping("/getmany")
    public ResponseEntity<String> getParams(@RequestParam Map<String, String> params ) {

        System.out.println(params.keySet());
        System.out.println(params.values());

        String pstt= params.get("params");
        String response = "Params " + pstt;
        System.out.println(response); 
     
        return new ResponseEntity<String>("ok"+ params, HttpStatus.OK);
    }

    @GetMapping("/search2")
    public List<AuthorityDTO> search2(@RequestParam  Map<String, String> params){

        String p1 = params.get("aname");
        String p2 = params.get("emps");
        String p3 = params.get("aclearance");
        
        System.out.println("\n\n" +p1+ p2+ p3+ "\n\n");

        List<AuthorityDTO> authlist = authorityService.search2(p1, p2, p3);
        return authlist;
    }

    @GetMapping("/getauthorities")
    public List<AuthorityDTO> getAuther() {

        return authorityService.getAllAuthorites();
    }

    @GetMapping("/getauthority")
    public AuthorityDTO getauthid(@RequestParam Integer id) {

        //Optional<AuthorityDTO> auth= authorityService.getAutherbyid(id);
        AuthorityDTO auth= authorityService.getAutherbyid(id);
        //return (AuthorityDTO) auth.orElse(null);
        return auth;
        
    }
    
    @GetMapping("/getauthority1/{thepost}")
    public List<AuthorityDTO> getauthpostURL(@PathVariable String thepost) {
        
        List<AuthorityDTO> auths= authorityService.getAutherbypostURL(thepost);
        return auths;
        
    }

    @GetMapping("/getauthority3")
    public ResponseEntity<String> getauthpost(@RequestParam("thepost") String thepost) {
        
        //List<AuthorityDTO> auths= authorityService.getAutherbypost(thepost);

        return ResponseEntity.ok("Received post:"+ thepost);
       // return auths;
        
    }

    @GetMapping("/getauthority2")
    public List<AuthorityDTO> getauthemps(@RequestParam List<Integer> emps) {
        
        List<AuthorityDTO> auths= authorityService.getAutherbyemsp(emps);
        return auths;
        
    }

    @PostMapping("/createauthority")
    public AuthorityDTO createAuthor(@RequestBody AuthorityDTO authorityDTO) {
        
        return authorityService.createAuthority(authorityDTO);
    }
    



    
}
