package reports.generators;

import reports.core.AbstractReportGenerator;
import reports.core.OutputFormatter;
import reports.core.ReportData;

import java.util.*;

/**
 * Отчёт по продажам.
 * WMC = 3 (getTitle, fetchData, buildSummary).
 * Вся повторяющаяся логика убрана в AbstractReportGenerator.
 */
public class SalesReportGenerator extends AbstractReportGenerator {

    // В реальном проекте сюда приходит через DI (Spring, Guice и т.д.)
    private final Database db;

    public SalesReportGenerator(OutputFormatter formatter, Database db) {
        super(formatter);
        this.db = db;
    }

    @Override
    protected String getTitle() {
        return "SALES REPORT";
    }

    @Override
    protected ReportData fetchData(Date from, Date to) {
        List<Map<String, Object>> rows = db.query(
            "SELECT * FROM sales WHERE date BETWEEN ? AND ?", from, to
        );
        return new ReportData(from, to, rows);
    }

    /** Считаем сумму продаж для подвала. */
    @Override
    protected String buildSummary(ReportData data) {
        double total = data.getRows().stream()
            .mapToDouble(r -> ((Number) r.getOrDefault("amount", 0)).doubleValue())
            .sum();
        return String.format("Total: %.2f", total);
    }
}
