package com.webshop.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

/**
 * ArchUnit tests for naming convention enforcement.
 * Ensures consistent naming across the codebase.
 * 
 * @author WebShop Team
 * @version 1.0
 */
@AnalyzeClasses(packages = "com.webshop", importOptions = { ImportOption.DoNotIncludeTests.class })
public class NamingConventionTest {

        @ArchTest
        static final ArchRule services_should_be_suffixed = classes().that().resideInAPackage("..service..")
                        .and().areNotInterfaces()
                        .should().haveSimpleNameEndingWith("Service")
                        .because("Service classes should end with 'Service'");

        @ArchTest
        static final ArchRule controllers_should_be_suffixed = classes().that().resideInAPackage("..rest..")
                        .and().areAnnotatedWith(org.springframework.web.bind.annotation.RestController.class)
                        .should().haveSimpleNameEndingWith("Controller")
                        .because("REST controllers should end with 'Controller'");

        @ArchTest
        static final ArchRule repositories_should_be_suffixed = classes().that().resideInAPackage("..persistence..")
                        .and().areAssignableTo(org.springframework.data.repository.Repository.class)
                        .should().haveSimpleNameEndingWith("Repository")
                        .because("Repository interfaces should end with 'Repository'");

        @ArchTest
        static final ArchRule use_cases_should_be_suffixed = classes().that().resideInAPackage("..usecase..")
                        .should().haveSimpleNameEndingWith("UseCase")
                        .because("Use case classes should end with 'UseCase'");

        @ArchTest
        static final ArchRule dtos_should_be_suffixed = classes().that().resideInAPackage("..dto..")
                        .should().haveSimpleNameEndingWith("DTO").orShould().haveSimpleNameEndingWith("Dto")
                        .because("Data Transfer Objects should end with 'DTO' or 'Dto'");

        @ArchTest
        static final ArchRule exceptions_should_be_suffixed = classes().that().resideInAPackage("..shared.exception")
                        .and().areAssignableTo(Exception.class)
                        .should().haveSimpleNameEndingWith("Exception")
                        .because("Exception classes should end with 'Exception'")
                        .allowEmptyShould(true);

        @ArchTest
        static final ArchRule exceptions_should_be_in_shared_exception = classes().that()
                        .areAssignableTo(Exception.class)
                        .should().resideInAPackage("com.webshop.shared.exception")
                        .because("Exception classes should be in the shared.exception package")
                        .allowEmptyShould(true);

        @ArchTest
        static final ArchRule interfaces_in_ports_should_not_have_impl_suffix = classes().that()
                        .resideInAPackage("..port..")
                        .and().areInterfaces()
                        .should().haveSimpleNameNotEndingWith("Impl")
                        .because("Interfaces should not have 'Impl' suffix");
}