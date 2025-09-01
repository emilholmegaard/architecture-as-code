package com.webshop.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

/**
 * ArchUnit tests for package dependency rules.
 * Ensures proper package structure and dependencies.
 * 
 * @author WebShop Team
 * @version 1.0
 */
@AnalyzeClasses(packages = "com.webshop", importOptions = ImportOption.DoNotIncludeTests.class)
public class PackageDependencyTest {

        @ArchTest
        static final ArchRule controllers_should_only_be_in_presentation_rest = classes().that()
                        .haveSimpleNameEndingWith("Controller")
                        .should().resideInAPackage("..presentation.rest..")
                        .because("Controllers should be in presentation.rest package");

        @ArchTest
        static final ArchRule entities_should_only_be_in_domain_model = classes().that()
                        .haveSimpleNameEndingWith("Entity")
                        .or().areAnnotatedWith("jakarta.persistence.Entity")
                        .should().resideInAPackage("..domain.model..")
                        .because("Entities should be in domain.model package");

        @ArchTest
        static final ArchRule repositories_should_only_be_in_infrastructure_persistence = classes().that()
                        .haveSimpleNameEndingWith("Repository")
                        .should().resideInAPackage("..infrastructure.persistence..")
                        .because("Repositories should be in infrastructure.persistence package");

        @ArchTest
        static final ArchRule use_cases_should_only_be_in_application_usecase = classes().that()
                        .haveSimpleNameEndingWith("UseCase")
                        .should().resideInAPackage("..application.usecase..")
                        .because("Use cases should be in application.usecase package");

        @ArchTest
        static final ArchRule dtos_should_only_be_in_presentation_dto = classes().that().haveSimpleNameEndingWith("DTO")
                        .or().haveSimpleNameEndingWith("Dto")
                        .should().resideInAPackage("..presentation.dto..")
                        .because("DTOs should be in presentation.dto package");

        @ArchTest
        static final ArchRule ports_should_be_interfaces = classes().that().resideInAPackage("..application.port..")
                        .should().beInterfaces()
                        .because("Ports should be interfaces defining contracts");

        @ArchTest
        static final ArchRule config_classes_should_be_in_config_package = classes().that()
                        .haveSimpleNameEndingWith("Config")
                        .or().haveSimpleNameEndingWith("Configuration")
                        .should().resideInAPackage("..config..")
                        .because("Configuration classes should be in config package");
}