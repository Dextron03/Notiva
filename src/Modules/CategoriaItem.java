package Modules;

public class CategoriaItem {
    public int id;
    public String nombre;

    public CategoriaItem(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String toString() {
        return this.nombre;
    }
}