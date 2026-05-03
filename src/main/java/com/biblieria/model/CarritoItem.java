package com.biblieria.model;

import java.math.BigDecimal;

public class CarritoItem {
    private final Libro libro;
    private final int cantidad;

    public CarritoItem(Libro libro, int cantidad) {
        this.libro = libro;
        this.cantidad = cantidad;
    }

    public Libro getLibro() { return libro; }
    public int getCantidad() { return cantidad; }

    public BigDecimal getSubtotal() {
        return libro.getPrecio().multiply(BigDecimal.valueOf(cantidad));
    }
}
