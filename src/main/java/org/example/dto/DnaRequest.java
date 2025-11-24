package org.example.dto;
import lombok.Data;
import org.example.validation.ValidDna;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

@Data
public class DnaRequest implements Serializable{
    @NotNull(message = "El AND no puede ser nulo")
    @ValidDna
    private String[] dna;
}
