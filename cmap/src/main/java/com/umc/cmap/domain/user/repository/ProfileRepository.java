package com.umc.cmap.domain.user.repository;

import com.umc.cmap.domain.user.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProfileRepository  extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUserName(String userName);
    Optional<Profile> findByUserIdx(Long userIdx);
}
