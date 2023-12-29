package com.cgvsu.math;

// Это заготовка для собственной библиотеки для работы с линейной алгеброй
public class Vector2f {
    public Vector2f(float x, float y) {
        this.x = x;
        this.y = y;
    }

    float x, y;

    public String coordstoStringSplitBySpace() {
        return String.format("%s %s", x, y);
    }

    @Override
    public String toString() {
        return coordstoStringSplitBySpace();
    }
}
