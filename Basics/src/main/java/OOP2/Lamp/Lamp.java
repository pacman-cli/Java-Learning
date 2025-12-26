package OOP2.Lamp;

public class Lamp {
    boolean isOn;

    //default constructor
//    Lamp(){
//
//    } -->if we don't create any constructor java will automatically create a default constructor
    void TurnOn() {
        isOn = true;
        System.out.println("Lamp is on");
    }

    void TurnOff() {
        isOn = false;
        System.out.println("Lamp is off");
    }
}
