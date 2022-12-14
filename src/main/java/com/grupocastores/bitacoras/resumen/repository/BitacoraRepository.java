package com.grupocastores.bitacoras.resumen.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.grupocastores.commons.inhouse.BitacoraResumenViajesCustom;
import com.grupocastores.commons.inhouse.BitacoraResumenViajesNegociacion;
import com.grupocastores.commons.inhouse.Esquemasdocumentacion;
import com.grupocastores.commons.inhouse.EstatusunidadBitacoraResumen;
import com.grupocastores.commons.inhouse.Ruta;


@Repository
public class BitacoraRepository{
    
    @Autowired
    private UtilitiesRepository utilitiesRepository;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    static final String queryFilterResumenViaje =" SELECT * FROM OPENQUERY( %s , '"
            + " SELECT "
            + "  tv.folio, "
            + "  tv.idruta, "
            + "  tv.idunidad, "
            + "  tv.idcliente, "
            + "  tv.idoficinacliente, "
            + "  tv.tipounidad AS idtipounidad, "
            + "  cti.nombre AS nombretipounidad, "
            + "  cc.noeconomico, "
            + "  tv.idremolque, "
            + "  tv.estatus, "
            + "  tes.nombre AS nombreestatus, "
            + "  tv.fechaviaje, "
            + "  tv.fechamod, "
            + "  tet.idnegociacion, "
            + "  tet.idesquema, "
            + "  tv.idoficinaorigen, "
            + "  cof1.plaza AS plazaorigen, "
            + "  tv.idoficinadestino, "
            + "  cof2.plaza AS plazadestino, "
            + "  tv.observaciones, "
            + "  cop.nombre AS nombreoperador "
            + " FROM "
            + "  talones.viajes tv "
            + "  INNER JOIN talones.viajes_esquema_gasto tve "
            + "    ON tv.idviaje = tve.idviaje "
            + "  INNER JOIN talones.guiaviaje tgv "
            + "    ON tv.idviaje = tgv.idviaje "
            + "  INNER JOIN talones.guias tg "
            + "    ON tgv.no_guia = tg.no_guia "
            + "  INNER JOIN talones.tg112022 tgma "
            + "    ON tg.no_guia = tgma.no_guia "
            + "  INNER JOIN talones.talones tt "
            + "    ON tgma.cla_talon = tt.cla_talon "
            + "  INNER JOIN talones.especificacion_talon tet "
            + "    ON tgma.cla_talon = tet.cla_talon "
            + "  INNER JOIN camiones.camiones cc "
            + "    ON tv.idunidad = cc.unidad "
            + "  INNER JOIN camiones.tipounidad cti "
            + "    ON tv.tipounidad = cti.idtipounidad "
            + "  INNER JOIN talones.estatusviajes tes "
            + "    ON tv.estatus = tes.idestatusviajes "
            + "  INNER JOIN camiones.operadores cop "
            + "    ON tv.idoperador = cop.idpersonal "
            + "  INNER JOIN castores.oficinas cof1  "
            + "    ON tv.idoficinaorigen = cof1.idoficina "
            + "  INNER JOIN castores.oficinas cof2  "
            + "    ON tv.idoficinadestino = cof2.idoficina "
            + " WHERE  %s  tv.idCliente = %s AND tv.idOficinacliente = \"%s\"  GROUP BY tv.idviaje; ');";
    
    static final String queryGetNegociacion = 
            "SELECT *FROM OPENQUERY(%s, 'SELECT nc.id_negociacion_cliente, nc.id_negociacion, n.desc_negociacion FROM bitacorasinhouse.negociaciones_clientes nc INNER JOIN bitacorasinhouse.negociaciones n ON n.id_negociacion = nc.id_negociacion WHERE id_negociacion_cliente = %s;');";
    
    static final String queryGetEsquema =
            "SELECT *FROM OPENQUERY(%s, 'SELECT id_esquema, nombre_esquema, estatus FROM bitacorasinhouse.esquemas WHERE estatus =1 AND id_esquema = %s');";
    
    static final String queryGetEstatusunidad =
            "SELECT *FROM OPENQUERY(%s, 'SELECT cb.idunidad, ce.nombre   FROM camiones.bitacora cb INNER JOIN camiones.estatus ce ON cb.estatusunidad = ce.idstatus WHERE idunidad = %s');";
    
    static final String queryGetRuta =
            "SELECT *FROM OPENQUERY(%s, 'SELECT * FROM talones.ruta WHERE idruta = %s');";
    
    
    /**
     * filterViajes: funcion para ejecutar query de filtrar viajes.
     * 
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra]
     * @return List<BitacoraResumenViajesCustom>
     * @date 2022-12-06
     */
    @SuppressWarnings("unused")
    public List<BitacoraResumenViajesCustom> filterViajes(String fechaInicio, String fechaFin, String idViaje, String noEconomico, int tipoUnidad,
            int estatusViaje, int idEsquema, int idNegociacion, int idCliente,String idOficinaCliente, String linkedServer) {
      
        String queryWherePart = "";
      
        
        if( idViaje.equals("0") && noEconomico.equals("0") ) {
 
            queryWherePart = queryWherePart + " ( tv.fechaviaje BETWEEN \""+fechaInicio+"\" AND \""+fechaFin+"\")  ";
            if(tipoUnidad != 0) {            
                queryWherePart = queryWherePart + " AND tv.tipounidad = "+tipoUnidad+" ";
            }
 
            if(estatusViaje != 9999) {
                queryWherePart = queryWherePart +" AND tv.estatus = "+estatusViaje+" ";
            }else {
                queryWherePart = queryWherePart +" AND tv.estatus NOT IN(5) ";
            }
            
            if(idEsquema != 0) {
                queryWherePart = queryWherePart +" AND tet.idesquema = "+idEsquema+" ";
            }
            if(idNegociacion != 0) {
                queryWherePart = queryWherePart +" AND tet.idnegociacion = "+tipoUnidad+" ";
            }
            
        }
        
        if( !idViaje.equals("0") ) {
            queryWherePart = queryWherePart + " tv.folio = "+idViaje+" AND ";
            queryWherePart = queryWherePart +"tv.estatus NOT IN(5) ";
        }
        
        if( !noEconomico.equals("0") ) {
            queryWherePart = queryWherePart + " cc.noeconomico = "+noEconomico+" AND ";
            queryWherePart = queryWherePart +" tv.estatus NOT IN(5) ";
            
        }
        
        queryWherePart = queryWherePart + " AND ";
        Query query = entityManager.createNativeQuery(String.format(
                queryFilterResumenViaje,
                linkedServer,
                
                queryWherePart,
                idCliente,
                idOficinaCliente),
                BitacoraResumenViajesCustom.class
            );
        List<BitacoraResumenViajesCustom> list = query.getResultList();
        
        return query.getResultList();
    }
    
    /**
     * getNegocioacion: Funcion para ejecutar consulta de negociacion .
     * 
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra]
     * @return BitacoraResumenViajesNegociacion
     * @date 2022-12-06
     */
    public BitacoraResumenViajesNegociacion getNegocioacion(int idNegociacion) {
        Query query = entityManager.createNativeQuery(String.format(
                queryGetNegociacion,
                utilitiesRepository.getDb23(),
                idNegociacion),
                BitacoraResumenViajesNegociacion.class
            );
        
        BitacoraResumenViajesNegociacion negociacion = (BitacoraResumenViajesNegociacion) query.getResultList().get(0);
        return negociacion;
    }
    
    /**
     * getEsquema: Funcion para ejecutar consulta de esquema .
     * 
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra]
     * @return Esquemasdocumentacion
     * @date 2022-12-06
     */
    public Esquemasdocumentacion getEsquema( int idEsquemaViaje) {
        Query query = entityManager.createNativeQuery(String.format(
                queryGetEsquema,
                utilitiesRepository.getDb23(),
                idEsquemaViaje),
                Esquemasdocumentacion.class
            );
        
        Esquemasdocumentacion negociacion = (Esquemasdocumentacion) query.getResultList().get(0);
        return negociacion;
    }
    
    /**
     * getEstatusUnidad: Funcion que ejecuta consulta para obtener estatus de unidad.
     * 
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra]
     * @return EstatusunidadBitacoraResumen
     * @date 2022-12-06
     */
    public EstatusunidadBitacoraResumen getEstatusUnidad( int idunidad) {
        Query query = entityManager.createNativeQuery(String.format(
                queryGetEstatusunidad,
                utilitiesRepository.getDb13(),
                idunidad),
                EstatusunidadBitacoraResumen.class
            );
        
        EstatusunidadBitacoraResumen estatus = (EstatusunidadBitacoraResumen) query.getResultList().get(0);
        return estatus;
    }
    
    /**
     * getRuta: Funcion que ejecuta consulta para obtener ruta.
     * 
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra]
     * @return Ruta
     * @date 2022-12-06
     */
    public Ruta getRuta( int idRuta) {
        Query query = entityManager.createNativeQuery(String.format(
                queryGetRuta,
                utilitiesRepository.getDb23(),
                idRuta),
                Ruta.class
            );
        
        Ruta ruta = (Ruta) query.getResultList().get(0);
        return ruta;
    }


   

   
   
}
