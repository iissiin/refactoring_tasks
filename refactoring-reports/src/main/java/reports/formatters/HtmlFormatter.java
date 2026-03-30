package reports.formatters;

import reports.core.OutputFormatter;
import reports.core.ReportData;

import java.util.Map;

/** Strategy: вывод в формате HTML-таблицы. */
public class HtmlFormatter implements OutputFormatter {

    @Override
    public String formatHeader(String title, ReportData data) {
        return String.format(
            "<html><body><h2>%s</h2><p>Period: %s &mdash; %s</p><table border='1'><tbody>%n",
            title, data.getFrom(), data.getTo()
        );
    }

    @Override
    public String formatRow(Map<String, Object> row) {
        StringBuilder sb = new StringBuilder("<tr>");
        row.values().forEach(v -> sb.append("<td>").append(v).append("</td>"));
        sb.append("</tr>").append(System.lineSeparator());
        return sb.toString();
    }

    @Override
    public String formatFooter(ReportData data, String summary) {
        String sumHtml = summary.isEmpty() ? "" : "<p><b>" + summary + "</b></p>";
        return String.format("</tbody></table>%s</body></html>%n", sumHtml);
    }

    @Override
    public String formatEmpty() {
        return "<p><i>No data for the selected period.</i></p></body></html>";
    }
}
