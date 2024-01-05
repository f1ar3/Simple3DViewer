package com.cgvsu.Math.Coordinate;

import com.cgvsu.Math.Vectors.TwoDimensionalVector;

public class BarycentricCoordinates {
    private final double u;
    private final double v;
    private final double w;

    public double getU() {
        return u;
    }

    public double getV() {
        return v;
    }

    public double getW() {
        return w;
    }

    public BarycentricCoordinates(TwoDimensionalVector a, TwoDimensionalVector b, TwoDimensionalVector c, TwoDimensionalVector p) {
        double s = (b.getA() - a.getA()) * (c.getB() - a.getB()) -
                (c.getA() - a.getA()) * (b.getB() - a.getB());
        if (s == 0) {
            throw new IllegalArgumentException("The points do not form a valid triangle (area is zero)");
        }
        double s1 = (b.getA() - p.getA()) * (c.getB() - p.getB()) -
                (c.getA() - p.getA()) * (b.getB() - p.getB());
        double s2 = (c.getA() - p.getA()) * (a.getB() - p.getB()) -
                (a.getA() - p.getA()) * (c.getB() - p.getB());
        u = s1 / s;
        v = s2 / s;
        w = 1 - u - v;
    }

    public boolean belongsToTriangle() {
        return u >= 0 && u <= 1 && v >= 0 && v <= 1 && w >= 0 && w <= 1;
    }

}
