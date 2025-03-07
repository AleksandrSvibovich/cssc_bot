package bot.bd;

import bot.getfromenvs.GetFromEnvs;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DataSourceFactory {
    private static HikariDataSource dataSource;

    static {
        HikariConfig config = new HikariConfig();

        GetFromEnvs getFromEnvs = new GetFromEnvs();
        config.setJdbcUrl(getFromEnvs.getFromEnvsByName("db.url"));
        config.setUsername(getFromEnvs.getFromEnvsByName("db.user"));
        config.setPassword(getFromEnvs.getFromEnvsByName("db.password"));
        config.setMaximumPoolSize(10);
        dataSource = new HikariDataSource(config);
    }

    public static HikariDataSource getDataSource() {
        return dataSource;
    }
}