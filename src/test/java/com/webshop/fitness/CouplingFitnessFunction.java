package com.webshop.fitness;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Fitness function to measure and control coupling between packages.
 * Ensures loose coupling for maintainability.
 * 
 * @author WebShop Team
 * @version 1.0
 */
public class CouplingFitnessFunction {

    private static final int MAX_PACKAGE_DEPENDENCIES = 5;
    private static final int MAX_CLASS_DEPENDENCIES = 7;

    @Test
    public void testPackageCoupling() {
        JavaClasses classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.webshop");

        // Get unique package names
        Set<String> packageNames = classes.stream()
                .map(cls -> cls.getPackageName())
                .distinct()
                .collect(java.util.stream.Collectors.toSet());

        for (String packageName : packageNames) {
            // Get all classes in the current package
            Set<com.tngtech.archunit.core.domain.JavaClass> packageClasses = classes.stream()
                    .filter(cls -> cls.getPackageName().equals(packageName))
                    .collect(java.util.stream.Collectors.toSet());

            // Count dependencies to other packages
            long dependencyCount = packageClasses.stream()
                    .flatMap(cls -> cls.getDirectDependenciesFromSelf().stream())
                    .map(dep -> dep.getTargetClass().getPackageName())
                    .distinct()
                    .filter(targetPkg -> !targetPkg.equals(packageName))
                    .filter(targetPkg -> targetPkg.startsWith("com.webshop"))
                    .count();

            assertTrue(
                    dependencyCount <= MAX_PACKAGE_DEPENDENCIES,
                    String.format("Package %s has too many dependencies: %d (max: %d)",
                            packageName, dependencyCount, MAX_PACKAGE_DEPENDENCIES));
        }
    }

    @Test
    public void testClassCoupling() {
        JavaClasses classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.webshop");

        classes.forEach(javaClass -> {
            long dependencyCount = javaClass.getDirectDependenciesFromSelf().stream()
                    .map(dep -> dep.getTargetClass())
                    .filter(target -> target.getPackageName().startsWith("com.webshop"))
                    .distinct()
                    .count();

            assertTrue(
                    dependencyCount <= MAX_CLASS_DEPENDENCIES,
                    String.format("Class %s has too many dependencies: %d (max: %d)",
                            javaClass.getName(), dependencyCount, MAX_CLASS_DEPENDENCIES));
        });
    }

    @Test
    public void testLayerCoupling() {
        JavaClasses classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.webshop");

        // Check that domain layer has minimal coupling
        classes.stream()
                .filter(cls -> cls.getPackageName().contains("domain"))
                .forEach(domainClass -> {
                    long externalDependencies = domainClass.getDirectDependenciesFromSelf().stream()
                            .filter(dep -> !dep.getTargetClass().getPackageName().contains("domain"))
                            .filter(dep -> dep.getTargetClass().getPackageName().startsWith("com.webshop"))
                            .count();

                    assertTrue(
                            externalDependencies <= 2,
                            String.format("Domain class %s has too many external dependencies: %d",
                                    domainClass.getName(), externalDependencies));
                });
    }

    /**
     * Calculate coupling score for reporting
     * 
     * @return coupling score (0-100, higher is better)
     */
    public double calculateCouplingScore() {
        JavaClasses classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.webshop");

        int totalClasses = 0;
        int wellCoupledClasses = 0;

        for (var javaClass : classes) {
            if (!javaClass.isInterface()) {
                totalClasses++;

                long dependencies = javaClass.getDirectDependenciesFromSelf().stream()
                        .filter(dep -> dep.getTargetClass().getPackageName().startsWith("com.webshop"))
                        .count();

                if (dependencies <= MAX_CLASS_DEPENDENCIES) {
                    wellCoupledClasses++;
                }
            }
        }

        return totalClasses > 0 ? (wellCoupledClasses * 100.0) / totalClasses : 100;
    }
}