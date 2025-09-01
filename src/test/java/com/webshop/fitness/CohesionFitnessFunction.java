package com.webshop.fitness;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;

import org.junit.jupiter.api.Test;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Fitness function to measure and enforce package cohesion.
 * Ensures that related functionality stays together.
 * 
 * @author WebShop Team
 * @version 1.0
 */

@AnalyzeClasses(packages = "com.webshop", importOptions = { ImportOption.DoNotIncludeTests.class })
public class CohesionFitnessFunction {

    private static final double MIN_PACKAGE_COHESION = 0.6;
    private static final int MAX_CLASSES_PER_PACKAGE = 15;
    private static final int MIN_CLASSES_PER_PACKAGE = 2;

    @Test
    public void testPackageCohesion() {
        JavaClasses classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.webshop");

        Set<String> packages = classes.stream()
                .map(JavaClass::getPackageName)
                .collect(Collectors.toSet());

        for (String pkgName : packages) {
            Set<JavaClass> packageClasses = classes.stream()
                    .filter(cls -> cls.getPackageName().equals(pkgName))
                    .collect(Collectors.toSet());

            if (packageClasses.size() >= MIN_CLASSES_PER_PACKAGE) {
                double cohesion = calculatePackageCohesion(packageClasses);

                assertTrue(
                        cohesion >= MIN_PACKAGE_COHESION,
                        String.format("Package %s has low cohesion: %.2f (min: %.2f)",
                                pkgName, cohesion, MIN_PACKAGE_COHESION));
            }
        }
    }

    @Test
    public void testPackageSize() {
        JavaClasses classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.webshop");

        classes.stream()
                .map(JavaClass::getPackageName)
                .distinct()
                .forEach(pkgName -> {
                    long classCount = classes.stream()
                            .filter(cls -> cls.getPackageName().equals(pkgName))
                            .count();

                    assertTrue(
                            classCount <= MAX_CLASSES_PER_PACKAGE,
                            String.format("Package %s has too many classes: %d (max: %d)",
                                    pkgName, classCount, MAX_CLASSES_PER_PACKAGE));
                });
    }

    @Test
    public void testLayerCohesion() {
        JavaClasses classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.webshop");

        // Check that related functionality is grouped together
        // For example, all DTOs should be in the same package
        long dtoPackages = classes.stream()
                .filter(cls -> cls.getSimpleName().endsWith("DTO"))
                .map(cls -> cls.getPackageName())
                .distinct()
                .count();

        assertTrue(
                dtoPackages <= 1,
                String.format("DTOs are scattered across %d packages, should be in one package", dtoPackages));

        // Check that repositories are together
        long repoPackages = classes.stream()
                .filter(cls -> cls.getSimpleName().endsWith("Repository"))
                .map(cls -> cls.getPackageName())
                .distinct()
                .count();

        assertTrue(
                repoPackages <= 1,
                String.format("Repositories are scattered across %d packages, should be in one package", repoPackages));
    }

    /**
     * Calculate cohesion metric for a package
     * Based on how many classes within the package reference each other
     * 
     * @param packageClasses classes in the package
     * @return cohesion score (0-1)
     */
    private double calculatePackageCohesion(Set<JavaClass> packageClasses) {
        if (packageClasses.size() <= 1) {
            return 1.0;
        }

        int totalPossibleConnections = packageClasses.size() * (packageClasses.size() - 1);
        int actualConnections = 0;

        for (JavaClass cls1 : packageClasses) {
            for (JavaClass cls2 : packageClasses) {
                if (!cls1.equals(cls2)) {
                    boolean hasConnection = cls1.getDirectDependenciesFromSelf().stream()
                            .anyMatch(dep -> dep.getTargetClass().equals(cls2));

                    if (hasConnection) {
                        actualConnections++;
                    }
                }
            }
        }

        return totalPossibleConnections > 0 ? (double) actualConnections / totalPossibleConnections : 0;
    }

    /**
     * Calculate overall cohesion score for reporting
     * 
     * @return cohesion score (0-100)
     */
    public double calculateCohesionScore() {
        JavaClasses classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.webshop");

        double totalCohesion = 0;
        int packageCount = 0;

        Set<String> packages = classes.stream()
                .map(JavaClass::getPackageName)
                .collect(Collectors.toSet());

        for (String pkgName : packages) {
            Set<JavaClass> packageClasses = classes.stream()
                    .filter(cls -> cls.getPackageName().equals(pkgName))
                    .collect(Collectors.toSet());

            if (packageClasses.size() >= MIN_CLASSES_PER_PACKAGE) {
                totalCohesion += calculatePackageCohesion(packageClasses);
                packageCount++;
            }
        }

        return packageCount > 0 ? (totalCohesion * 100) / packageCount : 100;
    }
}
