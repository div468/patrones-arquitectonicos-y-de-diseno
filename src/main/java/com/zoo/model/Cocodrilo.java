package com.zoo.model;

import com.zoo.bridge.Habitat;

public class Cocodrilo extends Animal {
    public Cocodrilo(String nombre, int edad, Habitat habitat) {
        super(nombre, edad, habitat);
    }

    @Override
    public String hacerSonido() {
        return "Gruñido";
    }
}