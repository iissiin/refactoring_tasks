package reports.core;

/**
 * Базовый класс для обработчиков цепочки.
 * Хранит ссылку на следующее звено и вызывает его автоматически.
 * Конкретные обработчики переопределяют только handle().
 */
public abstract class BasePostProcessor implements PostProcessor {

    private PostProcessor next;

    @Override
    public PostProcessor setNext(PostProcessor next) {
        this.next = next;
        return next; // позволяет писать: a.setNext(b).setNext(c)
    }

    @Override
    public final void process(String title, String content) {
        handle(title, content);
        if (next != null) {
            next.process(title, content);
        }
    }

    /** Конкретная логика этого звена. */
    protected abstract void handle(String title, String content);
}
