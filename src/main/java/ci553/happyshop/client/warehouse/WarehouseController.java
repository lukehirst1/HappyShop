package ci553.happyshop.client.warehouse;

import java.io.IOException;
import java.sql.SQLException;

/**
 * Handles the different actions that corresponds to the user's response in the Warehouse Client.
 */
public class WarehouseController {
    public WarehouseModel model;

    void process(String action) throws SQLException, IOException {
        switch (action) {
            case "🔍":
                model.doSearch();
                break;
            case "Edit":
                model.doEdit();
                break;
            case "Delete":
                model.doDelete();
                break;
            case "➕":
                model.doChangeStockBy("add");
                break;
            case "➖":
                model.doChangeStockBy("sub");
                break;
            case "Submit":
                model.doSummit();
                break;
            case "Cancel":  // clear the editChild
                model.doCancel();
                break;
        }
    }
}
