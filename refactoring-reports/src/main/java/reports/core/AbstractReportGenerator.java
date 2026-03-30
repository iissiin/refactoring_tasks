package reports.core;

import java.util.Date;


public abstract class AbstractReportGenerator {

    private final OutputFormatter formatter;
    private PostProcessor postProcessorChain;

    protected AbstractReportGenerator(OutputFormatter formatter) {
        this.formatter = formatter;
    }

    public void setPostProcessorChain(PostProcessor chain) {
        this.postProcessorChain = chain;
    }

    public final String generate(Date from, Date to) {
        ReportData data = fetchData(from, to);

        StringBuilder sb = new StringBuilder();
        sb.append(formatter.formatHeader(getTitle(), data));

        if (data.isEmpty()) {
            sb.append(formatter.formatEmpty());
        } else {
            for (var row : data.getRows()) {
                sb.append(formatter.formatRow(row));
            }
            sb.append(formatter.formatFooter(data, buildSummary(data)));
        }

        String result = sb.toString();

        if (postProcessorChain != null) {
            postProcessorChain.process(getTitle(), result);
        }

        return result;
    }

    protected abstract String getTitle();

    protected abstract ReportData fetchData(Date from, Date to);

    protected String buildSummary(ReportData data) {
        return "";
    }
}
