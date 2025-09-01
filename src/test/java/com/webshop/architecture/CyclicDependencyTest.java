package com.webshop.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

/**
 * ArchUnit tests for detecting cyclic dependencies.
 * Prevents circular dependencies between packages.
 * 
 * @author WebShop Team
 * @version 1.0
 */
@AnalyzeClasses(packages = "com.webshop", importOptions = ImportOption.DoNotIncludeTests.class)
public class CyclicDependencyTest {

    @ArchTest
    static final ArchRule no_cycles_between_packages = slices().matching("com.webshop.(*)..")
            .should().beFreeOfCycles()
            .because("Cyclic dependencies between packages are not allowed");

    @ArchTest
    static final ArchRule no_cycles_in_domain = slices().matching("com.webshop.domain.(*)..")
            .should().beFreeOfCycles()
            .because("Domain packages should not have cyclic dependencies");

    @ArchTest
    static final ArchRule no_cycles_in_application = slices().matching("com.webshop.application.(*)..")
            .should().beFreeOfCycles()
            .because("Application packages should not have cyclic dependencies");

    @ArchTest
    static final ArchRule no_cycles_in_infrastructure = slices().matching("com.webshop.infrastructure.(*)..")
            .should().beFreeOfCycles()
            .because("Infrastructure packages should not have cyclic dependencies");

    @ArchTest
    static final ArchRule no_cycles_between_layers = slices()
            .matching("com.webshop.(domain|application|infrastructure|presentation)..")
            .should().beFreeOfCycles()
            .because("There should be no cyclic dependencies between architectural layers");
}