package com.coffee_sales.backend.repository;

import com.coffee_sales.backend.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AppUserRepo extends JpaRepository<AppUser,Integer> {
    @Query("SELECT u.id FROM AppUser u")
    List<Integer> findAllUserId();

    @Query("SELECT a.id FROM AppUser a WHERE a.name IN :names")
    List<Integer> findUserIdsByNames(@Param("names") List<String> names);

    @Query("SELECT COUNT(u.id) FROM AppUser u GROUP BY u.id")
    Integer countUser();

    AppUser findByUsername(String username);

    Optional<AppUser> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
