package com.biblieria.model;

import java.math.BigDecimal;

public class Libro {
    private int id;
    private String titulo;
    private String autor;
    private Integer anio;
    private BigDecimal precio;
    private int stock;
    private String descripcion;
    private String imagenRuta;
    private String imagenMime;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public Integer getAnio() { return anio; }
    public void setAnio(Integer anio) { this.anio = anio; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getImagenRuta() { return imagenRuta; }
    public void setImagenRuta(String imagenRuta) { this.imagenRuta = imagenRuta; }

    public String getImagenMime() { return imagenMime; }
    public void setImagenMime(String imagenMime) { this.imagenMime = imagenMime; }

    public boolean tieneImagen() { return imagenRuta != null && !imagenRuta.trim().isEmpty(); }
}
