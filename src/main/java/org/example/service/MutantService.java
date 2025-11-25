package org.example.service;
import org.example.entity.DnaRecord;
import org.example.repository.DnaRecordRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class MutantService {
    private final MutantDetector mutantDetector;
    private final DnaRecordRepository dnaRecordRepository;

    public boolean analyzeDna(String[] dna){
        String dnaHash = String.valueOf(calculateDnaHash(dna));
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

    private String calculateDnaHash(String[] dna){
        try{
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            String dnaString = String.join("", dna);
            byte[] encodedhash = digest.digest(dnaString.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2 * encodedhash.length);
            for(byte b : encodedhash){
                String hex = Integer.toHexString(0xff & b);
                if(hex.length() == 1){
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch(Exception e) {
            throw new RuntimeException("Error al calcular hash SHA-256", e);
        }
    }
}
