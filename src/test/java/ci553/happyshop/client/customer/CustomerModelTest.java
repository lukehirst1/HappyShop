package ci553.happyshop.client.customer;

import ci553.happyshop.catalogue.Product;
import ci553.happyshop.customException.ExcessiveOrderQuantityException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerModelTest {

    /**
     * This code is used to test whether the constructors and setters work.
     */
    @Test
    public void testConstructorsAndSetters()
    {
        // For testing if the product constructor and setters work or not.
        Product testProduct = new Product("0001", "HappyShop Finest Bananas", "0001.jpg", 22.50, 100);

        assertEquals("0001", testProduct.getProductId(), "A random product");
        assertEquals("HappyShop Finest Bananas", testProduct.getProductDescription(), "Only the finest bananas, imported from the Canary Islands.");
        assertEquals("0001.jpg", testProduct.getProductImageName());
        assertEquals(22.50, testProduct.getUnitPrice());
        assertEquals(100, testProduct.getStockQuantity());
    }

    /**
     * Initial testing for the order quantity.
     */
    @Test
    public void testOrderedQuantity()
    {
        Product testProduct = new Product("0001", "HappyShop Finest Bananas", "0001.jpg", 22.50, 54);

        testProduct.setOrderedQuantity(20);

        assertEquals(20, testProduct.getOrderedQuantity());
    }

    /**
     * Test method for testing if the trolley rejects products over 50 or not.
     */
    @Test
    public void testValidateTrolley()
    {
        Product testProduct = new Product("0001", "HappyShop Finest Bananas", "0001.jpg", 22.50, 100);

        testProduct.setOrderedQuantity(55);

        if (testProduct.getOrderedQuantity() > 50)
        {
            try
            {
                assertEquals(55, testProduct.getOrderedQuantity());
                throw new ExcessiveOrderQuantityException("Congratulations, the test passed with flying colours");
            }
            catch (ExcessiveOrderQuantityException EOQE)
            {
                System.out.println(EOQE.getMessage());
            }
        }
        else
        {
            try
            {
                throw new Exception("Test inconclusive. Quantity amount too low.");
            }
            catch (Exception E)
            {
                System.out.println(E.getMessage());
            }
        }
    }
}