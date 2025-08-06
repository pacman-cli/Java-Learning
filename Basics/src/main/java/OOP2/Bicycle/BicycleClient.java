package OOP2.Bicycle;

public class BicycleClient {
    public static void main(String[] args) {
        //creating object
        Bicycle sportsBicycle = new Bicycle();
        sportsBicycle.braking();
        System.out.println(sportsBicycle.gear);

        Bicycle touringBicycle = new Bicycle();
        touringBicycle.braking();
        System.out.println(touringBicycle.gear);
    }
}
