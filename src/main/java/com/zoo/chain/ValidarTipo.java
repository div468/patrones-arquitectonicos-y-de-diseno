package com.zoo.chain;

import com.zoo.dto.AnimalDTO;

import java.util.Set;

/**
 * Eslabón 3 — Chain of Responsibility
 *
 * Valida que el tipo de animal sea uno de los reconocidos por el sistema.
 */
public class ValidarTipo extends Validador {

    private static final Set<String> TIPOS_VALIDOS =
        Set.of("LEON", "ELEFANTE", "MONO", "TIBURON", "AGUILA", "COCODRILO");

    @Override
    protected void ejecutarValidacion(AnimalDTO dto) {
        if (dto.getTipo() == null || !TIPOS_VALIDOS.contains(dto.getTipo().toUpperCase())) {
            throw new IllegalArgumentException(
                "Tipo de animal no reconocido: " + dto.getTipo()
            );
        }
    }
}