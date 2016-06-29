package domainapp.fixture.scenarios.demo;

import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.apache.isis.applib.fixturescripts.FixtureScript;

import org.isisaddons.module.excel.dom.ExcelFixture;
import org.isisaddons.module.excel.dom.ExcelFixtureRowHandler;

import domainapp.dom.fvessel.FermentationVessel;
import domainapp.dom.fvessel.FermentationVessels;
import domainapp.dom.fvessel.VesselType;
import lombok.Getter;
import lombok.Setter;

public class DemoFixtureRowHandler implements ExcelFixtureRowHandler {

    @Getter @Setter
    private String name;

    @Override
    public List<Object> handleRow(
            final FixtureScript.ExecutionContext executionContext,
            final ExcelFixture excelFixture,
            final Object previousRow) {

        final List<FermentationVessel> matching = repository.findByName(name);
        if(matching.isEmpty()) {
            FermentationVessel fermentationVessel = repository.create(name, VesselType.CYLINDROCONICAL);
            executionContext.addResult(excelFixture, fermentationVessel);
        }
        return Collections.emptyList();
    }

    @Inject
    FermentationVessels repository;

}
