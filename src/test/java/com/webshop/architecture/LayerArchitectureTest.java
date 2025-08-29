package com.webshop.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

/**
 * ArchUnit tests for enforcing layer architecture rules.
 * Ensures proper layer dependencies and isolation.
 * 
 * @author WebShop Team
 * @version 1.0
 */
@AnalyzeClasses(packages = "com.webshop", importOptions = ImportOption.DoNotIncludeTests.class)
public class LayerArchitectureTest {
    
    @ArchTest
    static final ArchRule layer_dependencies_are_respected = layeredArchitecture()
        .consideringAllDependencies()
        .layer("Presentation").definedBy("com.webshop.presentation..")
        .layer("Application").definedBy("com.webshop.application..")
        .layer("Domain").definedBy("com.webshop.domain..")
        .layer("Infrastructure").definedBy("com.webshop.infrastructure..")
        .layer("Config").definedBy("com.webshop.config..")
        .layer("Shared").definedBy("com.webshop.shared..")
        
        .whereLayer("Presentation").mayNotBeAccessedByAnyLayer()
        .whereLayer("Application").mayOnlyBeAccessedByLayers("Presentation", "Infrastructure", "Config")
        .whereLayer("Domain").mayOnlyBeAccessedByLayers("Application", "Presentation", "Infrastructure")
        .whereLayer("Infrastructure").mayOnlyBeAccessedByLayers("Config")
        .whereLayer("Shared").mayOnlyBeAccessedByLayers("Presentation", "Application", "Domain", "Infrastructure", "Config");
    
    @ArchTest
    static final ArchRule domain_should_not_depend_on_infrastructure = 
        noClasses().that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAPackage("..infrastructure..")
            .because("Domain layer should not depend on Infrastructure");
    
    @ArchTest
    static final ArchRule domain_should_not_depend_on_application = 
        noClasses().that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAPackage("..application..")
            .because("Domain layer should not depend on Application");
    
    @ArchTest
    static final ArchRule domain_should_not_depend_on_presentation = 
        noClasses().that().resideInAPackage("..domain..")
            .should().dependOnClassesThat().resideInAPackage("..presentation..")
            .because("Domain layer should not depend on Presentation");
    
    @ArchTest
    static final ArchRule domain_should_not_use_spring_annotations_except_service = 
        noClasses().that().resideInAPackage("..domain.model..")
            .should().dependOnClassesThat().resideInAPackage("org.springframework..")
            .because("Domain models should be POJOs without framework dependencies");
    
    @ArchTest
    static final ArchRule application_should_not_depend_on_presentation = 
        noClasses().that().resideInAPackage("..application..")
            .should().dependOnClassesThat().resideInAPackage("..presentation..")
            .because("Application layer should not depend on Presentation");
    
    @ArchTest
    static final ArchRule infrastructure_should_not_depend_on_presentation = 
        noClasses().that().resideInAPackage("..infrastructure..")
            .should().dependOnClassesThat().resideInAPackage("..presentation..")
            .because("Infrastructure should not depend on Presentation");
}
