package com.webshop.fitness;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Fitness function to ensure adequate test coverage.
 * Monitors that critical components have corresponding tests.
 * 
 * @author WebShop Team
 * @version 1.0
 */
public class TestCoverageFitnessFunction {

    private static final double MIN_CRITICAL_COVERAGE = 0.8; // 80% for critical components
    private static final double MIN_STANDARD_COVERAGE = 0.6; // 60% for other components

    @Test
    public void testCriticalComponentsCoverage() {
        JavaClasses productionClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.webshop");

        JavaClasses testClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.ONLY_INCLUDE_TESTS)
                .importPackages("com.webshop");

        // Check that use cases have tests
        long useCaseCount = productionClasses.stream()
                .filter(cls -> cls.getSimpleName().endsWith("UseCase"))
                .count();

        long useCaseTestCount = testClasses.stream()
                .filter(cls -> cls.getSimpleName().contains("UseCase") && cls.getSimpleName().contains("Test"))
                .count();

        assertTrue(
                useCaseTestCount >= useCaseCount * MIN_CRITICAL_COVERAGE,
                String.format("Insufficient test coverage for use cases: %d/%d",
                        useCaseTestCount, useCaseCount));

        // Check that services have tests
        long serviceCount = productionClasses.stream()
                .filter(cls -> cls.getSimpleName().endsWith("Service"))
                .count();

        long serviceTestCount = testClasses.stream()
                .filter(cls -> cls.getSimpleName().contains("Service") && cls.getSimpleName().contains("Test"))
                .count();

        assertTrue(
                serviceTestCount >= serviceCount * MIN_CRITICAL_COVERAGE,
                String.format("Insufficient test coverage for services: %d/%d",
                        serviceTestCount, serviceCount));
    }

    @Test
    public void testRepositoryIntegrationTests() {
        JavaClasses productionClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.webshop");

        JavaClasses testClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.ONLY_INCLUDE_TESTS)
                .importPackages("com.webshop");

        long repositoryCount = productionClasses.stream()
                .filter(cls -> cls.getSimpleName().endsWith("Repository"))
                .count();

        long repositoryTestCount = testClasses.stream()
                .filter(cls -> cls.getSimpleName().contains("Repository") && cls.getSimpleName().contains("Test"))
                .count();

        assertTrue(
                repositoryTestCount >= repositoryCount * MIN_STANDARD_COVERAGE,
                String.format("Insufficient test coverage for repositories: %d/%d",
                        repositoryTestCount, repositoryCount));
    }

    /**
     * Calculate test coverage score for reporting
     * 
     * @return coverage score (0-100)
     */
    public double calculateCoverageScore() {
        JavaClasses productionClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.webshop");

        JavaClasses testClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.ONLY_INCLUDE_TESTS)
                .importPackages("com.webshop");

        // Simple approximation: ratio of test classes to production classes
        long productionCount = productionClasses.stream()
                .filter(cls -> !cls.isInterface())
                .count();

        long testCount = testClasses.size();

        double coverageRatio = productionCount > 0 ? (double) testCount / productionCount : 0;

        return Math.min(100, coverageRatio * 100);
    }

    @Test
    public void generateFitnessReport() {
        System.out.println("\n=== Architecture Fitness Report ===\n");

        ModularityFitnessFunction modularity = new ModularityFitnessFunction();
        System.out.printf("Modularity Score: %.1f%%\n", modularity.calculateModularityScore());

        CouplingFitnessFunction coupling = new CouplingFitnessFunction();
        System.out.printf("Coupling Score: %.1f%%\n", coupling.calculateCouplingScore());

        CohesionFitnessFunction cohesion = new CohesionFitnessFunction();
        System.out.printf("Cohesion Score: %.1f%%\n", cohesion.calculateCohesionScore());

        ComplexityFitnessFunction complexity = new ComplexityFitnessFunction();
        System.out.printf("Complexity Score: %.1f%%\n", complexity.calculateComplexityScore());

        System.out.printf("Test Coverage Score: %.1f%%\n", calculateCoverageScore());

        System.out.println("\n=================================\n");
    }
}