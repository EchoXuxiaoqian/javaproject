package main.java.cn.xxq.tools.flyway;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.CRC32;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationInfoService;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.flywaydb.core.api.configuration.Configuration;
import org.flywaydb.core.api.logging.Log;
import org.flywaydb.core.api.logging.LogFactory;
import org.flywaydb.core.internal.jdbc.JdbcUtils;
import org.flywaydb.core.internal.util.FileCopyUtils;

/**
 * @author HEX145
 * @date 2019年9月17日
 */
public class FlywayService {
    private static final Log LOG = LogFactory.getLog(FlywayService.class);
    private static DateTimeFormatter datefmt = DateTimeFormatter.ISO_DATE_TIME;
    private static ZoneId zoneId = ZoneId.systemDefault();
    private String url;
    private String user;
    private String password;
    private Flyway flyway;
    
    /**
     * @return the flyway
     */
    public Flyway getFlyway() {
        return flyway;
    }

    /**
     * @param flyway the flyway to set
     */
    public void setFlyway(Flyway flyway) {
        this.flyway = flyway;
    }

    /**
     * @param dburl
     * @param dbuser
     * @param dbpassword
     */
    public FlywayService(String dburl, String dbuser, String dbpassword) {
        LOG.isDebugEnabled();
        this.url = dburl;
        this.user = dbuser;
        this.password = dbpassword;
        ClassicConfiguration config = new ClassicConfiguration();
        config.setDataSource(url, user, password);
        config.setLocationsAsStrings("filesystem:src/main/resources/sql");
        config.setOutOfOrder(true);
        config.setValidateOnMigrate(false);
        /*config.setCleanOnValidationError(true); 验证失败的情况下执行clean清空数据库*/
        this.flyway = new Flyway(config);
    }

    public Configuration getFlywayConfig() {
        return this.flyway.getConfiguration();
    }

    public void getFlywayInfo() {
        MigrationInfoService info = flyway.info();
        MigrationInfo[] infoList = info.all();
        StringBuffer flywayInfo = new StringBuffer();
        for (MigrationInfo t : infoList) {
            if (flywayInfo.length() == 0) {
                flywayInfo = flywayInfo.append("flyway_schema_history");
            }
            flywayInfo = flywayInfo.append("\n")
                .append(t.getInstalledRank().toString().concat("  ").concat(t.getVersion().getVersion()).concat("  ")
                    .concat(t.getScript()).concat("  ").concat(t.getChecksum().toString()).concat("  ")
                    .concat(datefmt.format(LocalDateTime.ofInstant(t.getInstalledOn().toInstant(), zoneId))));
        }
        LOG.info(flywayInfo.toString());
    }

    public void cleanFlyway() {
        flyway.clean();
    }

    public void migrateFlyway() {
        String sqlDir = new File("src/main/resources/sql").getAbsolutePath();
        Map<File, String> sqlFiles = new HashMap<File, String>(1000);
        getSqlFile(sqlDir, sqlFiles);
        sqlFiles.forEach((key, value) -> {
            checkFileIsEdit(key, value);
        });
        flyway.migrate();
    }

    private void getSqlFile(String path, Map<File, String> files) {
        File file = new File(path);
        if (file.exists()) {
            File[] sqlFileList = file.listFiles();
            if (null == sqlFileList || sqlFileList.length == 0) {
                return;
            } else {
                for (File sqlFile : sqlFileList) {
                    if (sqlFile.isDirectory()) {
                        getSqlFile(sqlFile.getAbsolutePath(), files);
                    } else {
                        String fileName = sqlFile.getName().substring(0, 2);
                        files.put(sqlFile, fileName);
                    }
                }
            }
        }
    }

    private void checkFileIsEdit(File file, String type) {
        String scriptFile = file.getAbsolutePath();
        int index = scriptFile.lastIndexOf("src/main/resources/sql".replace("/", File.separator));
        scriptFile = scriptFile.substring(index+"src/main/resources/sql".length()+1);
        int fileChecksum = calculateFileChecksum(file);
        int scriptChecksum = getFlywayScriptChecksum(scriptFile);
        boolean updateFlag =
            (scriptChecksum != 0 && scriptChecksum != fileChecksum && ("V2".equals(type) || "V4".equals(type)));
        if (updateFlag) {
            deleteVersion(scriptFile);
        }
    }

    private void deleteVersion(String script) {
        Connection connect = JdbcUtils.openConnection(
            this.flyway.getConfiguration().getDataSource(), this.flyway.getConfiguration().getConnectRetries());
        String sql = "delete from flyway_schema_history where script = \"".concat(script.replaceAll("\\\\", "/")).concat("\";");
        PreparedStatement pstmt = null;
        try {
            pstmt = connect.prepareStatement(sql) ;
            pstmt.execute();
        } catch (SQLException e) {
             e.printStackTrace();
        }  finally{
            if(pstmt != null){
                try {
                    pstmt.close();
                } catch (SQLException e) {
                     e.printStackTrace();
                }
            }
            JdbcUtils.closeConnection(connect);
        }
    }

    private int calculateFileChecksum(File file) {
        final CRC32 crc32 = new CRC32();
        try {
            InputStream input = new FileInputStream(file);
            Reader reader = new InputStreamReader(input, Charset.forName("UTF-8"));
            String str = FileCopyUtils.copyToString(reader);
            BufferedReader buffer = new BufferedReader(new StringReader(str));
            String line;
            while ((line = buffer.readLine()) != null) {
                crc32.update(line.getBytes("UTF-8"));
            }
            buffer.close();
            reader.close();
            input.close();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return (int)crc32.getValue();
    }

    private int getFlywayScriptChecksum(String script) {
        MigrationInfoService info = flyway.info();
        MigrationInfo[] infoList = info.all();
        for (MigrationInfo t : infoList) {
            if (t.getScript().equals(script.replaceAll("\\\\", "/"))) {
                return t.getChecksum();
            }
        }
        return 0;
    }

    public static void main(String[] args) {
        String url = "jdbc:mysql://172.16.101.221:3306/test";
        String user = "root";
        String password = "root";
        FlywayService service = new FlywayService(url, user, password);
        service.migrateFlyway();
    }
}


