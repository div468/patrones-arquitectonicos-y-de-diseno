package com.zoo.chain;

import com.zoo.dto.AnimalDTO;

/**
 * Eslabón 2 — Chain of Responsibility
 *
 * Valida que la edad sea un valor realista para un animal de zoológico:
 * entre 1 y 80 años.
 */
public class ValidarEdad extends Validador {

    private static final int EDAD_MIN = 1;
    private static final int EDAD_MAX = 80;

    @Override
    protected void ejecutarValidacion(AnimalDTO dto) {
        int edad = dto.getEdad();

        if (edad < EDAD_MIN) {
            throw new IllegalArgumentException(
                "La edad debe ser al menos " + EDAD_MIN + " año"
            );
        }
        if (edad > EDAD_MAX) {
            throw new IllegalArgumentException(
                "La edad no puede superar " + EDAD_MAX + " años"
            );
        }
    }
}