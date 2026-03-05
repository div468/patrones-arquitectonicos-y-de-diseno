package com.zoo.factory;

import com.zoo.bridge.*;
import com.zoo.dto.AnimalDTO;
import com.zoo.model.*;
import org.springframework.stereotype.Component;

/**
 * Factory Method — implementación concreta
 *
 * Centraliza la creación de animales y la asignación de su hábitat (Bridge).
 * Ninguna otra clase necesita saber qué subclase concreta instanciar;
 * solo llaman a crearAnimal(dto).
 *
 * Bridge en acción: el animal recibe una implementación de Habitat,
 * no un String. Así Leon con Sabana y Leon con Desierto son combinaciones
 * libres sin necesidad de subclases LeonSabana / LeonDesierto.
 */
@Component
public class AnimalFactoryImpl extends AnimalFactory {

    @Override
    public Animal crearAnimal(AnimalDTO dto) {

        // Bridge: construir el hábitat como objeto independiente
        Habitat habitat = switch (dto.getHabitat().toUpperCase()) {
            case "SABANA"  -> new Sabana();
            case "SELVA"   -> new Selva();
            case "ACUARIO" -> new Acuario();
            case "RIO"     -> new Rio();
            case "DESIERTO"-> new Desierto();
            case "MONTANA" -> new Montana();
            default -> throw new IllegalArgumentException("Hábitat no reconocido: " + dto.getHabitat());
        };

        // Factory Method: construir el animal concreto
        return switch (dto.getTipo().toUpperCase()) {
            case "LEON"      -> new Leon(dto.getNombre(), dto.getEdad(), habitat);
            case "ELEFANTE"  -> new Elefante(dto.getNombre(), dto.getEdad(), habitat);
            case "MONO"      -> new Mono(dto.getNombre(), dto.getEdad(), habitat);
            case "TIBURON"   -> new Tiburon(dto.getNombre(), dto.getEdad(), habitat);
            case "AGUILA"    -> new Aguila(dto.getNombre(), dto.getEdad(), habitat);
            case "COCODRILO" -> new Cocodrilo(dto.getNombre(), dto.getEdad(), habitat);
            default -> throw new IllegalArgumentException("Tipo no reconocido: " + dto.getTipo());
        };
    }
}