package pers.jeffrey.blockchain.hisdatasync;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HisDataSyncServiceTest {

    @Autowired
    HisDataSyncService hisDataSyncService;

    @Before
    public void setUp() throws Exception {
        hisDataSyncService.init();
    }

    @Test
    public void executeTickSync() throws Exception {
        hisDataSyncService.executeTickSync("eoseth",0);
        hisDataSyncService.executeTickSync("omgeth",0);

        Thread.sleep(100000);
    }

}