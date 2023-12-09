package com.server.infoviescat.Entities;

import com.server.informaViesCat.Entities.Incident.IncidentCriteria;
import com.server.informaViesCat.Entities.Incident.IncidentCriteriaBuilder;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author leith
 */
public class IncidentCriteriaBuilderTest {

    private IncidentCriteriaBuilder incidentCriteriaBuilder;

    public void IncidentCriteriaBuilderTest() {

        this.incidentCriteriaBuilder = new IncidentCriteriaBuilder();
    }

    public void testCreate_Success() {
        // Arrange

        IncidentCriteria incidentCriteria = new IncidentCriteria();

        incidentCriteria.UserId = "userId";
        // Act
        var queryConditions = incidentCriteriaBuilder.buildConditions(incidentCriteria);

        // Assert
    }
}
