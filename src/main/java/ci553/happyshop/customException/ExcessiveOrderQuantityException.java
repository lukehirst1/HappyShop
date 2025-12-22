package ci553.happyshop.customException;

/**
 * Responsible for blocking the user from adding more than 50 of any product to the cart at a time.
 */
public class ExcessiveOrderQuantityException extends RuntimeException
{
    public ExcessiveOrderQuantityException(String message)
    {
        super(message);
    }
}
