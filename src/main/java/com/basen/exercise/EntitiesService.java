package com.basen.exercise;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;

import java.util.*;

public class EntitiesService extends AbstractVerticle {


  private final Map<String, Entity> entities = new LinkedHashMap<>();

  @Override
  public void start(Promise<Void> startPromise) {

    createEntities();
    Integer port = config().getInteger("http.port", 8080);

    // Create a router object.
    Router router = Router.router(vertx);
    router.route().handler(BodyHandler.create());
    router.get("/api/entities").handler(this::getAll);
    router.post("/api/entities").handler(this::addEntity);
    router.get("/api/entities/:id").handler(this::getEntity);

    vertx.createHttpServer().requestHandler(router)
      .listen(port, http -> {
        if (http.succeeded()) {
          startPromise.complete();
          System.out.println(("HTTP server started on port " + port));
        } else {
          startPromise.fail(http.cause());
        }
      });
  }

  private void createEntities() {
    Map<String, String> data = new HashMap<>();
    data.put("status", "active");
    data.put("connectedClients", "10");
    Set<String> subEntities = new HashSet<>();
    subEntities.add("1");
    subEntities.add("20");
    Entity second = new BasicEntity("2", subEntities, data);
    entities.put(second.getId(), second);
  }

  private void getAll(RoutingContext routingContext) {
    routingContext.response()
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(Json.encodePrettily(entities.values()));
  }

  private void getEntity(RoutingContext routingContext) {
    final String id = routingContext.request().getParam("id");
    if (id == null) {
      routingContext.response().setStatusCode(400).end();
    } else {
      Entity entity = entities.get(id);
      if (entity == null) {
        routingContext.response().setStatusCode(404).end();
      } else {
        routingContext.response()
          .putHeader("content-type", "application/json; charset=utf-8")
          .end(Json.encodePrettily(entity));
      }
    }
  }

  private void addEntity(RoutingContext routingContext) {
    final Entity newEntity = Json.decodeValue(routingContext.getBodyAsString(), BasicEntity.class);
    entities.put(newEntity.getId(), newEntity);
    routingContext.response()
      .putHeader("content-type", "application/json; charset=utf-8")
      .end(Json.encodePrettily(newEntity));
  }

}
