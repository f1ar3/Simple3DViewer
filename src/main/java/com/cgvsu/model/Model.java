package com.cgvsu.model;

import com.cgvsu.Math.Vectors.ThreeDimensionalVector;
import com.cgvsu.Math.Vectors.TwoDimensionalVector;

import java.util.*;

public class Model {

    private List<ThreeDimensionalVector> vertices;
    private List<TwoDimensionalVector> textureVertices;
    private List<ThreeDimensionalVector> normals;
    private List<Polygon> polygons;
    public double scaleX = 1;
    public double scaleY = 1;
    public double scaleZ = 1;
    public double translateX = 1;
    public double translateY = 1;
    public double translateZ = 1;
    public double rotateX = 0;
    public double rotateY = 0;
    public double rotateZ = 0;
    private String path;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Model(List<ThreeDimensionalVector> vertices, List<TwoDimensionalVector> textureVertices, List<ThreeDimensionalVector> normals, List<Polygon> polygons) {
        this.vertices = vertices;
        this.textureVertices = textureVertices;
        this.normals = normals;
        this.polygons = polygons;
        recalculateNormals();
    }

    public Model() {
        this.vertices = new ArrayList<>();
        this.textureVertices = new ArrayList<>();
        this.normals = new ArrayList<>();
        this.polygons = new ArrayList<>();
        recalculateNormals();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Model model = (Model) o;
        return Objects.equals(vertices, model.vertices) && Objects.equals(textureVertices, model.textureVertices) && Objects.equals(normals, model.normals) && Objects.equals(polygons, model.polygons);
    }

    public List<ThreeDimensionalVector> getVertices() {
        return vertices;
    }

    public void setVertices(List<ThreeDimensionalVector> vertices) {
        this.vertices = vertices;
    }

    public List<TwoDimensionalVector> getTextureVertices() {
        return textureVertices;
    }

    public void setTextureVertices(List<TwoDimensionalVector> textureVertices) {
        this.textureVertices = textureVertices;
    }

    public List<ThreeDimensionalVector> getNormals() {
        return normals;
    }

    public void setNormals(List<ThreeDimensionalVector> normals) {
        this.normals = normals;
    }

    public List<Polygon> getPolygons() {
        return polygons;
    }

    public void setPolygons(List<Polygon> polygons) {
        this.polygons = polygons;
    }

    public boolean isEmpty() {
        return vertices.isEmpty();
    }

    public void triangulate() {
        List<Polygon> triangulatedPolygons = new ArrayList<>();
        for (Polygon polygon : polygons) {
            triangulatedPolygons.addAll(triangulatePolygon(polygon));
        }
        polygons = triangulatedPolygons;
    }

    private List<Polygon> triangulatePolygon(Polygon polygon){
        List<Integer> vertexIndices = polygon.getVertexIndices();
        List<Integer> textureVertexIndices = polygon.getTextureVertexIndices();
        List<Polygon> triangulatedPolygons = new ArrayList<>();
        if (vertexIndices.size() > 3) {
            for (int i = 2; i < vertexIndices.size(); i++) {
                Polygon triangle = new Polygon();
                triangle.getVertexIndices().add(vertexIndices.get(0));
                triangle.getVertexIndices().add(vertexIndices.get(i - 1));
                triangle.getVertexIndices().add(vertexIndices.get(i));
                triangle.getTextureVertexIndices().add(textureVertexIndices.get(0));
                triangle.getTextureVertexIndices().add(textureVertexIndices.get(i - 1));
                triangle.getTextureVertexIndices().add(textureVertexIndices.get(i));
                triangulatedPolygons.add(triangle);
            }
        } else {
            triangulatedPolygons.add(polygon);
        }
        return triangulatedPolygons;
    }

    private void calculateNormalInPolygon(final Polygon polygon, Map<Integer, List<ThreeDimensionalVector>> allNormals){
        List<Integer> vertexIndices = polygon.getVertexIndices();

        ThreeDimensionalVector vector1 = ThreeDimensionalVector.subtraction(vertices.get(vertexIndices.get(1)), vertices.get(vertexIndices.get(0)));
        ThreeDimensionalVector vector2 = ThreeDimensionalVector.subtraction(vertices.get(vertexIndices.get(2)), vertices.get(vertexIndices.get(0)));

        ThreeDimensionalVector normal = vector1.vectorProduct(vector2).normalization();

        for (Integer vertexIndex : vertexIndices) {
            allNormals.get(vertexIndex).add(normal);
        }
    }

    public void recalculateNormals() {
        Map<Integer, List<ThreeDimensionalVector>> allNormals = new HashMap<>(vertices.size());
        for (int i = 0; i < vertices.size(); i++){
            allNormals.put(i, new ArrayList<>());
        }

        for (Polygon polygon : polygons){
            calculateNormalInPolygon(polygon, allNormals);
        }

        List<ThreeDimensionalVector> recalculatedNormals = new ArrayList<>();
        for (int i = 0; i < vertices.size(); i++) {
            ThreeDimensionalVector tmp = ThreeDimensionalVector.sum(allNormals.get(i));
            tmp = tmp.normalization();
            recalculatedNormals.add(tmp);
        }
        normals = recalculatedNormals;
    }
}
