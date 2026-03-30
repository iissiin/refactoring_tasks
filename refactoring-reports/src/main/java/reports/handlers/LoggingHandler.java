package reports.handlers;

import reports.core.BasePostProcessor;

import java.util.logging.Logger;

/**
 * Звено 1 — логирование.
 * Записывает факт генерации отчёта в лог.
 */
public class LoggingHandler extends BasePostProcessor {

    private static final Logger log = Logger.getLogger(LoggingHandler.class.getName());

    @Override
    protected void handle(String title, String content) {
        log.info(String.format("[REPORT] '%s' generated, size=%d chars", title, content.length()));
    }
}
