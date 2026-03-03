package com.zoo.service;

import com.zoo.chain.Validador;
import com.zoo.chain.ValidarEdad;
import com.zoo.chain.ValidarHabitat;
import com.zoo.chain.ValidarNombre;
import com.zoo.chain.ValidarTipo;
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

        if ("edad".equalsIgnoreCase(criterio)) {
            copia.sort(Comparator.comparingInt(Animal::getEdad));
            return copia;
        }

        if ("nombre".equalsIgnoreCase(criterio)) {
            copia.sort(Comparator.comparing(Animal::getNombre, String.CASE_INSENSITIVE_ORDER));
            return copia;
        }

        throw new IllegalArgumentException("Criterio inválido. Usa 'edad' o 'nombre'.");
    }

    private Validador construirCadena() {
        Validador nombre = new ValidarNombre();
        Validador edad = new ValidarEdad();
        Validador tipo = new ValidarTipo();
        Validador habitat = new ValidarHabitat();

        nombre.setSiguiente(edad);
        edad.setSiguiente(tipo);
        tipo.setSiguiente(habitat);

        return nombre;
    }
}
