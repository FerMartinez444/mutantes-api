package org.example.service;
import org.example.entity.DnaRecord;
import org.example.repository.DnaRecordRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import java.util.Optional;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class MutantService {
    private final MutantDetector mutantDetector;
    private final DnaRecordRepository dnaRecordRepository;

    public boolean analyzeDna(String[] dna){
        String dnaHash = String.valueOf(Arrays.deepHashCode(dna));
        Optional<DnaRecord> existingRecord = dnaRecordRepository.findByDnaHash(dnaHash);
        if(existingRecord.isPresent()){
            return existingRecord.get().isMutant();
        }
        boolean isMutant = mutantDetector.isMutant(dna);
        DnaRecord newRecord = DnaRecord.builder()
                .dnaHash(dnaHash)
                .isMutant(isMutant)
                .build();
        dnaRecordRepository.save(newRecord);
        return isMutant;
    }
}
