package OOP2.AccessModifier;

public class DataClient {
    public static void main(String[] args) {
        Data d = new Data();
        //d.name="Java"; --> Not possible for private access modifier
        d.setName("Java");
        System.out.println(d.getName());
    }
}
