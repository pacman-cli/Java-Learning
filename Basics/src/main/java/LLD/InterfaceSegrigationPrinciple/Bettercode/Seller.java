package LLD.InterfaceSegrigationPrinciple.Bettercode;

public class Seller implements ICanModify, ICanBuy, ICanSell {
    @Override
    public void canBuy() {
        System.out.println("Seller can buy");
    }

    @Override
    public void canModify() {
        System.out.println("Seller can Modify");
    }

    @Override
    public void canSell() {
        System.out.println("Seller can Sell");
    }
}
