package com.zoo.service;

import com.zoo.chain.*;
import com.zoo.dto.AnimalDTO;
import com.zoo.factory.AnimalFactory;
import com.zoo.model.Animal;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class AnimalService {

    private final List<Animal> animales = new ArrayList<>();
    private final AnimalFactory factory;

    public AnimalService(AnimalFactory factory) {
        this.factory = factory;
    }

    public void registrarAnimal(AnimalDTO dto) {
        construirCadena().validar(dto);
        Animal animal = factory.crearAnimal(dto);
        animales.add(animal);
    }

    public List<Animal> listar() {
        return List.copyOf(animales);
    }

    public List<Animal> ordenarPor(String criterio) {
        List<Animal> copia = new ArrayList<>(animales);

        return switch (criterio.toLowerCase()) {
            case "edad"   -> { copia.sort(Comparator.comparingInt(Animal::getEdad)); yield copia; }
            case "nombre" -> { copia.sort(Comparator.comparing(Animal::getNombre, String.CASE_INSENSITIVE_ORDER)); yield copia; }
            default -> throw new IllegalArgumentException("Criterio inválido. Usa 'edad' o 'nombre'.");
        };
    }

    /**
     * Construye la cadena de responsabilidad con 5 eslabones:
     *
     *   ValidarNombre → ValidarEdad → ValidarTipo → ValidarHabitat → ValidarCompatibilidad
     *
     * El orden importa: primero validamos que los campos existan y sean
     * reconocidos, y solo al final validamos la regla de negocio (compatibilidad),
     * porque esa validación asume que tipo y hábitat ya son valores válidos.
     */
    private Validador construirCadena() {
        Validador nombre         = new ValidarNombre();
        Validador edad           = new ValidarEdad();
        Validador tipo           = new ValidarTipo();
        Validador habitat        = new ValidarHabitat();
        Validador compatibilidad = new ValidarCompatibilidad();

        nombre.setSiguiente(edad);
        edad.setSiguiente(tipo);
        tipo.setSiguiente(habitat);
        habitat.setSiguiente(compatibilidad);   // ← eslabón de lógica de negocio

        return nombre;
    }
}