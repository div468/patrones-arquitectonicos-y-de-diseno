package com.zoo.factory;

import com.zoo.bridge.Acuario;
import com.zoo.bridge.Habitat;
import com.zoo.bridge.Sabana;
import com.zoo.bridge.Selva;
import com.zoo.dto.AnimalDTO;
import com.zoo.model.Animal;
import com.zoo.model.Elefante;
import com.zoo.model.Leon;
import com.zoo.model.Mono;
import org.springframework.stereotype.Component;

@Component
public class AnimalFactoryImpl extends AnimalFactory {

    @Override
    public Animal crearAnimal(AnimalDTO dto) {
        Habitat habitat = switch (dto.getHabitat().toUpperCase()) {
            case "SABANA" -> new Sabana();
            case "SELVA" -> new Selva();
            case "ACUARIO" -> new Acuario();
            default -> throw new IllegalArgumentException("Habitat inválido");
        };

        return switch (dto.getTipo().toUpperCase()) {
            case "LEON" -> new Leon(dto.getNombre(), dto.getEdad(), habitat);
            case "ELEFANTE" -> new Elefante(dto.getNombre(), dto.getEdad(), habitat);
            case "MONO" -> new Mono(dto.getNombre(), dto.getEdad(), habitat);
            default -> throw new IllegalArgumentException("Tipo inválido");
        };
    }
}
