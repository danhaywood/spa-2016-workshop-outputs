/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package domainapp.dom.fvessel;

import javax.jdo.annotations.Column;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.VersionStrategy;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.CommandReification;
import org.apache.isis.applib.annotation.DomainObject;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.Property;
import org.apache.isis.applib.annotation.Publishing;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.apache.isis.applib.services.eventbus.PropertyDomainEvent;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.services.repository.RepositoryService;
import org.apache.isis.applib.util.ObjectContracts;

import domainapp.dom.batch.Batch;
import lombok.Getter;
import lombok.Setter;

@javax.jdo.annotations.PersistenceCapable(
        identityType=IdentityType.DATASTORE,
        schema = "fvessel",
        table = "FermentationVessel"
)
@javax.jdo.annotations.DatastoreIdentity(
        strategy=javax.jdo.annotations.IdGeneratorStrategy.IDENTITY,
         column="id")
@javax.jdo.annotations.Version(
//        strategy=VersionStrategy.VERSION_NUMBER,
        strategy= VersionStrategy.DATE_TIME,
        column="version")
@javax.jdo.annotations.Queries({
        @javax.jdo.annotations.Query(
                name = "find", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.fvessel.SimpleObject "),
        @javax.jdo.annotations.Query(
                name = "findByName", language = "JDOQL",
                value = "SELECT "
                        + "FROM domainapp.dom.fvessel.SimpleObject "
                        + "WHERE name.indexOf(:name) >= 0 ")
})
@javax.jdo.annotations.Unique(name="SimpleObject_name_UNQ", members = {"name"})
@DomainObject(
        publishing = Publishing.ENABLED
)
public class FermentationVessel implements Comparable<FermentationVessel> {


    public enum State {
        CLEAN,
        QUICK_CIP,
        READY_FOR_BATCH,
        FERMENTING,
        DIRTY,
        CIP, // clean-in-place
        OUT_OF_ORDER
    }

    //region > title

    public String title() {
        return getName();
    }

    //endregion

    @Column(allowsNull = "true")
    @Property()
    @Getter @Setter
    private Batch batch;

    @Column(allowsNull = "false")
    @Property()
    @Getter @Setter
    private VesselType type;

    @Column(allowsNull = "true") // TODO
    @Property()
    @Getter @Setter
    private State state;

    //region > name (property)

    public static final int NAME_LENGTH = 40;

    public static class NameDomainEvent extends PropertyDomainEvent<FermentationVessel,String> {}
    @javax.jdo.annotations.Column(
            allowsNull="false",
            length = NAME_LENGTH
    )
    @Property(
        domainEvent = NameDomainEvent.class
    )
    @Getter @Setter
    private String name;

    public TranslatableString validateName(final String name) {
        return name != null && name.contains("!")? TranslatableString.tr("Exclamation mark is not allowed"): null;
    }

    //endregion

    //region > updateName (action)

    public static class UpdateNameDomainEvent extends ActionDomainEvent<FermentationVessel> {}
    @Action(
            command = CommandReification.ENABLED,
            publishing = Publishing.ENABLED,
            semantics = SemanticsOf.IDEMPOTENT,
            domainEvent = UpdateNameDomainEvent.class
    )
    @MemberOrder(name="name", sequence = "1") // associate with 'name' property
    public FermentationVessel updateName(@ParameterLayout(named="Name") final String name) {
        setName(name);
        return this;
    }
    public String default0UpdateName() {
        return getName();
    }
    public TranslatableString validate0UpdateName(final String name) {
        return validateName(name);
    }

    //endregion

    //region > delete (action)

    public static class DeleteDomainEvent extends ActionDomainEvent<FermentationVessel> {}
    @Action(
            domainEvent = DeleteDomainEvent.class,
            semantics = SemanticsOf.NON_IDEMPOTENT_ARE_YOU_SURE
    )
    public void delete() {
        repositoryService.remove(this);
    }

    //endregion

    //region > compareTo
    @Override
    public int compareTo(final FermentationVessel other) {
        return ObjectContracts.compare(this, other, "name");
    }

    //endregion

    //region > dependencies
    @javax.inject.Inject
    RepositoryService repositoryService;
    //endregion

}
