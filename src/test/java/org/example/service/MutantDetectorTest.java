package org.example.service;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MutantDetectorTest {
    private MutantDetector mutantDetector;

    @BeforeEach
    void setUp(){
        mutantDetector = new MutantDetector();
    }

    @Test
    @DisplayName("Debe detectar mutante con secuencias horizontales")
    void testMutantHorizontal(){
        String[] dna = {
                "AAAA",
                "CCCC",
                "TCAG",
                "GGTC"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe detectar mutante con secuencias verticales")
    void testMutantVertical(){
        String[] dna = {
                "ATCG",
                "ATCG",
                "ATCG",
                "ATCG"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe detectar mutante con secuencias diagonales")
    void testMutantDiagonal(){
        String[] dna = {
                "ATCG",
                "GAGG",
                "CGAT",
                "GCGA"
        };
        assertTrue(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe retornar false para un humano simple")
    void testHuman(){
        String[] dna = {
                "ATCG",
                "CAGT",
                "TTAT",
                "AGAC"
        };
        assertFalse(mutantDetector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe lanzar excepción con ADN nulo")
    void testNullDna(){
        assertThrows(IllegalArgumentException.class, () -> {
            mutantDetector.isMutant(null);
        });
    }

    @Test
    @DisplayName("Debe lanzar exepción con ADN vacío")
    void testEmptyDna(){
        assertThrows(IllegalArgumentException.class, () -> {
            mutantDetector.isMutant(new String[]{});
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción con matriz no cuadrada")
    void testNonSquareMatrix(){
        String[] dna = {
                "ATCG",
                "AAT"
        };
        assertThrows(IllegalArgumentException.class, () -> {
            mutantDetector.isMutant(dna);
        });
    }

    @Test
    @DisplayName("Debe lanzar excepción con caracteres inválidos")
    void testInvalidCharacters(){
        String[] dna = {
                "ATCG",
                "ATCX",
                "ATCG",
                "ATCG"
        };
        assertThrows(IllegalArgumentException.class, () -> {
            mutantDetector.isMutant(dna);
        });
    }
}
