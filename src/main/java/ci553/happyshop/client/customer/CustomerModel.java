package ci553.happyshop.client.customer;

import ci553.happyshop.catalogue.Order;
import ci553.happyshop.catalogue.Product;
import ci553.happyshop.client.Main;
import ci553.happyshop.client.warehouse.WarehouseClient;
import ci553.happyshop.client.warehouse.WarehouseModel;
import ci553.happyshop.customException.ExcessiveOrderQuantityException;
import ci553.happyshop.storageAccess.DatabaseRW;
import ci553.happyshop.orderManagement.OrderHub;
import ci553.happyshop.storageAccess.DatabaseRWFactory;
import ci553.happyshop.storageAccess.DerbyRW;
import ci553.happyshop.utility.StorageLocation;
import ci553.happyshop.utility.ProductListFormatter;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Array;
import java.sql.SQLException;
import java.util.*;

/**
 * TODO
 * You can either directly modify the CustomerModel class to implement the required tasks,
 * or create a subclass of CustomerModel and override specific methods where appropriate.
 */
public class CustomerModel {
    public CustomerView cusView;
    public DatabaseRW databaseRW; //Interface type, not specific implementation
    // Benefits: Flexibility: Easily change the database implementation.

    private Product theProduct = null; // product found from search
    private ArrayList<Product> productList = new ArrayList<>();
    private final ArrayList<Product> trolley = new ArrayList<>(); // a list of products in trolley
    protected ArrayList<Product> insufficientProducts = new ArrayList<>();

    // Four UI elements to be passed to CustomerView for display updates.
    String imageName = "imageHolder.jpg";                // Image to show in product preview (Search Page)
    private String displayLaSearchResult = "No Product was searched yet"; // Label showing search result message (Search Page)
    private String displayTaTrolley = "";                                // Text area content showing current trolley items (Trolley Page)
    private String displayTaReceipt = "";                                // Text area content showing receipt after checkout (Receipt Page)

    protected boolean stockEmpty = false;
    protected boolean exceededQuantity = false;

    // Holder for audio strings
    protected String cancelled = "src/main/resources/audio/CustomerCancel.wav";
    protected String customerAdded = "src/main/resources/audio/CustomerItemAdded.wav";
    protected String searchNull = "src/main/resources/audio/CustomerSearchNull.wav";
    protected String searchResult = "src/main/resources/audio/CustomerSearchResult.wav";
    protected String goodbye = "src/main/resources/audio/CustomerGoodbye.wav";
    protected String customerWarn = "src/main/resources/audio/CustomerStockWarning.wav";
    protected String customerEmpty = "src/main/resources/audio/CustomerStockNotAvailable.wav";

    //SELECT productID, productName, description, image, unitPrice,inStock quantity

    /**
     * Utilises SQL to search for a ProductID. If a Product is found, it is then shown in the search result.
     * However, if the product is not found, then the window displays an error.
     *
     * @throws SQLException
     */
    void search() throws SQLException {
//        String productId = cusView.tfId.getText().trim();
//        String productName = cusView.tfName.getText().trim();

        String keyword = cusView.tfUnified.getText().trim();

        // First: Is the keyword NOT empty?
        if (!keyword.isEmpty())
        {
//            theProduct = databaseRW.searchByProductId(keyword); //search database
           // trolley = databaseRW.searchProduct(productName); // Search the database using the name
            productList = databaseRW.searchProduct(keyword); // Search the database using the name or ID of the product.
            Main.mainHolder.StopSound();
            Main.mainHolder.PlaySound(searchResult);
            cusView.updateMulti(productList); // For searching flexibly

            if (theProduct != null && theProduct.getStockQuantity() > 0)
            {
                lowStockCheck(); // Check if the stock is low or not.
                double unitPrice = theProduct.getUnitPrice();
                String description = theProduct.getProductDescription();
                int stock = theProduct.getStockQuantity();
                String baseInfo = String.format("Product_Id: %s\n%s,\nPrice: £%.2f", keyword, description, unitPrice);
                String quantityInfo = stock < 100 ? String.format("\n%d units left.", stock) : "";
                displayLaSearchResult = baseInfo + quantityInfo;
                System.out.println(displayLaSearchResult);
            }
        }
        else
        {
            theProduct=null;
            displayLaSearchResult = "Please type either the ProductID or ProductName";
            System.out.println("Please type the ProductID or ProductName.");
            Main.mainHolder.StopSound();
            Main.mainHolder.PlaySound(searchNull);
            productList.clear();
        }
        updateView();
    }

    /**
     * This class checks if the currently selected stock is more than zero. If it is not, then it
     * warns the customer that the product is low on stock.
     */
    void lowStockCheck()
    {
        // Is the stock quantity less than or equal to 15, but more than zero?
        if (theProduct.getStockQuantity() <= 15 && theProduct.getStockQuantity() > 0)
        {
            // Warn the customer that the stock is currently low, and open a new window
            System.out.println("The stock that you requested is currently low.");
            Main.mainHolder.PlaySound(customerWarn);
            Main.mainHolder.StopSound();
            Main.startLowStockWarn(new Stage());
        }
        // The stock is completely gone, alert the customer
        else if (theProduct.getStockQuantity() < 0)
        {
            Main.mainHolder.PlaySound(customerEmpty);
            Main.mainHolder.StopSound();
            System.out.println("We're sorry, but the requested stock is not available.");
            stockEmpty = true;
        }
    }

    /**
     * Adds the selected product to a trolley. If it does not exist, it asks the user to search for a product that
     * exists, before it continues. The method also organises the trolley by Product ID.
     */
    void addToTrolley(){
        theProduct = cusView.obrLvProducts.getSelectionModel().getSelectedItem();
        if (theProduct != null)
        {
            // trolley.add(theProduct) — Product is appended to the end of the trolley.
            // To keep the trolley organized, add code here or call a method that:
            //TODO
            // 1. Merges items with the same product ID (combining their quantities).
            // 2. Sorts the products in the trolley by product ID.

            // This code originally caused the trolley to duplicate.
            // It also sorts out the trolley by ProductID.
            organisedTrolley();
            displayTaTrolley = ProductListFormatter.buildString(trolley); //build a String for trolley so that we can show it
            Main.mainHolder.StopSound();
            Main.mainHolder.PlaySound(customerAdded);
            System.out.println("Added to trolley");
        }
        // No product selected, throw an error exception
        else
        {
            displayLaSearchResult = "Please search for an available product before adding it to the trolley";
            System.out.println("must search and get an available product before add to trolley");
        }
        displayTaReceipt=""; // Clear receipt to switch back to trolleyPage (receipt shows only when not empty)
        updateView();
    }

    /**
     * This class organises the trolley by productID, in descending order. It checks if the productID is equal to theProduct's ID.
     * It then makes a new trolley, which is then used by Collections.sort() to organise the trolley.
     *
     */
    void organisedTrolley()
    {
        // Each product in a trolley
        for(Product p : trolley)
        {
            // is the getProductID() from p equal to getProductID() from theProduct?
            if (p.getProductId().equals(theProduct.getProductId()))
            {
                // set the ordered quantity from p, and get the orderedQuantity from theProduct.
                p.setOrderedQuantity(p.getOrderedQuantity() + theProduct.getOrderedQuantity());
                return;
            }
        }
        /*
         * This will cause the trolley price to double each time, which is
         * very incorrect. The revised code is down below.
         */
        Product pNew = new Product(theProduct.getProductId(), theProduct.getProductDescription(), theProduct.getProductImageName(), theProduct.getUnitPrice(), theProduct.getStockQuantity());
       // trolley.add(theProduct); - Commented out due to duplication
        trolley.add(pNew);
        // Sorts out the trolley numerically by Product ID.
        trolley.sort(Comparator.comparing(Product::getProductId));
    }

    /**
     * This method checks for if the requested stock of an item is not more than 50.
     * If it is, it throws a excessiveOrderQuantityException (EOQE)
     */
    void validateTrolley()
    {
        // Cycle through the trolley
            for(Product p : trolley) {
                int i = p.getOrderedQuantity() + theProduct.getOrderedQuantity();
                // Is the requested stock amount more than or equal to 50?
                if (i > 50)
                    try
                    {
                        // Throw the exception
                        exceededQuantity = true;
                        throw new ExcessiveOrderQuantityException("Sorry, it appears that item is above the allowed limit.");
                    }
                catch (ExcessiveOrderQuantityException EOQE)
                {
                    System.out.println(EOQE.getMessage());
            }
        }
    }

    /**
     * Allows the user to check out the trolley, and it generates a new
     * order which then goes to OrderHub
     * @throws IOException
     * @throws SQLException
     */
    void checkOut() throws IOException, SQLException
    {
        // First: Is the trolley NOT empty?
        if(!trolley.isEmpty()){
            // Group the products in the trolley by productId to optimize stock checking
            // Check the database for sufficient stock for all products in the trolley.
            // If any products are insufficient, the update will be rolled back.
            // If all products are sufficient, the database will be updated, and insufficientProducts will be empty.
            // Note: If the trolley is already organized (merged and sorted), grouping is unnecessary.
            // ArrayList<Product> groupedTrolley= groupProductsById(trolley);

            // The first thing to do is to check if the trolley has the right amount of products.
            validateTrolley();

            // Next: Is the quantity of the product NOT more than 50?
            if (!exceededQuantity)
            {
                // This reduces the product quantity down by how much has been purchased.
                insufficientProducts = databaseRW.purchaseStocks(trolley);

                // Is it possible to buy the products, and is the requested order quantity less than or equal to 50?
                if (insufficientProducts.isEmpty() && theProduct.getOrderedQuantity() <= 50)
                {
                    // If stock is sufficient for all products
                    //get OrderHub and tell it to make a new Order
                    OrderHub orderHub =OrderHub.getOrderHub();
                    Order theOrder = orderHub.newOrder(trolley);
                    trolley.clear();
                    displayTaTrolley ="";
                    displayTaReceipt = String.format(
                            "Order_ID: %s\nOrdered_Date_Time: %s\n%s",
                            theOrder.getOrderId(),
                            theOrder.getOrderedDateTime(),
                            ProductListFormatter.buildString(theOrder.getProductList())
                    );
                    System.out.println(displayTaReceipt);
                    Main.mainHolder.PlaySound(goodbye);
                }
            }
            else
            { // Some products have insufficient stock — build an error message to inform the customer
                StringBuilder errorMsg = new StringBuilder();
                for(Product p : insufficientProducts){
                    errorMsg.append("\u2022 "+ p.getProductId()).append(", ")
                            .append(p.getProductDescription()).append(" (Only ")
                            .append(p.getStockQuantity()).append(" available, ")
                            .append(p.getOrderedQuantity()).append(" requested)\n");
                }
                theProduct=null;

                //TODO
                // Add the following logic here:
                // 1. Remove products with insufficient stock from the trolley.
                // 2. Trigger a message window to notify the customer about the insufficient stock, rather than directly changing displayLaSearchResult.
                //You can use the provided RemoveProductNotifier class and its showRemovalMsg method for this purpose.
                //remember close the message window where appropriate (using method closeNotifierWindow() of RemoveProductNotifier class)
//                stockCheck();
                displayLaSearchResult = "Checkout failed due to insufficient stock for the following products:\n" + errorMsg.toString();
            }
        }

        // Commented out in favour of another method in search().
//        if (theProduct.getStockQuantity() < theProduct.getOrderedQuantity())
//        {
//            // Throw a error
//            System.out.println("Sorry, that stock is not available at the moment.. Please try again later.");
//        }

        else
        {
            displayTaTrolley = "Your trolley is empty";
            System.out.println("Your trolley is empty");
        }
        updateView();
        cusView.updateMulti(productList);
    }

    /**
     * Groups products by their productId to optimize database queries and updates.
     * By grouping products, we can check the stock for a given `productId` once, rather than repeatedly
     */
    protected ArrayList<Product> groupProductsById(ArrayList<Product> proList) {
        Map<String, Product> grouped = new HashMap<>();
        for (Product p : proList) {
            String id = p.getProductId();
            if (grouped.containsKey(id)) {
                Product existing = grouped.get(id);
                existing.setOrderedQuantity(existing.getOrderedQuantity() + p.getOrderedQuantity());
            } else {
                // Make a shallow copy to avoid modifying the original
                grouped.put(id,new Product(p.getProductId(),p.getProductDescription(),
                        p.getProductImageName(),p.getUnitPrice(),p.getStockQuantity()));
            }
        }
        return new ArrayList<>(grouped.values());
    }

    /**
     * This class completely clears the order, and allows the customer to start again.
     */
    void cancel(){
        trolley.clear();
        Main.mainHolder.StopSound();
        Main.mainHolder.PlaySound(cancelled);
        displayTaTrolley="";
        updateView();
    }

    /**
     * Closes the receipt.
     */
    void closeReceipt(){
        displayTaReceipt="";
    }

    /**
     * Updates the Customer's GUI window, depending on what is currently going on at the time.
     */
    void updateView() {
        if(theProduct != null)
        {
            imageName = theProduct.getProductImageName();
            String relativeImageUrl = StorageLocation.imageFolder +imageName; //relative file path, eg images/0001.jpg
            // Get the full absolute path to the image
            Path imageFullPath = Paths.get(relativeImageUrl).toAbsolutePath();
            imageName = imageFullPath.toUri().toString(); //get the image full Uri then convert to String
            System.out.println("Image absolute path: " + imageFullPath); // Debugging to ensure path is correct
        }
        else{
            imageName = "imageHolder.jpg";
        }
        cusView.update(imageName, displayLaSearchResult, displayTaTrolley,displayTaReceipt);
    }
     // extra notes:
     //Path.toUri(): Converts a Path object (a file or a directory path) to a URI object.
     //File.toURI(): Converts a File object (a file on the filesystem) to a URI object

    //for test only

    /**
     * Not used in the project - for testing purposes only.
     * @return
     */
    public ArrayList<Product> getTrolley() {
        return trolley;
    }
}
