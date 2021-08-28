package com.chanctn.jhipster_sample_application;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

class ArchTest {

    @Test
    void servicesAndRepositoriesShouldNotDependOnWebLayer() {
        JavaClasses importedClasses = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("com.chanctn.jhipster_sample_application");

        noClasses()
            .that()
            .resideInAnyPackage("com.chanctn.jhipster_sample_application.service..")
            .or()
            .resideInAnyPackage("com.chanctn.jhipster_sample_application.repository..")
            .should()
            .dependOnClassesThat()
            .resideInAnyPackage("..com.chanctn.jhipster_sample_application.web..")
            .because("Services and repositories should not depend on web layer")
            .check(importedClasses);
    }
}
