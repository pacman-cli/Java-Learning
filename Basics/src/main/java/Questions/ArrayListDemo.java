package Questions;

import java.util.ArrayList;

public class ArrayListDemo {
    public static void main(String[] args) {
        ArrayList<String> lst = new ArrayList<>();
        lst.add("Java");
        lst.add("Python");
        lst.add("C");
        System.out.println(lst);
        //access
        String first = lst.getFirst();
        String second = lst.get(1);
        String last = lst.getLast();
        System.out.println(first + " " + second + " " + last);

        //change
        lst.set(2, "Java");
        System.out.println(lst);

        //remove
        lst.remove("Java"); //lst.remove(2);--> also can remove by index
        System.out.println(lst);
    }
}
