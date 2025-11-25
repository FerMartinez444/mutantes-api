package org.example.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.example.dto.DnaRequest;
import org.example.dto.StatsResponse;
import org.example.service.MutantService;
import org.example.service.StatsService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@Tag(name = "Mutant Detector", description = "Operaciones para detección de mutantes")
public class MutantController {
    private final MutantService mutantService;
    private final StatsService statsService;

    @Operation(summary = "Detectar si es humano o mutante")
    @ApiResponses(value ={
            @ApiResponse(responseCode = "200", description = "Es mutante"),
            @ApiResponse(responseCode = "403", description = "Es humano"),
            @ApiResponse(responseCode = "400", description = "ADN inválido")
    })

    @PostMapping("/mutant")
    public ResponseEntity<Void> checkMutant(@Valid @RequestBody DnaRequest dnaRequest){
        boolean isMutant = mutantService.analyzeDna(dnaRequest.getDna());
        if(isMutant){
            return ResponseEntity.ok().build();
        } else{
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/stats")
    public ResponseEntity<StatsResponse> getStats(){
        return ResponseEntity.ok(statsService.getStats());
    }
}
