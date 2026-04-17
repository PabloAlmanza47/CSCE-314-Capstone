package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.Scoreboard;

public class DisplayController {

  //vars for Displaying the game information
  private Scoreboard model;

  @FXML private Label homeTeamLabel;
  @FXML private Label awayTeamLabel;
  @FXML private Label homeScoreLabel;
  @FXML private Label awayScoreLabel;
  @FXML private Label lastActionLabel;

  //Displaying both windows on the users screen
  public void setModel(Scoreboard model) {
      this.model = model;
      updateView();
  }

  //Used to connect the actions from the Controler to the scoreboard display
  public void updateView() {
      if (model == null || homeTeamLabel == null) return;

      homeTeamLabel.setText( model.getHomeName().isEmpty() ? "HOME" : model.getHomeName());
      awayTeamLabel.setText(model.getAwayName().isEmpty() ? "AWAY" : model.getAwayName());
      homeScoreLabel.setText(String.valueOf(model.getHomeScore()));
      awayScoreLabel.setText(String.valueOf(model.getAwayScore()));

      String last = model.getLastActionDescription();
      lastActionLabel.setText(last == null || last.isEmpty() ? "Last: -" : last);
  }
}