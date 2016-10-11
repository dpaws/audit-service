package com.pluralsight.dockerproductionaws.audit;

import com.pluralsight.dockerproductionaws.admin.Migrate;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.HttpClientOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.unit.Async;
import io.vertx.ext.unit.TestContext;
import io.vertx.ext.unit.junit.VertxUnitRunner;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by jmenga on 11/10/16.
 */
@RunWith(VertxUnitRunner.class)
public class AuditVerticleTest {
    private Vertx vertx;
    private Config config;

    private JsonObject stockQuote() {
        return new JsonObject()
                .put("exchange", "vert.x stock exchange")
                .put("symbol", "MCH")
                .put("name", "MacroHard")
                .put("bid", 3389.0)
                .put("ask", 3391.0)
                .put("volume", 90000)
                .put("open", 1000)
                .put("shares", 88000);
    }

    private JsonObject stockTrade(String action, int amount, int newAmount) {
        return new JsonObject()
                .put("action", action)
                .put("quote",stockQuote())
                .put("date", System.currentTimeMillis())
                .put("amount", amount)
                .put("owned", newAmount);
    }

    @Before
    public void testSetup(TestContext context) {
        vertx = Vertx.vertx();
        config = ConfigFactory.load();
    }

    @Test
    public void testStockTradesAudited(TestContext context) {
        Async async = context.async();
        String portfolioAddr = config.getString("portfolio.events");
        Migrate.main(null);
        vertx.deployVerticle(AuditVerticle.class.getName(), ar -> {
            vertx.eventBus().send(portfolioAddr, stockTrade("BUY", 3, 3));
            vertx.eventBus().send(portfolioAddr, stockTrade("SELL", 2, 1));
            vertx.eventBus().send(portfolioAddr, stockTrade("SELL", 1, 0));
            HttpClientOptions options = new HttpClientOptions().setDefaultHost(config.getString("http.host"));
            options.setDefaultPort(config.getInt("http.port"));
            HttpClient client = vertx.createHttpClient(options);
            client.get("/", response -> {
                context.assertEquals(response.statusCode(), 200);
                response.bodyHandler(buffer -> {
                    JsonArray body = buffer.toJsonArray();
                    context.assertEquals(body.size(), 3);
                    async.complete();

                });
            }).end();
        });
    }
}
