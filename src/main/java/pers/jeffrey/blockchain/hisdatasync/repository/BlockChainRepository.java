package pers.jeffrey.blockchain.hisdatasync.repository;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import pers.jeffrey.blockchain.hisdatasync.bean.Tick;

import java.util.*;

/**
 * @author zengjianfeng
 * @name
 * @desc
 * @jdk
 * @group
 * @os
 * @date 2018-06-09
 */
@Service
public class BlockChainRepository {

    private Logger LOG = LoggerFactory.getLogger(BlockChainRepository.class);

    public static final String TICK_TABLE = "T_TICK_";
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Value("${schema}")
    private String schema;

    /**
     * TICK建表语句
     */
    private final String TICK_DDL = "CREATE TABLE `T_TICK_${symbol}` (\n" +
            "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
            "  `symbol` varchar(20) NOT NULL,\n" +
            "  `trade_id` varchar(32) NOT NULL,\n" +
            "  `price` double NOT NULL,\n" +
            "  `amount` double NOT NULL,\n" +
            "  `direction` varchar(10) NOT NULL,\n" +
            "  `ts` bigint(20) NOT NULL,\n" +
            "  `createtime` datetime DEFAULT NULL,\n" +
            "  PRIMARY KEY (`id`),\n" +
            "  KEY `ts` (`ts`)\n" +
            ") ENGINE=InnoDB DEFAULT CHARSET=utf8;\n" +
            "\n";

    public String getLastTickId(String symbol) {
        Map<String, Object> map = jdbcTemplate.queryForMap("select max(trade_id) as TRADE_ID from " + TICK_TABLE + symbol);

        if (map != null) {
            return (String) map.get("TRADE_ID");
        } else {
            return "0";
        }

    }
    /**
     * 插入tick数据
     * @param symbol
     * @param ticks
     */
    public void insertTick(String symbol, List<Tick> ticks) {

        if (ticks == null || ticks.size() == 0) {
            return;
        }

        jdbcTemplate.update(tick2Sql(symbol, ticks));

    }

    private String tick2Sql(String symbol,List<Tick> ticks) {
        String date = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        StringBuffer sql = new StringBuffer("insert into " + TICK_TABLE + symbol + " (symbol,trade_id,price,amount,direction,ts,createtime) values ");

        for (Tick tick : ticks) {
            sql.append("('" + symbol + "','" + tick.getId() + "'," + tick.getPrice() + "," + tick.getAmount() + ",'" + tick.getDirection() + "'," + tick.getTs() + ",'" + date + "'),");
        }

        sql.deleteCharAt(sql.length() - 1);

        return sql.toString();
    }

    /**
     * 检查tick表是否存在，若不存在，直接创建
     * @param symbols
     */
    public void checkTickTable(String[] symbols) {
        // 查询库中已经存在的表记录
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select TABLE_NAME from information_schema.TABLES where TABLE_SCHEMA = ?", this.schema);

        Set<String> existTables = new HashSet<>();

        for (Map<String, Object> map : list) {
            String tableName = (String) map.get("TABLE_NAME");
            existTables.add(tableName.toUpperCase());
        }

        // 检查是否存在没有创建的
        for (String symbol : symbols) {
            String targetTable = TICK_TABLE + symbol.toUpperCase();

            if (!existTables.contains(targetTable)) {
                LOG.warn("symbol : " + symbol +" not created tick table, create it now!");
                // 不存在 进行创建
                String sql = TICK_DDL.replace("${symbol}", symbol.toUpperCase());
                jdbcTemplate.update(sql);
            }

        }

    }




}
