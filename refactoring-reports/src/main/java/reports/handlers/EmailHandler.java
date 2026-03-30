package reports.handlers;

import reports.core.BasePostProcessor;

/**
 * Звено 2 — отправка email.
 * Адресат задаётся при создании — каждый тип отчёта может иметь своего получателя.
 */
public class EmailHandler extends BasePostProcessor {

    private final String recipient;
    private final EmailService emailService;

    public EmailHandler(String recipient, EmailService emailService) {
        this.recipient = recipient;
        this.emailService = emailService;
    }

    @Override
    protected void handle(String title, String content) {
        emailService.send(recipient, title, content);
    }

    /** Интерфейс email-сервиса — легко мокается в тестах. */
    public interface EmailService {
        void send(String to, String subject, String body);
    }
}
