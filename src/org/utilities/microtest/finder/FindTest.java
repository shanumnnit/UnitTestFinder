package org.utilities.microtest.finder;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.utilities.microtest.finder.model.TestPackages;

public class FindTest {

    public static void main(String[] args) {
        Path rootPathMain = Paths.get("C:\\Users\\shanu.gupta\\Desktop\\test\\main");
        Path rootPathMicroTest = Paths.get("C:\\Users\\shanu.gupta\\Desktop\\test\\test");
        FindTestHelper fth = new FindTestHelper();
        fth.buildMainSourceMap(rootPathMain);
        fth.buildMicroTestMap(rootPathMicroTest);
        Map<String, Set<String>> mainMap = fth.getSrcMap();
        Map<String, Set<String>> testMap = fth.getTestMap();
        List<TestPackages> packageList = populatePackageList(mainMap, testMap);
        computeCoverageAndMissingTest(packageList);
        Collections.sort(packageList, Comparator.comparing(a -> a.coverage));
        Iterator<TestPackages> pkgItr = packageList.iterator();
        while (pkgItr.hasNext()) {
            TestPackages tPkg = pkgItr.next();
            System.out.println("Package: " + tPkg.getPackageName() + " missing " + tPkg.getMissingMCT().size()
                    + " microTest out of " + tPkg.getMainclasses().size() + ". Coverage = " + tPkg.getCoverage() + "%");
        }
    }

    private static void computeCoverageAndMissingTest(List<TestPackages> packageList) {
        Iterator<TestPackages> pkgItr = packageList.iterator();
        while (pkgItr.hasNext()) {
            TestPackages tPkg = pkgItr.next();
            Iterator<String> mainSetItr = tPkg.getMainclasses().iterator();
            while (mainSetItr.hasNext()) {
                String mainClass = mainSetItr.next();
                String proposedTestClass = mainClass.substring(0, mainClass.indexOf(".java")) + "MicroTest.java";
                if (tPkg.getTestClasses() == null || !tPkg.getTestClasses().contains(proposedTestClass)) {
                    tPkg.getMissingMCT().add(mainClass);
                }
            }
            float coverage = ((tPkg.getMainclasses().size() - (float) tPkg.getMissingMCT().size())
                    / (float) tPkg.getMainclasses().size()) * 100;
            tPkg.setCoverage(coverage);
        }
    }

    private static List<TestPackages> populatePackageList(Map<String, Set<String>> mainMap,
                                                          Map<String, Set<String>> testMap) {
        List<TestPackages> packageList = new ArrayList<TestPackages>();
        Iterator<String> itr = mainMap.keySet().iterator();
        while (itr.hasNext()) {
            String pkg = itr.next();
            Set mainSet = mainMap.get(pkg);
            Set testSet = testMap.get(pkg);
            TestPackages aPkg = new TestPackages(pkg, mainSet, testSet);
            packageList.add(aPkg);
        }
        return packageList;
    }

}
