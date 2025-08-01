package LLD.LiskovSubstitutionPrinciple.BetterCode;

public class VisaCard extends CreditCard implements InternationalCompatibleCard {
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
    public void internationalPayments() {
        System.out.println("International Payments in Visa Card");
    }
}
