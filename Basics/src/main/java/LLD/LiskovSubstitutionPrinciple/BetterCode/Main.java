package LLD.LiskovSubstitutionPrinciple.BetterCode;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        List<InternationalCompatibleCard> intCompCards = new ArrayList<>();
        for (InternationalCompatibleCard intCompCard : intCompCards) {
            intCompCard.internationalPayments();
        }
        List<UpiCompatibleCreditCard> upiCompCards = new ArrayList<>();
        for (UpiCompatibleCreditCard upiCompCard : upiCompCards) {
            upiCompCard.upiPayments();
        }
    }
}
