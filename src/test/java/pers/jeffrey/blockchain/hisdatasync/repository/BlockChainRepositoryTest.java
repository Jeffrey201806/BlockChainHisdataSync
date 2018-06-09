package pers.jeffrey.blockchain.hisdatasync.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest
public class BlockChainRepositoryTest {

    @Autowired
    BlockChainRepository blockChainRepository;

    @Test
    public void getLastTickId() throws Exception {
    }

    @Test
    public void checkTickTable() throws Exception {
        blockChainRepository.checkTickTable(new String[]{"eoseth","omgeth"});
    }

}