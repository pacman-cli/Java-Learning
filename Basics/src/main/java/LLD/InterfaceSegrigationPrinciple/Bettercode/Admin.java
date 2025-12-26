package LLD.InterfaceSegrigationPrinciple.Bettercode;

public class Admin implements ICanApprove {
    @Override
    public boolean canApprove() {
        return true;
    }

    @Override
    public void isApproved() {
        if (canApprove()) {
            System.out.println("Approved.");
        }
        System.out.println("Not Approved.");
    }
}
