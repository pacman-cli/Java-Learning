package LLD.LiskovSubstitutionPrinciple.problematicCode;

public class VisaCard extends CreditCard {
    @Override
    public void tapAndPay() {
        System.out.println("Tap and Pay in Visa Card");
    }

    @Override
    public void onlineTransfer() {
        System.out.println("Online Transfer in Visa Card");
    }

    @Override
    public void swipeAndPay() {
        System.out.println("Swipe and Pay in Visa Card");
    }

    @Override
    public void mandatePayments() {
        System.out.println("Mandate Payments in Visa Card");
    }

    @Override
    public void upiPayments() {

    }
}
