package ci553.happyshop.client.customer;

import ci553.happyshop.client.Main;
import ci553.happyshop.utility.UIStyle;
import ci553.happyshop.utility.WinPosManager;
import javafx.css.Stylesheet;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.FocusModel;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

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

    private final int WIDTH = UIStyle.lowStockWinWidth;
    private final int HEIGHT = UIStyle.lowStockWinHeight;

    public CustomerModel cusModel;

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

        Label warning = new Label("The stock you have requested to add to your trolley is low. Would you like to add it anyway?");
        warning.setStyle(UIStyle.labelStyle);
        HBox warningLabel = new HBox(10, warning);
        warningLabel.setAlignment(Pos.TOP_LEFT);

        //
        VBox warningBox = new VBox(55, warningLabel, btnLowStockWarn, btnLowStockWarnNo);

        warningBox.setAlignment(Pos.CENTER);
        warningBox.setStyle(UIStyle.lowStockWarningStyle);
        warningBox.setPrefWidth(WIDTH);
        warningBox.setPrefHeight(HEIGHT);
        warningBox.setSpacing(10);

        btnLowStockWarn.setAlignment(Pos.CENTER_LEFT);
        btnLowStockWarnNo.setAlignment(Pos.CENTER_RIGHT);

        WinPosManager.registerWindow(window, WIDTH, HEIGHT);

        Scene warningScene = new Scene(warningBox, WIDTH, HEIGHT);
        window.setScene(warningScene);
        window.setTitle("HappyShop Low Stock Warning");
        window.show();
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
            Stage warnStage = (Stage) warnButton.getScene().getWindow();
            warnStage.close();
        }
        else if (warnLabel.equals("Cancel Order"))
        {
            Main.mainHolder.PlaySound(clicked);
            cusModel.cancel();
            Stage warnStage = (Stage) warnButton.getScene().getWindow();
            warnStage.close();
        }
    }
}
