package domainapp.dom;

import com.google.common.eventbus.Subscribe;

import org.apache.isis.applib.annotation.*;
import org.apache.isis.applib.services.eventbus.AbstractDomainEvent;

import domainapp.dom.batch.Batch;
import domainapp.dom.batch.BatchState;

@DomainService(
        nature = NatureOfService.DOMAIN
)
@DomainServiceLayout(
        menuOrder = "1"
)
public class HideTransferSubscriber extends org.apache.isis.applib.AbstractSubscriber {


    @Subscribe
    public void on(Batch.TransferDomainEvent ev) {
        final AbstractDomainEvent.Phase eventPhase = ev.getEventPhase();
        switch (eventPhase) {
            case HIDE:
                if(ev.getSource().getState() != BatchState.MASHED_IN) {
                    ev.hide();
                }
            break;
            case DISABLE:
                break;
            case VALIDATE:
                break;
            case EXECUTING:
                break;
            case EXECUTED:
                break;
        }
    }
}
