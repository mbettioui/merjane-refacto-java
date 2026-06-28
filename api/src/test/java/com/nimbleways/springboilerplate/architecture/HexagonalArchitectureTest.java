package com.nimbleways.springboilerplate.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

@AnalyzeClasses(
        packages = "com.nimbleways.springboilerplate",
        importOptions = ImportOption.DoNotIncludeTests.class)
class HexagonalArchitectureTest {

    @ArchTest
    static final ArchRule domain_is_framework_free = noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat()
            .resideInAnyPackage(
                    "org.springframework..",
                    "javax.persistence..",
                    "jakarta.persistence..");

    @ArchTest
    static final ArchRule api_is_pure = noClasses()
            .that().resideInAPackage("..api..")
            .should().dependOnClassesThat()
            .resideInAnyPackage(
                    "org.springframework..",
                    "javax.persistence..",
                    "..infra..");

    @ArchTest
    static final ArchRule domain_does_not_depend_on_infra = noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat()
            .resideInAPackage("..infra..");

    @ArchTest
    static final ArchRule domain_uses_ports_not_jpa = noClasses()
            .that().resideInAPackage("..domain..")
            .should().dependOnClassesThat()
            .resideInAnyPackage(
                    "..infra.persistence..",
                    "javax.persistence..",
                    "jakarta.persistence..");

    @ArchTest
    static final ArchRule web_uses_usecase_not_repositories = noClasses()
            .that().resideInAPackage("..infra.ui.web..")
            .should().dependOnClassesThat()
            .resideInAPackage("..repositories..");

    @ArchTest
    static final ArchRule no_legacy_packages_remain = noClasses()
            .should().resideInAnyPackage(
                    "..entities..",
                    "..repositories..",
                    "..dto.product..",
                    "..services.implementations..",
                    "..contollers..");
}
