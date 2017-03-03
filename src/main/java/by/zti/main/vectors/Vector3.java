package by.zti.main.vectors;

import java.io.Serializable;

public class Vector3<T> implements Serializable{
    private String delimiter;
    private T x, y, z;

    public Vector3(T x, T y, T z) {
        this.x = x;
        this.y = y;
        this.z = z;
        delimiter = " ";
    }

    @Override
    public String toString() {
        return x + delimiter + y + delimiter + z;
    }

    public T getX() {
        return x;
    }

    public void setX(T x) {
        this.x = x;
    }

    public T getY() {
        return y;
    }

    public void setY(T y) {
        this.y = y;
    }

    public T getZ() {
        return z;
    }

    public void setZ(T z) {
        this.z = z;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }
}
