package com.example.ecommercespring.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ecommercespring.models.ERole;
import com.example.ecommercespring.models.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    Optional<Role> findFirstByName(ERole name);

}
