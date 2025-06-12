package org.togetherjava.aoc.internal.telemetry;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class JavaFileFinder {
    
    /**
     * Finds the Java source file associated with the given class.
     * 
     * @param clazz The class to find the source file for
     * @return Optional containing the File if found, empty otherwise
     */
    public static Optional<File> findJavaFile(Class<?> clazz) {
        return findJavaFile(clazz, "src/main/java");
    }
    
    /**
     * Finds the Java source file associated with the given class in a specific source directory.
     * 
     * @param clazz The class to find the source file for
     * @param sourceRoot The root directory to search in (e.g., "src/main/java")
     * @return Optional containing the File if found, empty otherwise
     */
    public static Optional<File> findJavaFile(Class<?> clazz, String sourceRoot) {
        try {
            // Get the class name and convert to file path
            String className = clazz.getName();
            String filePath = className.replace('.', File.separatorChar) + ".java";
            
            // Construct the full path
            Path fullPath = Paths.get(sourceRoot, filePath);
            File javaFile = fullPath.toFile();
            
            // Check if file exists
            if (javaFile.exists() && javaFile.isFile()) {
                return Optional.of(javaFile);
            }
            
            return Optional.empty();
            
        } catch (Exception e) {
            // Handle any exceptions (security, invalid paths, etc.)
            return Optional.empty();
        }
    }
    
    /**
     * Finds the Java source file by searching common source directories.
     * 
     * @param clazz The class to find the source file for
     * @return Optional containing the File if found, empty otherwise
     */
    public static Optional<File> findJavaFileInCommonPaths(Class<?> clazz) {
        final String[] commonSourcePaths = {
            "src/main/java",           // Maven/Gradle standard
            "src/java",                // Alternative structure
            "src",                     // Simple structure
            "java",                    // Another alternative
            "."                        // Current directory
        };
        
        for (String sourcePath : commonSourcePaths) {
            Optional<File> result = findJavaFile(clazz, sourcePath);
            if (result.isPresent()) {
                return result;
            }
        }
        
        return Optional.empty();
    }
    
    /**
     * Gets the relative path of the Java file from the source root.
     * 
     * @param clazz The class to get the path for
     * @return The relative file path as a string
     */
    public static String getRelativeJavaFilePath(Class<?> clazz) {
        String className = clazz.getName();
        return className.replace('.', File.separatorChar) + ".java";
    }
}