package com.zoo.chain;

import com.zoo.dto.AnimalDTO;

public class ValidarNombre extends Validador {

    @Override
    protected void ejecutarValidacion(AnimalDTO dto) {
        if (dto.getNombre() == null || dto.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException("Nombre inválido");
        }
    }
}
