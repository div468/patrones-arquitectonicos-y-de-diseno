package com.zoo.model;

import com.zoo.bridge.Habitat;

public class Aguila extends Animal {
    public Aguila(String nombre, int edad, Habitat habitat) {
        super(nombre, edad, habitat);
    }

    @Override
    public String hacerSonido() {
        return "Chillido agudo";
    }
}