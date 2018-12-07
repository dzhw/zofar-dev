package de.zofar.management.persistence.daos.user;

import org.springframework.data.jpa.repository.JpaRepository;

import de.zofar.management.persistence.entities.user.UserEntity;


/**
 * @author dick
 * 
 */

public interface UserDao extends JpaRepository<UserEntity, Integer> {
    UserEntity findByName(String name);
}
