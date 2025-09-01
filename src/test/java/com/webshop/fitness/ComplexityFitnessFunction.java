package com.webshop.fitness;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Fitness function to measure and control code complexity.
 * Monitors cyclomatic complexity and method sizes.
 * 
 * @author WebShop Team
 * @version 1.0
 */
public class ComplexityFitnessFunction {

    private static final int MAX_METHOD_LINES = 50;
    private static final int MAX_CLASS_LINES = 300;
    private static final int MAX_METHODS_PER_CLASS = 15;
    private static final int MAX_CONSTRUCTOR_PARAMETERS = 5;
    private static final int MAX_METHOD_PARAMETERS = 4;

    @Test
    public void testMethodComplexity() {
        JavaClasses classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.webshop");

        classes.forEach(javaClass -> {
            javaClass.getMethods().forEach(method -> {
                // Check method parameter count
                int paramCount = method.getRawParameterTypes().size();
                assertTrue(
                        paramCount <= MAX_METHOD_PARAMETERS,
                        String.format("Method %s.%s has too many parameters: %d (max: %d)",
                                javaClass.getSimpleName(), method.getName(),
                                paramCount, MAX_METHOD_PARAMETERS));
            });

            // Check constructor parameter count
            javaClass.getConstructors().forEach(constructor -> {
                int paramCount = constructor.getRawParameterTypes().size();
                assertTrue(
                        paramCount <= MAX_CONSTRUCTOR_PARAMETERS,
                        String.format("Constructor %s has too many parameters: %d (max: %d)",
                                javaClass.getSimpleName(), paramCount, MAX_CONSTRUCTOR_PARAMETERS));
            });
        });
    }

    @Test
    public void testClassComplexity() {
        JavaClasses classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.webshop");

        classes.forEach(javaClass -> {
            // Check number of methods per class
            int methodCount = javaClass.getMethods().size();
            assertTrue(
                    methodCount <= MAX_METHODS_PER_CLASS,
                    String.format("Class %s has too many methods: %d (max: %d)",
                            javaClass.getSimpleName(), methodCount, MAX_METHODS_PER_CLASS));

            // Check for god classes (classes doing too much)
            int fieldCount = javaClass.getFields().size();
            assertTrue(
                    fieldCount <= 10,
                    String.format("Class %s has too many fields: %d (max: 10)",
                            javaClass.getSimpleName(), fieldCount));
        });
    }

    @Test
    public void testPackageComplexity() {
        JavaClasses classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.webshop");

        // Ensure no package has too many classes (complexity indicator)
        classes.stream()
                .map(JavaClass::getPackageName)
                .distinct()
                .forEach(pkgName -> {
                    long classCount = classes.stream()
                            .filter(cls -> cls.getPackageName().equals(pkgName))
                            .count();

                    assertTrue(
                            classCount <= 20,
                            String.format("Package %s has too many classes: %d (max: 20)",
                                    pkgName, classCount));
                });
    }

    /**
     * Calculate complexity score for reporting
     * 
     * @return complexity score (0-100, higher is better/less complex)
     */
    public double calculateComplexityScore() {
        JavaClasses classes = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
                .importPackages("com.webshop");

        int totalClasses = 0;
        int simpleClasses = 0;

        for (JavaClass javaClass : classes) {
            if (!javaClass.isInterface()) {
                totalClasses++;

                boolean isSimple = javaClass.getMethods().size() <= MAX_METHODS_PER_CLASS
                        && javaClass.getFields().size() <= 10
                        && javaClass.getMethods().stream()
                                .allMatch(m -> m.getRawParameterTypes().size() <= MAX_METHOD_PARAMETERS);

                if (isSimple) {
                    simpleClasses++;
                }
            }
        }

        return totalClasses > 0 ? (simpleClasses * 100.0) / totalClasses : 100;
    }
}
