package com.zoo.factory;

import com.zoo.dto.AnimalDTO;
import com.zoo.model.Animal;

public abstract class AnimalFactory {
    public abstract Animal crearAnimal(AnimalDTO dto);
}
