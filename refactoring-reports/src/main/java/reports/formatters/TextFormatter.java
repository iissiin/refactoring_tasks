package reports.formatters;

import reports.core.OutputFormatter;
import reports.core.ReportData;

import java.util.Map;

/** Strategy: вывод в виде plain text. */
public class TextFormatter implements OutputFormatter {

    @Override
    public String formatHeader(String title, ReportData data) {
        return String.format(
            "%n=== %s ===%nPeriod: %s - %s%n",
            title, data.getFrom(), data.getTo()
        );
    }

    @Override
    public String formatRow(Map<String, Object> row) {
        StringBuilder sb = new StringBuilder();
        row.forEach((k, v) -> sb.append(k).append(": ").append(v).append("  "));
        return sb.append(System.lineSeparator()).toString();
    }

    @Override
    public String formatFooter(ReportData data, String summary) {
        return summary.isEmpty()
            ? String.format("--- Generated at: %s%n", new java.util.Date())
            : String.format("%s%nGenerated at: %s%n", summary, new java.util.Date());
    }

    @Override
    public String formatEmpty() {
        return "No data for the selected period." + System.lineSeparator();
    }
}
