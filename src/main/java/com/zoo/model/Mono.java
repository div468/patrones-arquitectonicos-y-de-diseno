package com.zoo.model;

import com.zoo.bridge.Habitat;

public class Mono extends Animal {

    public Mono(String nombre, int edad, Habitat habitat) {
        super(nombre, edad, habitat);
    }

    @Override
    public String hacerSonido() {
        return "Chillido";
    }
}
