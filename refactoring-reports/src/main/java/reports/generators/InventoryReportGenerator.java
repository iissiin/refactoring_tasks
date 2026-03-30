package reports.generators;

import reports.core.AbstractReportGenerator;
import reports.core.OutputFormatter;
import reports.core.ReportData;

import java.util.*;

/**
 * Отчёт по складским остаткам.
 * WMC = 2 (getTitle, fetchData). buildSummary не нужен — оставляем дефолт.
 */
public class InventoryReportGenerator extends AbstractReportGenerator {

    private final Database db;

    public InventoryReportGenerator(OutputFormatter formatter, Database db) {
        super(formatter);
        this.db = db;
    }

    @Override
    protected String getTitle() {
        return "INVENTORY REPORT";
    }

    @Override
    protected ReportData fetchData(Date from, Date to) {
        List<Map<String, Object>> rows = db.query(
            "SELECT * FROM inventory WHERE updated BETWEEN ? AND ?", from, to
        );
        return new ReportData(from, to, rows);
    }
}
