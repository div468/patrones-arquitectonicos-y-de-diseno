package com.zoo.chain;

import com.zoo.dto.AnimalDTO;

import java.util.Map;
import java.util.Set;

/**
 * Eslabón 5 — Chain of Responsibility
 *
 * Valida que el animal sea compatible con su hábitat asignado.
 * Esta regla tiene lógica de negocio real: un Tiburón no puede
 * vivir en la Sabana, ni un León en el Acuario.
 *
 * La compatibilidad está definida por animal (cada tipo conoce
 * sus hábitats válidos), no al revés.
 */
public class ValidarCompatibilidad extends Validador {

    // Cada animal define los hábitats donde puede vivir
    private static final Map<String, Set<String>> COMPATIBILIDAD = Map.of(
        "LEON",      Set.of("SABANA", "DESIERTO"),
        "ELEFANTE",  Set.of("SABANA", "SELVA"),
        "MONO",      Set.of("SELVA"),
        "TIBURON",   Set.of("ACUARIO", "RIO"),
        "AGUILA",    Set.of("MONTANA"),
        "COCODRILO", Set.of("RIO", "SELVA")
    );

    @Override
    protected void ejecutarValidacion(AnimalDTO dto) {
        String tipo    = dto.getTipo() == null    ? "" : dto.getTipo().toUpperCase();
        String habitat = dto.getHabitat() == null ? "" : dto.getHabitat().toUpperCase();

        Set<String> habitatsValidos = COMPATIBILIDAD.get(tipo);

        if (habitatsValidos == null) return; 
        if (!habitatsValidos.contains(habitat)) {
            String habitatsStr = String.join(", ", habitatsValidos);
            throw new IllegalArgumentException(
                "Incompatible: " + tipo + " no puede vivir en " + habitat +
                ". Hábitats válidos: " + habitatsStr
            );
        }
    }
}