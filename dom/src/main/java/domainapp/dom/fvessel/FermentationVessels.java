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

import java.util.List;

import org.apache.isis.applib.annotation.Action;
import org.apache.isis.applib.annotation.ActionLayout;
import org.apache.isis.applib.annotation.BookmarkPolicy;
import org.apache.isis.applib.annotation.DomainService;
import org.apache.isis.applib.annotation.DomainServiceLayout;
import org.apache.isis.applib.annotation.MemberOrder;
import org.apache.isis.applib.annotation.NatureOfService;
import org.apache.isis.applib.annotation.ParameterLayout;
import org.apache.isis.applib.annotation.SemanticsOf;
import org.apache.isis.applib.query.QueryDefault;
import org.apache.isis.applib.services.eventbus.ActionDomainEvent;
import org.apache.isis.applib.services.i18n.TranslatableString;
import org.apache.isis.applib.services.repository.RepositoryService;

@DomainService(
        nature = NatureOfService.VIEW,
        repositoryFor = FermentationVessel.class
)
@DomainServiceLayout(
        menuOrder = "10"
)
public class FermentationVessels {

    //region > title
    public TranslatableString title() {
        return TranslatableString.tr("Simple Objects");
    }
    //endregion

    //region > listAll (action)
    public static class ListAllEvent extends ActionDomainEvent<FermentationVessels> {}
    @Action(
            semantics = SemanticsOf.SAFE,
            domainEvent = ListAllEvent.class
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT
    )
    @MemberOrder(sequence = "1")
    public List<FermentationVessel> listAll() {
        return repositoryService.allInstances(FermentationVessel.class);
    }
    //endregion

    //region > findByName (action)
    public static class FindByNameEvent extends ActionDomainEvent<FermentationVessels> {}
    @Action(
            semantics = SemanticsOf.SAFE,
            domainEvent = FindByNameEvent.class
    )
    @ActionLayout(
            bookmarking = BookmarkPolicy.AS_ROOT
    )
    @MemberOrder(sequence = "2")
    public List<FermentationVessel> findByName(
            @ParameterLayout(named="Name")
            final String name
    ) {
        return repositoryService.allMatches(
                new QueryDefault<>(
                        FermentationVessel.class,
                        "findByName",
                        "name", name));
    }
    //endregion

    //region > create (action)
    public static class CreateDomainEvent extends ActionDomainEvent<FermentationVessels> {}
    @Action(
            domainEvent = CreateDomainEvent.class
    )
    @MemberOrder(sequence = "3")
    public FermentationVessel create(
            final String name,
            final VesselType type) {
        final FermentationVessel obj = repositoryService.instantiate(FermentationVessel.class);
        obj.setName(name);
        obj.setType(type);
        obj.setState(FermentationVessel.State.READY_FOR_BATCH);
        repositoryService.persist(obj);
        return obj;
    }

    //endregion

    //region > injected services

    @javax.inject.Inject
    RepositoryService repositoryService;

    //endregion
}
