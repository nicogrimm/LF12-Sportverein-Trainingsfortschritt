package com.github.nicogrimm.LF12_Sportverein_Trainingsfortschritt.backend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@CrossOrigin // TODO: limit origins
public class TestController {

    @GetMapping("/test")
    public TestModel testEndpoint() {
        log.info("Testing endpoint");
        return new TestModel("Hello, World!");
    }
}
