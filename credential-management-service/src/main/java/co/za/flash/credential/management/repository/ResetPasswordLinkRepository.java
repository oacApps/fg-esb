package co.za.flash.credential.management.repository;

import co.za.flash.credential.management.entity.ResetPasswordLinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResetPasswordLinkRepository extends JpaRepository<ResetPasswordLinkEntity, Long> {
    List<ResetPasswordLinkEntity> findByToken(String token);
    List<ResetPasswordLinkEntity> findByUserNameWithDomain(String userNameWithDomain);
    List<ResetPasswordLinkEntity> findByEmailAddress(String emailAddress);
}
