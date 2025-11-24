package org.example.dto;
import lombok.Data;
import java.io.Serializable;

@Data
public class DnaRequest implements Serializable{
    private String[] dna;
}
