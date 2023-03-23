package co.za.flash.esb.oneforyou.trader.dblookup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TreasuryCredentialLookupRepo extends JpaRepository<TreasuryCredentialLookupEntity, Long> {

    @Query(value = "select * from TreasuryCredentialLookup where Organization=:organization and KeyType=:keyType and Active=1", nativeQuery = true)
    TreasuryCredentialLookupEntity findByOrganizationAndKeyType(@Param("organization") String organization, @Param("keyType") String keyType);
}
