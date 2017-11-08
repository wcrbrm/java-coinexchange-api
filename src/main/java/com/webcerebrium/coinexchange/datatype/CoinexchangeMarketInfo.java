package com.webcerebrium.coinexchange.datatype;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.webcerebrium.coinexchange.api.CoinexchangeApiException;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Data
@Slf4j
public class CoinexchangeMarketInfo {
    Map<Long, CoinexchangeMarket> mapMarkets = new HashMap<>();

    public CoinexchangeMarketInfo() {
    }

    public CoinexchangeMarketInfo(JsonObject response) throws CoinexchangeApiException {
        if (!response.has("result")) {
            throw new CoinexchangeApiException("Missing result in response object while trying to read markets information");
        }
        JsonArray result = response.get("result").getAsJsonArray();
        mapMarkets.clear();
        for (JsonElement element: result) {
            CoinexchangeMarket market = new CoinexchangeMarket(element.getAsJsonObject());
            mapMarkets.put(market.getMarketID(), market);
        }
    }

    public Set<String> getCoinsOf(String coin) throws CoinexchangeApiException {
        Set<String> result = new TreeSet<>();
        for (Map.Entry<Long, CoinexchangeMarket> entry: mapMarkets.entrySet()) {
            CoinexchangeMarket market = entry.getValue();
            if (!market.isActive()) continue;

            if (market.getBaseCurrencyCode().equals(coin.toUpperCase())) {
                result.add(market.getMarketAssetCode());
            } else if (market.getMarketAssetCode().equals(coin.toUpperCase())) {
                result.add(market.getBaseCurrencyCode());
            }
        }
        return result;
    }
}
