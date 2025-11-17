package ci553.happyshop.customException;

public class ExcessiveOrderQuantityException extends RuntimeException
{
    public ExcessiveOrderQuantityException(String message)
    {
        super(message);
    }
}
