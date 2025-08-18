package lk.ijse.gdse.traditionalexperiencebackend.repo;


import lk.ijse.gdse.traditionalexperiencebackend.entity.RoleType;

import lk.ijse.gdse.traditionalexperiencebackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {


  User findByEmail(String email); // Find user by email
  boolean existsByEmail(String email);





}


