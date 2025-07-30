package OOP2.ConstructorExmple;

import OOP2.Lamp.Lamp;

public class Main {
    String name;

    Main() {
        System.out.println("Constructor called");
        name = "lamp";
    }

    Main(String lang) {
        this.name = lang;
        System.out.println(this.name + " is a programming language.");
    }

    public static void main(String[] args) {
        Main obj = new Main(); //-->In new Main() here constructor has been called
        System.out.println(obj.name);

        Main obj1 = new Main("Java");
    }
}
