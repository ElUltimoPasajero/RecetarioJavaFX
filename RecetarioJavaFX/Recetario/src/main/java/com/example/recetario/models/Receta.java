package com.example.recetario.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Receta {

    private String nombre;
    private String tipo;
    private Integer duracion;
    private String dificultad;

}
