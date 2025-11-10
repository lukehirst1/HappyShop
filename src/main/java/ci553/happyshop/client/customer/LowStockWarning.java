package ci553.happyshop.client.customer;

import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class LowStockWarning
{
    // Display the low stock warning button
    public Button lowStockWarn;
    public Button lowStockWarnNo;

    public void start(Stage window)
    {
        lowStockWarn = new Button("Yes");
        lowStockWarnNo = new Button("No");
        lowStockWarn.setOnAction(this::buttonClicked);

        TextField warning = new TextField("It appears the stock you requested is currently low. Would you like to continue to add this to the cart?");
        warning.setEditable(false);
        VBox warningBox = new VBox(25, warning, lowStockWarn, lowStockWarnNo);

        warningBox.setAlignment(Pos.CENTER);
        warningBox.setSpacing(10);

        Scene warningScene = new Scene(warningBox, 550, 150);
        window.setScene(warningScene);
        window.setTitle("HappyShop Low Stock Warning");
        window.show();
    }
    public void buttonClicked(ActionEvent warningEvent)
    {
        Button warnButton = ((Button) warningEvent.getSource());
        String warnLabel = warnButton.getText();

        if (warnLabel.equals("Yes"))
        {
            Stage warnStage = (Stage) warnButton.getScene().getWindow();
            warnStage.close();
        }
        if (warnLabel.equals("No"))
        {
            Stage warnStage = (Stage) warnButton.getScene().getWindow();
            warnStage.close();
        }
    }
}
