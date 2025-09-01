package com.webshop.fitness;

import jdepend.framework.JDepend;
import jdepend.framework.JavaPackage;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.Collection;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Fitness function to measure and enforce modularity.
 * Uses JDepend to analyze package cohesion and coupling metrics.
 * 
 * @author WebShop Team
 * @version 1.0
 */
public class ModularityFitnessFunction {

    private static final double MAX_AFFERENT_COUPLING = 10.0;
    private static final double MAX_EFFERENT_COUPLING = 8.0;
    private static final double MIN_ABSTRACTNESS = 0.2;
    private static final double MAX_INSTABILITY = 0.7;

    @Test
    public void testPackageModularity() throws IOException {
        JDepend jDepend = new JDepend();
        jDepend.addDirectory("target/classes");

        Collection<JavaPackage> packages = jDepend.analyze();

        for (JavaPackage pkg : packages) {
            String packageName = pkg.getName();

            // Skip test packages and external dependencies
            if (packageName.startsWith("com.webshop")) {

                // Check Afferent Coupling (Ca) - number of packages that depend on this package
                assertTrue(
                        pkg.afferentCoupling() <= MAX_AFFERENT_COUPLING,
                        String.format("Package %s has too high afferent coupling: %.0f (max: %.0f)",
                                packageName, pkg.afferentCoupling(), MAX_AFFERENT_COUPLING));

                // Check Efferent Coupling (Ce) - number of packages this package depends on
                assertTrue(
                        pkg.efferentCoupling() <= MAX_EFFERENT_COUPLING,
                        String.format("Package %s has too high efferent coupling: %.0f (max: %.0f)",
                                packageName, pkg.efferentCoupling(), MAX_EFFERENT_COUPLING));

                // Check Instability (Ce / (Ce + Ca))
                double instability = pkg.instability();
                if (!Double.isNaN(instability)) {
                    assertTrue(
                            instability <= MAX_INSTABILITY,
                            String.format("Package %s is too unstable: %.2f (max: %.2f)",
                                    packageName, instability, MAX_INSTABILITY));
                }

                // For packages with interfaces, check abstractness
                if (packageName.contains("port") || packageName.contains("service")) {
                    assertTrue(
                            pkg.abstractness() >= MIN_ABSTRACTNESS,
                            String.format("Package %s should be more abstract: %.2f (min: %.2f)",
                                    packageName, pkg.abstractness(), MIN_ABSTRACTNESS));
                }
            }
        }
    }

    @Test
    public void testDomainLayerIndependence() throws IOException {
        JDepend jDepend = new JDepend();
        jDepend.addDirectory("target/classes");
        Collection<JavaPackage> packages = jDepend.analyze();

        for (JavaPackage pkg : packages) {
            if (pkg.getName().contains("domain")) {
                // Domain packages should have low efferent coupling
                assertTrue(
                        pkg.efferentCoupling() <= 3,
                        String.format("Domain package %s has too many dependencies: %.0f",
                                pkg.getName(), pkg.efferentCoupling()));
            }
        }
    }

    /**
     * Calculate modularity metric for reporting
     * 
     * @return modularity score (0-100)
     */
    public double calculateModularityScore() {
        try {
            JDepend jDepend = new JDepend();
            jDepend.addDirectory("target/classes");
            Collection<JavaPackage> packages = jDepend.analyze();

            double totalScore = 0;
            int packageCount = 0;

            for (JavaPackage pkg : packages) {
                if (pkg.getName().startsWith("com.webshop")) {
                    double packageScore = 100;

                    // Deduct points for high coupling
                    if (pkg.afferentCoupling() > MAX_AFFERENT_COUPLING) {
                        packageScore -= (pkg.afferentCoupling() - MAX_AFFERENT_COUPLING) * 2;
                    }
                    if (pkg.efferentCoupling() > MAX_EFFERENT_COUPLING) {
                        packageScore -= (pkg.efferentCoupling() - MAX_EFFERENT_COUPLING) * 2;
                    }

                    // Deduct points for high instability
                    if (!Double.isNaN(pkg.instability()) && pkg.instability() > MAX_INSTABILITY) {
                        packageScore -= (pkg.instability() - MAX_INSTABILITY) * 20;
                    }

                    totalScore += Math.max(0, packageScore);
                    packageCount++;
                }
            }

            return packageCount > 0 ? totalScore / packageCount : 0;

        } catch (IOException e) {
            return 0;
        }
    }
}
