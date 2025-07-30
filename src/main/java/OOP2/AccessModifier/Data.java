package OOP2.AccessModifier;

public class Data {
    private String name; //name is private so that it is not directly accessible form other class

    //that's why using getter and setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
