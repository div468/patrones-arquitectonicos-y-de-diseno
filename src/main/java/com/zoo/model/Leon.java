package com.zoo.model;

import com.zoo.bridge.Habitat;

public class Leon extends Animal {

    public Leon(String nombre, int edad, Habitat habitat) {
        super(nombre, edad, habitat);
    }

    @Override
    public String hacerSonido() {
        return "Rugido";
    }
}
