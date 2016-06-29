package domainapp.dom.batch;

public interface WithBatchState {

    BatchState getState();
    void setState(BatchState batchState);
}
