package domainapp.dom.batch;

import org.joda.time.LocalDate;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;

import domainapp.dom.recipe.Recipe;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = Batch.class
)
public class BatchRepository {

    @Programmatic
    public java.util.List<Batch> listAll() {
        return container.allInstances(Batch.class);
    }

    @Programmatic
    public Batch findByGyleNumber(
            final String gyleNumber
    ) {
        return container.uniqueMatch(
                new org.apache.isis.applib.query.QueryDefault<>(
                        Batch.class,
                        "findByGyleNumber",
                        "gyleNumber", gyleNumber));
    }

    @Programmatic
    public java.util.List<Batch> findByGyleNumberContains(
            final String gyleNumber
    ) {
        return container.allMatches(
                new org.apache.isis.applib.query.QueryDefault<>(
                        Batch.class,
                        "findByGyleNumberContains",
                        "gyleNumber", gyleNumber));
    }

    @Programmatic
    public Batch create(final String gyleNumber, final LocalDate brewDate, final Recipe recipe) {
        final Batch batch = container.newTransientInstance(Batch.class);
        batch.setGyleNumber(gyleNumber);
        batch.setBrewDate(brewDate);
        batch.setRecipe(recipe);
        batch.setState(BatchState.CREATED);
        container.persistIfNotAlready(batch);
        return batch;
    }

    @javax.inject.Inject
    org.apache.isis.applib.DomainObjectContainer container;
}
