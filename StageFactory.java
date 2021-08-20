// StageFactory.java
// Author: Zachariah Ingle C3349554
// Created: 30/5/2021
// A factory to create stages

public class StageFactory implements StageFactoryInterface {
    @Override
    public Stage createStage(String type, String name, int M, int N) {
        switch(type) {
            case "Producer":
                return new ProducerStage(name, M, N);

            case "Worker":
                return new WorkerStage(name, M, N);

            case "Consumer":
                return new ConsumerStage(name, M, N);
            
            default:
                return null;
        }
    }
}
