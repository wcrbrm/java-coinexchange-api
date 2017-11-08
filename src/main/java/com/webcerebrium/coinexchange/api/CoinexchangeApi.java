package com.webcerebrium.coinexchange.api;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.common.escape.Escaper;
import com.google.common.net.UrlEscapers;
import com.google.gson.JsonObject;
import com.webcerebrium.coinexchange.datatype.CoinexchangeMarketInfo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Slf4j
@Data
public class CoinexchangeApi {


    /* Actual API key and Secret Key that will be used */
    public String apiKey;
    public String secretKey;

    public CoinexchangeConfig config = new CoinexchangeConfig();

    /**
     * API Base URL
     */
    public String baseUrl = "https://www.coinexchange.io/api/";

    /**
     * Guava Class Instance for escaping
     */
    private Escaper esc = UrlEscapers.urlFormParameterEscaper();


    /**
     * Constructor of API when you exactly know the keys
     * @param apiKey Public API Key
     * @param secretKey Secret API Key
     * @throws CoinexchangeApiException in case of any error
     */
    public CoinexchangeApi(String apiKey, String secretKey) throws CoinexchangeApiException {

        this.apiKey = apiKey;
        this.secretKey = secretKey;
        validateCredentials();
    }

    /**
     * Constructor of API - keys are loaded from VM options, environment variables, resource files
     * @throws CoinexchangeApiException in case of any error
     */
    public CoinexchangeApi() {
        this.apiKey = config.getVariable("COINEXCHANGE_API_KEY");
        this.secretKey = config.getVariable("COINEXCHANGE_SECRET");
    }

    /**
     * Validation we have API keys set up
     * @throws CoinexchangeApiException in case of any error
     */
    protected void validateCredentials() throws CoinexchangeApiException {
        String humanMessage = "Please check environment variables or VM options";
        if (Strings.isNullOrEmpty(this.getApiKey()))
            throw new CoinexchangeApiException("Missing COINEXCHANGE_API_KEY. " + humanMessage);
        if (Strings.isNullOrEmpty(this.getSecretKey()))
            throw new CoinexchangeApiException("Missing COINEXCHANGE_SECRET_KEY. " + humanMessage);
    }

    // ======= ======= ======= ======= ======= =======
    // MARKET INFORMATION
    // ======= ======= ======= ======= ======= =======

    public CoinexchangeMarketInfo getMarketsInfo() throws CoinexchangeApiException {
        JsonObject response = new CoinexchangeRequest(baseUrl + "v1/getmarkets").read().asJsonObject();
        return new CoinexchangeMarketInfo(response);
    }

    public Set<String> getCoinsOf(String coin) {
        try {
            CoinexchangeMarketInfo stats = this.getMarketsInfo();
            return stats.getCoinsOf(coin.toUpperCase());
        } catch (Exception e) {
            log.error("Coinexchange UNCAUGHT EXCEPTION {}", e);
        } catch (CoinexchangeApiException e) {
            log.warn("Coinexchange ERROR {}", e.getMessage());
        }
        return ImmutableSet.of();
    }

}
