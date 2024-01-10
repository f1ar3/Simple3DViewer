package com.cgvsu.render_engine.camera;

import com.cgvsu.Math.Matrix.NDimensionalMatrix;
import com.cgvsu.Math.Vectors.ThreeDimensionalVector;
import com.cgvsu.Math.Vectors.Vector;
import com.cgvsu.render_engine.graphicConveyor.GraphicConveyor;


public class Camera {
    private ThreeDimensionalVector position;
    private ThreeDimensionalVector target;
    private final float fov;
    private float aspectRatio;
    private final float nearPlane;
    private final float farPlane;

    public Camera(
            final ThreeDimensionalVector position,
            final ThreeDimensionalVector target,
            final float fov,
            final float aspectRatio,
            final float nearPlane,
            final float farPlane) {
        this.position = position;
        this.target = target;
        this.fov = fov;
        this.aspectRatio = aspectRatio;
        this.nearPlane = nearPlane;
        this.farPlane = farPlane;
    }

    public void setPosition(final ThreeDimensionalVector position) {
        this.position = position;
    }

    public void setTarget(final ThreeDimensionalVector target) {
        this.target = target;
    }

    public void setAspectRatio(final float aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    public ThreeDimensionalVector getPosition() {
        return position;
    }

    public ThreeDimensionalVector getTarget() {
        return target;
    }

    public void movePosition(final ThreeDimensionalVector translation) {
        this.position = addAndConvertToThreeDimensionalVector(this.position.addition(translation));
    }

    public void moveTarget(final ThreeDimensionalVector translation) {
        this.target = addAndConvertToThreeDimensionalVector(this.target.addition(translation));
    }
    private ThreeDimensionalVector addAndConvertToThreeDimensionalVector(Vector additionResult) {
        double[] tmp = additionResult.getArrValues();
        return new ThreeDimensionalVector(tmp[0], tmp[1], tmp[2]);
    }
    public void moveCamera(final ThreeDimensionalVector translation) {
        movePosition(translation);
        moveTarget(translation);
    }
    public void rotateCamera(final NDimensionalMatrix mR) {
        ThreeDimensionalVector vZ = ThreeDimensionalVector.subtraction(target, position);
        vZ = GraphicConveyor.multiplyMatrix4ByVector3(mR, vZ);
        target = ThreeDimensionalVector.addition(position, vZ);

    }
    public NDimensionalMatrix getViewMatrix() {
        return GraphicConveyor.lookAt(position, target);
    }

    public NDimensionalMatrix getProjectionMatrix() {
        return GraphicConveyor.perspective(fov, aspectRatio, nearPlane, farPlane);
    }
}