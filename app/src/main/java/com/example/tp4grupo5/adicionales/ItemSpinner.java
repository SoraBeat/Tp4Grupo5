package com.example.tp4grupo5.adicionales;

public class ItemSpinner {
    private String nombre;
    private String valor;

    public ItemSpinner(String nombre, String valor) {
        this.nombre = nombre;
        this.valor = valor;
    }

    public String getNombre() {
        return nombre;
    }

    public String getValor() {
        return valor;
    }

    @Override
    public String toString() {
        // Esto define cómo se mostrará el elemento en el Spinner
        return nombre;
    }
}
