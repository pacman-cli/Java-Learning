package OOP2.ThisKey;

public class Complex {
    private final int a;
    private final int b;

    private Complex(int i, int j) {
        this.a = i;
        this.b = j;
    }

    private Complex(int i) {
        this(i, i);
    }

    private Complex() {
        this(0);
    }

    @Override
    public String toString() {
        return "Complex{" + "a=" + this.a + ", b=" + this.b + '}';
    }

    public static void main(String[] args) {
        Complex complex = new Complex(4, 5);
        System.out.println(complex);

        Complex complex1 = new Complex(2);
        System.out.println(complex1);

        Complex complex2 = new Complex();
        System.out.println(complex2);
    }
}
