package org.example.service;
import org.example.entity.DnaRecord;
import org.example.repository.DnaRecordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MutantServiceTest {
    @Mock
    private MutantDetector mutantDetector;

    @Mock
    private DnaRecordRepository dnaRecordRepository;

    @InjectMocks
    private MutantService mutantService;

    @Test
    @DisplayName("Si el ADN ya existe en BD, debe retornar el valor cacheado sin re-analizar")
    void testAnalyzeDna_Cached(){
        String[] dna = {"ATCG", "CAGT", "TTAT", "AGAC"};
        DnaRecord existingRecord = new DnaRecord();
        existingRecord.setMutant(true);

        when(dnaRecordRepository.findByDnaHash(anyString()))
                .thenReturn(Optional.of(existingRecord));

        boolean result = mutantService.analyzeDna(dna);

        assertTrue(result);

        verify(mutantDetector, never()).isMutant(any());
        verify(dnaRecordRepository, never()).save(any());
    }

    @Test
    @DisplayName("Si el ADN es nuevo, debe analizarlo y guardarlo")
    void testAnalyzeDna_New(){
        String[] dna = {"ATCG", "CAGT", "TTAT", "AGAC"};

        when(dnaRecordRepository.findByDnaHash(anyString()))
                .thenReturn(Optional.empty());

        when(mutantDetector.isMutant(any())).thenReturn(true);
        boolean result = mutantService.analyzeDna(dna);

        assertTrue(result);

        verify(mutantDetector, times(1)).isMutant(dna);
        verify(dnaRecordRepository, times(1)).save(any(DnaRecord.class));
    }
}
