package com.service.repositories;

import com.service.entities.UserProfile;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserProfileRepository extends CrudRepository<UserProfile, Long> {

    List<UserProfile> findAll();
    UserProfile findUserProfileById(Long user_id);
}
