package com.cgvsu.objwriter;

import com.cgvsu.Math.Vectors.TwoDimensionalVector;
import com.cgvsu.Math.Vectors.ThreeDimensionalVector;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;
import com.cgvsu.objreader.IncorrectFileException;
import com.cgvsu.objreader.ObjReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

class ObjWriterTest {
    @Test
    public void testWriteVertices() throws IOException {
        ArrayList<ThreeDimensionalVector> vertices = new ArrayList<>();
        vertices.add(new ThreeDimensionalVector(1.0f, 2.0f, 3.0f));

        File file = new File("testFile.obj");
        if (file.createNewFile()) {
            System.out.println("File created");
        }

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            ObjWriter.writeVertices(bufferedWriter, vertices);
        }

        String fileContent = Files.readString(Path.of("testFile.obj"));
        Assertions.assertTrue(fileContent.contains("v 1.0000 2.0000 3.0000"));
    }

    @Test
    public void testWriteTextureVertices() throws IOException {
        ArrayList<TwoDimensionalVector> textureVertices = new ArrayList<>();
        textureVertices.add(new TwoDimensionalVector(1.0f, 2.0f));

        File file = new File("testFile.obj");
        if (file.createNewFile()) {
            System.out.println("File created");
        }

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file)))  {
            ObjWriter.writeTextureVertices(bufferedWriter, textureVertices);
        }

        String fileContent = Files.readString(Path.of("testFile.obj"));
        Assertions.assertTrue(fileContent.contains("vt 1.0000 2.0000"));
    }

    @Test
    public void testWriteNormals() throws IOException {
        ArrayList<ThreeDimensionalVector> normals = new ArrayList<>();
        normals.add(new ThreeDimensionalVector(1.0f, 2.0f, 3.0f));

        File file = new File("testFile.obj");
        if (file.createNewFile()) {
            System.out.println("File created");
        }

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file)))  {
            ObjWriter.writeNormals(bufferedWriter, normals);
        }

        String fileContent = Files.readString(Path.of("testFile.obj"));
        Assertions.assertTrue(fileContent.contains("vn 1.0000 2.0000 3.0000"));
    }

    @Test
    public void testWritePolygons() throws IOException {
        ArrayList<Polygon> polygons = new ArrayList<>();
        Polygon polygon = new Polygon();
        ArrayList<Integer> vertexIndices = new ArrayList<>();
        ArrayList<Integer> textureVertexIndices = new ArrayList<>();
        ArrayList<Integer> normalIndices = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            vertexIndices.add(i);
            textureVertexIndices.add(i);
            normalIndices.add(i);
        }
        polygon.setVertexIndices(vertexIndices);
        polygon.setTextureVertexIndices(textureVertexIndices);
        polygon.setNormalIndices(normalIndices);
        polygons.add(polygon);

        File file = new File("testFile.obj");
        if (file.createNewFile()) {
            System.out.println("File created");
        }

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            ObjWriter.writePolygons(bufferedWriter, polygons);
        }

        String fileContent = Files.readString(Path.of("testFile.obj"));
        Assertions.assertTrue(fileContent.contains("f 1/1/1 2/2/2 3/3/3 "));
    }

    @Test
    public void testWritePolygonsWithoutNormals() throws IOException {
        ArrayList<Polygon> polygons = new ArrayList<>();
        Polygon polygon = new Polygon();
        ArrayList<Integer> vertexIndices = new ArrayList<>();
        ArrayList<Integer> textureVertexIndices = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            vertexIndices.add(i);
            textureVertexIndices.add(i);
        }
        polygon.setVertexIndices(vertexIndices);
        polygon.setTextureVertexIndices(textureVertexIndices);
        polygons.add(polygon);

        File file = new File("testFile.obj");
        if (file.createNewFile()) {
            System.out.println("File created");
        }

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file)))  {
            ObjWriter.writePolygons(bufferedWriter, polygons);
        }

        String fileContent = Files.readString(Path.of("testFile.obj"));
        Assertions.assertTrue(fileContent.contains("f 1/1 2/2 3/3 "));
    }

    @Test
    public void testWritePolygonsWithoutTextureVertices() throws IOException {
        ArrayList<Polygon> polygons = new ArrayList<>();
        Polygon polygon = new Polygon();
        ArrayList<Integer> vertexIndices = new ArrayList<>();
        ArrayList<Integer> normalIndices = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            vertexIndices.add(i);
            normalIndices.add(i);
        }
        polygon.setVertexIndices(vertexIndices);
        polygon.setNormalIndices(normalIndices);
        polygons.add(polygon);

        File file = new File("testFile.obj");
        if (file.createNewFile()) {
            System.out.println("File created");
        }

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file)))  {
            ObjWriter.writePolygons(bufferedWriter, polygons);
        }

        String fileContent = Files.readString(Path.of("testFile.obj"));
        Assertions.assertTrue(fileContent.contains("f 1//1 2//2 3//3 "));
    }

    @Test
    public void testWritePolygonsWithOnlyVertices() throws IOException {
        ArrayList<Polygon> polygons = new ArrayList<>();
        Polygon polygon = new Polygon();
        ArrayList<Integer> vertexIndices = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            vertexIndices.add(i);
        }
        polygon.setVertexIndices(vertexIndices);
        polygons.add(polygon);

        File file = new File("testFile.obj");
        if (file.createNewFile()) {
            System.out.println("File created");
        }

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file)))  {
            ObjWriter.writePolygons(bufferedWriter, polygons);
        }

        String fileContent = Files.readString(Path.of("testFile.obj"));
        Assertions.assertTrue(fileContent.contains("f 1 2 3 4 5 "));
    }

    @Test
    public void testWriteToNonWritableDirectory() throws IOException, IncorrectFileException {
        Path fileName = Path.of("3DModels/Faceform/WrapSkull.obj");
        String fileContent = Files.readString(fileName);
        Model model = ObjReader.read(fileContent);
        String nonWritableDirectory = "/non/writable/directory/testFile.obj";
        Assertions.assertThrows(ObjWriterException.class, () -> {
            ObjWriter.write(nonWritableDirectory, model);
        });
    }

    @Test
    public void testWriteEmptyModel() {
        Model model = new Model();
        Assertions.assertThrows(ObjWriterException.class, () -> {
            ObjWriter.write("testFile.obj", model);
        });
    }
}
