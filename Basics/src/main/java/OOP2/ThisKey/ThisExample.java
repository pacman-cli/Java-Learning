package OOP2.ThisKey;

public class ThisExample {
    int x, y;

    ThisExample(int x, int y) {
        this.x = x;
        this.y = y;
        System.out.println(this.x + " " + this.y);
        add(this);
        System.out.println(this.x + " " + this.y);
    }

    void add(ThisExample ex) {
        ex.x += 2;
        ex.y += 2;
    }

    public static void main(String[] args) {
        ThisExample tx = new ThisExample(1, 2);
    }
}
