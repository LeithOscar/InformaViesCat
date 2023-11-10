
package com.server.informaViesCat.Interfaces.IRepository;


/**
 *
 * @author leith
 * repositori per la entitat session
 */
public interface ISessionRepository {
     boolean AddSession(String sessionId,int userId);
     boolean CloseSession(String sessionId);

}
