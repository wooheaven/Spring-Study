package com.mysite.brew;

import com.mysite.brew.model.BrewUpdate;
import com.mysite.brew.repository.BrewUpdateRepository;
import com.mysite.brew.service.BrewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class BrewMaintenanceApplicationTests {

    @Autowired
    private BrewUpdateRepository brewUpdateRepository;
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
}
