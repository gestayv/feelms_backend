package ejb;

import json.EdgeJson;
import json.GraphJson;
import json.NodeJson;
import org.neo4j.driver.v1.*;
import org.neo4j.driver.v1.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.ConcurrencyManagement;
import javax.ejb.ConcurrencyManagementType;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.*;

/**
 * Created by arturo on 04-06-17.
 */
@ConcurrencyManagement(ConcurrencyManagementType.CONTAINER)
@Startup
@Singleton
public class Neo4jEJB {

    private Driver driver;

    java.util.logging.Logger logger = java.util.logging.Logger.getLogger(Neo4jEJB.class.getName());

    @PostConstruct
    public void init() {
        //Reemplazar despues por una coneccion a un recurso dado por glassfish o algo.
        driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "TBD123123"));
    }

    public GraphJson getGraph(List<Integer> ids, int days_arg) {
        int days = days_arg;
        if(days_arg < 1) {
            days = 1;
        }
        LocalDate dateFrom = LocalDate.now().minusDays(days);
        int dateInt = Integer.parseInt(dateFrom.format(DateTimeFormatter.BASIC_ISO_DATE));

        try (Session session = driver.session()) {

            return session.readTransaction(new TransactionWork<GraphJson>() {
                @Override
                public GraphJson execute(Transaction tx) {
                    return matchFilmUsersFromDate(tx, ids, dateInt);
                }
            });
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error en Neo4jEJB: " + e.getMessage());
        }

        return null;
    }

    private static GraphJson matchFilmUsersFromDate(Transaction tx, List<Integer> ids, int dateInt) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("filmList", ids);
        params.put("date", dateInt);

        List<String> users = new ArrayList<String>();
        List<String> films = new ArrayList<String>();

        List<EdgeJson> edges = new ArrayList<EdgeJson>();

        StatementResult result = tx.run("MATCH (u:User)-[t:Tweeting]->(f:Film) " +
                "WHERE t.last_tweet >= $date AND f.film_id IN $filmList " +
                "RETURN u.name AS user, f.name AS film", params);

        while(result.hasNext()) {
            Record r = result.next();
            String user = r.get("user").asString();
            String film = r.get("film").asString();

            if(!users.contains(user)) {
                users.add(user);
            }

            if(!films.contains(film)) {
                films.add(film);
            }

            edges.add(new EdgeJson(user, film));
        }

        List<NodeJson> nodes = new ArrayList<NodeJson>();
        for (String film: films) {
            nodes.add(new NodeJson(film, "m"));
        }

        for (String user: users) {
            nodes.add(new NodeJson(user, "u"));
        }

        if(!nodes.isEmpty() && !edges.isEmpty()) {
            return new GraphJson(nodes, edges);
        } else {
            return null;
        }

    }


    @PreDestroy
    public void destroy() {
        driver.close();
    }

}
