package com.kronos.microtest.finder.model;

import java.util.HashSet;
import java.util.Set;

public class TestPackages {
	String packageName;
	Set<String> mainclasses;
	Set<String> testClasses;
	Set<String> missingMCT;
	public Float coverage;

	public TestPackages(String packageName, Set<String> mainclasses, Set<String> testClasses) {
		super();
		this.packageName = packageName;
		this.mainclasses = mainclasses;
		this.testClasses = testClasses;
		this.missingMCT = new HashSet<String>();
	}

	public Set<String> getMissingMCT() {
		return missingMCT;
	}

	public void setMissingMCT(Set<String> missingMCT) {
		this.missingMCT = missingMCT;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public Set<String> getMainclasses() {
		return mainclasses;
	}

	public void setMainclasses(Set<String> mainclasses) {
		this.mainclasses = mainclasses;
	}

	public Set<String> getTestClasses() {
		return testClasses;
	}

	public void setTestClasses(Set<String> testClasses) {
		this.testClasses = testClasses;
	}

	public float getCoverage() {
		return coverage;
	}

	public void setCoverage(float coverage) {
		this.coverage = coverage;
	}

	@Override
	public String toString() {
		return "TestPackages [packageName=" + packageName + ", mainclasses=" + mainclasses + ", testClasses="
				+ testClasses + ", missingMCT=" + missingMCT + ", coverage=" + coverage + "]";
	}
	
}
