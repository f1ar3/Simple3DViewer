package com.cgvsu.render_engine.graphicConveyor;

import com.cgvsu.Math.Matrix.FourDimensionalMatrix;
import com.cgvsu.Math.Matrix.NDimensionalMatrix;
import com.cgvsu.Math.Vectors.NDimensionalVector;
import com.cgvsu.Math.Vectors.ThreeDimensionalVector;
import org.junit.Test;

import static org.junit.Assert.*;

public class GraphicConveyorTest {
    @Test
    public void testGetRotationMatrix() {
        ThreeDimensionalVector angle = new ThreeDimensionalVector(90, 0, 0);
        NDimensionalMatrix rotationMatrix = GraphicConveyor.getRotationMatrix(angle);

        float[][] expectedValues = {
                {1, 0, 0, 0},
                {0, 0, 1, 0},
                {0, -1, 0, 0},
                {0, 0, 0, 1}
        };

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                assertEquals(expectedValues[i][j], rotationMatrix.getMatrixInVectors()[i].getArrValues()[j], 0.01);
            }
        }
    }
    @Test
    public void testGetScaleMatrix() {
        ThreeDimensionalVector scale = new ThreeDimensionalVector(2, 3, 4);
        NDimensionalMatrix scaleMatrix = GraphicConveyor.getScaleMatrix(scale);

        float[][] expectedValues = {
                {2, 0, 0, 0},
                {0, 3, 0, 0},
                {0, 0, 4, 0},
                {0, 0, 0, 1}
        };

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                assertEquals("Matrix value at [" + i + "][" + j + "] is incorrect", expectedValues[i][j], scaleMatrix.getMatrixInVectors()[i].getArrValues()[j], 0.01);
            }
        }
    }
    @Test
    public void testGetRotationMatrixX() {
        double xAngle = 90;
        NDimensionalMatrix rotationMatrixX = GraphicConveyor.getRotationMatrixX(xAngle);

        float[][] expectedValues = {
                {1, 0, 0, 0},
                {0, 0, 1, 0},
                {0, -1, 0, 0},
                {0, 0, 0, 1}
        };

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                assertEquals(expectedValues[i][j], rotationMatrixX.getMatrixInVectors()[i].getArrValues()[j], 0.01);
            }
        }
    }
    @Test
    public void multiplyMatrix4ByVector3_withZeroMatrix() {
        ThreeDimensionalVector vector = new ThreeDimensionalVector(1, 1, 1);
        FourDimensionalMatrix zeroMatrix = new FourDimensionalMatrix(
                new NDimensionalVector(0, 0, 0, 0),
                new NDimensionalVector(0, 0, 0, 0),
                new NDimensionalVector(0, 0, 0, 0),
                new NDimensionalVector(0, 0, 0, 0)
        );

        ThreeDimensionalVector result = GraphicConveyor.multiplyMatrix4ByVector3(zeroMatrix, vector);

        ThreeDimensionalVector expected = new ThreeDimensionalVector(0, 0, 0);
        assertEquals(expected, result);
    }
}
