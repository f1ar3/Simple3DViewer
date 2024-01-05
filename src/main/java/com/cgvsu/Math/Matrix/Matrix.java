package com.cgvsu.Math.Matrix;

import com.cgvsu.Math.Vectors.Vector;

/** @author <a href="https://github.com/valyalshikovd/LinearAlgebra">Валяльщиков Дмитрий Алексеевич</a> */
public interface Matrix {
    public Vector[] getMatrixInVectors();
    public int getDimensional();
    public Matrix addition(Matrix matrix);
    public Vector multiplyVector(Vector vector);
    public Matrix multiplyMatrix(Matrix vector);
    public Matrix transposition();
    public void printMatrix();
    public double getDeterminant();
    public Matrix inverseMatrix();
}
