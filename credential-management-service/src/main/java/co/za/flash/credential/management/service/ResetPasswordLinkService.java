package co.za.flash.credential.management.service;

import co.za.flash.credential.management.entity.ResetPasswordLinkEntity;
import co.za.flash.credential.management.helper.utils.TimeUtil;
import co.za.flash.credential.management.repository.ResetPasswordLinkRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
public class ResetPasswordLinkService {
    private static long forgetPasswordLinkExpireAtInMills = 20 * 60 * 100; // 20 minutes

    @Autowired
    private ResetPasswordLinkRepository repository;

    public List<ResetPasswordLinkEntity> getAll() throws Exception {
        return repository.findAll();
    }

    public ResetPasswordLinkEntity createResetPasswordLink(String baseUrl,
                                                            String userNameWithDomain,
                                                            String userId, String email) throws Exception {
        UUID uuid = UUID.randomUUID();
        ResetPasswordLinkEntity entity = ResetPasswordLinkEntity.createEntity(
                baseUrl,
                uuid.toString(),
                userNameWithDomain,
                userId, email, forgetPasswordLinkExpireAtInMills
        );
        repository.save(entity);
        return entity;
    }

    public ResetPasswordLinkEntity isValidLinkExist (String userNameWithDomain) throws Exception {
        List<ResetPasswordLinkEntity> result = repository.findByUserNameWithDomain(userNameWithDomain);
        if (result != null && !result.isEmpty()) {
            result.sort(Comparator.comparing(ResetPasswordLinkEntity::getCreatedTimeStamp).reversed());
            ResetPasswordLinkEntity lastEntity = result.get(0);
            boolean isExpired = TimeUtil.isTimestampExpired(lastEntity.getExpireTimeStamp(), 0);
            if (!isExpired)
                return lastEntity;
        }
        return null;
    }

    public static String createResetPasswordMailContent(String userNameWithDomain, String url) {
        StringBuilder sb = new StringBuilder();
        sb.append("Dear customer " + userNameWithDomain);
        sb.append(System.lineSeparator());
        sb.append("Here is the forget password link for you:");
        sb.append(System.lineSeparator());
        sb.append(url);
        sb.append(System.lineSeparator());
        sb.append("It will expire in " + TimeUtil.millsToMinutes(forgetPasswordLinkExpireAtInMills));
        sb.append(System.lineSeparator());
        return sb.toString();
    }
}
