package by.zti.main.vectors;

import java.io.Serializable;

public class Vector2<T> implements Serializable{
    private String delimiter;
    private T x, y;

    public Vector2(T x, T y) {
        this.x = x;
        this.y = y;
        delimiter = " ";
    }

    @Override
    public String toString() {
        return x + delimiter + y;
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

    public String getDelimiter() {
        return delimiter;
    }

    public void setDelimiter(String delimiter) {
        this.delimiter = delimiter;
    }
}
