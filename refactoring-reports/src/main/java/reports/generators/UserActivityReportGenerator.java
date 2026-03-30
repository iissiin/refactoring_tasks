package reports.generators;

import reports.core.AbstractReportGenerator;
import reports.core.OutputFormatter;
import reports.core.ReportData;

import java.util.*;

/**
 * Третий тип отчёта — активность пользователей.
 * Добавили новый отчёт без единого изменения в базовом классе (OCP).
 * WMC = 3 (getTitle, fetchData, buildSummary).
 */
public class UserActivityReportGenerator extends AbstractReportGenerator {

    private final Database db;

    public UserActivityReportGenerator(OutputFormatter formatter, Database db) {
        super(formatter);
        this.db = db;
    }

    @Override
    protected String getTitle() {
        return "USER ACTIVITY REPORT";
    }

    @Override
    protected ReportData fetchData(Date from, Date to) {
        List<Map<String, Object>> rows = db.query(
            "SELECT * FROM user_events WHERE created_at BETWEEN ? AND ?", from, to
        );
        return new ReportData(from, to, rows);
    }

    @Override
    protected String buildSummary(ReportData data) {
        return String.format("Total events: %d", data.getRows().size());
    }
}
