package ci553.happyshop.client.emergency;

import ci553.happyshop.client.Main;
import ci553.happyshop.utility.UIStyle;
import ci553.happyshop.utility.WinPosManager;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import static javafx.application.Platform.exit;

/**
 * The class EmergencyExit used to immediately shut down the entire application.
 * It is a singleton with static access, instantiation is restricted.
 */
public class EmergencyExit {
    protected String emergency = "src/main/resources/audio/EmergencyShutdown.wav";
    final int WIDTH = UIStyle.EmergencyExitWinWidth;
    final int HEIGHT = UIStyle.EmergencyExitWinHeight;
    private static EmergencyExit emergencyExit;

    //used by Main class to get the single instance
    public static EmergencyExit getEmergencyExit() {
        if (emergencyExit == null)
            emergencyExit = new EmergencyExit();
        return emergencyExit;
    }

    //Private constructor creates a shutdown window.
    //The window displays a single button with a shutdown image,positioned via `WinPosManager`,
    private EmergencyExit() {
        ImageView ivExit = new ImageView("ShutDown.jpg");
        ivExit.setFitWidth(WIDTH-100);
        ivExit.setFitHeight(WIDTH-100);
        ivExit.setPreserveRatio(true);

        Button btnExit = new Button();
        btnExit.setGraphic(ivExit);
        btnExit.setOnAction(event -> {
            Main.mainHolder.PlaySound(emergency);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(1900);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    exit();
                }
            });
        });

        BorderPane borderPane = new BorderPane();
        borderPane.setCenter(btnExit);

        borderPane.setStyle(UIStyle.rootStyle);
        Scene scene = new Scene(borderPane, WIDTH, HEIGHT);
        Stage window = new Stage();
        window.setScene(scene);
        window.setTitle("🛒 EXIT");
        WinPosManager.registerWindow(window,WIDTH,HEIGHT); //calculate position x and y for this window
        window.show();
    }

}
