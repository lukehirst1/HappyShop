package ci553.happyshop.client.customer;

import ci553.happyshop.client.Main;
import ci553.happyshop.utility.UIStyle;
import ci553.happyshop.utility.WinPosManager;
import ci553.happyshop.utility.WindowBounds;
import javafx.css.Stylesheet;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.FocusModel;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;

import javax.sound.sampled.Line;

/**
 * This class is responsible for displaying a new GUI window when a product's stock falls below 15.
 */
public class LowStockWarning {
    // Display the low stock warning button
    public Button btnLowStockWarn;
    public Button btnLowStockWarnNo;

    protected String clicked = "src/resources/audio/ButtonClick.wav";

    public Boolean basketRequest = false; // Has the user requested to add a low stock product to the trolley?
    public Boolean windowOpen = false;

    public CustomerModel cusModel;


    private Stage lowStockStage;

    protected Stage warnStage;

    /**
     * Constructs the new lowStockWarning window.
     * @param window
     */
    public void start(Stage window)
    {
        btnLowStockWarn = new Button("Add to Basket");
        btnLowStockWarnNo = new Button("Cancel Order");
        btnLowStockWarn.setOnAction(this::buttonClicked);
        btnLowStockWarnNo.setOnAction(this::buttonClicked);

        btnLowStockWarn.setStyle(UIStyle.buttonStyle);
        btnLowStockWarnNo.setStyle(UIStyle.buttonStyle);

        int WIDTH = UIStyle.lowStockWinWidth;

        Label warning = new Label("It appears this product is currently low on stock. Would you like to add this product to the basket?");
        warning.setStyle(UIStyle.labelStyle);
        VBox warningLabel = new VBox(10, warning);
        warningLabel.setAlignment(Pos.CENTER);

        //
        VBox warningBox = new VBox(55, warningLabel, btnLowStockWarn, btnLowStockWarnNo);

        warningBox.setAlignment(Pos.BOTTOM_CENTER);
        warningBox.setStyle(UIStyle.lowStockWarningStyle);
        warningBox.setPrefWidth(WIDTH);
        int HEIGHT = UIStyle.lowStockWinHeight;
        warningBox.setPrefHeight(HEIGHT);
        warningBox.setSpacing(10);

        btnLowStockWarn.setAlignment(Pos.CENTER_LEFT);
        btnLowStockWarnNo.setAlignment(Pos.CENTER_RIGHT);

        WinPosManager.registerWindow(window, WIDTH, HEIGHT);

        Scene warningScene = new Scene(warningBox, WIDTH, HEIGHT);
        window.setScene(warningScene);
        window.setTitle("Product with low stock warning");
        window.show();
        window.setResizable(false); // Fix the window in place
        lowStockStage=window;
        WindowBounds lowBounds = getWindowBounds();
    }

    WindowBounds getWindowBounds() {
        return new WindowBounds(lowStockStage.getX(), lowStockStage.getY(),
                lowStockStage.getWidth(), lowStockStage.getHeight());
    }

    /**
     * Handles actions for different button clicks.
     * @param warningEvent
     */
    public void buttonClicked(ActionEvent warningEvent)
    {
        Button warnButton = ((Button) warningEvent.getSource());
        String warnLabel = warnButton.getText();

        if (warnLabel.equals("Add to Basket"))
        {
            Main.mainHolder.PlaySound(clicked);
            basketRequest = true;
            cusModel.addToTrolley();
            windowOpen = true;
        }
        else if (warnLabel.equals("Cancel Order"))
        {
            Main.mainHolder.PlaySound(clicked);
            cusModel.cancel();
            warnStage = (Stage) warnButton.getScene().getWindow();
            warnStage.close();
        }
    }
}
