package ci553.happyshop.customException;

public class StockEmptyException extends RuntimeException {
    public StockEmptyException(String message) {
        super(message);
    }
}
