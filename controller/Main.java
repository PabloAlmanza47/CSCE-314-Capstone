package controller;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Scoreboard;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scoreboard model = new Scoreboard();

        //Control window
        FXMLLoader controlLoader = new FXMLLoader(getClass().getResource("/view/control.fxml"));
        Scene controlScene = new Scene(controlLoader.load());
        ControlController controlController = controlLoader.getController();

        //Display window
        FXMLLoader displayLoader = new FXMLLoader(getClass().getResource("/view/scoreboard.fxml"));
        Scene displayScene = new Scene(displayLoader.load());
        DisplayController displayController = displayLoader.getController();

        //connecting to the controllers and model
        controlController.setModel(model);
        displayController.setModel(model);
        controlController.setDisplayController(displayController);

        //----[Displaying both windows on the users screen]----
        Stage controlStage = new Stage();
        controlStage.setTitle("Control");
        controlStage.setScene(controlScene);
        controlStage.setX(100);
        controlStage.setY(200);

        Stage displayStage = new Stage();
        displayStage.setTitle("Display");
        displayStage.setScene(displayScene);
        displayStage.setX(800);
        displayStage.setY(200);
        
        //adding style 
        controlScene.getStylesheets().add(
            getClass().getResource("/view/style.css").toExternalForm()
        );

        displayScene.getStylesheets().add(
            getClass().getResource("/view/style.css").toExternalForm()
        );

        controlStage.show();
        displayStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}