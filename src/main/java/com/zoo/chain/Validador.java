package com.zoo.chain;

import com.zoo.dto.AnimalDTO;

public abstract class Validador {

    protected Validador siguiente;

    public void setSiguiente(Validador siguiente) {
        this.siguiente = siguiente;
    }

    public void validar(AnimalDTO dto) {
        ejecutarValidacion(dto);
        if (siguiente != null) {
            siguiente.validar(dto);
        }
    }

    protected abstract void ejecutarValidacion(AnimalDTO dto);
}
