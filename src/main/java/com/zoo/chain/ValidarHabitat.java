package com.zoo.chain;

import com.zoo.dto.AnimalDTO;

import java.util.Set;

public class ValidarHabitat extends Validador {

    private static final Set<String> HABITATS_VALIDOS = Set.of("SABANA", "SELVA", "ACUARIO");

    @Override
    protected void ejecutarValidacion(AnimalDTO dto) {
        if (dto.getHabitat() == null || !HABITATS_VALIDOS.contains(dto.getHabitat().toUpperCase())) {
            throw new IllegalArgumentException("Habitat inválido");
        }
    }
}
