package co.za.flash.esb.cellular.database;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CredentialLookupRepository extends JpaRepository<CredentialLookup, Integer> {

    @Query(value = "select * from OneBalanceCredentialLookup obcl where" +
            " obcl.Organization = :Organization AND obcl.KeyType = :KeyType", nativeQuery = true)
    CredentialLookup findByOrganizationAndKeyType(
            @Param("Organization") String Organization,
            @Param("KeyType") String KeyType);
}
