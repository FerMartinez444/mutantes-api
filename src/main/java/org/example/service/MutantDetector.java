package org.example.service;
import org.springframework.stereotype.Service;
import java.util.regex.Pattern;

@Service
public class MutantDetector {
    private static final int SEQUENCE_LENGTH = 4;
    private static final int MUTANT_SEQUENCE_THRESHOLD = 1;
    private static final Pattern VALID_DNA_PATTERN = Pattern.compile("^[ATCG]+$");

    public boolean isMutant(String[] dna){
        if (dna == null || dna.length == 0){
            throw new IllegalArgumentException("El ADN no puede ser nulo ni vacio");
        }

        int n = dna.length;
        char[][] matrix = new char[n][n];

        for(int i = 0; i < n; i++){
            String row = dna[i];
            if(row == null || row.length() != n || !VALID_DNA_PATTERN.matcher(row).matches()){
                throw new IllegalArgumentException("ADN inválido: debe ser NxN y contener solo A,T,C,G.");
            }
            matrix[i] = row.toCharArray();
        }
        int sequenceCount = 0;

        //Recorrer matriz
        for(int row = 0; row < n; row++){
            for(int col = 0; col < n; col++){
                //Horizontal
                if(col <= n - SEQUENCE_LENGTH){
                    if(checkHorizontal(matrix, row, col)){
                        sequenceCount++;
                        if(sequenceCount > MUTANT_SEQUENCE_THRESHOLD) return true;
                    }
                }
                //Vertical
                if(row <= n - SEQUENCE_LENGTH){
                    if(checkVertical(matrix, row, col)){
                        sequenceCount++;
                        if(sequenceCount > MUTANT_SEQUENCE_THRESHOLD) return true;
                    }
                }
                //Diagonal
                if(row <= n - SEQUENCE_LENGTH && col <= n - SEQUENCE_LENGTH){
                    if(checkDiagonal(matrix, row, col)){
                        sequenceCount++;
                        if(sequenceCount > MUTANT_SEQUENCE_THRESHOLD) return true;
                    }
                }
                //Diagonal Inversa
                if(row >= SEQUENCE_LENGTH - 1 && col <= n - SEQUENCE_LENGTH){
                    if (checkAntiDiagonal(matrix, row, col)) {
                        sequenceCount++;
                        if (sequenceCount > MUTANT_SEQUENCE_THRESHOLD) return true; // Early Termination
                    }
                }
            }
        }
        return false;
    }
    //Métodos auxiliares para la comparación.
    private boolean checkHorizontal(char[][] matrix, int row, int col){
        char base = matrix[row][col];
        return base == matrix[row][col+1] && base == matrix[row][col+2] && base == matrix[row][col+3];
    }

    private boolean checkVertical(char[][] matrix, int row, int col){
        char base = matrix[row][col];
        return base == matrix[row+1][col] && base == matrix[row+2][col] && base == matrix[row+3][col];
    }

    private boolean checkDiagonal(char[][] matrix, int row, int col){
        char base = matrix[row][col];
        return base == matrix[row+1][col+1] && base == matrix[row+2][col+2] && base == matrix[row+3][col+3];
    }

    private boolean checkAntiDiagonal(char[][] matrix, int row, int col){
        char base = matrix[row][col];
        return base == matrix[row-1][col+1] && base == matrix[row-2][col+2] && base == matrix[row-3][col+3];
    }
}
