package com.biblieria.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

public final class FileUtil {
    private FileUtil() {}

    public static Path uploadBaseDir() throws IOException {
        String configured = System.getenv("APP_UPLOAD_DIR");
        Path base;
        if (configured == null || configured.trim().isEmpty()) {
            base = Paths.get(System.getProperty("user.home"), "biblieria-uploads");
        } else {
            base = Paths.get(configured);
        }
        Files.createDirectories(base);
        return base;
    }

    public static String extension(String fileName) {
        if (fileName == null) return "";
        int dot = fileName.lastIndexOf('.');
        if (dot < 0 || dot == fileName.length() - 1) return "";
        return fileName.substring(dot + 1).toLowerCase(Locale.ROOT);
    }

    public static String sanitizeFileName(String fileName) {
        if (fileName == null || fileName.trim().isEmpty()) return "archivo";
        return fileName.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
