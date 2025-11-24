package org.example;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MutantDetectorApplication {
    public static void main(String[] args){
        SpringApplication.run(MutantDetectorApplication.class, args);
        System.out.println("La API está corriendo en el puerto 8080.");
    }
}
