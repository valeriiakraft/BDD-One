package PageObjects;

import Data.DataHelper;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;

public class TransferPage {
    private SelenideElement heading = $(By.tagName("h1")).shouldHave(Condition.text("Пополнение карты"));
    private SelenideElement transferAmount = $("[data-test-id=amount] input");
    private SelenideElement fromfield = $("[data-test-id=from] input");
    private SelenideElement topUpButton = $("[data-test-id=action-transfer]");

    private SelenideElement errorMessage = $("[data-test-id=error-notification]");
    private SelenideElement cancellationButton = $("[data-test-id=action-cancel]");

    public TransferPage() {
        heading.shouldBe(Condition.visible);
    }

    public void makeTransfer(String amount, DataHelper.CardInfo cardInfo) {
        transferAmount.setValue(amount);
        fromfield.setValue(cardInfo.getCardNumber());
        topUpButton.click();
    }


    public DashBoardPage makeValidTransfer(String amount, DataHelper.CardInfo cardInfo) {
        makeTransfer(amount, cardInfo);
        return new DashBoardPage();
    }

    public void getErrorMessage(String expectedText) {
        errorMessage.shouldHave(Condition.text(expectedText), Duration.ofSeconds(15)).shouldBe(Condition.visible);
    }

    public DashBoardPage cancelingATransfer(){
        cancellationButton.click();
        return new DashBoardPage();
    }



}
