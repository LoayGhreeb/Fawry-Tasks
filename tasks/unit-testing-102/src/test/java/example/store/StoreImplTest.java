package example.store;

import example.account.AccountManager;
import example.account.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class StoreImplTest {
    private AccountManager accountManager;
    private Customer customer;
    private Product product;
    private Store store;

    @BeforeEach
    void setup() {
        accountManager = mock(AccountManager.class);
        customer = new Customer();
        product = new Product();
        store = new StoreImpl(accountManager);
    }

    @Test
    void buy_ShouldDecreaseProductQuantity_WhenPurchaseIsSuccessful() {
        product.setQuantity(2);
        when(accountManager.withdraw(any(), anyInt())).thenReturn("success");

        store.buy(product, customer);
        assertEquals(1, product.getQuantity());
    }

    @Test
    void buy_ShouldThrowException_WhenProductIsOutOfStock() {
        product.setQuantity(0);
        when(accountManager.withdraw(any(), anyInt())).thenReturn("success");

        assertThatThrownBy(() -> store.buy(product, customer))
                  .withFailMessage("Product out of stock")
                  .isInstanceOf(RuntimeException.class);
        assertEquals(0, product.getQuantity());
    }

    @Test
    void buy_ShouldThrowException_WhenPaymentFails() {
        product.setQuantity(2);
        when(accountManager.withdraw(any(), anyInt())).thenReturn("maximum credit exceeded");

        assertThatThrownBy(() -> store.buy(product, customer))
                .withFailMessage("Payment failure: maximum credit exceeded")
                .isInstanceOf(RuntimeException.class);
        assertEquals(2, product.getQuantity());
    }
}