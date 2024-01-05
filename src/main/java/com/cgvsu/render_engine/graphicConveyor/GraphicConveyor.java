package com.cgvsu.render_engine.graphicConveyor;

import com.cgvsu.Math.Matrix.NDimensionalMatrix;
import com.cgvsu.Math.Point.Point2f;
import com.cgvsu.Math.Vectors.NDimensionalVector;
import com.cgvsu.Math.Vectors.ThreeDimensionalVector;
import com.cgvsu.Math.Vectors.Vector;


public class GraphicConveyor {

    public static NDimensionalMatrix getScaleMatrix(ThreeDimensionalVector scale) {
        NDimensionalVector v1 = new NDimensionalVector(scale.getA(), 0, 0, 0);
        NDimensionalVector v2 = new NDimensionalVector(0, scale.getB(), 0, 0);
        NDimensionalVector v3 = new NDimensionalVector(0, 0, scale.getC(), 0);
        NDimensionalVector v4 = new NDimensionalVector(0, 0, 0, 1);

        return new NDimensionalMatrix(v1, v2, v3, v4);
    }

    public static NDimensionalMatrix getRotationMatrix(ThreeDimensionalVector angle) {
        NDimensionalMatrix matrixRotationX = getRotationMatrixX(angle.getA());
        NDimensionalMatrix matrixRotationY = getRotationMatrixY(angle.getB());
        NDimensionalMatrix matrixRotationZ = getRotationMatrixZ(angle.getC());

        return (NDimensionalMatrix) matrixRotationX.multiplyMatrix(matrixRotationY).multiplyMatrix(matrixRotationZ);
    }

    protected static NDimensionalMatrix getRotationMatrixX(double xAngle) {
        xAngle = Math.toRadians(xAngle);
        double cosX = Math.cos(xAngle);
        double sinX = Math.sin(xAngle);

        NDimensionalVector v1 = new NDimensionalVector(1, 0, 0, 0);
        NDimensionalVector v2 = new NDimensionalVector(0, cosX, sinX, 0);
        NDimensionalVector v3 = new NDimensionalVector(0, -sinX, cosX, 0);
        NDimensionalVector v4 = new NDimensionalVector(0, 0, 0, 1);

        return new NDimensionalMatrix(v1, v2, v3, v4);
    }

    private static NDimensionalMatrix getRotationMatrixY(double yAngle) {
        yAngle = Math.toRadians(yAngle);
        double cosY = Math.cos(yAngle);
        double sinY = Math.sin(yAngle);

        NDimensionalVector v1 = new NDimensionalVector(cosY, 0, -sinY, 0);
        NDimensionalVector v2 = new NDimensionalVector(0, 1, 0, 0);
        NDimensionalVector v3 = new NDimensionalVector(sinY, 0, cosY, 0);
        NDimensionalVector v4 = new NDimensionalVector(0, 0, 0, 1);

        return new NDimensionalMatrix(v1, v2, v3, v4);
    }

    private static NDimensionalMatrix getRotationMatrixZ(double zAngle) {
        zAngle = Math.toRadians(zAngle);
        double cosZ = Math.cos(zAngle);
        double sinZ = Math.sin(zAngle);

        NDimensionalVector v1 = new NDimensionalVector(cosZ, sinZ, 0, 0);
        NDimensionalVector v2 = new NDimensionalVector(-sinZ, cosZ, 0, 0);
        NDimensionalVector v3 = new NDimensionalVector(0, 0, 1, 0);
        NDimensionalVector v4 = new NDimensionalVector(0, 0, 0, 1);

        return new NDimensionalMatrix(v1, v2, v3, v4);
    }

    private static NDimensionalMatrix getTranslationMatrix(ThreeDimensionalVector translate) {
        NDimensionalVector v1 = new NDimensionalVector(1, 0, 0, translate.getA());
        NDimensionalVector v2 = new NDimensionalVector(0, 1, 0, translate.getB());
        NDimensionalVector v3 = new NDimensionalVector(0, 0, 1, translate.getC());
        NDimensionalVector v4 = new NDimensionalVector(0, 0, 0, 1);

        return new NDimensionalMatrix(v1, v2, v3, v4);
    }

    public static NDimensionalMatrix getModelMatrix(ThreeDimensionalVector translate, ThreeDimensionalVector anglesOfRotate, ThreeDimensionalVector scale) {
        NDimensionalMatrix translationMatrix = getTranslationMatrix(translate);
        NDimensionalMatrix rotationMatrix = getRotationMatrix(anglesOfRotate);
        NDimensionalMatrix scaleMatrix = getScaleMatrix(scale);

        return (NDimensionalMatrix) translationMatrix.multiplyMatrix(rotationMatrix).multiplyMatrix(scaleMatrix);
    }

    public static NDimensionalMatrix lookAt(ThreeDimensionalVector eye, ThreeDimensionalVector target) {
        return lookAt(new ThreeDimensionalVector(eye.getArrValues()[0], eye.getArrValues()[1], eye.getArrValues()[2]), new ThreeDimensionalVector(target.getArrValues()[0], target.getArrValues()[1], target.getArrValues()[2]), new ThreeDimensionalVector(0F, 1.0F, 0F));
    }

    public static NDimensionalMatrix lookAt(ThreeDimensionalVector eye, ThreeDimensionalVector target, ThreeDimensionalVector up) {
        Vector tempNd = target.subtraction(eye);
        ThreeDimensionalVector resultZ = new ThreeDimensionalVector(tempNd.getArrValues()[0], tempNd.getArrValues()[1], tempNd.getArrValues()[2]);
        ThreeDimensionalVector resultX = up.vectorProduct(resultZ);
        ThreeDimensionalVector resultY = resultZ.vectorProduct(resultX);

        resultX = resultX.normalization();
        resultY = resultY.normalization();
        resultZ = resultZ.normalization();

        return new NDimensionalMatrix(
                new NDimensionalVector(resultX.getA(), resultY.getA(), resultZ.getA(), 0),
                new NDimensionalVector(resultX.getB(), resultY.getB(), resultZ.getB(), 0),
                new NDimensionalVector(resultX.getC(), resultY.getC(), resultZ.getC(), 0),
                new NDimensionalVector(-resultX.scalarProduct(eye), -resultY.scalarProduct(eye), -resultZ.scalarProduct(eye), 1)
        );
    }

    public static NDimensionalMatrix perspective(
            final float fov,
            final float aspectRatio,
            final float nearPlane,
            final float farPlane) {
        float tangentMinusOnDegree = (float) (1.0F / (Math.tan(fov * 0.5F)));

        return new NDimensionalMatrix(
                new NDimensionalVector(tangentMinusOnDegree / aspectRatio, 0, 0, 0),
                new NDimensionalVector(0, tangentMinusOnDegree, 0, 0),
                new NDimensionalVector(0, 0, (farPlane + nearPlane) / (farPlane - nearPlane), 1),
                new NDimensionalVector(0, 0, 2 * (nearPlane * farPlane) / (nearPlane - farPlane), 0)
        );
    }

    public static ThreeDimensionalVector multiplyMatrix4ByVector3(final NDimensionalMatrix matrix, final ThreeDimensionalVector vertex) {
        final double x = (vertex.getA() * matrix.getMatrixInVectors()[0].getArrValues()[0]) + (vertex.getB() * matrix.getMatrixInVectors()[1].getArrValues()[0]) + (vertex.getC() * matrix.getMatrixInVectors()[2].getArrValues()[0]) + matrix.getMatrixInVectors()[3].getArrValues()[0];
        final double y = (vertex.getA() * matrix.getMatrixInVectors()[0].getArrValues()[1]) + (vertex.getB() * matrix.getMatrixInVectors()[1].getArrValues()[1]) + (vertex.getC() * matrix.getMatrixInVectors()[2].getArrValues()[1]) + matrix.getMatrixInVectors()[3].getArrValues()[1];
        final double z = (vertex.getA() * matrix.getMatrixInVectors()[0].getArrValues()[2]) + (vertex.getB() * matrix.getMatrixInVectors()[1].getArrValues()[2]) + (vertex.getC() * matrix.getMatrixInVectors()[2].getArrValues()[2]) + matrix.getMatrixInVectors()[3].getArrValues()[2];
        final double w = (vertex.getA() * matrix.getMatrixInVectors()[0].getArrValues()[3]) + (vertex.getB() * matrix.getMatrixInVectors()[1].getArrValues()[3]) + (vertex.getC() * matrix.getMatrixInVectors()[2].getArrValues()[3]) + matrix.getMatrixInVectors()[3].getArrValues()[3];
        return new ThreeDimensionalVector(x / w, y / w, z / w);
    }

    public static Point2f vertexToPoint(final ThreeDimensionalVector vertex, final int width, final int height) {
        return new Point2f((float) (vertex.getA() * width + width / 2.0F), (float) (-vertex.getB() * height + height / 2.0F));
    } // TODO: исправить костыль
}
