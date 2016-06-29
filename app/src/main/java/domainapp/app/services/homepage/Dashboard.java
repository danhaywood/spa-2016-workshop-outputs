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
package domainapp.app.services.homepage;

import java.util.List;

import org.apache.isis.applib.annotation.ViewModel;

import domainapp.dom.batch.Batch;
import domainapp.dom.batch.BatchRepository;
import domainapp.dom.fvessel.FermentationVessel;
import domainapp.dom.fvessel.FermentationVessels;
import domainapp.dom.recipe.Recipe;
import domainapp.dom.recipe.RecipeRepository;

@ViewModel
public class Dashboard {

    //region > title
    public String title() {
        return "Dashboard";
    }
    //endregion

    public List<Batch> getBatches() {
        return batchRepository.listAll();
    }

    public List<Recipe> getRecipes() {
        return recipeRepository.listAll();
    }

    public List<FermentationVessel> getActiveVessels() {
        return fermentationVessels.listAll();
    }

    public List<FermentationVessel> getOtherVessels() {
        return fermentationVessels.listAll();
    }

    //region > injected services

    @javax.inject.Inject
    FermentationVessels fermentationVessels;
    @javax.inject.Inject
    BatchRepository batchRepository;
    @javax.inject.Inject
    RecipeRepository recipeRepository;

    //endregion
}
