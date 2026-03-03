package com.zoo.model;

import com.zoo.bridge.Habitat;

public abstract class Animal {

    protected String nombre;
    protected int edad;
    protected Habitat habitat;

    protected Animal(String nombre, int edad, Habitat habitat) {
        this.nombre = nombre;
        this.edad = edad;
        this.habitat = habitat;
    }

    public abstract String hacerSonido();

    public String getNombre() {
        return nombre;
    }

    public int getEdad() {
        return edad;
    }

    public String getHabitat() {
        return habitat.describir();
    }

    public String getTipo() {
        return getClass().getSimpleName().toUpperCase();
    }
}
