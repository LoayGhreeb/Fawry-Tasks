package example.account;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AccountManagerImplTest {

    private Customer customer;
    private AccountManager accountManager;

    @BeforeEach
    public void setup() {
        customer = new Customer();
        accountManager = new AccountManagerImpl();
    }

    @Test
    void deposit_ShouldIncreaseBalance_WhenAmountIsPositive() {
        customer.setBalance(100);

        accountManager.deposit(customer, 50);

        assertEquals(150, customer.getBalance());
    }

    @Test
    void deposit_ShouldThrowException_WhenAmountIsNegative() {
        customer.setBalance(100);

        assertThatThrownBy(() -> accountManager.deposit(customer, -50))
                  .isInstanceOf(IllegalArgumentException.class);
        assertEquals(100, customer.getBalance());
    }

    @Test
    void deposit_ShouldAccumulateBalance_WhenCalledMultipleTimes() {
        customer.setBalance(100);

        accountManager.deposit(customer, 50);
        accountManager.deposit(customer, 100);

        assertEquals(250, customer.getBalance());
    }

    @Test
    void withdraw_ShouldSucceed_WhenBalanceIsSufficient() {
        customer.setBalance(100);

        String actual = accountManager.withdraw(customer, 50);
        assertEquals("success", actual);
        assertEquals(50, customer.getBalance());
    }

    @Test
    void withdraw_ShouldThrowException_WhenAmountIsNegative() {
        customer.setBalance(100);

        assertThatThrownBy(() -> accountManager.withdraw(customer, -50))
                .isInstanceOf(IllegalArgumentException.class);
        assertEquals(100, customer.getBalance());
    }

    @Test
    void withdraw_ShouldSucceed_WhenCustomerIsVip() {
        customer.setBalance(100);
        customer.setVip(true);

        String actual = accountManager.withdraw(customer, 1500);
        assertEquals("success", actual);
        assertEquals(-1400, customer.getBalance());
    }

    @Test
    void withdraw_ShouldFail_WhenBalanceIsInsufficientAndCreditIsNotAllowed() {
        customer.setBalance(100);

        String actual = accountManager.withdraw(customer, 1500);
        assertEquals("insufficient account balance", actual);
        assertEquals(100, customer.getBalance());
    }

    @Test
    void withdraw_ShouldFail_WhenBalanceIsInsufficientAndMaxCreditIsExceeded() {
        customer.setBalance(100);
        customer.setCreditAllowed(true);

        String actual = accountManager.withdraw(customer, 1500);
        assertEquals("maximum credit exceeded", actual);
        assertEquals(100, customer.getBalance());
    }

    @Test
    void withdraw_ShouldSucceed_WhenBalanceIsInsufficientAndMaxCreditIsNotExceeded() {
        customer.setBalance(100);
        customer.setCreditAllowed(true);

        String actual = accountManager.withdraw(customer, 1000);
        assertEquals("success", actual);
        assertEquals(-900, customer.getBalance());
    }

    @Test
    void withdraw_ShouldSucceed_WhenBalanceIsInsufficientAndMaxCreditIsExceededAndCustomerIsVip() {
        customer.setBalance(100);
        customer.setVip(true);
        customer.setCreditAllowed(true);

        String actual = accountManager.withdraw(customer, 1500);
        assertEquals("success", actual);
        assertEquals(-1400, customer.getBalance());
    }

    @Test
    void withdraw_ShouldSucceed_WhenBalanceIsInsufficientAndMaxCreditIsNotExceededAndCustomerIsVip() {
        customer.setBalance(100);
        customer.setVip(true);
        customer.setCreditAllowed(true);

        String actual = accountManager.withdraw(customer, 1000);
        assertEquals("success", actual);
        assertEquals(-900, customer.getBalance());
    }
}