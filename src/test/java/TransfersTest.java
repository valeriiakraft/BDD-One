
import Data.DataHelper;
import PageObjects.DashBoardPage;
import PageObjects.LoginPage;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;

import static Data.DataHelper.CardInfo.*;
import static Data.DataHelper.TransferAmount.getInvalidTransferAmount;
import static Data.DataHelper.TransferAmount.getValidTransferAmount;
import static Data.DataHelper.getAuthInfo;
import static Data.DataHelper.getVerificationCode;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class TransfersTest {

    static LoginPage loginPage;
    static DashBoardPage dashBoardPage;


    @BeforeEach
    void setup() {
        loginPage = open("http://localhost:9999/", LoginPage.class);
        var authInfo = getAuthInfo();
        var verifyPage = loginPage.validLogin(authInfo);
        var verifyCode = getVerificationCode();
        dashBoardPage = verifyPage.validVerify(verifyCode);
    }


    @Test
    public void validTransferFromFirstCardToSecondCard() {//да
        var firstCard = getFirstCardInfo();
        var secondCard = getSecondCardInfo();
        var firstCardBalance = dashBoardPage.getBalance(firstCard);
        var secondCardBalance = dashBoardPage.getBalance(secondCard);
        var amount = getValidTransferAmount(firstCardBalance);
        var transferPage = dashBoardPage.selectCardToTransfer(secondCard);
        dashBoardPage = transferPage.makeValidTransfer(String.valueOf(amount), firstCard);
        var expectedFirstCardBalance = firstCardBalance - amount;
        var expectedSecondCardBalance = secondCardBalance + amount;
        var actualFirstCardBalance = dashBoardPage.getBalance(firstCard);
        var actualSecondCardBalance = dashBoardPage.getBalance(secondCard);
        assertEquals(expectedFirstCardBalance, actualFirstCardBalance);
        assertEquals(expectedSecondCardBalance, actualSecondCardBalance);


    }

    @Test
    public void cancelingATransfer() {//да
        var secondCard = getSecondCardInfo();
        var transferPage = dashBoardPage.selectCardToTransfer(secondCard);
        dashBoardPage = transferPage.cancelingATransfer();
    }

    @Test
    public void transferWithSameCardNumbers() {//нет
        var firstCard = getFirstCardInfo();
        var firstCardBalance = dashBoardPage.getBalance(firstCard);
        var amount = getValidTransferAmount(firstCardBalance);
        var transferPage = dashBoardPage.selectCardToTransfer(firstCard);
        transferPage.makeTransfer(String.valueOf(amount), firstCard);
        transferPage.getErrorMessage("Ошибка");

    }

    @Test
    public void transferWithInvalidCardNumber() {//да
        var invalidCard = getInvalidCardInfo();
        var secondCard = getSecondCardInfo();
        var secondCardBalance = dashBoardPage.getBalance(secondCard);
        var amount = getValidTransferAmount(secondCardBalance);
        var transferPage = dashBoardPage.selectCardToTransfer(secondCard);
        transferPage.makeValidTransfer(String.valueOf(amount), invalidCard);
        transferPage.getErrorMessage("Ошибка");
    }

    @Test
    public void transferWithEmptyCardNumber() {//да
        var emptyCard = getEmptyCardInfo();
        var secondCard = getSecondCardInfo();
        var secondCardBalance = dashBoardPage.getBalance(secondCard);
        var amount = getValidTransferAmount(secondCardBalance);
        var transferPage = dashBoardPage.selectCardToTransfer(secondCard);
        transferPage.makeTransfer(String.valueOf(amount), emptyCard);
        transferPage.getErrorMessage("Ошибка");
    }

    @Test
    public void transferNoAmount() {//нет
        var firstCard = getFirstCardInfo();
        var secondCard = getSecondCardInfo();
        var firstCardBalance = dashBoardPage.getBalance(firstCard);
        var secondCardBalance = dashBoardPage.getBalance(secondCard);
        var transferPage = dashBoardPage.selectCardToTransfer(firstCard);
        transferPage.makeTransfer("", secondCard);
        transferPage.getErrorMessage("Ошибка");
    }

    @Test
    public void transferWithZeroRub() {//нет
        var firstCard = getFirstCardInfo();
        var secondCard = getSecondCardInfo();
        String amount = "0";
        var transferPage = dashBoardPage.selectCardToTransfer(firstCard);
        transferPage.makeTransfer(amount, secondCard);
        transferPage.getErrorMessage("Ошибка");

    }

    @Test
    public void transferWithOnePennyRub() { //нет
        var firstCard = getFirstCardInfo();
        var secondCard = getSecondCardInfo();
        var firstCardBalance = dashBoardPage.getBalance(firstCard);
        var secondCardBalance = dashBoardPage.getBalance(secondCard);
        double amount = 0.1;
        var transferPage = dashBoardPage.selectCardToTransfer(secondCard);
        transferPage.makeValidTransfer(String.valueOf(amount), firstCard);
        double expectedFirstCardBalance = firstCardBalance - amount;
        double expectedSecondCardBalance = secondCardBalance + amount;
        var actualFirstCardBalance = dashBoardPage.getBalance(firstCard);
        var actualSecondCardBalance = dashBoardPage.getBalance(secondCard);
        assertEquals(expectedFirstCardBalance, actualFirstCardBalance);
        assertEquals(expectedSecondCardBalance, actualSecondCardBalance);

    }

    @Test
    public void transferWhenAmountIsMoreThanCardBalance() {//нет
        var firstCard = getFirstCardInfo();
        var secondCard = getSecondCardInfo();
        var firstCardBalance = dashBoardPage.getBalance(firstCard);
        var secondCardBalance = dashBoardPage.getBalance(secondCard);
        var amount = getInvalidTransferAmount(secondCardBalance);
        var transferPage = dashBoardPage.selectCardToTransfer(firstCard);
        transferPage.makeTransfer(String.valueOf(amount), secondCard);
        transferPage.getErrorMessage("Ошибка");

    }


}


