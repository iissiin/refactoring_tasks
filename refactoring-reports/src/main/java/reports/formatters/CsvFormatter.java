package reports.formatters;

import reports.core.OutputFormatter;
import reports.core.ReportData;

import java.util.Map;
import java.util.StringJoiner;

/** Strategy: вывод в формате CSV. */
public class CsvFormatter implements OutputFormatter {

    @Override
    public String formatHeader(String title, ReportData data) {
        // В CSV заголовок — это комментарий и строка с именами колонок
        return String.format("# %s | %s - %s%n", title, data.getFrom(), data.getTo());
    }

    @Override
    public String formatRow(Map<String, Object> row) {
        StringJoiner sj = new StringJoiner(",");
        // Если значение содержит запятую — оборачиваем в кавычки
        row.values().forEach(v -> {
            String val = String.valueOf(v);
            sj.add(val.contains(",") ? "\"" + val + "\"" : val);
        });
        return sj.toString() + System.lineSeparator();
    }

    @Override
    public String formatFooter(ReportData data, String summary) {
        return summary.isEmpty() ? "" : "# " + summary + System.lineSeparator();
    }

    @Override
    public String formatEmpty() {
        return "# No data" + System.lineSeparator();
    }
}
