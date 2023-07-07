package com.mysite;

import com.mysite.brew.model.BrewUpdate;
import com.mysite.brew.repository.BrewUpdateRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class MySoftwareMaintenanceApplicationTests {
    @Autowired
    private BrewUpdateRepository brewUpdateRepository;

    @Test
    void contextLoads() {
        assert brewUpdateRepository != null;
    }

    @Test
    void brewUpdateRepository_save_findAll_test() {
        String content = "Already up-to-date";
        BrewUpdate brewUpdate = new BrewUpdate();
        brewUpdate.setContent(content);
        brewUpdateRepository.save(brewUpdate);

        List<BrewUpdate> brewUpdateList = brewUpdateRepository.findAll();

        assert 1 == brewUpdateList.size();
        assert content == brewUpdateList.get(0).getContent();
    }
}
