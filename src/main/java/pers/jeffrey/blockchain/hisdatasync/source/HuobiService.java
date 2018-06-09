package pers.jeffrey.blockchain.hisdatasync.source;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pers.jeffrey.blockchain.hisdatasync.bean.Tick;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zengjianfeng
 * @name
 * @desc
 * @jdk
 * @group
 * @os
 * @date 2018-06-08
 */
@Service
public class HuobiService {

    private Logger LOG = LoggerFactory.getLogger(HuobiService.class);

    @Autowired
    private RestTemplate restTemplate;
    @Value("${huobi.apiurl}")
    private String apiUrl;

    @Value("${huobi.AccessKeyId}")
    private String accessKeyId;

    public List<Tick> queryLastTicks(String symbol, int size) {
        List<Tick> ticks = new ArrayList<>();
       /* Map<String, Object> params = new HashMap<>();
        params.put("AccessKeyId", accessKeyId);
        params.put("symbol", symbol);
        params.put("size", size);*/

        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Content-Type", "application/x-www-form-urlencoded");
        requestHeaders.add("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36");

        HttpEntity<String> requestEntity = new HttpEntity<>(null, requestHeaders);

        String url = apiUrl + "market/history/trade?symbol="+symbol+"&size="+size+"&AccessKeyId="+accessKeyId;

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity, String.class);


        String body = response.getBody();
        JSONObject jsonObject = JSON.parseObject(body);

        String status = (String) jsonObject.get("status");

        if (!status.equalsIgnoreCase("ok")) {
            LOG.error("queryLastTicks error,url : " + url + ", response:" + body);
        } else {
            JSONArray jsonArray = jsonObject.getJSONArray("data");

            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject subJsonObject = jsonArray.getJSONObject(i);
                JSONArray data = subJsonObject.getJSONArray("data");
                String tickStr = data.getString(0);
                Tick tick = JSON.parseObject(tickStr, Tick.class);
                ticks.add(tick);
            }

        }

        return ticks;
    }


}
