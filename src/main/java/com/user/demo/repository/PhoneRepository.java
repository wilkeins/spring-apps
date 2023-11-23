package com.user.demo.repository;

import com.user.demo.model.Phone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface PhoneRepository extends JpaRepository<Phone, Long> {
}

