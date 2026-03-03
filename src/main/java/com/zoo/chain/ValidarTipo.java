package com.zoo.chain;

import com.zoo.dto.AnimalDTO;

import java.util.Set;

public class ValidarTipo extends Validador {

    private static final Set<String> TIPOS_VALIDOS = Set.of("LEON", "ELEFANTE", "MONO");

    @Override
    protected void ejecutarValidacion(AnimalDTO dto) {
        if (dto.getTipo() == null || !TIPOS_VALIDOS.contains(dto.getTipo().toUpperCase())) {
            throw new IllegalArgumentException("Tipo inválido");
        }
    }
}
