package domainapp.dom.batch;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.RestrictTo;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.clock.ClockService;

import domainapp.dom.recipe.Recipe;

@DomainService(
        nature = NatureOfService.VIEW_MENU_ONLY
)
@DomainServiceLayout(
        named = "Batches",
        menuOrder = "1"
)
public class BatchMenu {

    @Action(
            semantics = SemanticsOf.SAFE,
            restrictTo = RestrictTo.PROTOTYPING
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT
    )
    @MemberOrder(sequence = "1")
    public java.util.List<Batch> listAll() {
        return batchrepository.listAll();
    }

    @Action(
            semantics = SemanticsOf.SAFE
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT
    )
    @MemberOrder(sequence = "2")
    public java.util.List<Batch> findByGyleNumber(
            final String gyleNumber
    ) {
        return batchrepository.findByGyleNumberContains(gyleNumber);
    }

    @Action(
    )
    @MemberOrder(sequence = "3")
    public Batch create(
            @ParameterLayout(named = "Gyle Number") final String gyleNumber,
            @ParameterLayout(named = "Brew Date") final LocalDate brewDate,
            final Recipe recipe) {
        return batchrepository.create(gyleNumber, brewDate, recipe);
    }

    public LocalDate default1Create() {
        return clock.now();
    }

    @javax.inject.Inject
    BatchRepository batchrepository;

    @javax.inject.Inject
    ClockService clock;
}
