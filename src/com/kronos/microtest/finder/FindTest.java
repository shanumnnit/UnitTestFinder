package com.kronos.microtest.finder;

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

import com.kronos.microtest.finder.model.TestPackages;

public class FindTest {

	public static void main(String[] args) {
		Path rootPathMain = Paths.get("C:\\Users\\shanu.gupta\\Desktop\\test\\main");
		Path rootPathMicroTest = Paths.get("C:\\Users\\shanu.gupta\\Desktop\\test\\test");
		// System.out.println("hello");
		FindTestHelper fth = new FindTestHelper();
		fth.buildMainSourceMap(rootPathMain);
		fth.buildMicroTestMap(rootPathMicroTest);
		Map<String, Set<String>> mainMap = fth.getSrcMap();
		Map<String, Set<String>> testMap = fth.getTestMap();
		/*
		 * System.out.println(":::::::::::MAIN MAP::::::::::\n"+ "has " +
		 * mainMap.keySet().size() + " packages"+ "\n"); Iterator<String> itr =
		 * mainMap.keySet().iterator(); while (itr.hasNext()) { String pkg =
		 * itr.next(); Set fSet = mainMap.get(pkg); System.out.println(
		 * "Package " + pkg + " has " + fSet.size() + " files."); }
		 * System.out.println("\n\n\n\n");
		 * System.out.println(":::::::::::TEST MAP::::::::::\n"+ "has " +
		 * testMap.keySet().size() + " packages"+ "\n"); Iterator<String> itr2 =
		 * testMap.keySet().iterator(); while (itr2.hasNext()) { String pkg =
		 * itr2.next(); Set fSet = testMap.get(pkg); System.out.println(
		 * "Package " + pkg + " has " + fSet.size() + " files."); }
		 */
		List<TestPackages> packageList = populatePackageList(mainMap, testMap);
		computeCoverageAndMissingTest(packageList);
		/*
		 * System.out.println("\n\n\n\n"); System.out.println(
		 * ":::::::::::MISSING MICROTEST MAP::::::::::\n" + "has " +
		 * testMap.keySet().size() + " packages" + "\n"); Iterator<String> itr3
		 * = missingMicroTests.keySet().iterator(); while (itr3.hasNext()) {
		 * String pkg = itr3.next(); Set fSet = missingMicroTests.get(pkg);
		 * System.out.println("Package " + pkg + " has " + fSet.size() +
		 * " files."); }
		 */
		Collections.sort(packageList, (a, b) -> a.coverage.compareTo(b.coverage));
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
			float coverage = ((float) (tPkg.getMainclasses().size() - (float) tPkg.getMissingMCT().size())
					/ (float) tPkg.getMainclasses().size()) * 100;
			tPkg.setCoverage(coverage);
		}
	}

	private static List<TestPackages> populatePackageList(Map<String, Set<String>> mainMap,
			Map<String, Set<String>> testMap) {
		// Map<String, Set<String>> missingMicroTests = new
		// LinkedHashMap<String, Set<String>>();
		List<TestPackages> packageList = new ArrayList<TestPackages>();
		Iterator<String> itr = mainMap.keySet().iterator();
		List<String> bestPackages = new ArrayList<String>();
		while (itr.hasNext()) {
			String pkg = itr.next();
			Set mainSet = mainMap.get(pkg);
			Set testSet = testMap.get(pkg);
			/*
			 * if (!testMap.containsKey(pkg)) { missingMicroTests.put(pkg,
			 * mainSet); } else if (wholePackageIsCovered(mainSet, testSet)) {
			 * bestPackages.add(pkg); } else { Iterator<String> setItr =
			 * mainSet.iterator(); while (setItr.hasNext()) { String mainClass =
			 * setItr.next(); String proposedTestClass = mainClass.substring(0,
			 * mainClass.indexOf(".java")) + "MicroTest.java"; if
			 * (!testSet.contains(proposedTestClass)) { Set tempSet =
			 * missingMicroTests.get(pkg); if (tempSet == null) { tempSet = new
			 * HashSet<String>(); tempSet.add(proposedTestClass);
			 * missingMicroTests.put(pkg, tempSet); } else
			 * tempSet.add(proposedTestClass); } } }
			 */
			TestPackages aPkg = new TestPackages(pkg, mainSet, testSet);
			packageList.add(aPkg);
		} /*
			 * System.out.println("\n::::::::BEST PACKAGES:::::::::");
			 * Iterator<String> listItr = bestPackages.iterator(); while
			 * (listItr.hasNext()) System.out.println(listItr.next());
			 */
		return packageList;
	}

	private static boolean wholePackageIsCovered(Set mainSet, Set testSet) {
		Iterator<String> setItr = mainSet.iterator();
		while (setItr.hasNext()) {
			String mainClass = setItr.next();
			String proposedTestClass = mainClass.substring(0, mainClass.indexOf(".java")) + "MicroTest.java";
			if (!testSet.contains(proposedTestClass))
				return false;
		}
		return true;
	}

}
