package com.zoo.model;

import com.zoo.bridge.Habitat;

public class Tiburon extends Animal {
    public Tiburon(String nombre, int edad, Habitat habitat) {
        super(nombre, edad, habitat);
    }

    @Override
    public String hacerSonido() {
        return "Silencio";
    }
}