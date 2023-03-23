package co.za.flash.esb.oneforyou.trader.dblookup;

import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;

@Repository
public interface SeqNumGenerationRepo extends CrudRepository<SeqNumGeneration, Long> {

    @Procedure(procedureName = "sp_NumericSequenceNumberGeneration", outputParameterName = "NumericSequence")
    public Long spNumericSeqNumGeneration(String accountNumber, String sequenceNumber, String messageType);
}
