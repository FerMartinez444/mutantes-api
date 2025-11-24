package org.example.service;
import org.example.dto.StatsResponse;
import org.example.repository.DnaRecordRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatsService {
    private final DnaRecordRepository dnaRecordRepository;
    public StatsResponse getStats(){
        long countMutantDna = dnaRecordRepository.countByIsMutant(true);
        long countHumanDna = dnaRecordRepository.countByIsMutant(false);

        double ratio = 0;
        if(countHumanDna > 0){
            ratio = (double) countMutantDna / countHumanDna;
        } else if (countMutantDna >0) {
            ratio = 1.0;
        }

        return StatsResponse.builder()
                .countMutantDna(countMutantDna)
                .countHumanDna(countHumanDna)
                .ratio(ratio)
                .build();
    }
}
