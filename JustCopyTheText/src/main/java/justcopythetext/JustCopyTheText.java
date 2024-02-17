package justcopythetext;

import java.io.IOException;
import java.util.prefs.Preferences;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * La classe {@code JustCopyTheText} rappresenta l'applicazione.
 */
public class JustCopyTheText extends Application {

    /**
     * Icona dell'applicazione.
     */
    public static final String APP_ICON = "images/icon.png";

    private Preferences preferences;

    /**
     * Metodo {@code start} dell'applicazione JavaFX.
     *
     * @param stage Stage primario.
     * @throws IOException Sollevata in caso di errori durante l'accesso alle
     * risorse.
     */
    @Override
    public void start(Stage stage) throws IOException {
        preferences = Preferences.userNodeForPackage(JustCopyTheText.class);
        stage.setOnCloseRequest((event) -> {
            preferences.putDouble("APP_POSITION_X", stage.getX());
            preferences.putDouble("APP_POSITION_Y", stage.getY());
        });
        stage.setScene(new Scene(FXMLLoader.load(JustCopyTheText.class.getResource("views/JustCopyTheText.fxml"))));
        stage.getIcons().add(new Image(JustCopyTheText.class.getResourceAsStream(APP_ICON)));
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);
        if (preferences.getDouble("APP_POSITION_X", -1) == -1
                || preferences.getDouble("APP_POSITION_Y", -1) == -1) {
            stage.centerOnScreen();
        } else {
            stage.setX(preferences.getDouble("APP_POSITION_X", 0));
            stage.setY(preferences.getDouble("APP_POSITION_Y", 0));
        }
        stage.show();
    }

    /**
     * Metodo {@code main} dell'applicazione Java.
     *
     * @param args Argomenti da riga di comando.
     */
    public static void main(String[] args) {
        launch(args);
    }

}
