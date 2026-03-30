package reports;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import reports.formatters.*;
import reports.generators.*;
import reports.handlers.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportTest {

    @Mock Database db;
    @Mock EmailHandler.EmailService emailService;

    private final Date from = new Date(0);
    private final Date to   = new Date();

    // ─── данные-заглушки ──────────────────────────────────────────────────

    private List<Map<String, Object>> saleRows() {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("id", 1);
        row.put("amount", 500.0);
        return List.of(row);
    }

    private List<Map<String, Object>> emptyRows() {
        return Collections.emptyList();
    }

    // ─── Template Method ─────────────────────────────────────────────────

    @Test
    void sales_report_contains_title_and_total() {
        when(db.query(anyString(), any(), any())).thenReturn(saleRows());

        SalesReportGenerator gen = new SalesReportGenerator(new TextFormatter(), db);
        String result = gen.generate(from, to);

        assertTrue(result.contains("SALES REPORT"));
        assertTrue(result.contains("Total: 500.00"));
    }

    @Test
    void empty_data_shows_no_data_message() {
        when(db.query(anyString(), any(), any())).thenReturn(emptyRows());

        SalesReportGenerator gen = new SalesReportGenerator(new TextFormatter(), db);
        String result = gen.generate(from, to);

        assertTrue(result.contains("No data"));
    }

    @Test
    void inventory_report_has_correct_title() {
        when(db.query(anyString(), any(), any())).thenReturn(emptyRows());

        InventoryReportGenerator gen = new InventoryReportGenerator(new TextFormatter(), db);
        String result = gen.generate(from, to);

        assertTrue(result.contains("INVENTORY REPORT"));
    }

    @Test
    void user_activity_report_shows_event_count() {
        when(db.query(anyString(), any(), any())).thenReturn(saleRows());

        UserActivityReportGenerator gen = new UserActivityReportGenerator(new TextFormatter(), db);
        String result = gen.generate(from, to);

        assertTrue(result.contains("Total events: 1"));
    }

    // ─── Форматтеры (Strategy) ───────────────────────────────────────────

    @Test
    void csv_formatter_produces_no_html_tags() {
        when(db.query(anyString(), any(), any())).thenReturn(saleRows());

        SalesReportGenerator gen = new SalesReportGenerator(new CsvFormatter(), db);
        String result = gen.generate(from, to);

        assertFalse(result.contains("<"));
        assertFalse(result.contains(">"));
    }

    @Test
    void html_formatter_wraps_in_table() {
        when(db.query(anyString(), any(), any())).thenReturn(saleRows());

        SalesReportGenerator gen = new SalesReportGenerator(new HtmlFormatter(), db);
        String result = gen.generate(from, to);

        assertTrue(result.contains("<table"));
        assertTrue(result.contains("<tr>"));
        assertTrue(result.contains("</html>"));
    }

    @Test
    void text_formatter_uses_equals_separator() {
        when(db.query(anyString(), any(), any())).thenReturn(emptyRows());

        SalesReportGenerator gen = new SalesReportGenerator(new TextFormatter(), db);
        String result = gen.generate(from, to);

        assertTrue(result.contains("==="));
    }

    // ─── Chain of Responsibility ─────────────────────────────────────────

    @Test
    void email_handler_sends_to_correct_recipient() {
        when(db.query(anyString(), any(), any())).thenReturn(saleRows());

        EmailHandler emailHandler = new EmailHandler("manager@co.com", emailService);
        LoggingHandler loggingHandler = new LoggingHandler();
        loggingHandler.setNext(emailHandler);

        SalesReportGenerator gen = new SalesReportGenerator(new TextFormatter(), db);
        gen.setPostProcessorChain(loggingHandler);
        gen.generate(from, to);

        verify(emailService).send(eq("manager@co.com"), contains("SALES"), anyString());
    }

    @Test
    void chain_executes_all_handlers_in_order() {
        when(db.query(anyString(), any(), any())).thenReturn(saleRows());

        EmailHandler emailHandler = new EmailHandler("x@y.com", emailService);
        LoggingHandler loggingHandler = new LoggingHandler();
        loggingHandler.setNext(emailHandler);

        SalesReportGenerator gen = new SalesReportGenerator(new TextFormatter(), db);
        gen.setPostProcessorChain(loggingHandler);
        gen.generate(from, to);

        // оба звена должны были отработать — email точно вызван
        verify(emailService, times(1)).send(anyString(), anyString(), anyString());
    }

    @Test
    void no_chain_set_does_not_throw() {
        when(db.query(anyString(), any(), any())).thenReturn(saleRows());

        SalesReportGenerator gen = new SalesReportGenerator(new TextFormatter(), db);
        // postProcessorChain не задан — не должно бросить NPE
        assertDoesNotThrow(() -> gen.generate(from, to));
    }

    @Test
    void archive_handler_creates_file(@TempDir java.nio.file.Path tempDir) {
        when(db.query(anyString(), any(), any())).thenReturn(saleRows());

        ArchiveHandler archiveHandler = new ArchiveHandler(tempDir.toString());
        SalesReportGenerator gen = new SalesReportGenerator(new TextFormatter(), db);
        gen.setPostProcessorChain(archiveHandler);
        gen.generate(from, to);

        // В папке должен появиться хотя бы один файл
        assertTrue(tempDir.toFile().listFiles().length > 0);
    }
}
