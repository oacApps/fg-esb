package co.za.flash.esb.oneforyou.trader.service;

import co.za.flash.esb.oneforyou.trader.dblookup.SeqNumGenerationRepo;
import co.za.flash.esb.oneforyou.trader.dto.SequenceDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;

@Service
public class SequenceService {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    SeqNumGenerationRepo seqNumGenerationRepo;

    public Long sequenceNumber(SequenceDTO sequenceDTO) {
        Long sequenceNumber = null;
        try {
            sequenceNumber = seqNumGenerationRepo.spNumericSeqNumGeneration(sequenceDTO.getAccountNumber(), sequenceDTO.getSequenceNumber(), sequenceDTO.getMessageType());
        }catch (Exception e){
            LOGGER.info("Sequence generation Request :" + sequenceDTO.toString());
            LOGGER.info("Generated Sequence number :" + sequenceNumber);
            LOGGER.error("sequenceNumber Exception: " + e.getLocalizedMessage());
        }
        return sequenceNumber;
    }
}
