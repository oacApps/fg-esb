package co.za.flash.credential.management.entity;

import co.za.flash.credential.management.helper.utils.StringUtil;
import co.za.flash.credential.management.helper.utils.TimeUtil;

import javax.persistence.*;

import java.io.Serializable;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Table
public class ResetPasswordLinkEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Integer id;

    @Column(nullable = false, unique=true)
    private String token;

    @Column(nullable = false, unique=true)
    private String url;

    @Column(nullable = false)
    private String userNameWithDomain;

    @Column
    private String userId; // can be null for soap implementation

    @Column(nullable = false)
    private String emailAddress;

    @Column(nullable = false)
    private long expireTimeStamp;

    @Column(nullable = false)
    private long createdTimeStamp;

    @Column
    private long usedTimeStamp;

    public Integer getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public String getUserNameWithDomain() {
        return userNameWithDomain;
    }

    public String getUserId() {
        return userId;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getExpireTimeStamp() {
        return expireTimeStamp;
    }

    public long getCreatedTimeStamp() {
        return createdTimeStamp;
    }

    public long getUsedTimeStamp() {
        return usedTimeStamp;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUserNameWithDomain(String userNameWithDomain) {
        this.userNameWithDomain = userNameWithDomain;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setExpireTimeStamp(long expireTimeStamp) {
        this.expireTimeStamp = expireTimeStamp;
    }

    public void setCreatedTimeStamp(long createdTimeStamp) {
        this.createdTimeStamp = createdTimeStamp;
    }

    public void setUsedTimeStamp(long usedTimeStamp) {
        this.usedTimeStamp = usedTimeStamp;
    }

    public static ResetPasswordLinkEntity createEntity(String baseUrl, String token, String userNameWithDomain,
                                                       String userId, String emailAddress, long expireAfterMills) {
        ResetPasswordLinkEntity entity = new ResetPasswordLinkEntity();
        entity.token = token;
        entity.url = StringUtil.joinUrl(baseUrl, "", token);
        entity.userNameWithDomain = userNameWithDomain;
        entity.userId = userId;
        entity.emailAddress = emailAddress;
        entity.createdTimeStamp = TimeUtil.getCurrentTimestampInMills();
        entity.expireTimeStamp = TimeUtil.getTimestampInMills(entity.getCreatedTimeStamp(), expireAfterMills);
        entity.usedTimeStamp = 0;
        return entity;
    }
}

