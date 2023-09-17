package supermanproblem;

//Holder or Bill Pugh's Pattern
public class Superman2 {
    private Superman2() {}

    //This will not be loaded before Superman2 class. It will be loaded when user call getInstance method
    private static class Holder {
        private static final Superman2 superman = new Superman2();
    }

    public static Superman2 getInstance() {
        return Holder.superman;
    }
}
