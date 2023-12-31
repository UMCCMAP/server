package com.umc.cmap.domain.user.repository;

import com.umc.cmap.domain.user.entity.Profile;
import com.umc.cmap.domain.user.profile.dto.MatesInfoMapping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProfileRepository  extends JpaRepository<Profile, Long> {
    Optional<Profile> findByUserNickname(String userNickname);
    Optional<Profile> findByUserIdx(Long userIdx);
    List<MatesInfoMapping> findProfileByUserIdx(Long userIdx);
}
