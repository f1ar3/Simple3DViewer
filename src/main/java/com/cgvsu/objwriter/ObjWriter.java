package com.cgvsu.objwriter;

import com.cgvsu.Math.Vectors.ThreeDimensionalVector;
import com.cgvsu.Math.Vectors.TwoDimensionalVector;
import com.cgvsu.model.Model;
import com.cgvsu.model.Polygon;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ObjWriter {

    public static void write(String fileName, Model model) {
        if (model == null || model.isEmpty()) {
            throw new ObjWriterException("Null model for writing");
        }

        File file = new File(fileName);

        try {
            if (file.createNewFile()) {
                System.out.println("File created");
            }
        } catch (IOException e) {
            throw new ObjWriterException("Error creating the file: " + fileName + ". " + e.getMessage());
        }

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName))) {
            writeVertices(bufferedWriter, model.getVertices());
            writeTextureVertices(bufferedWriter, model.getTextureVertices());
            writeNormals(bufferedWriter, model.getNormals());
            writePolygons(bufferedWriter, model.getPolygons());
        } catch (IOException e) {
            throw new ObjWriterException("Error writing to file: " + fileName + ". " + e.getMessage());
        }
    }

    protected static void writeVertices(BufferedWriter writer, List<ThreeDimensionalVector> vertices) throws IOException {

        for (ThreeDimensionalVector vector : vertices) {
            final String vx = String.format("%.4f", vector.getA()).replace(',', '.');
            final String vy = String.format("%.4f", vector.getB()).replace(',','.');
            final String vz = String.format("%.4f", vector.getC()).replace(',','.');
            writer.write("v " + vx + " " + vy + " " + vz);
            writer.newLine();
        }
    }

    protected static void writeTextureVertices(BufferedWriter writer, List<TwoDimensionalVector> textureVertices) throws IOException {

        for (TwoDimensionalVector vector : textureVertices) {
            final String vtx = String.format("%.4f", vector.getA()).replace(',', '.');
            final String vty = String.format("%.4f", vector.getB()).replace(',', '.');
            writer.write("vt " + vtx + " " + vty);
            writer.newLine();
        }
    }

    protected static void writeNormals(BufferedWriter writer, List<ThreeDimensionalVector> normals) throws IOException {

        for (ThreeDimensionalVector vector : normals) {
            final String vx = String.format("%.4f", vector.getA()).replace(',', '.');
            final String vy = String.format("%.4f", vector.getB()).replace(',','.');
            final String vz = String.format("%.4f", vector.getC()).replace(',','.');
            writer.write("vn " + vx + " " + vy + " " + vz);
            writer.newLine();
        }
    }

    protected static void writePolygons(BufferedWriter writer, List<Polygon> polygons) throws IOException {

        for (Polygon polygon : polygons){
            writer.write("f ");
            List<Integer> vertexIndices = polygon.getVertexIndices();
            List<Integer> textureVertexIndices = polygon.getTextureVertexIndices();
            List<Integer> normalIndices = polygon.getNormalIndices();

            for (int j = 0; j < polygon.getVertexIndices().size(); j++) {
                if (textureVertexIndices.isEmpty() && normalIndices.isEmpty()){
                    writer.write((vertexIndices.get(j) + 1) + " ");
                }
                if (!textureVertexIndices.isEmpty() && normalIndices.isEmpty()) {
                    writer.write((vertexIndices.get(j) + 1) + "/" + (textureVertexIndices.get(j) + 1) + " ");
                }
                if (!textureVertexIndices.isEmpty() && !normalIndices.isEmpty()) {
                    writer.write((vertexIndices.get(j) + 1) + "/" + (textureVertexIndices.get(j) + 1) + "/"
                            + (normalIndices.get(j) + 1) + " ");
                }
                if (textureVertexIndices.isEmpty() && !normalIndices.isEmpty()) {
                    writer.write((vertexIndices.get(j) + 1) + "//" + (normalIndices.get(j) + 1) + " ");
                }
            }
            writer.newLine();
        }
    }
}

//    private static String polygonToObjString(List<Integer> vertexIndices, List<Integer> textureVertexIndices, List<Integer> normalIndices) {
//        StringBuilder objString = new StringBuilder(OBJ_FACE_TOKEN + " ");
//        for (int i = 0; i < vertexIndices.size(); i++) {
//            if (!textureVertexIndices.isEmpty()) {
//                objString.append(vertexIndices.get(i) + 1).append("/").append(textureVertexIndices.get(i) + 1);
//            } else {
//                objString.append(vertexIndices.get(i) + 1);
//            }
//            if (!normalIndices.isEmpty()) {
//                if (textureVertexIndices.isEmpty()) {
//                    objString.append("/");
//                }
//                objString.append("/").append(normalIndices.get(i) + 1);
//            }
//            objString.append(" ");
//        }
//        return objString.toString();
//    }
//}
