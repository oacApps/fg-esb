package co.za.flash.esb.cellular.service;

import co.za.flash.esb.cellular.database.CredentialLookup;
import co.za.flash.esb.cellular.database.CredentialLookupRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.stereotype.Service;

@Service
public class CredentialLookupService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CredentialLookupRepository credentialLookupRepository;

    public CredentialLookup credentialLookup(String Organization, String KeyType) {
        CredentialLookup credentialLookup = null;
        try {
            credentialLookup = credentialLookupRepository.findByOrganizationAndKeyType(Organization, KeyType);
        } catch (DataAccessResourceFailureException dataAccessResourceFailureException){
            LOGGER.error("credentialLookup DataAccessResourceFailureException: " + dataAccessResourceFailureException.getLocalizedMessage());
            dataAccessResourceFailureException.printStackTrace();
        } catch (RuntimeException  e){
            LOGGER.error("credentialLookup RuntimeException: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
        return credentialLookup;
    }
}
