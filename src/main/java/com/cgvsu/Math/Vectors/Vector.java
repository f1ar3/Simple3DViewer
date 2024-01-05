package com.cgvsu.Math.Vectors;


/** @author <a href="https://github.com/valyalshikovd/LinearAlgebra">Валяльщиков Дмитрий Алексеевич</a> */
public interface Vector {
    public static double cosAngleBetweenVectors(Vector vector1, Vector vector2){
        return (vector1.scalarProduct(vector2))/(vector1.length()*vector2.length());
    }
    public double[] getArrValues();
    public Vector subtraction( Vector vector);
    public Vector  addition(Vector vector);
    public Vector scale(double a);
    public double length();
    public Vector normalization();
    public double scalarProduct(Vector vector);
    public double cosAngleBetweenVectors(Vector vector);
    public int getDimensional();
    public double getLength();

}
