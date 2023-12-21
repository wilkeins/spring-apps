package com.user.demo.service;

import com.user.demo.model.Users;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
public class UserSpecification {
    public static Specification<Users> findByCriteria(String name, String email, Boolean isActive) {
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.conjunction();

            if (name != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("name"), name));
            }
            if (email != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("email"), email));
            }
            if (isActive != null) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("isActive"), isActive));
            }
            return predicate;
        };
    }
}

