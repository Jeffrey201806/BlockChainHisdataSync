package pers.jeffrey.blockchain.hisdatasync;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import pers.jeffrey.blockchain.hisdatasync.bean.Tick;
import pers.jeffrey.blockchain.hisdatasync.source.HuobiService;
import pers.jeffrey.blockchain.hisdatasync.repository.BlockChainRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

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
public class HisDataSyncService {
    @Value("${huobi.symbol}")
    private String[] symbols;

    @Value("${period}")
    private int period;
    @Value("${pullcount}")
    private int[] pullcount;

    @Autowired
    BlockChainRepository blockChainRepository;
    @Autowired
    HuobiService dataService;

    private Logger LOG = LoggerFactory.getLogger(HisDataSyncService.class);

    private Map<String,String> maxTickIdCache = null;

    private Executor executor = null;

    public void init(){

        executor = Executors.newCachedThreadPool();

        maxTickIdCache = new ConcurrentHashMap<>();

        blockChainRepository.checkTickTable(symbols);

        for (String symbol : symbols) {
            String lastTickId = blockChainRepository.getLastTickId(symbol);
            maxTickIdCache.put(symbol, lastTickId == null? "0" : lastTickId);
        }

       startTickJob();

    }

    public void startTickJob () {
        new Thread(() -> {
            while(true) {

                long st = System.currentTimeMillis();
                CountDownLatch countDownLatch = new CountDownLatch(this.symbols.length);
                for (String symbol : symbols) {
                    executor.execute(() -> {
                        executeTickSync(symbol,0);
                        countDownLatch.countDown();
                    });
                }

                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                long et = System.currentTimeMillis();

                LOG.info("total pulled {} symbols,cost {} ms",symbols.length,(et-st));

                try {
                    Thread.sleep(period * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public void executeTickSync(String symbol,int pullLevel) {
        try {
            long st = System.currentTimeMillis();
            List<Tick> ticks = dataService.queryLastTicks(symbol, this.pullcount[pullLevel]);

            // 检查过后的tick结果，去掉已经存在的
            List<Tick> resultTicks = new ArrayList<>();

            String maxId = maxTickIdCache.get(symbol);

            // 是否有历史数据
            boolean hasOldData = false;

            for (Tick tick : ticks) {
                if (tick.getId().compareTo(maxId) > 0) {
                    resultTicks.add(tick);
                } else {
                    hasOldData = true;
                }
            }

            if (!hasOldData && pullLevel < pullcount.length) {
                LOG.info("symbol : {} use next level to pull data",symbol);
                // 没有历史数据 且 后续还有其他拉取的数量级别
                executeTickSync(symbol,pullLevel + 1);


            } else {
                for (Tick tick : resultTicks) {
                    if (tick.getId().compareTo(maxId) > 0) {
                        maxId = tick.getId();
                    }
                }

                this.maxTickIdCache.put(symbol, maxId);

                this.blockChainRepository.insertTick(symbol, resultTicks);

                long et = System.currentTimeMillis();

                LOG.info("symbol : {}, pull {} ticks use {} poolcount, the max id change to {} ,cost {} ms", symbol, resultTicks.size(),this.pullcount[pullLevel], maxId, (et - st));
            }



        } catch (Exception e) {
            LOG.error("symbol : " + symbol + " pull data error",e);
        }
    }
}
