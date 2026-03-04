package com.zoo.chain;

import com.zoo.dto.AnimalDTO;

import java.util.Set;

/**
 * Eslabón 4 — Chain of Responsibility
 *
 * Valida que el hábitat sea uno de los disponibles en el zoológico.
 */
public class ValidarHabitat extends Validador {

    private static final Set<String> HABITATS_VALIDOS =
        Set.of("SABANA", "SELVA", "ACUARIO", "RIO", "DESIERTO", "MONTANA");

    @Override
    protected void ejecutarValidacion(AnimalDTO dto) {
        if (dto.getHabitat() == null || !HABITATS_VALIDOS.contains(dto.getHabitat().toUpperCase())) {
            throw new IllegalArgumentException(
                "Hábitat no reconocido: " + dto.getHabitat()
            );
        }
    }
}