
package com.server.informaViesCat.Entities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;

/**
 *
 * @author leith
 */
    public  class Coordenada {
        
        private float latitud;
        private float longitud;

        public Coordenada(float latitud, float longitud) {
            this.latitud = latitud;
            this.longitud = longitud;
        }

        public float getLatitud() {
            return latitud;
        }

        public float getLongitud() {
            return longitud;
        }
        
        
        
    public JSONObject convertObjectToJson() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonString = objectMapper.writeValueAsString(this);
            return new JSONObject(jsonString);
        } catch (JsonProcessingException e) {
            e.printStackTrace(); // Maneja la excepción según tus necesidades
            return null;
        }
    }
    }

    
    
