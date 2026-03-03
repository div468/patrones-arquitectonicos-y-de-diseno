package com.zoo.model;

import com.zoo.bridge.Habitat;

public class Elefante extends Animal {

    public Elefante(String nombre, int edad, Habitat habitat) {
        super(nombre, edad, habitat);
    }

    @Override
    public String hacerSonido() {
        return "Barrito";
    }
}
