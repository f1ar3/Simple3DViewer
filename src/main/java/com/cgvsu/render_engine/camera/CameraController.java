package com.cgvsu.render_engine.camera;

import com.cgvsu.Math.Matrix.NDimensionalMatrix;
import com.cgvsu.Math.Vectors.ThreeDimensionalVector;
import com.cgvsu.Math.Vectors.TwoDimensionalVector;
import com.cgvsu.render_engine.graphicConveyor.GraphicConveyor;

public class CameraController {
    private ThreeDimensionalVector forwardV;
    private ThreeDimensionalVector backwardV;
    private ThreeDimensionalVector leftV;
    private ThreeDimensionalVector rightV;
    private ThreeDimensionalVector upV;
    private ThreeDimensionalVector downV;
    private final Camera camera;

    public CameraController(Camera camera, float translation) {
        this.camera = camera;
        forwardV = new ThreeDimensionalVector(0, 0, -translation);
        backwardV = new ThreeDimensionalVector(0, 0, translation);
        leftV = new ThreeDimensionalVector(translation, 0, 0);
        rightV = new ThreeDimensionalVector(-translation, 0, 0);
        upV = new ThreeDimensionalVector(0, translation, 0);
        downV = new ThreeDimensionalVector(0, -translation, 0);
    }

    public void handleCameraForward() {
        camera.moveCamera(forwardV);
    }

    public void handleCameraBackward() {
        camera.moveCamera(backwardV);
    }

    public void handleCameraLeft() {
        camera.moveCamera(leftV);
    }


    public void handleCameraRight() {
        camera.moveCamera(rightV);
    }


    public void handleCameraUp() {
        camera.moveCamera(upV);
    }

    public void handleCameraDown() {
        camera.moveCamera(downV);
    }

    public void rotateCamera(final TwoDimensionalVector angleOfRotate) {
        if (angleOfRotate.getA() >= 90) {
            angleOfRotate.setA(89.9F);
        } else if (angleOfRotate.getB() <= -90) {
            angleOfRotate.setB(-89.9F);
        }
        if (angleOfRotate.getA() >= 90) {
            angleOfRotate.setA(89.9F);
        } else if (angleOfRotate.getA() <= -90) {
            angleOfRotate.setA(-89.9F);
        }

        NDimensionalMatrix mR = GraphicConveyor.getRotationMatrix(new ThreeDimensionalVector(angleOfRotate.getB(), 0, 0));
        forwardV = GraphicConveyor.multiplyMatrix4ByVector3(mR, forwardV);
        backwardV = GraphicConveyor.multiplyMatrix4ByVector3(mR, backwardV);
        leftV = GraphicConveyor.multiplyMatrix4ByVector3(mR, leftV);
        rightV = GraphicConveyor.multiplyMatrix4ByVector3(mR, rightV);
        mR = GraphicConveyor.getRotationMatrix(new ThreeDimensionalVector(angleOfRotate.getB(), angleOfRotate.getA(), 0));
        camera.rotateCamera(mR);
    }

    public Camera getCamera() {
        return camera;
    }
}
