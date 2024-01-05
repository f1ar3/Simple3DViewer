package com.cgvsu.Math.Vectors;


import java.util.List;

/** @author <a href="https://github.com/valyalshikovd/LinearAlgebra">Валяльщиков Дмитрий Алексеевич</a> */
public class ThreeDimensionalVector extends NDimensionalVector{

    private double a;
    private double b;
    private double c;


    public ThreeDimensionalVector(double a, double b, double c) {
        super(a,b,c);
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public double getA() {
        return a;
    }

    public double getB() {
        return b;
    }

    public double getC() {
        return c;
    }

    public void setA(double a) {
        this.a = a;
    }

    public void setB(double b) {
        this.b = b;
    }

    public void setC(double c) {
        this.c = c;
    }

    public static ThreeDimensionalVector addition(ThreeDimensionalVector vector1, ThreeDimensionalVector vector2) {
        return new ThreeDimensionalVector(
                vector1.a + vector2.a,
                vector1.b + vector2.b,
                vector1.c + vector2.c
        );
    }
    public static ThreeDimensionalVector subtraction(ThreeDimensionalVector vector1, ThreeDimensionalVector vector2) {
        return new ThreeDimensionalVector(
                vector1.a - vector2.a,
                vector1.b - vector2.b,
                vector1.c - vector2.c
        );
    }
    @Override
    public double length() {
        return length;
    }

    @Override
    public ThreeDimensionalVector normalization() {
        return new ThreeDimensionalVector(this.a / length, this.b / length, this.c / length);
    }
    @Override
    public double[] getArrValues() {
        return new double[]{a, b, c};
    }

    public ThreeDimensionalVector vectorProduct(ThreeDimensionalVector vector) {
        double i = b * vector.getC() - c * vector.getB();
        double j = a * vector.getC() - c * vector.getA();
        double k = a * vector.getB() - b * vector.getA();
        return new ThreeDimensionalVector(i, -j, k);
    }

    public static ThreeDimensionalVector sum(List<ThreeDimensionalVector> vectors) {
        final var result = new ThreeDimensionalVector(1,1,1);
        vectors.forEach(result::addition);
        return result;
    }

}
