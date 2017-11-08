package com.webcerebrium.coinexchange.datatype;

import com.google.gson.JsonObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
public class CoinexchangeMarket {
    Long marketID;
    String marketAssetName;
    String marketAssetCode;
    Long marketAssetID;
    String marketAssetType;
    String baseCurrency;
    String baseCurrencyCode;
    Long baseCurrencyID;
    boolean active;

    public CoinexchangeMarket() {
    }

    public CoinexchangeMarket(JsonObject obj) {

        marketAssetName = obj.get("MarketAssetName").getAsString();
        marketAssetCode = obj.get("MarketAssetCode").getAsString();
        marketAssetType = obj.get("MarketAssetType").getAsString();
        baseCurrency = obj.get("BaseCurrency").getAsString();
        baseCurrencyCode = obj.get("BaseCurrencyCode").getAsString();

        marketID = obj.get("MarketID").getAsLong();
        marketAssetID = obj.get("MarketAssetID").getAsLong();
        baseCurrencyID = obj.get("BaseCurrencyID").getAsLong();
        active = obj.get("Active").getAsBoolean();
    }

}
