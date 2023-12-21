package com.user.demo.repository;

import com.user.demo.model.Users;
import com.user.demo.service.UserSpecification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface UserRepository extends JpaRepository<Users, Long>, JpaSpecificationExecutor<Users> {
    boolean existsByEmail(String email);
    default List<Users> findByCriteria(String name, String email, Boolean isActive) {
        return findAll(UserSpecification.findByCriteria(name, email, isActive));
    }
}

