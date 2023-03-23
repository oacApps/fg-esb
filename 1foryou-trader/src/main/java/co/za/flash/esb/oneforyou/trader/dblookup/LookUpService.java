package co.za.flash.esb.oneforyou.trader.dblookup;

import co.za.flash.esb.oneforyou.trader.mapper.CredentialLookupMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Service;

@Service
public class LookUpService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    TreasuryCredentialLookupRepo treasuryCredentialLookupRepo;

    public CredentialLookupDTO getByOrganizationAndKeyType(String organization, String keyType){
        CredentialLookupDTO credentialLookupDTO=null;
        try {
            TreasuryCredentialLookupEntity lookupEntity = treasuryCredentialLookupRepo.findByOrganizationAndKeyType(organization, keyType);
            credentialLookupDTO = CredentialLookupMapper.INSTANCE.toDto(lookupEntity);
        }catch (DataAccessResourceFailureException dataAccessResourceFailureException){
            LOGGER.error("LookUpService DataAccessResourceFailureException: " + dataAccessResourceFailureException.getLocalizedMessage());
        } catch (RuntimeException  e){
            LOGGER.error("LookUpService RuntimeException: " + e.getLocalizedMessage());
        }
        return credentialLookupDTO;
    }


}
