package com.cgvsu;

import com.cgvsu.Math.Vectors.ThreeDimensionalVector;
import com.cgvsu.Math.Vectors.TwoDimensionalVector;
import com.cgvsu.deleter.PolygonsDeleter;
import com.cgvsu.deleter.VerticesDeleter;
import com.cgvsu.exceptions.NullModelException;
import com.cgvsu.model.Model;
import com.cgvsu.objreader.IncorrectFileException;
import com.cgvsu.objreader.ObjReader;
import com.cgvsu.objreader.ObjReaderException;
import com.cgvsu.objwriter.ObjWriter;
import com.cgvsu.objwriter.ObjWriterException;
import com.cgvsu.render_engine.RenderEngine;
import com.cgvsu.render_engine.camera.Camera;
import com.cgvsu.render_engine.camera.CameraController;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.nio.charset.MalformedInputException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class GUIController {

    final private float TRANSLATION = 1F;

    @FXML
    private ListView<String> viewModels = new ListView<>();

    @FXML
    AnchorPane anchorPane;

    @FXML
    private TextField polygonsFrom, polygonsCount, verticesFrom, verticesCount, scaleX, scaleY, scaleZ, rotateX, rotateY, rotateZ, translateX, translateY, translateZ;

    @FXML
    private CheckBox freeVertices;

    @FXML
    private Canvas canvas;

    private RenderEngine renderEngine;

    private Timeline timeline;

    private Camera camera = new Camera(
            new ThreeDimensionalVector(0, 0, 100),
            new ThreeDimensionalVector(0, 0, 0),
            1.0F, 1, 0.01F, 100);

    private final TwoDimensionalVector currentMouseCoordinates = new TwoDimensionalVector(0, 0);
    private final TwoDimensionalVector centerCoordinates = new TwoDimensionalVector(0, 0);

    private boolean isRotationActive;

    private Model selectedModel = null;

    private int countId = 1;

    private ObservableList<String> items = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        anchorPane.prefWidthProperty().addListener((ov, oldValue, newValue) -> canvas.setWidth(newValue.doubleValue()));
        anchorPane.prefHeightProperty().addListener((ov, oldValue, newValue) -> canvas.setHeight(newValue.doubleValue()));

        viewModels.setItems(items);
        viewModels.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            handleModelSelection(newValue);
        });

        timeline = new Timeline();
        timeline.setCycleCount(Animation.INDEFINITE);

        renderEngine = new RenderEngine(canvas.getGraphicsContext2D(), camera, (int) canvas.getWidth(), (int) canvas.getHeight());

        renderEngine.setCameraController(new CameraController(camera, TRANSLATION));

        KeyFrame frame = new KeyFrame(Duration.millis(33), event -> {
            renderEngine.setWidth((int) canvas.getWidth());
            renderEngine.setHeight((int) canvas.getHeight());

            if (isRotationActive) {
                rotateCamera();
            }

            canvas.getGraphicsContext2D().clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            camera.setAspectRatio((float) (canvas.getWidth() / canvas.getHeight()));

            renderEngine.render();
        });

        timeline.getKeyFrames().add(frame);
        timeline.play();

    }

    @FXML
    private void handleScale() {
        try {
            selectedModel.scaleX = Double.parseDouble(scaleX.getText());
            selectedModel.scaleY = Double.parseDouble(scaleY.getText());
            selectedModel.scaleZ = Double.parseDouble(scaleZ.getText());
        } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException exception) {
            showErrorDialog("Incorrect input" + exception.getMessage());
        }
    }

    @FXML
    private void handleRotate() {
        try {
            selectedModel.rotateX = Double.parseDouble(rotateX.getText());
            selectedModel.rotateY = Double.parseDouble(rotateY.getText());
            selectedModel.rotateZ = Double.parseDouble(rotateZ.getText());
        } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException exception) {
            showErrorDialog("Incorrect input" + exception.getMessage());
        }
    }

    @FXML
    private void handleTranslate() {
        try {
            selectedModel.translateX = Double.parseDouble(translateX.getText());
            selectedModel.translateY = Double.parseDouble(translateY.getText());
            selectedModel.translateZ = Double.parseDouble(translateZ.getText());
        } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException exception) {
            showErrorDialog("Incorrect input: " + exception.getMessage());
        }
    }

    @FXML
    private void onLoadModelMenuItemClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Load Model");

        File file = fileChooser.showOpenDialog(canvas.getScene().getWindow());
        if (file == null) {
            return;
        }

        Path filePath = Path.of(file.getAbsolutePath());
        String fileName = filePath.toString();

        try {

            String fileContent = Files.readString(filePath);
            selectedModel = ObjReader.read(fileContent);
            if (renderEngine.getModels().containsKey(fileName)) {
                int extensionIndex = fileName.lastIndexOf(".");
                String baseName = fileName.substring(0, extensionIndex);
                String extension = fileName.substring(extensionIndex);
                fileName = baseName + countId + extension;
                countId++;
            }
            selectedModel.setPath(fileName);
            renderEngine.addModel(selectedModel);
            items.add(fileName);

        } catch (ObjReaderException | IOException | IncorrectFileException exception) {
            showErrorDialog(exception.getMessage());
        }
    }

    @FXML
    private void onSaveModelMenuItemClick() {

        String fileName;
        try {
            fileName = selectedModel.getPath();
        } catch (NullPointerException | NullModelException exception) {
            showErrorDialog(exception.getMessage());
            return;
        }

        try {
            ObjWriter.write(fileName, selectedModel);
            showSaveModelDialog();
        } catch (ObjWriterException exception) {
            showErrorDialog(exception.getMessage());
        }
    }

    @FXML
    private void onSaveModelAsMenuItemClick() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Model (*.obj)", "*.obj"));
        fileChooser.setTitle("Save model");

        File file = fileChooser.showSaveDialog(canvas.getScene().getWindow());
        if (file == null) {
            return;
        }

        String fileName = file.getAbsolutePath();

        try {
            ObjWriter.write(fileName, selectedModel);
        } catch (ObjWriterException exception) {
            showErrorDialog(exception.getMessage());
        }
    }

    @FXML
    private void onDeleteMenuItemClick() {
        try {
            renderEngine.getModels().remove(selectedModel.getPath());
            items.remove(selectedModel.getPath());
            selectedModel = renderEngine.getModels().values().stream().reduce((first, second) -> second).orElse(null);
        } catch (NullPointerException exception) {
            showErrorDialog(exception.getMessage());
        }
    }

    private void handleModelSelection(String modelName) {
        for (Model model : renderEngine.getModels().values()) {
            if (model.getPath().equals(modelName)) {
                selectedModel = model;
                break;
            }
        }
    }

    @FXML
    public void handleCameraForward() {
        try {
            renderEngine.getCameraController().handleCameraForward();
        } catch (Exception e) {
            showErrorDialog(e.getMessage());
        }
    }

    @FXML
    public void handleCameraBackward() {
        try {
            renderEngine.getCameraController().handleCameraBackward();
        } catch (Exception e) {
            showErrorDialog(e.getMessage());
        }
    }

    @FXML
    public void handleCameraLeft() {
        try {
            renderEngine.getCameraController().handleCameraLeft();
        } catch (Exception e) {
            showErrorDialog(e.getMessage());
        }
    }

    @FXML
    public void handleCameraRight() {
        try {
            renderEngine.getCameraController().handleCameraRight();
        } catch (Exception e) {
            showErrorDialog(e.getMessage());
        }
    }

    @FXML
    public void handleCameraUp() {
        try {
            renderEngine.getCameraController().handleCameraUp();
        } catch (Exception e) {
            showErrorDialog(e.getMessage());
        }
    }

    @FXML
    public void handleCameraDown() {
        try {
            renderEngine.getCameraController().handleCameraDown();
        } catch (Exception e) {
            showErrorDialog(e.getMessage());
        }
    }
    @FXML
    public void takeFocusCanvas() {
        canvas.requestFocus();
    }

    public void rotateCamera() {
        centerCoordinates.setA(canvas.getWidth() / 2);
        centerCoordinates.setB(canvas.getHeight() / 2);

        double diffX = currentMouseCoordinates.getA() - centerCoordinates.getA();
        double diffY = currentMouseCoordinates.getB() - centerCoordinates.getB();

        double xAngle = (diffX / canvas.getWidth()) * 1;
        double yAngle = (diffY / canvas.getHeight()) * -1;

        try {
            renderEngine.getCameraController().rotateCamera(new TwoDimensionalVector(xAngle, yAngle));
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    @FXML
    public void currentMouseCoordinates(MouseEvent mouseDragEvent) {
        currentMouseCoordinates.setA((float) mouseDragEvent.getX());
        currentMouseCoordinates.setB((float) mouseDragEvent.getY());
    }
    
    @FXML
    private void deletePolygons() {

        int from, count;
        boolean freeVert;

        try {
            from = Integer.parseInt(polygonsFrom.getText());
            count = Integer.parseInt(polygonsCount.getText());
            freeVert = freeVertices.isSelected();
        } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException exception) {
            showErrorDialog("Incorrect input: " + exception.getMessage());
            return;
        }

        int len = from + count;
        ArrayList<Integer> list = new ArrayList<>();
        try {
            for (int i = from; i < len; i++) {
                list.add(i);
            }
            selectedModel = PolygonsDeleter.deletePolygons(selectedModel, list, freeVert);
        } catch (IndexOutOfBoundsException | NullModelException exception) {
            showErrorDialog(exception.getMessage());
        }
    }

    @FXML
    private void deleteVertices() {

        int from, count;

        try {
            from = Integer.parseInt(verticesFrom.getText());
            count = Integer.parseInt(verticesCount.getText());
        } catch (NullPointerException | IllegalArgumentException | IndexOutOfBoundsException exception) {
            showErrorDialog("Incorrect input: " + exception.getMessage());
            return;
        }

        int len = from + count;
        ArrayList<Integer> list = new ArrayList<>();
        try {
            for (int i = from; i < len; i++) {
                list.add(i);
            }
            selectedModel = VerticesDeleter.removeVerticesFromModel(selectedModel, list);
        } catch (IndexOutOfBoundsException | NullModelException exception) {
            showErrorDialog(exception.getMessage());
        }
    }

    private void showErrorDialog(String e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("An error has occurred!");
        alert.setContentText(e);
        alert.showAndWait();
    }

    private void showSaveModelDialog() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Info");
        alert.setHeaderText("You saved the model");
        alert.showAndWait();
    }
}