import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        //Control window
        FXMLLoader controlLoader = new FXMLLoader(getClass().getResource("/view/control.fxml"));
        Scene controlScene = new Scene(controlLoader.load());

        //Display window
        FXMLLoader displayLoader = new FXMLLoader(getClass().getResource("/view/scoreboard.fxml"));
        Scene displayScene = new Scene(displayLoader.load());

        //----[Displaying both windows on the users screen]----
        Stage controlStage = new Stage();
        controlStage.setTitle("Control");
        controlStage.setScene(controlScene);
        controlStage.setX(425);
        controlStage.setY(430);

        Stage displayStage = new Stage();
        displayStage.setTitle("Display");
        displayStage.setScene(displayScene);
        displayStage.setX(425);
        displayStage.setY(0);

        controlStage.show();
        displayStage.show();

        //adding style 
        controlScene.getStylesheets().add(
            getClass().getResource("/view/style.css").toExternalForm()
        );

        displayScene.getStylesheets().add(
            getClass().getResource("/view/style.css").toExternalForm()
        );
    }

    public static void main(String[] args) {
        launch(args);
    }
}