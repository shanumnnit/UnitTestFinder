package org.utilities.microtest.finder;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class FindTestHelper {
	Map<String, Set<String>> srcMap;
	Map<String, Set<String>> testMap;

	public Map<String, Set<String>> getSrcMap() {
		return srcMap;
	}

	public Map<String, Set<String>> getTestMap() {
		return testMap;
	}

	public void buildMainSourceMap(Path rootPathMain) {
		Map<String, Set<String>> srcMap = new LinkedHashMap<String, Set<String>>();
		Map<String, Set<String>> testMap = new LinkedHashMap<String, Set<String>>();
		try {
			Files.walkFileTree(rootPathMain, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {

					if (file.toFile().exists()) {
						// System.out.println("file: " + file.toString());
						int beginIndex = ((file.getNameCount() - 1) > 7) ? 7 : 6;
						String key = file.subpath(beginIndex, file.getNameCount() - 1).toString();
						String fileName = file.getFileName().toString();
						if (!key.startsWith("com") || !fileName.endsWith("java"))
							return FileVisitResult.CONTINUE;
						//System.out.println("key " + key + ", &&&&&& filename " + fileName);
						Set tempSet = srcMap.get(key);
						if (tempSet == null) {
							tempSet = new HashSet<String>();
							tempSet.add(fileName);
							srcMap.put(key, tempSet);
						} else
							tempSet.add(fileName);
					}

					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					// System.out.println("pre visit dir:" + dir);
					if (dir.toString().endsWith("klg") || dir.toString().endsWith("dralasoft")) {
						// System.out.println("! ! ! ! !Skipping :" +
						// dir.toString() + " ! ! ! ! !");
						return FileVisitResult.SKIP_SUBTREE;
					}
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		this.srcMap = srcMap;
	}

	public void buildMicroTestMap(Path rootPathMicroTest) {
		Map<String, Set<String>> testMap = new LinkedHashMap<String, Set<String>>();
		try {
			Files.walkFileTree(rootPathMicroTest, new SimpleFileVisitor<Path>() {
				@Override
				public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {

					if (file.toFile().exists()) {
						// System.out.println("file: " + file.toString());
						int beginIndex = ((file.getNameCount() - 1) > 7) ? 7 : 6;
						String key = file.subpath(beginIndex, file.getNameCount() - 1).toString();
						String fileName = file.getFileName().toString();
						if (!key.startsWith("com") || !fileName.endsWith("java"))
							return FileVisitResult.CONTINUE;
						//System.out.println("key " + key + ", &&&&&& filename " + fileName);
						Set tempSet = testMap.get(key);
						if (tempSet == null) {
							tempSet = new HashSet<String>();
							tempSet.add(fileName);
							testMap.put(key, tempSet);
						} else
							tempSet.add(fileName);
					}

					return FileVisitResult.CONTINUE;
				}

				@Override
				public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
					// System.out.println("pre visit dir:" + dir);
					if (dir.toString().endsWith("klg") || dir.toString().endsWith("dralasoft")) {
						// System.out.println("! ! ! ! !Skipping :" +
						// dir.toString() + " ! ! ! ! !");
						return FileVisitResult.SKIP_SUBTREE;
					}
					return FileVisitResult.CONTINUE;
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
		this.testMap = testMap;
	}
}
