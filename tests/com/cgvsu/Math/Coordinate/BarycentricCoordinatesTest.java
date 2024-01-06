package com.cgvsu.Math.Coordinate;

import com.cgvsu.Math.Vectors.TwoDimensionalVector;
import org.junit.Test;

import static org.junit.Assert.*;

public class BarycentricCoordinatesTest {
    @Test
    public void testSumOfCoordinates() {
        TwoDimensionalVector a = new TwoDimensionalVector(0, 0);
        TwoDimensionalVector b = new TwoDimensionalVector(1, 0);
        TwoDimensionalVector c = new TwoDimensionalVector(0, 1);
        TwoDimensionalVector p = new TwoDimensionalVector(0.5, 0.5);

        BarycentricCoordinates coord = new BarycentricCoordinates(a, b, c, p);
        double sum = coord.getU() + coord.getV() + coord.getW();
        assertEquals(1, sum, 0.01);
    }

    @Test
    public void testBelongsToTriangle() {
        TwoDimensionalVector a = new TwoDimensionalVector(0, 0);
        TwoDimensionalVector b = new TwoDimensionalVector(1, 0);
        TwoDimensionalVector c = new TwoDimensionalVector(0, 1);
        TwoDimensionalVector pInside = new TwoDimensionalVector(0.5, 0.25);
        TwoDimensionalVector pOutside = new TwoDimensionalVector(-0.5, 0.5);

        BarycentricCoordinates coordInside = new BarycentricCoordinates(a, b, c, pInside);
        BarycentricCoordinates coordOutside = new BarycentricCoordinates(a, b, c, pOutside);

        assertTrue(coordInside.belongsToTriangle());
        assertFalse(coordOutside.belongsToTriangle());
    }

    @Test
    public void testVertexCoordinates() {
        TwoDimensionalVector a = new TwoDimensionalVector(0, 0);
        TwoDimensionalVector b = new TwoDimensionalVector(1, 0);
        TwoDimensionalVector c = new TwoDimensionalVector(0, 1);

        BarycentricCoordinates coordA = new BarycentricCoordinates(a, b, c, a);
        assertBarycentricCoordinates(coordA, 1, 0, 0);
        BarycentricCoordinates coordB = new BarycentricCoordinates(a, b, c, b);
        assertBarycentricCoordinates(coordB, 0, 1, 0);
        BarycentricCoordinates coordC = new BarycentricCoordinates(a, b, c, c);
        assertBarycentricCoordinates(coordC, 0, 0, 1);
    }
    private void assertBarycentricCoordinates(BarycentricCoordinates coordinates, double expectedU, double expectedV, double expectedW) {
        final double DELTA = 0.0001;
        assertEquals(expectedU, coordinates.getU(), DELTA);
        assertEquals(expectedV, coordinates.getV(), DELTA);
        assertEquals(expectedW, coordinates.getW(), DELTA);
    }

    @Test
    public void testValidCoordinates() {
        TwoDimensionalVector a = new TwoDimensionalVector(0, 0);
        TwoDimensionalVector b = new TwoDimensionalVector(1, 0);
        TwoDimensionalVector c = new TwoDimensionalVector(0, 1);
        TwoDimensionalVector p = new TwoDimensionalVector(0.5, 0.5);

        BarycentricCoordinates coord = new BarycentricCoordinates(a, b, c, p);
        assertEquals(0, coord.getU(), 0.01);
        assertEquals(0.5, coord.getV(), 0.01);
        assertEquals(0.5, coord.getW(), 0.01);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidTriangle() {
        TwoDimensionalVector a = new TwoDimensionalVector(0, 0);
        TwoDimensionalVector b = new TwoDimensionalVector(0, 0);
        TwoDimensionalVector c = new TwoDimensionalVector(1, 1);
        TwoDimensionalVector p = new TwoDimensionalVector(0.5, 0.5);

        new BarycentricCoordinates(a, b, c, p);
    }
}
