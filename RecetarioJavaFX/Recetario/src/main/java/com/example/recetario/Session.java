package com.example.recetario;

import com.example.recetario.models.Receta;
import lombok.Data;

import java.util.ArrayList;


public class Session {
    private static Receta recetaActual = null;
    private static ArrayList<Receta> recetas = new ArrayList<>(0);
    private static Integer pos = null;

    public static Receta getRecetaActual() {
        return recetaActual;
    }

    public static void setRecetaActual(Receta recetaActual) {
        Session.recetaActual = recetaActual;
    }

    public static ArrayList<Receta> getRecetas() {
        return recetas;
    }

    public static void setRecetas(ArrayList<Receta> recetas) {
        Session.recetas = recetas;
    }

    public static Integer getPos() {
        return pos;
    }

    public static void setPos(Integer pos) {
        Session.pos = pos;
    }
}
