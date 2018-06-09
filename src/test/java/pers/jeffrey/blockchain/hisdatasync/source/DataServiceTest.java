package pers.jeffrey.blockchain.hisdatasync.source;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import pers.jeffrey.blockchain.hisdatasync.bean.Tick;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DataServiceTest {

    @Autowired
    HuobiService dataService;

    @org.junit.Test
    public void queryLastTicks() throws Exception {
        List<Tick> ticks = dataService.queryLastTicks("eoseth", 100);
        for (Tick tick : ticks) {
            System.out.println(tick);
        }
    }

}