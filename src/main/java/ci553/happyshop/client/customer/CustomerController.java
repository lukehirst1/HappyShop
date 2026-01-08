package ci553.happyshop.client.customer;

import ci553.happyshop.client.Main;

import java.io.IOException;
import java.sql.SQLException;

public class CustomerController {
    public CustomerModel cusModel;
    protected String clicked = "src/main/resources/audio/ButtonClick.wav";
    protected String cancelled = "src/main/resources/audio/CustomerCancel.wav";
    protected String customerAdded = "src/main/resources/audio/CustomerItemAdded.wav";
    protected String searchResult = "src/main/resources/audio/CustomerSearchResult.wav";
    protected String goodbye = "src/main/resources/audio/CustomerGoodbye.wav";
    protected String searchNull = "src/main/resources/audio/CustomerSearchNull.wav";
    protected String customerWarn = "src/main/resources/audio/CustomerStockWarning.wav";
    /**
     * Performs a different action according to which button is pressed
     * @param action
     * @throws SQLException
     * @throws IOException
     */
    public void doAction(String action) throws SQLException, IOException {
        switch (action) {
            case "Search":
                cusModel.search();
                if (cusModel.itemFound)
                {
                    Main.mainHolder.PlaySound(searchResult);
                }
                else
                {
                    Main.mainHolder.PlaySound(searchNull);
                }
                Main.mainHolder.PlaySound(clicked);
                break;
            case "Add to Trolley":
                cusModel.addToTrolley();
                cusModel.lowStockCheck();
                if (cusModel.stockLow)
                {
                    Main.mainHolder.PlaySound(customerWarn);
                }
                else
                {
                    Main.mainHolder.PlaySound(customerAdded);
                }
                Main.mainHolder.PlaySound(clicked);
                break;
            case "Cancel":
                cusModel.cancel();
                Main.mainHolder.PlaySound(clicked);
                Main.mainHolder.PlaySound(cancelled);
                break;
            case "Check Out":
                cusModel.checkOut();
                Main.mainHolder.PlaySound(clicked);
                Main.mainHolder.PlaySound(goodbye);
                break;
            case "OK & Close":
                cusModel.closeReceipt();
                Main.mainHolder.PlaySound(clicked);
                break;
        }
    }

}
