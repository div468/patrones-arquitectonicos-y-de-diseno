package com.zoo.chain;

import com.zoo.dto.AnimalDTO;

/**
 * Eslabón 1 — Chain of Responsibility
 *
 * Valida que el nombre no esté vacío, no tenga números ni
 * caracteres especiales, y no exceda 30 caracteres.
 */
public class ValidarNombre extends Validador {

    @Override
    protected void ejecutarValidacion(AnimalDTO dto) {
        String nombre = dto.getNombre();

        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede estar vacío");
        }
        if (nombre.trim().length() > 30) {
            throw new IllegalArgumentException("El nombre no puede superar 30 caracteres");
        }
        if (!nombre.trim().matches("[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+")) {
            throw new IllegalArgumentException("El nombre solo puede contener letras");
        }
    }
}