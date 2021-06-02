public class PA3 {
    public static void main(String[] args) {
        int M = Integer.parseInt(args[0]);
        int N = Integer.parseInt(args[1]);
        int QMax = Integer.parseInt(args[2]);

        new ProductionLineSimulator(M, N, QMax).start();
    }
}
