package LLD.InterfaceSegrigationPrinciple.ProblematicCode;

public class Seller implements User {
    @Override
    public boolean canBuyProducts() {
        return false;
    }

    @Override
    public boolean canModifyProducts() {
        return false;
    }

    @Override
    public boolean canAddProducts() {
        return false;
    }

    @Override
    public boolean canApproveProducts() {
        return false;
    }

    @Override
    public void approveProducts() {
        System.out.println("Seller canApproveProducts");
    }
}
