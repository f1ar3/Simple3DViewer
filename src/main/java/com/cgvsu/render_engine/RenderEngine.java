package com.cgvsu.render_engine;

import com.cgvsu.Math.AffineTransformation.AffineTransformation;
import com.cgvsu.Math.Matrix.NDimensionalMatrix;
import com.cgvsu.Math.Point.Point2f;
import com.cgvsu.Math.Vectors.ThreeDimensionalVector;
import com.cgvsu.model.Model;
import com.cgvsu.render_engine.camera.Camera;
import com.cgvsu.render_engine.camera.CameraController;
import javafx.scene.canvas.GraphicsContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.cgvsu.render_engine.graphicConveyor.GraphicConveyor.*;

public class RenderEngine {
    private final GraphicsContext graphicsContext;
    private final Camera camera;
    private final Map<String, Model> models = new HashMap<>();

    private CameraController cameraController;
    private int width;
    private int height;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public CameraController getCameraController() {
        return cameraController;
    }

    public void setCameraController(CameraController cameraController) {
        this.cameraController = cameraController;
    }

    public void addModel(Model model) {
        models.put(model.getPath(), model);
    }

    public Map<String, Model> getModels() {
        return models;
    }

    public RenderEngine(GraphicsContext graphicsContext, Camera camera, int width, int height) {
        this.graphicsContext = graphicsContext;
        this.camera = camera;
        this.width = width;
        this.height = height;
    }

    public void render() {
        NDimensionalMatrix modelMatrix = getModelMatrix(new ThreeDimensionalVector(0, 0, 0), new ThreeDimensionalVector(0, 0, 0), new ThreeDimensionalVector(1, 1, 1));
        NDimensionalMatrix viewMatrix = camera.getViewMatrix();
        NDimensionalMatrix projectionMatrix = camera.getProjectionMatrix();

        NDimensionalMatrix modelViewProjectionMatrix = modelMatrix;
        modelViewProjectionMatrix = (NDimensionalMatrix) modelViewProjectionMatrix.multiplyMatrix(viewMatrix);
        modelViewProjectionMatrix = (NDimensionalMatrix) modelViewProjectionMatrix.multiplyMatrix(projectionMatrix);

        for (Model mesh : models.values()) {
            drawPolygons(mesh, modelViewProjectionMatrix);
        }
    }

    private void drawPolygons(Model mesh, NDimensionalMatrix modelViewProjectionMatrix) {
        NDimensionalMatrix affineMatrix = (NDimensionalMatrix) new AffineTransformation().scale(mesh.scaleX, mesh.scaleY, mesh.scaleZ)
                .multiplyMatrix(new AffineTransformation().rotate((float) (mesh.rotateX), (float) (mesh.rotateY), (float) (mesh.rotateZ)))
                .multiplyMatrix(new AffineTransformation().translate(mesh.translateX, mesh.translateY, mesh.translateZ));
        final int nPolygons = mesh.getPolygons().size();
        for (int polygonInd = 0; polygonInd < nPolygons; ++polygonInd) {
            final int nVerticesInPolygon = mesh.getPolygons().get(polygonInd).getVertexIndices().size();

            ArrayList<Point2f> resultPoints = new ArrayList<>();
            for (int vertexInPolygonInd = 0; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {


                ThreeDimensionalVector vertex = mesh.getVertices().get(mesh.getPolygons().get(polygonInd).getVertexIndices().get(vertexInPolygonInd));
                Point2f resultPoint = vertexToPoint(multiplyMatrix4ByVector3(modelViewProjectionMatrix, multiplyMatrix4ByVector3(affineMatrix, vertex)), width, height);

                resultPoints.add(resultPoint);
            }

            for (int vertexInPolygonInd = 1; vertexInPolygonInd < nVerticesInPolygon; ++vertexInPolygonInd) {
                graphicsContext.strokeLine(
                        resultPoints.get(vertexInPolygonInd - 1).getX(),
                        resultPoints.get(vertexInPolygonInd - 1).getY(),
                        resultPoints.get(vertexInPolygonInd).getX(),
                        resultPoints.get(vertexInPolygonInd).getY());
            }

            if (nVerticesInPolygon > 0)
                graphicsContext.strokeLine(
                        resultPoints.get(nVerticesInPolygon - 1).getX(),
                        resultPoints.get(nVerticesInPolygon - 1).getY(),
                        resultPoints.get(0).getX(),
                        resultPoints.get(0).getY());
        }
    }
}