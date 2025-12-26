package OOP2.Default;

public class C implements A, B {

    @Override
    public void Message() {
        B.super.Message();
        A.super.Message();
    }
}
