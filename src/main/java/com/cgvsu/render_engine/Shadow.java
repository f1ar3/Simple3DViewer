package com.cgvsu.render_engine;

import com.cgvsu.Math.Coordinate.BarycentricCoordinates;
import com.cgvsu.Math.Vectors.ThreeDimensionalVector;

public class Shadow {
    float c1;
    float c2;
    float c3;

    public Shadow(ThreeDimensionalVector[] normals, ThreeDimensionalVector target, ThreeDimensionalVector position) {
        this.c1 = calculateShade(normals[0], target, position);
        this.c2 = calculateShade(normals[1], target, position);
        this.c3 = calculateShade(normals[2], target, position);
    }

    private float calculateShade(
            ThreeDimensionalVector normal,
            ThreeDimensionalVector target,
            ThreeDimensionalVector position) {

        ThreeDimensionalVector v = ThreeDimensionalVector.subtraction(target, position);
        float cosine = (float) normal.normalization().scalarProduct(v.normalization());
        return Float.max(0.8f, Math.abs(cosine));
    }

    public float calculateShadeCoefficients(BarycentricCoordinates bCoordinates) {
        return (float) (bCoordinates.getU() * c1 + bCoordinates.getV() * c2 + bCoordinates.getW() * c3);
    }
}
