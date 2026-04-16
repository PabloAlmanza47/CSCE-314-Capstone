package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Scoreboard;

public class ControlController {

    private Scoreboard model;
    private DisplayController displayController;

    @FXML private Label mainLabel;
    @FXML private RadioButton homeRadio;
    @FXML private RadioButton awayRadio;

    public void setModel(Scoreboard model) {this.model = model;}

    public void setDisplayController(DisplayController dc) {this.displayController = dc;}

    private void showError(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void addPoints(int points) {
        try {
            if (!homeRadio.isSelected() && !awayRadio.isSelected()) {
                showError("Please select a team.");
                return;
            }

            if (homeRadio.isSelected()) {
                model.addPointsToHome(points);
            } else {
                model.addPointsToAway(points);
            }

            displayController.updateView();

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }
    
    //button functions to connect the screen to the model
    @FXML
    private void handleAdd6() {addPoints(6);}
    @FXML
    private void handleAdd3() {addPoints(3);}
    @FXML
    private void handleAdd2() {addPoints(2);}
    @FXML
    private void handleAdd1() {addPoints(1);}
}