package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MainController {

    @FXML
    private Label mainLabel;

    @FXML
    private void handleTeam1() {
        mainLabel.setText("Team 1 selected");
    }

    @FXML
    private void handleTeam2() {
        mainLabel.setText("Team 2 selected");
    }

    @FXML
    private void handleTeam3() {
        mainLabel.setText("Team 3 selected");
    }
}