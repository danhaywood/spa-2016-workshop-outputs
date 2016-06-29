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

package domainapp.fixture.scenarios;

import javax.inject.Inject;

import org.apache.isis.applib.fixturescripts.FixtureScript;
import org.apache.isis.applib.services.clock.ClockService;

import domainapp.dom.batch.BatchRepository;
import domainapp.dom.fvessel.FermentationVessels;
import domainapp.dom.fvessel.VesselType;
import domainapp.dom.recipe.Recipe;
import domainapp.dom.recipe.RecipeRepository;
import domainapp.fixture.dom.simple.BreweryTearDown;

public class RecreateBrewery extends FixtureScript {

    public RecreateBrewery() {
        withDiscoverability(Discoverability.DISCOVERABLE);
    }


    @Override
    protected void execute(final ExecutionContext ec) {

        ec.executeChild(this, new BreweryTearDown());

//        final Recipe malt = recipeRepository.create("Malt");
//        final Recipe water = recipeRepository.create("Water");
//        final Recipe hops = recipeRepository.create("Hops");
//        final Recipe yeast = recipeRepository.create("Yeast");
//

        final Recipe iterationIpa = recipeRepository.create("Iteration IPA");
        final Recipe oldSmokey = recipeRepository.create("Old Smokey");
        final Recipe maryMaloney = recipeRepository.create("Mary Maloney");

        fermentationVessels.create("VS1", VesselType.CYLINDROCONICAL);
        fermentationVessels.create("VS2", VesselType.GRUNDY);
        fermentationVessels.create("VS3", VesselType.YORKSHIRE_SQUARE);

        batchRepository.create("123-456", clockService.now(), iterationIpa);
        batchRepository.create("456-789", clockService.now(), oldSmokey);
    }

    @Inject
    ClockService clockService;

    @Inject
    BatchRepository batchRepository;

    @Inject
    RecipeRepository recipeRepository;

    @Inject
    FermentationVessels fermentationVessels;

}
