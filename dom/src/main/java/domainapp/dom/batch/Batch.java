package domainapp.dom.batch;

import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.jdo.annotations.Column;
import javax.jdo.annotations.DatastoreIdentity;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.Queries;
import javax.jdo.annotations.Query;
import javax.jdo.annotations.Unique;
import javax.jdo.annotations.Version;
import javax.jdo.annotations.VersionStrategy;

import com.google.common.collect.Lists;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.Collection;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.DomainObjectLayout;
import org.apache.isis.applib.annotation.Editing;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.apache.isis.applib.util.ObjectContracts;

import domainapp.dom.fvessel.FermentationVessel;
import domainapp.dom.fvessel.FermentationVessels;
import domainapp.dom.recipe.Recipe;
import lombok.Getter;
import lombok.Setter;

@PersistenceCapable(
        identityType = IdentityType.DATASTORE,
        schema = "batch",
        table = "Batch"
)
@DatastoreIdentity(
        strategy = IdGeneratorStrategy.IDENTITY,
        column = "id")
@Version(
        strategy = VersionStrategy.VERSION_NUMBER,
        column = "version")
@Queries({
        @Query(
                name = "find", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.batch.Batch "),
        @Query(
                name = "findByGyleNumberContains", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.batch.Batch "
                        + "WHERE gyleNumber.indexOf(:gyleNumber) >= 0 "),
        @Query(
                name = "findByGyleNumber", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.batch.Batch "
                        + "WHERE gyleNumber == :gyleNumber ")
})
@Unique(name = "Batch_gyleNumber_UNQ", members = { "gyleNumber" })
@DomainObject(
        editing = Editing.DISABLED
)
@DomainObjectLayout(
        bookmarking = BookmarkPolicy.AS_ROOT
)
public class Batch implements Comparable<Batch>, WithBatchState {

    public String title() {
        return getGyleNumber() + " (" + getBrewDate() + ")";
    }

    @Column(allowsNull = "false")
    @Property()
    @Getter @Setter
    private BatchState state;


    // converted to WithBatchState_mashIn

//    @Action(
//            semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE
//    )
//    public Batch mashIn() {
//        setState(BatchState.MASHED_IN);
//        return this;
//    }


    public static class TransferDomainEvent extends ActionDomainEvent<Batch> {}

    @Action(
            domainEvent = TransferDomainEvent.class,
            semantics = SemanticsOf.NON_IDEMPOTENT
    )
    public Batch transfer(final FermentationVessel vessel) {
        vessel.setState(FermentationVessel.State.FERMENTING);
        // bidirectional relationship, so no need/don't also add to getVessels
        vessel.setBatch(this);

        setState(BatchState.FERMENTING);
        return this;
    }
    public List<FermentationVessel> choices0Transfer() {
        final List<FermentationVessel> available =
                this.fermentationVessels.listAll().stream()
                .filter((x) -> x.getState() == FermentationVessel.State.READY_FOR_BATCH)
                        .collect(Collectors.toList());
        return Lists.newArrayList(available);
    }


    @Column(allowsNull = "false")
    @Property()
    @Getter @Setter
    private String gyleNumber;

    @Column(allowsNull = "false")
    @Property()
    @Getter @Setter
    private LocalDate brewDate;

    @Column(allowsNull = "false")
    @Property()
    @Getter @Setter
    private Recipe recipe;

    @Persistent(mappedBy = "batch", dependentElement = "false")
    @Collection()
    @Getter @Setter
    private SortedSet<FermentationVessel> vessels = new TreeSet<FermentationVessel>();

    //region > compareTo, toString
    @Override
    public int compareTo(final Batch other) {
        return ObjectContracts.compare(this, other, "gyleNumber");
    }

    @Override
    public String toString() {
        return ObjectContracts.toString(this, "gyleNumber");
    }
    //endregion


    @Inject
    FermentationVessels fermentationVessels;

}
