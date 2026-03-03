package com.zoo.chain;

import com.zoo.dto.AnimalDTO;

public class ValidarEdad extends Validador {

    @Override
    protected void ejecutarValidacion(AnimalDTO dto) {
        if (dto.getEdad() <= 0) {
            throw new IllegalArgumentException("Edad inválida");
        }
    }
}
