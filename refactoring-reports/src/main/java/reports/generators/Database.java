package reports.generators;

import java.util.Date;
import java.util.List;
import java.util.Map;

/** Заглушка БД — в реальном проекте заменяется настоящей реализацией. */
public interface Database {
    List<Map<String, Object>> query(String sql, Date from, Date to);
}
