package com.mysite.brew;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.mysite.brew.entity.BrewOutdated;
import com.mysite.brew.entity.BrewOutdatedPivot;
import com.mysite.brew.entity.BrewUpdate;
import com.mysite.brew.repository.BrewOutdatedPivotRepository;
import com.mysite.brew.repository.BrewOutdatedRepository;
import com.mysite.brew.repository.BrewUpdateRepository;
import com.mysite.brew.service.BrewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

@SpringBootTest
class BrewMaintenanceApplicationTests {

    @Autowired
    private BrewUpdateRepository brewUpdateRepository;
    @Autowired
    private BrewOutdatedRepository brewOutdatedRepository;
    @Autowired
    private BrewOutdatedPivotRepository brewOutdatedPivotRepository;
    @Autowired
    private BrewService brewService;

    @Test
    void contextLoads() {
    }

    @Test
    void BrewService_update_test() throws Exception {
        long before = this.brewUpdateRepository.count();
        assert 0 == before;
        this.brewService.update();
        long after = this.brewUpdateRepository.count();
        assert 1 == after;
        Optional<BrewUpdate> optional = brewUpdateRepository.findById(after);
        String content = "";
        if (optional.isPresent()) {
            content = optional.get().getContent();
        }
        assert 0 < content.length();
    }

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

    @Test
    void BrewService_outdatedPivot_test() {
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

    private void prepare_BrewService_outdatedPivot_test() {
        String content = "{\n" +
                "  \"formulae\": [\n" +
                "    {\n" +
                "      \"name\": \"git\",\n" +
                "      \"installed_versions\": [\n" +
                "        \"2.41.0_1\"\n" +
                "      ],\n" +
                "      \"current_version\": \"2.41.0_2\",\n" +
                "      \"pinned\": false,\n" +
                "      \"pinned_version\": null\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"harfbuzz\",\n" +
                "      \"installed_versions\": [\n" +
                "        \"7.3.0_1\"\n" +
                "      ],\n" +
                "      \"current_version\": \"8.0.0\",\n" +
                "      \"pinned\": false,\n" +
                "      \"pinned_version\": null\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"openldap\",\n" +
                "      \"installed_versions\": [\n" +
                "        \"2.6.4_1\"\n" +
                "      ],\n" +
                "      \"current_version\": \"2.6.5\",\n" +
                "      \"pinned\": false,\n" +
                "      \"pinned_version\": null\n" +
                "    }\n" +
                "  ],\n" +
                "  \"casks\": [\n" +
                "  ]\n" +
                "}";

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
