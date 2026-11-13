import javafx.application.Application;      //per ogni app JavaFx
import javafx.stage.Stage;                  //rappresentala finestra principale
import utils.SceneManager;                  //importo una mia classe per gestire il cambio schermate

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("App Conferenze");
        SceneManager.setStage(primaryStage);
        SceneManager.switchScene("/views/autenticazione.fxml");      //mostro inizialmente il login
    }

    public static void main(String[] args) {
        launch(args);
    }
}

