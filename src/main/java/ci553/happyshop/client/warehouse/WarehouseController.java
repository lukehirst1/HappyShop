package ci553.happyshop.client.warehouse;

import ci553.happyshop.client.Main;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Handles the different actions that corresponds to the user's response in the Warehouse Client.
 */
public class WarehouseController {
    public WarehouseModel model;
    protected String clicked = "src/main/resources/audio/ButtonClick.wav";

    void process(String action) throws SQLException, IOException {
        switch (action) {
            case "🔍":
                model.doSearch();
                Main.mainHolder.PlaySound(clicked);
                break;
            case "Edit":
                model.doEdit();
                Main.mainHolder.PlaySound(clicked);
                break;
            case "Delete":
                model.doDelete();
                Main.mainHolder.PlaySound(clicked);
                break;
            case "➕":
                model.doChangeStockBy("add");
                Main.mainHolder.PlaySound(clicked);
                break;
            case "➖":
                model.doChangeStockBy("sub");
                Main.mainHolder.PlaySound(clicked);
                break;
            case "Submit":
                model.doSummit();
                Main.mainHolder.PlaySound(clicked);
                break;
            case "Cancel":  // clear the editChild
                model.doCancel();
                Main.mainHolder.PlaySound(clicked);
                break;
        }
    }
}
