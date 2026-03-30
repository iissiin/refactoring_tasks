package reports.handlers;

import reports.core.BasePostProcessor;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Звено 3 — архивирование.
 * Сохраняет отчёт в файл с временной меткой.
 */
public class ArchiveHandler extends BasePostProcessor {

    private final String archiveDir;

    public ArchiveHandler(String archiveDir) {
        this.archiveDir = archiveDir;
    }

    @Override
    protected void handle(String title, String content) {
        try {
            Path dir = Paths.get(archiveDir);
            Files.createDirectories(dir);

            String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String filename = title.replaceAll("[^a-zA-Z0-9]", "_") + "_" + timestamp + ".txt";
            Path file = dir.resolve(filename);

            try (PrintWriter pw = new PrintWriter(file.toFile())) {
                pw.print(content);
            }

            System.out.println("[ARCHIVE] Saved: " + file.toAbsolutePath());
        } catch (IOException e) {
            System.err.println("[ARCHIVE] Failed to save report: " + e.getMessage());
        }
    }
}
