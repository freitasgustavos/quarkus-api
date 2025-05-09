package org.acme.seed;

import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import io.quarkus.runtime.StartupEvent;

public class SeedRunner {

    @Inject
    DataSeeder dataSeeder;

    void onStart(@Observes StartupEvent ev) {
        dataSeeder.seed();
    }
}