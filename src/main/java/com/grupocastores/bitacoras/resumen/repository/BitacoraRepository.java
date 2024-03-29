package com.grupocastores.bitacoras.resumen.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.grupocastores.bitacoras.resumen.DTO.AsistenciaOperadorDTO;
import com.grupocastores.bitacoras.resumen.DTO.BitacoraResumenTalonDetail;
import com.grupocastores.bitacoras.resumen.DTO.BitacoraResumenViajesCustom;
import com.grupocastores.bitacoras.resumen.DTO.BitacoraViajesDetalleVales;
import com.grupocastores.bitacoras.resumen.DTO.EstatusUnidadBitacoraResumen;
import com.grupocastores.bitacoras.resumen.DTO.HorarioOperador;
import com.grupocastores.bitacoras.resumen.DTO.Moneda;
import com.grupocastores.bitacoras.resumen.service.domain.BitacoraResumenViajesNegociacion;
import com.grupocastores.bitacoras.resumen.service.domain.Esquemasdocumentacion;
import com.grupocastores.bitacoras.resumen.service.domain.Ruta;


@Repository
public class BitacoraRepository{
    
    @Autowired
    private UtilitiesRepository utilitiesRepository;
    
    @PersistenceContext
    private EntityManager entityManager;
    
    static final String queryFilterResumenViaje =" SELECT * FROM OPENQUERY( %s , '"
            + " SELECT "
            + "  tv.idviaje, "
            + "  tv.folio, "
            + "  tv.idruta, "
            + "  tv.idunidad, "
            + "  cur.unidad AS unidadurea, "
            + "  cabi.idunidad AS unidaddiesel, "
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
            + "  INNER JOIN talones.guiaviaje tgv "
            + "    ON tv.idviaje = tgv.idviaje "
            + "  INNER JOIN talones.guias tg "
            + "    ON tgv.no_guia = tg.no_guia "
            + "  INNER JOIN talones.tg%s tgma "
            + "    ON tg.no_guia = tgma.no_guia "
            + "  INNER JOIN talones.talones tt "
            + "    ON tgma.cla_talon = tt.cla_talon "
            + "  INNER JOIN talones.especificacion_talon tet "
            + "    ON tgma.cla_talon = tet.cla_talon "
            + "  INNER JOIN camiones.camiones cc "
            + "    ON tv.idunidad = cc.unidad "
            + "  LEFT JOIN camiones.unidades_urea cur "
            + "    ON cc.unidad = cur.unidad "
            + "  LEFT JOIN camiones.bitacora cabi "
            + "    ON cc.unidad = cabi.idunidad "
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
            + " WHERE  %s tv.idCliente = %s AND tv.idOficinacliente = \"%s\"  GROUP BY tv.idviaje; ');";
    
    static final String queryGetNegociacion = 
            "SELECT *FROM OPENQUERY(%s, 'SELECT nc.id_negociacion_cliente, nc.id_negociacion, n.desc_negociacion FROM bitacorasinhouse.negociaciones_clientes nc INNER JOIN bitacorasinhouse.negociaciones n ON n.id_negociacion = nc.id_negociacion WHERE id_negociacion_cliente = %s;');";
    
    static final String queryGetEsquema =
            "SELECT *FROM OPENQUERY(%s, 'SELECT id_esquema, nombre_esquema, estatus FROM bitacorasinhouse.esquemas WHERE estatus =1 AND id_esquema = %s');";
    
    static final String queryGetEstatusunidad =
            "SELECT *FROM OPENQUERY(%s, 'SELECT cb.idunidad, ce.nombre   FROM camiones.bitacora cb INNER JOIN camiones.estatus ce ON cb.estatusunidad = ce.idstatus WHERE idunidad = %s');";
    
    static final String queryGetRuta =
            "SELECT *FROM OPENQUERY(%s, 'SELECT * FROM talones.ruta WHERE idruta = %s');";
    
    static final String queryGetGuiasByViaje =
            "SELECT *FROM OPENQUERY(%s, 'SELECT * FROM talones.ruta WHERE idruta = %s');";
    
    static final String queryGetTalonDetail =
            "SELECT ofi.*, cdo.ciudadorigen, cdo.estadoorigen, cdd.ciudaddestino, cdd.estadodestino "
            + "FROM OPENQUERY(%s, 'SELECT "
            + "  t.cla_talon, "
            + "  tip.nombre AS tipotalon, "
            + "  pag.nombre AS tipopago, "
            + "  t.rfcorigen, "
            + "  t.nomorigen, "
            + "  t.calleorigen, "
            + "  t.coloniaorigen, "
            + "  t.cporigen, "
            + "  t.telorigen, "
            + "  t.serecogera, "
            + "  t.rfcdestino, "
            + "  t.nomdestino, "
            + "  t.calledestino, "
            + "  t.coloniadestino, "
            + "  t.cpdestino, "
            + "  t.teldestino, "
            + "  t.seentregara, "
            + "  t.suma_flete  AS sumaflete, "
            + "  t.importeseguro, "
            + "  t.casetas, "
            + "  t.otroscargos, "
            + "  t.revac, "
            + "  t.completo, "
            + "  t.recoleccion, "
            + "  t.entrega,  "
            + "  t.maniobras,  "
            + "  t.ferry, "
            + "  t.gps,  "
            + "  t.importesubtotal,  "
            + "  t.importeiva,  "
            + "  t.importeiva_ret AS importeivaret,  "
            + "  t.otras_lineas AS otraslineas,  "
            + "  t.importetotal,  "
            + "  t.val_decl AS valdecl, "
            + "  deco.empaque, "
            + "  co.etiquetas, "
            + "  co.bultos, "
            + "  deco.que_contiene AS quecontiene, "
            + "  com.cons_ccp   AS consccp, "
            + "  com.cantidad, "
            + "  com.claveunidadpeso, "
            + "  com.descripcion_unidad AS descripcionunidad, "
            + "  com.claveprodservcp, "
            + "  com.descripcion_claveprod AS descripcionclaveprod, "
            + "  com.peso, "
            + "  com.materialpeligroso, "
            + "  com.cvematerialpeligroso, "
            + "  com.tipoembalaje, "
            + "  com.descripcion_embalaje, "
            + "  com.valormercancia,"
            + "  t.remision, "
            + "  t.ubicacion, "
            + "  cf.fecha, "
            + "  cf.estatus,  "
            + "  t.cdorigen, "
            + "  t.cddestino, "
            + "  IF(tcs.servicio_full IS NULL, \"NA\", tcs.servicio_full) AS servicio_full, "
            + "  IF(tcs.servicio_contact_center IS NULL, \"NA\", tcs.servicio_contact_center) AS servicio_contact_center, "
            + "  IF(tcs.id_clasificacion_completos IS NULL, 0, tcs.id_clasificacion_completos) AS id_clasificacion_completos, "
            + "  IF(cc.nombre IS NULL, \"NA\", cc.nombre) AS nombre_clasificacion "
            + " FROM "
            + "  talones.tr%S t "
            + "  INNER JOIN talones.co%S co  "
            + "    ON t.cla_talon = co.cla_talon "
            + "  INNER JOIN talones.detaco deco  "
            + "    ON t.cla_talon = deco.cla_talon "
            + "  INNER JOIN cfdinomina.complementocp_mercancia com  "
            + "    ON t.cla_talon = com.idper_fac "
            + "  INNER JOIN talones.tipotalon tip"
            + "    ON t.tp_dc = tip.idtipotalon "
            + "  INNER JOIN talones.tipopago pag "
            + "    ON t.tipopago = pag.idtipopago "
            + "  INNER JOIN cfdinomina.cfdi cf "
            + "    ON t.cla_talon = cf.idper_fac "
            + "  LEFT JOIN talones.talones_completos_servicio tcs "
            + "    ON t.cla_talon = tcs.cla_talon "
            + "  LEFT JOIN talones.clasificacion_completos cc "
            + "    ON tcs.id_clasificacion_completos = cc.id_clasificacion_completos "
            + " WHERE t.cla_talon =\"%s\";') AS ofi "
            + "LEFT JOIN OPENQUERY(%s, 'SELECT "
            + "  idciudad, "
            + "  cdo.nombre AS ciudadorigen, "
            + "  ceo.nombre AS estadoorigen "
            + " FROM "
            + "  camiones.ciudades_completos cdo "
            + "  INNER JOIN camiones.estados ceo "
            + "    ON cdo.idestado = ceo.idestado;') AS cdo ON ofi.cdorigen = cdo.idciudad "
            + "LEFT JOIN OPENQUERY(%s, 'SELECT "
            + "  idciudad, "
            + "  cdd.nombre AS ciudaddestino, "
            + "  ced.nombre AS estadodestino "
            + " FROM "
            + "  camiones.ciudades_completos cdd "
            + "  INNER JOIN camiones.estados ced "
            + "    ON cdd.idestado = ced.idestado;') AS cdd ON ofi.cddestino = cdd.idciudad ";
    
    static final String queryGetMoneda = 
            "SELECT * FROM moneda where id_moneda = %s";
    
    static final String queryGetidParentRuta =
            "SELECT *FROM OPENQUERY(%s, 'SELECT bbv.id_viaje FROM bitacorasinhouse.bitacora_viajes bbv WHERE bbv.cla_talon != \"\" AND bbv.cla_talon != \"null\" AND cla_talon =\"%s\"');";
    
    static final String queryGetTablaVale =
            "SELECT *FROM OPENQUERY(%s, 'SELECT  v.idVale, v.tabla FROM camiones.vales v WHERE folioviaje = \"%S\" ');";
    
    static final String queryGetDetalleVale =
            "SELECT *FROM OPENQUERY(%s, 'SELECT v.idVale AS idvale, v.cantidad, v.cantidadRecibida AS cantidadrecibida, v.cantidadRecibidaG AS cantidadrecibidag, v.observaciones,  cati.nombre AS tipopago, cag.nombre AS tipogasto, v.fecha FROM camiones.vales%s v INNER JOIN camiones.tipopago cati ON  v.idTipoPago =cati.idTipoPago INNER JOIN camiones.tipogasto cag ON v.idgasto =  cag.idgasto  WHERE v.idVale = \"%S\"');";
    
    
    static final String queryGetAsistencias = 
            " SELECT * FROM OPENQUERY(%s, 'SELECT  "
            + " p.idpersonal,  "
            + " CONCAT(p.apepaterno,\" \", p.apematerno,\" \", p.nombre) AS operador,  "
            + " DATE(fecha_hora ) AS fecha,  "
            + " TIME(fecha_hora ) AS hora   "
            + " FROM pol.checador_operadores ch "
            + " INNER JOIN personal.personal p ON ch.id_personal = p.idpersonal  "
            + " WHERE DATE(fecha_hora) BETWEEN  \"%s\" AND \"%s\"  %s ;');";

    static final String queryGetHorariosOperador =
            " SELECT *FROM OPENQUERY(%s, ' SELECT  "
            + "os.idunidad,  "
            + "os.tipounidad,  "
            + "os.idoperador,  "
            + "os.idesquemapago,  "
            + "ep.nombre AS esquemapago,  "
            + "os.tipooperador,  "
            + "os.ordenoperador,  "
            + "os.horaentrada,  "
            + "os.horasalida,  "
            + "os.fechamod,  "
            + "os.horamod,  "
            + "os.idpersonalmod,  "
            + "os.idoperadoresunidad,  "
            + "o.nombre, "
            + "os.estatus "
            + "FROM bitacorasinhouse.operadores_secundarios_unidad os  "
            + "INNER JOIN bitacorasinhouse.esquemas_pago ep ON os.idesquemapago = ep.idesquemapago  "
            + "INNER JOIN camiones.operadores o ON os.idoperador = o.idpersonal "
            + " WHERE %s os.estatus = 1 ');";
    
    

    static final String queryGetUnidadesCliente = 
            "SELECT a.*, c.id_cliente_inhouse, c.alias_inhouse, c.rfc, o.horaentrada, o.horasalida, o.idesquemapago, o.nombre esquema FROM OPENQUERY (%s, 'SELECT u.idunidad, u.unidad, u.tipounidad, u.noeconomico, u.modelo, u.placas, c.marca, u.idoperador, o.idusuario, o.nombre, ur.idcliente, ur.idoficina, ur.cliente, ur.complementocliente, ur.observacion FROM monitoreo.unidades_renta ur LEFT JOIN camiones.unidades u ON ur.noeconomico = u.noeconomico LEFT JOIN camiones.camiones c ON u.unidad = c.unidad AND u.noeconomico = c.noeconomico AND u.idoperador = c.operador LEFT JOIN camiones.operadores o ON u.idoperador = o.idpersonal AND o.status = 1 WHERE u.estatus = 1 AND ur.estatus = 1 AND u.tipounidad = %s ') AS a " + 
            "INNER JOIN OPENQUERY(%s, 'SELECT ci.id_cliente_inhouse, ci.alias_inhouse, b.rfc, c.idcliente FROM bitacorasinhouse.clientes_inhouse ci INNER JOIN bitacorasinhouse.clientes_inhouse_clientes2009 b ON ci.id_cliente_inhouse = b.id_cliente_inhouse INNER JOIN clientes.clientes2009 c ON b.id_cliente = c.idcliente AND b.rfc = c.rfc WHERE b.estatus = 1 AND c.status = 1 AND ci.id_cliente_inhouse = %s GROUP BY ci.alias_inhouse;') AS c ON /*a.idcliente = c.idcliente AND */a.complementocliente = c.alias_inhouse " + 
            "LEFT JOIN OPENQUERY(%s, 'SELECT op.idoperador, op.idunidad, op.horaentrada, op.horasalida, op.idesquemapago, ep.nombre FROM bitacorasinhouse.operadores_secundarios_unidad op INNER JOIN bitacorasinhouse.esquemas_pago ep ON op.idesquemapago = ep.idesquemapago WHERE op.estatus = 1 AND op.tipooperador = 1 AND ep.estatus = 1;') AS o ON a.idoperador = o.idoperador AND a.idunidad = o.idunidad;";
    
    
    static final String queryGetOperadoresAsignados = 
            "SELECT a.*, b.nombre FROM OPENQUERY (%s, 'SELECT os.idunidad, os.tipounidad, os.idoperador, os.idesquemapago, ep.nombre esquemaPago, os.tipooperador, os.ordenoperador, os.horaentrada, os.horasalida, os.fechamod, os.horamod, os.idpersonalmod, os.idoperadoresunidad FROM bitacorasinhouse.operadores_secundarios_unidad os INNER JOIN bitacorasinhouse.esquemas_pago ep ON os.idesquemapago = ep.idesquemapago WHERE os.idunidad = \"%s\" AND os.estatus = 1;') AS a LEFT JOIN OPENQUERY(%s, 'SELECT idpersonal, nombre FROM camiones.operadores;') AS b ON a.idoperador = b.idpersonal ORDER BY 7;";
    
    
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
            int estatusViaje, int idEsquema, int idNegociacion, int idCliente,String idOficinaCliente, String tabla, String linkedServer) {
      
        String queryWherePart = "";
      
        
        if( idViaje.equals("0") && noEconomico.equals("0") ) {
 
            queryWherePart = queryWherePart + " ( tv.fechaviaje BETWEEN \""+fechaInicio+"\" AND \""+fechaFin+"\")  ";
            if(tipoUnidad != 0) {            
                queryWherePart = queryWherePart + " AND tv.tipounidad = "+tipoUnidad+" ";
            }
 
            if(estatusViaje != 0) {
                queryWherePart = queryWherePart +" AND tv.estatus = "+estatusViaje+" ";
            }else {
                queryWherePart = queryWherePart +" AND tv.estatus NOT IN(5) ";
            }
            
            if(idEsquema != 0) {
                queryWherePart = queryWherePart +" AND tet.idesquema = "+idEsquema+" ";
            }
            if(idNegociacion != 0) {
                queryWherePart = queryWherePart +" AND tet.idnegociacion = "+idNegociacion+" ";
            }
            
        }
        
        if( !idViaje.equals("0") ) {
            queryWherePart = queryWherePart + " tv.folio = \""+idViaje+"\" AND ";
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
                tabla,
                queryWherePart,
                idCliente,
                idOficinaCliente),
                BitacoraResumenViajesCustom.class
            );
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
    public EstatusUnidadBitacoraResumen getEstatusUnidad( int idunidad) {
        Query query = entityManager.createNativeQuery(String.format(
                queryGetEstatusunidad,
                utilitiesRepository.getDb13(),
                idunidad),
                EstatusUnidadBitacoraResumen.class
            );
        
        EstatusUnidadBitacoraResumen estatus = (EstatusUnidadBitacoraResumen) query.getResultList().get(0);
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
    
    /**
     * detTalonDetail: Servicio para obtener el detalle del resumen de talones.
     * 
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra]
     * @return List<TalonCustomResponse>
     * @date 2022-12-13
     */
    public List<BitacoraResumenTalonDetail> getTalonDetail(String tabla, String claTalon, String linkedServer) {
        Query query = entityManager.createNativeQuery(String.format(
                queryGetTalonDetail,
                linkedServer,
                tabla,
                tabla,
                claTalon,
                utilitiesRepository.getDb13(),
                utilitiesRepository.getDb13()),
                BitacoraResumenTalonDetail.class
            );
        
        return (List<BitacoraResumenTalonDetail>) query.getResultList();
        
    }
    
    /**
     * getMoneda: Obtiene datos del tipo de moneda por su id.
     * 
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra]
     * @return Moneda
     * @date 2022-12-14
     */
    public List<Moneda> getMoneda(int idMoneda) {
        Query query = entityManager.createNativeQuery(String.format(
                queryGetMoneda,
                idMoneda),
                Moneda.class
            );
        
        return  query.getResultList();
        
    }
    
    /**
     * getMoneda: Obtiene el idparent de un viaje de la tabla de solicitud de viaje.
     * 
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra]
     * @return int
     * @date 2022-12-19
     */
    public int getParentRuta(String claTalon) {
        Query query = entityManager.createNativeQuery(String.format(
                queryGetidParentRuta,
                utilitiesRepository.getDb23(),
                claTalon
                )
                
            );
        
        if( query.getResultList().isEmpty()) 
            return 0;
        
        return (int) query.getResultList().get(0);

    }
    
    /**
     * getVales: Servicio para obtener la tabla(mes anio) del vale por folio de viaje.
     * 
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra]
     * @return List<Object[]>
     * @date 2022-12-19
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> getTablaVales(String folioviaje) {
        Query query = entityManager.createNativeQuery(String.format(
                queryGetTablaVale,
                utilitiesRepository.getDb13(),
                folioviaje)
               
            );
        
        return (List<Object[]>) query.getResultList();
        
        
    }
    
    /**
     * getVales: Servicio para obtener los vales por idVale.
     * 
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra]
     * @return BitacoraResumenGuiaDetail
     * @date 2022-12-19
     */
    public BitacoraViajesDetalleVales getVales(String idVale, String tabla) {
        try {
            
            Query query = entityManager.createNativeQuery(String.format(
                    queryGetDetalleVale,
                    utilitiesRepository.getDb13(),
                    tabla,
                    idVale),
                    BitacoraViajesDetalleVales.class
                    
                    );
           
            return (BitacoraViajesDetalleVales) query.getResultList().get(0);
        } catch (Exception e) {
            return null;
            
        }     
    }   
   
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    @SuppressWarnings("unchecked")
    public List<AsistenciaOperadorDTO> filterAsistencias(String fechaInicio, String fechaFinal) {
        
        String queryWherePart = "";
        
         Query query = entityManager.createNativeQuery(String.format(
                    queryGetAsistencias,
                    utilitiesRepository.getDb13(),
                    fechaInicio,
                    fechaFinal,
                    queryWherePart),
                 AsistenciaOperadorDTO.class
                );
            List<AsistenciaOperadorDTO> list = query.getResultList();
            return list;     
    }
    
    public List<AsistenciaOperadorDTO> filterAsistencias(String fechaInicio, String fechaFinal, int idOperador) {
        
        String queryWherePart = " AND idpersonal = "+idOperador+" ";
        
         Query query = entityManager.createNativeQuery(String.format(
                    queryGetAsistencias,
                    utilitiesRepository.getDb13(),
                    fechaInicio,
                    fechaFinal,
                    queryWherePart),
                 AsistenciaOperadorDTO.class
                );
            List<AsistenciaOperadorDTO> list = query.getResultList();
            return list;
        
    }

    public List<HorarioOperador> filterHorario(int idunidad, int tipoOperador, int idOperador) {
        String queryWherePart = "";
       
        if( idunidad != 0)
            queryWherePart = queryWherePart+"  os.idunidad = "+idunidad+" AND ";
        
        if( tipoOperador != 0)
             queryWherePart = queryWherePart+"  os.tipooperador = "+tipoOperador+" AND ";
        
        if( idOperador!= 0)
            queryWherePart = queryWherePart+"  os.idoperador = "+idOperador+" AND ";
        
        Query query = entityManager.createNativeQuery(String.format(
                   queryGetHorariosOperador,
                   utilitiesRepository.getDb23(),
                   queryWherePart
                   ),
                HorarioOperador.class
               );
           List<HorarioOperador> list = query.getResultList();
           return list;
    }

    /**
     * getUnidadesCliente: Obtiene las unidades de renta de un cliente
     * 
     * @param idClienteInhouse (int)
     * @return List<Object[]>
     * @author Cynthia Fuentes Amaro
     * @param linkedServer 
     * @param idTipoUnidad 
     * @date 2022-10-28
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> getUnidadesCliente(int idClienteInhouse, int idTipoUnidad, String linkedServer) {
        Query query = entityManager
                .createNativeQuery(String.format(queryGetUnidadesCliente, utilitiesRepository.getDb13(), idTipoUnidad, linkedServer, idClienteInhouse, linkedServer));

        return query.getResultList();

    }
    
    /**
     * getOperadoresAsignados: Obtiene los datos de los operadores asignados
     * 
     * @param idUnidad (int)
     * @return List<Object[]>
     * @author Cynthia Fuentes Amaro
     * @date 2022-11-01
     */
    @SuppressWarnings("unchecked")
    public List<Object[]> getOperadoresAsignados(int idUnidad) {
        Query query = entityManager
                .createNativeQuery(String.format(queryGetOperadoresAsignados,utilitiesRepository.getDb23(),  idUnidad, utilitiesRepository.getDb13()));

        return query.getResultList();

    }
}
