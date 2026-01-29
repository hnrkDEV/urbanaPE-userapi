package com.desafio.userapi.repository;

import com.desafio.userapi.entity.User;
import com.desafio.userapi.repository.projections.UserCardCountProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    @Query(
            value = """
            SELECT 
                u.id AS userId,
                u.nome AS nome,
                u.role AS role,
                COUNT(c.id) AS totalCards
            FROM users u
            LEFT JOIN cards c ON c.user_id = u.id
            GROUP BY u.id, u.nome, u.role
        """,
            nativeQuery = true
    )
    List<UserCardCountProjection> findUsersWithCardCount();
}
