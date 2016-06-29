package domainapp.dom.recipe;

import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.Programmatic;

@DomainService(
        nature = NatureOfService.DOMAIN,
        repositoryFor = Recipe.class
)
public class RecipeRepository {

    @Programmatic
    public java.util.List<Recipe> listAll() {
        return container.allInstances(Recipe.class);
    }

    @Programmatic
    public Recipe findByName(
            final String name
    ) {
        return container.uniqueMatch(
                new org.apache.isis.applib.query.QueryDefault<>(
                        Recipe.class,
                        "findByName",
                        "name", name));
    }

    @Programmatic
    public java.util.List<Recipe> findByNameContains(
            final String name
    ) {
        return container.allMatches(
                new org.apache.isis.applib.query.QueryDefault<>(
                        Recipe.class,
                        "findByNameContains",
                        "name", name));
    }

    @Programmatic
    public Recipe create(final String name) {
        final Recipe recipe = container.newTransientInstance(Recipe.class);
        recipe.setName(name);
        container.persistIfNotAlready(recipe);
        return recipe;
    }

    @Programmatic
    public Recipe findOrCreate(
            final String name
    ) {
        Recipe recipe = findByName(name);
        if (recipe == null) {
            recipe = create(name);
        }
        return recipe;
    }

    @javax.inject.Inject
    org.apache.isis.applib.DomainObjectContainer container;
}
