# 历史数据同步服务

定时从火币pro站拉取数据存储到数据库中，方便后续回测

## K线

支持级别：1min, 5min, 15min, 30min, 60min, 1day, 1mon, 1week, 1year

每个级别最多只能取1000条，再往前的历史数据无法再获取

### 表结构定义

**t_kline_$symbol$**

| 中文名称 | 英文名称       | 类型          | 空否   | 备注            |
| ---- | ---------- | ----------- | ---- | ------------- |
| 主键id | id         | bigint      | n    |               |
| 交易对  | symbol     | varchar(50) | n    |               |
| K线类型 | period     | varchar(10) | n    | 即支持的级别        |
| 日期   | day        | int         | n    | 格式yyyyMMdd    |
| 时间   | time       | int         | n    | 格式HHmm        |
| k线ID | kid        | int         | n    | 原始数据ID，实际为时间戳 |
| 成交量  | amount     | double      | n    |               |
| 成交笔数 | count      | int         | n    |               |
| 开盘价  | open       | double      | n    |               |
| 收盘价  | close      | double      | n    |               |
| 最低价  | low        | double      | n    |               |
| 最高价  | high       | double      | n    |               |
| 成交额  | vol        | double      | n    |               |
| 创建时间 | createtime | date        |      |               |

## 逐笔成交

最多只能取最近的2000条数据

### 表结构定义

**t_tick_$symbol$**

| 中文名称 | 英文名称       | 类型          | 空否   | 备注         |
| ---- | ---------- | ----------- | ---- | ---------- |
| 主键id | id         | bigint      | n    |            |
| 交易对  | symbol     | varchar(50) | n    |            |
| 成交id | trade_id   | bigint      | n    |            |
| 成交价  | price      | double      | n    |            |
| 成交量  | amount     | int         | n    |            |
| 成交方向 | direction  | varchar(10) | n    | buy / sell |
| 成交时间 | ts         | bigint      | n    |            |
| 创建时间 | createtime | date        |      |            |
