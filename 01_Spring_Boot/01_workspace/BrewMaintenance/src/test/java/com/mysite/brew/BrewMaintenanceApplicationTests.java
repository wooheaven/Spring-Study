package com.mysite.brew;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mysite.brew.entity.BrewDeps;
import com.mysite.brew.entity.BrewOutdated;
import com.mysite.brew.entity.BrewOutdatedPivot;
import com.mysite.brew.entity.BrewUpdate;
import com.mysite.brew.repository.BrewDepsRepository;
import com.mysite.brew.repository.BrewOutdatedPivotRepository;
import com.mysite.brew.repository.BrewOutdatedRepository;
import com.mysite.brew.repository.BrewUpdateRepository;
import com.mysite.brew.service.BrewService;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.Mockito.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class BrewMaintenanceApplicationTests {

    @Autowired
    private BrewUpdateRepository brewUpdateRepository;
    @Autowired
    private BrewOutdatedRepository brewOutdatedRepository;
    @Autowired
    private BrewOutdatedPivotRepository brewOutdatedPivotRepository;
    @Autowired
    private BrewDepsRepository brewDepsRepository;
    @Autowired
    private BrewService brewService;
    @Mock
    private BrewService brewServiceMock;

    @Order(1)
    @Test
    void contextLoads() {
        assert brewService != null;
        assert brewServiceMock != null;
        assert brewUpdateRepository != null;
        assert brewOutdatedRepository != null;
        assert brewOutdatedPivotRepository != null;
        assert brewDepsRepository != null;
    }

    @Order(2)
    @Test
    void BrewService_update_test() throws Exception {
        // before
        long before = this.brewUpdateRepository.count();
        assert 0 == before;

        // do
        this.brewService.update();

        // after
        long after = this.brewUpdateRepository.count();
        assert 1 == after;

        // assert
        Optional<BrewUpdate> optional = brewUpdateRepository.findById(after);
        String content = "";
        if (optional.isPresent()) {
            content = optional.get().getContent();
        }
        assert content.equals("Already up-to-date.");
    }

    @Order(3)
    @Test
    void BrewService_outdated_test() throws Exception {
        long before = this.brewOutdatedRepository.count();
        assert 0 == before;
        this.brewService.outdated();
        long after = this.brewOutdatedRepository.count();
        assert 1 == after;
        Optional<BrewOutdated> optional = brewOutdatedRepository.findById(after);
        String formulae = "";
        if (optional.isPresent()) {
            formulae = optional.get().getProperties().get("formulae");
        }
        assert 0 < formulae.length();
    }

    @Order(4)
    @Test
    void BrewService_outdatedPivot_test() throws IOException {
        prepare_BrewService_outdatedPivot_test();

        long before = this.brewOutdatedPivotRepository.count();
        assert 0 == before;
        this.brewService.outdatedPivot();
        long after = this.brewOutdatedPivotRepository.count();
        assert 3 == after;

        List<BrewOutdatedPivot> brewOutdatedPivotList = brewOutdatedPivotRepository.findAll();
        BrewOutdatedPivot brewOutdatedPivot = brewOutdatedPivotList.get(0);
        String name = brewOutdatedPivot.getName();
        assert "git".equals(name);
    }

    @Order(5)
    @Test
    void BrewService_deps_test() throws Exception {
        long before = this.brewDepsRepository.count();
        assert 0 == before;
        this.brewService.deps();
        long after = this.brewDepsRepository.count();
        assert 0 < after;

        List<BrewDeps> brewDepsList = this.brewDepsRepository.findAll();
        BrewDeps brewDeps = brewDepsList.get(0);
        assert "git".equals(brewDeps.getRootNode());
        assert "git".equals(brewDeps.getParentNode());
        assert "gettext".equals(brewDeps.getChildNode());
    }

    @Order(6)
    @Test
    void BrewService_cleanup_test() throws Exception {
        doNothing().when(this.brewServiceMock).cleanup();
        this.brewServiceMock.cleanup();
        verify(this.brewServiceMock, times(1)).cleanup();
    }

    @Order(7)
    @Test
    void BrewService_upgrade_test() throws Exception {
        String targetLib = "targetLib";
        doNothing().when(this.brewServiceMock).upgrade(targetLib);
        this.brewServiceMock.upgrade(targetLib);
        verify(this.brewServiceMock, times(1)).upgrade(targetLib);
    }

    private void prepare_BrewService_outdatedPivot_test() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();
        File testFile = new File(classLoader.getResource("brew_outdated.json").getFile());
        String content = Files.lines(testFile.toPath()).collect(Collectors.joining(System.lineSeparator()));

        JsonObject jsonObject = (JsonObject) this.brewService.readJSON(content);
        JsonArray formulae = (JsonArray) jsonObject.get("formulae");
        Gson gson = new GsonBuilder().create();
        String formulaeString = gson.toJson(formulae);
        BrewOutdated brewOutdated = new BrewOutdated();
        brewOutdated.getProperties().put("formulae", formulaeString);

        // write outdated to table
        this.brewOutdatedRepository.save(brewOutdated);
    }
}
