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
package domainapp.integtests.tests.modules.simple;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import org.apache.isis.applib.fixturescripts.FixtureScripts;
import org.apache.isis.applib.services.wrapper.HiddenException;
import org.apache.isis.applib.services.xactn.TransactionService;

import org.isisaddons.module.fakedata.dom.FakeDataService;

import domainapp.dom.batch.Batch;
import domainapp.dom.batch.BatchRepository;
import domainapp.dom.batch.BatchState;
import domainapp.dom.batch.WithBatchState_mashIn;
import domainapp.dom.fvessel.FermentationVessel;
import domainapp.dom.fvessel.FermentationVessels;
import domainapp.fixture.scenarios.RecreateBrewery;
import domainapp.integtests.tests.DomainAppIntegTest;
import static org.assertj.core.api.Assertions.assertThat;

public class WithBatchState_mashIn_IntegTest extends DomainAppIntegTest {

    Batch batch;
    FermentationVessel fv;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() throws Exception {
        // given
        RecreateBrewery fs = new RecreateBrewery();
        fixtureScripts.runFixtureScript(fs, null);

        batch = randoms.collections().anyOf(batchRepository.listAll());
        fv = randoms.collections().anyOf(fermentationVessels.listAll());

    }

        @Test
        public void happy_case() throws Exception {

            // given
            assertThat(batch.getState()).isEqualTo(BatchState.CREATED);
            assertThat(fv.getState()).isEqualTo(FermentationVessel.State.READY_FOR_BATCH);

            // when
            wrap(mixin(WithBatchState_mashIn.class, batch)).mashIn();
            transactionService.nextTransaction();

            // then
            assertThat(batch.getState()).isEqualTo(BatchState.MASHED_IN);
        }

        @Test
        public void not_in_created_state() throws Exception {

            // given
            wrap(mixin(WithBatchState_mashIn.class, batch)).mashIn();
            transactionService.nextTransaction();

            assertThat(batch.getState()).isEqualTo(BatchState.MASHED_IN);

            // expect
            expectedException.expect(HiddenException.class);

            // when
            wrap(mixin(WithBatchState_mashIn.class, batch)).mashIn();
        }

    @Inject
    TransactionService transactionService;

    @Inject
    FixtureScripts fixtureScripts;

    @Inject
    FermentationVessels fermentationVessels;

    @Inject
    BatchRepository batchRepository;

    @Inject
    FakeDataService randoms;

}