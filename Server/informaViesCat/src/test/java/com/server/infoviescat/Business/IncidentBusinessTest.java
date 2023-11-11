package com.server.infoviescat.Business;

import com.server.informaViesCat.Business.IncidentBusiness;
import com.server.informaViesCat.Entities.Incident.Incident;
import com.server.informaViesCat.Interfaces.IBusiness.IIncidentsBusiness;
import com.server.informaViesCat.Repository.IncidentRepository;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 *
 * @author leith Clase de test unitaris de la clase Incident Busines, testeja tota
 * la logica del negoci referent a les incidencies
 *
 * frameWork JUnit Metodologia Assert
 */
public class IncidentBusinessTest {

    private IIncidentsBusiness incidentBusiness;
    private IncidentRepository repoMock;

    public IncidentBusinessTest() {

        repoMock = mock(IncidentRepository.class);

        incidentBusiness = new IncidentBusiness(repoMock);
    }

    @Test
    public void testCreate_Success() {
        // Arrange
      
        Incident mockIncident = new Incident(1,1,2,3,"","","","","","",false);

        when(repoMock.CreateIncident(mockIncident)).thenReturn(true);

        // Act
        boolean result = incidentBusiness.CreateNewIncident(mockIncident);

        // Assert
        verify(repoMock).CreateIncident(mockIncident);

    }
    
    @Test
    public void testGetAll_Success() {
        // Arrange
      
         List<Incident> incidentList =  new ArrayList<>();
         
        when(repoMock.GetAll("UserId")).thenReturn(incidentList);

        // Act
         List<Incident> result = incidentBusiness.GetAll("UserId");

        // Assert
        verify(repoMock).GetAll("UserId");

    }
}
