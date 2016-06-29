package domainapp.dom.batch;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.Mixin;
import org.apache.isis.applib.annotation.SemanticsOf;

// demonstrates how to factor out functionality into mixins, by way of an interface
@Mixin
public class WithBatchState_mashIn {

    private final WithBatchState withBatchState;

    public WithBatchState_mashIn(WithBatchState withBatchState) {
        this.withBatchState = withBatchState;
    }

    @Action(
            semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE
    )
    public WithBatchState mashIn() {
        withBatchState.setState(BatchState.MASHED_IN);
        return withBatchState;
    }

    public boolean hideMashIn() {
        return withBatchState.getState() != BatchState.CREATED;
    }

}
