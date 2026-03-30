package reports.core;

/**
 * Chain of Responsibility: звено цепочки постобработки.
 * Каждый обработчик делает своё дело и передаёт дальше.
 */
public interface PostProcessor {

    /** Установить следующий обработчик в цепочке. */
    PostProcessor setNext(PostProcessor next);

    /** Обработать готовый отчёт. */
    void process(String reportTitle, String reportContent);
}
