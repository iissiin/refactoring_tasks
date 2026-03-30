package reports.core;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Универсальный контейнер данных отчёта.
 * Хранит строки как Map<String, Object> — подходит для любого типа отчёта.
 */
public class ReportData {
    private final Date from;
    private final Date to;
    private final List<Map<String, Object>> rows;

    public ReportData(Date from, Date to, List<Map<String, Object>> rows) {
        this.from = from;
        this.to = to;
        this.rows = rows;
    }

    public Date getFrom()                    { return from; }
    public Date getTo()                      { return to; }
    public List<Map<String, Object>> getRows(){ return rows; }
    public boolean isEmpty()                 { return rows == null || rows.isEmpty(); }
}
