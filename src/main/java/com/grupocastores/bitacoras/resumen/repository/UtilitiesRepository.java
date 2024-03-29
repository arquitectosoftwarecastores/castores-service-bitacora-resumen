package com.grupocastores.bitacoras.resumen.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.ParameterMode;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;

import org.springframework.stereotype.Repository;

import com.grupocastores.bitacoras.resumen.service.domain.Parametro;
import com.grupocastores.bitacoras.resumen.service.domain.Personal;
import com.grupocastores.bitacoras.resumen.service.domain.Servidores;

@Repository
public class UtilitiesRepository {

    @PersistenceContext
    private EntityManager entityManager;
    
    public static String DB_23 = "PRODUCCION23";
    public static String DB_13 = "PRODUCCION13";
    
    public static final String queryGetLinkedServerByIdOficina = 
            "SELECT * FROM syn.dbo.v_Oficinas where Oficina = \'%s\'";
    static final String queryGetPersonalByIdUsuario = 
            "SELECT * FROM OPENQUERY("+ DB_13 +",'SELECT * FROM personal.personal p WHERE p.idusuario = \"%s\"');";
    
    static final String queryGetPersonal = 
            "SELECT * FROM OPENQUERY("+ DB_13 +",'SELECT * FROM personal.personal p WHERE p.idpersonal = \"%s\"');";
    
    static final String queryGetParametro = 
            "SELECT * FROM parametro WHERE clave = %s ";
    
    static final String queryFindPersonal =
            "SELECT * FROM OPENQUERY(" + DB_13 + ", 'SELECT %s FROM personal.personal WHERE %s = %s;')";
    
    /**
     * executeStoredProcedure: Ejecuta un procedimiento alamcenado para Guardar, Editar
     * 
     * @param query (String) consulta a ejecutar
     * @return Boolean
     * @author Moises Lopez Arrona [moisesarrona]
     * @date 2022-06-14
     */
    public Boolean executeStoredProcedure(String query) {
        int resp = 0;
        StoredProcedureQuery storedProcedure = entityManager.createStoredProcedureQuery("PcrExecSQL");
        storedProcedure.registerStoredProcedureParameter("sql", String.class, ParameterMode.IN);
        storedProcedure.registerStoredProcedureParameter("respuesta", String.class, ParameterMode.OUT);
        storedProcedure.setParameter("sql", query);
        storedProcedure.execute();
        resp = Integer.valueOf((String) storedProcedure.getOutputParameterValue("respuesta"));
        return resp > 0 ? true : false;
    }
    
    /**
     * getLinkedServerByOfice: Obtiene el servidor vinculado por id de oficina
     * 
     * @param idOficina (String) consulta a ejecutar
     * @return Boolean
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra]
     * @date 2022-09-22
     */
    @SuppressWarnings("unchecked")
    public Servidores getLinkedServerByOfice(String idOficina) {
        Query query = entityManager.createNativeQuery(String.format(queryGetLinkedServerByIdOficina,
                idOficina),Servidores.class
            );
        return (Servidores) query.getResultList().get(0);
    }
    
    /**
     * getPersonalByIdUsuario: Obtiene el personal por id de usuario.
     * 
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra]
     * @throws Exception 
     * @date 2022-08-19
     */
    @SuppressWarnings("unchecked")
    public Personal getPersonalByIdUsuario (int idUsuario) throws Exception {
        
        Query query = entityManager.createNativeQuery(String.format(
                queryGetPersonalByIdUsuario, idUsuario),Personal.class);
        
        List<Personal> list = query.getResultList();
        if (list == null)
            throw new Exception("No se pudo obtener el registro del usuario: "+idUsuario );
        return (Personal) list.get(0);
    }
    
    /**
     * getPersonal: Obtiene el personal por idpersonal.
     * 
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra]
     * @throws Exception 
     * @date 2022-12-13
     */
    @SuppressWarnings("unchecked")
    public Personal getPersonal (int idpersonal) throws Exception {
        
        Query query = entityManager.createNativeQuery(String.format(
                queryGetPersonal, idpersonal),Personal.class);
        
        List<Personal> list = query.getResultList();
        if (list == null)
            throw new Exception("No se pudo obtener el registro del eprsonal: "+idpersonal );
        return (Personal) list.get(0);
    }
    
    /**
     * getParametroByClave: Obtiene un parametro por clave de la tabla parametros
     * 
     * @param clave (String)
     * @return Parametro
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra]
     * @date 2023-03-18
     */
    @SuppressWarnings("unchecked")
    public Parametro getParametroByClave(String clave) {
        Query query = entityManager.createNativeQuery(String.format(queryGetParametro,
                clave),Parametro.class
            );
      
        if (query.getResultList().isEmpty())
           return null;
      
        return  (Parametro) query.getResultList().get(0);
    }
    
    /**
     * findPersonal: Encuentra el usuario por idPersonal o idUsuario
     *   
     * @param out (String)
     * @param in (String)
     * @param id (int)
     * @return Object
     * @author Cynthia Fuentes Amaro
     * @date 2022-07-29
     */ 
    @SuppressWarnings("unchecked")
    public Object findPersonal(String out, String in, String id) {
        Query query = entityManager
                .createNativeQuery(String.format(queryFindPersonal, out, in, id));
        
        List<Object> lstPersonal = query.getResultList();
        if (!lstPersonal.isEmpty())
            return lstPersonal.get(0);
        return null;

    }
    
    
    public static String getDb23() {
        return DB_23;
    }

    public static String getDb13() {
        return DB_13;
    }
}
