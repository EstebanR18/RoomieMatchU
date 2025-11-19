package co.edu.ucentral.service;

import co.edu.ucentral.entity.PerfilBuscoLugarEntity;
import co.edu.ucentral.entity.PerfilTengoLugarEntity;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class MatchScoreService {

    public int calcularScoreBuscoLugar(PerfilBuscoLugarEntity busco, PerfilTengoLugarEntity tengo) {
        int score = 0;

        // 1. Barrio (máxima prioridad)
        if (busco.getBarrio() != null && busco.getBarrio().equalsIgnoreCase(tengo.getBarrio())) {
            score += 50;
        }

        // 2. Presupuesto
        if (busco.getPresupuesto() != null && tengo.getArriendo() != null &&
                busco.getPresupuesto().compareTo(tengo.getArriendo()) >= 0) {
            score += 35;
        }

        // 3. Compatibilidad secundaria
        if (busco.getMascota() != null && tengo.getMascota() != null &&
                busco.getMascota().equals(tengo.getMascota())) {
            score += 5;
        }

        if (busco.getHabitos() != null && tengo.getHabitos() != null &&
                busco.getHabitos().equalsIgnoreCase(tengo.getHabitos())) {
            score += 5;
        }

        if (busco.getIdioma() != null && busco.getIdioma().equalsIgnoreCase(tengo.getIdioma())) {
            score += 5;
        }


        return score;
    }


    public int calcularScoreTengoLugar(PerfilTengoLugarEntity tengo, PerfilBuscoLugarEntity busco) {
        int score = 0;

        // 1. Barrio
        if (tengo.getBarrio() != null && tengo.getBarrio().equalsIgnoreCase(busco.getBarrio())) {
            score += 50;
        }

        // 2. Presupuesto del busco
        if (busco.getPresupuesto() != null && tengo.getArriendo() != null &&
                busco.getPresupuesto().compareTo(tengo.getArriendo()) >= 0) {
            score += 35;
        }

        // 3. Compatibilidad secundaria
        if (busco.getMascota() != null && tengo.getMascota() != null &&
                busco.getMascota().equals(tengo.getMascota())) {
            score += 5;
        }

        if (tengo.getHabitos() != null && busco.getHabitos() != null &&
                tengo.getHabitos().equalsIgnoreCase(busco.getHabitos())) {
            score += 5;
        }

        if (tengo.getIdioma() != null && busco.getIdioma() != null &&
                tengo.getIdioma().equalsIgnoreCase(busco.getIdioma())) {
            score += 5;
        }

        return score;
    }
}


