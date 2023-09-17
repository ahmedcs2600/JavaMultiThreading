package supermanproblem;

//Double-checked locking
public class Superman {
    private static volatile Superman superman;

    private Superman() {}

    public static Superman getInstance() {
        if(superman == null) {
            synchronized (Superman.class) {
                if(superman == null) {
                    superman = new Superman();
                }
            }
        }
        return superman;
    }
}
