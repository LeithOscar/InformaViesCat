
package com.server.informaViesCat.Entities;

import java.util.List;

public class CoordenadasUtils {

    public static Coordenada FindNearMe(float latitud, float longitud, List<Coordenada> listaCoordenadas) {
        Coordenada coordenadaMesPropera = null;
        double distanciaMinima = Double.MAX_VALUE;

        for (Coordenada coordenada : listaCoordenadas) {
            double distancia = CalculateDistance(latitud, longitud, coordenada.getLatitud(), coordenada.getLongitud());

            if (distancia < distanciaMinima) {
                distanciaMinima = distancia;
                coordenadaMesPropera = coordenada;
            }
        }

        return coordenadaMesPropera;
    }

    private static double CalculateDistance(float latitud1, float longitud1, float latitud2, float longitud2) {
        double radiusTerra = 6371; // Radio de la Tierra en kilómetros

        double latitudRad1 = Math.toRadians(latitud1);
        double longitudRad1 = Math.toRadians(longitud1);
        double latitudRad2 = Math.toRadians(latitud2);
        double longitudRad2 = Math.toRadians(longitud2);

        double dLatitud = latitudRad2 - latitudRad1;
        double dLongitud = longitudRad2 - longitudRad1;

        double a = Math.sin(dLatitud / 2) * Math.sin(dLatitud / 2) +
                   Math.cos(latitudRad1) * Math.cos(latitudRad2) *
                   Math.sin(dLongitud / 2) * Math.sin(dLongitud / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return radiusTerra * c;
    }


}

