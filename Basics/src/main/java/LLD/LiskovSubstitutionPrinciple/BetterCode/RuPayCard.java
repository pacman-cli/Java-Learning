package LLD.LiskovSubstitutionPrinciple.BetterCode;

public class RuPayCard extends CreditCard implements UpiCompatibleCreditCard {
    @Override
    public void tapAndPay() {
        System.out.println("Tap and Pay in RuPay Card");
    }

    @Override
    public void onlineTransfer() {
        System.out.println("Online Transfer in RuPay Card");
    }

    @Override
    public void swipeAndPay() {
        System.out.println("Swipe and Pay in RuPay Card");
    }

    @Override
    public void mandatePayments() {
        System.out.println("Mandate Payments in RuPay Card");
    }


    @Override
    public void upiPayments() {
        System.out.println("Upi Payments in RuPay Card");
    }
}

