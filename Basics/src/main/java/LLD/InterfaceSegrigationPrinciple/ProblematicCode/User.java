package LLD.InterfaceSegrigationPrinciple.ProblematicCode;

public interface User {
    //buyer
    boolean canBuyProducts();

    //seller
    boolean canModifyProducts();

    boolean canAddProducts();

    //Admin
    boolean canApproveProducts();

    void approveProducts();
}
