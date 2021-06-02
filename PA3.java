public class PA3 {
    public static void main(String[] args) {
        int M= Integer.parseInt(args[0]);
        int N = Integer.parseInt(args[1]);
        int QMax = Integer.parseInt(args[2]);

        if (QMax <= 1) {
            System.out.println("QMax needs to be greather than 1");
            return;
        }

        // Create new simulator and start it
        new ProductionLineSimulator(M, N, QMax).start();
    }
}
