package reports.core;

import java.util.Map;

/**
 * Strategy: интерфейс формата вывода.
 * Каждый формат (Text, CSV, HTML) реализует его независимо.
 * Чтобы добавить новый формат — не нужно трогать генераторы.
 */
public interface OutputFormatter {

    /** Шапка отчёта (заголовок + период). */
    String formatHeader(String title, ReportData data);

    /** Одна строка данных. */
    String formatRow(Map<String, Object> row);

    /** Подвал (итоги, timestamp и т.д.). */
    String formatFooter(ReportData data, String summary);

    /** Сообщение об отсутствии данных. */
    String formatEmpty();
}
