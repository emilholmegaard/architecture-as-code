package com.webshop.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Transactional;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.methods;

/**
 * ArchUnit tests for annotation usage rules.
 * Ensures proper use of framework annotations.
 * 
 * @author WebShop Team
 * @version 1.0
 */
@AnalyzeClasses(packages = "com.webshop", importOptions = ImportOption.DoNotIncludeTests.class)
public class AnnotationRulesTest {

        @ArchTest
        static final ArchRule controllers_should_be_annotated = classes().that()
                        .resideInAPackage("..presentation.rest..")
                        .and().haveSimpleNameEndingWith("Controller")
                        .should().beAnnotatedWith(RestController.class)
                        .because("Controllers should be annotated with @RestController");

        @ArchTest
        static final ArchRule services_should_be_annotated = classes().that().resideInAPackage("..service..")
                        .and().haveSimpleNameEndingWith("Service")
                        .and().areNotInterfaces()
                        .should().beAnnotatedWith(Service.class)
                        .because("Service classes should be annotated with @Service");

        @ArchTest
        static final ArchRule repositories_should_be_annotated = classes().that()
                        .resideInAPackage("..infrastructure.persistence..")
                        .and().haveSimpleNameEndingWith("Repository")
                        .should().beAnnotatedWith(Repository.class)
                        .because("Repository interfaces should be annotated with @Repository");

        @ArchTest
        static final ArchRule use_cases_should_have_transactional_methods = methods().that().areDeclaredInClassesThat()
                        .resideInAPackage("..usecase..")
                        .and().arePublic()
                        .and().doNotHaveRawReturnType(void.class)
                        .should().beAnnotatedWith(Transactional.class)
                        .because("Public use case methods should be transactional");

        @ArchTest
        static final ArchRule domain_models_should_not_have_persistence_annotations = classes().that()
                        .resideInAPackage("..domain.model..")
                        .should().notBeAnnotatedWith("javax.persistence.Entity")
                        .andShould().notBeAnnotatedWith("javax.persistence.Table")
                        .because("Domain models should be POJOs without JPA annotations");
}