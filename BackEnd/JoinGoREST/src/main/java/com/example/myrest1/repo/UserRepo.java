package com.example.myrest1.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.example.myrest1.entity.User;

//MySQL
public interface UserRepo extends JpaRepository<User, Integer>{

    @Query(value= "Select * FROM USER WHERE ID= ?1", nativeQuery = true)
    User getUserByUserID(String userId);

    @Query(value = "Select * FROM USER WHERE ID=?1 AND ADDRESS= ?2", nativeQuery= true)
    User getUserByUserIdandAddress(String userId, String address);

    // @Modifying(value = "Update * where id=?1", nativeQuery= true)
    // User Updateuserbydata(String userId);
    
}



// public interface UserRepo extends CrudRepository<User, Integer>{
// }
