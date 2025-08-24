package ru.litvak.userservice.util;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import ru.litvak.userservice.model.entity.UserProfile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class UserProfileSpecifications {
    public static Specification<UserProfile> searchByQuery(String rawQuery, UUID me) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.notEqual(root.get("id"), me));
            predicates.add(cb.isFalse(root.get("isDeleted")));

            if (rawQuery != null && !rawQuery.isBlank()) {
                String[] tokens = rawQuery.trim().split("\\s+");

                List<Predicate> tokenPredicates = new ArrayList<>();
                for (String token : tokens) {
                    String likePattern = "%" + token.toLowerCase() + "%";
                    tokenPredicates.add(
                            cb.or(
                                    cb.like(cb.lower(root.get("firstName")), likePattern),
                                    cb.like(cb.lower(root.get("familyName")), likePattern)
                            )
                    );
                }
                predicates.add(cb.and(tokenPredicates.toArray(new Predicate[0])));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
