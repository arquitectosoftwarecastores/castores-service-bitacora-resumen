package com.grupocastores.bitacoras.resumen.service;

import java.util.List;

import com.grupocastores.commons.inhouse.BitacoraResumenViajesCustom;
import com.grupocastores.commons.inhouse.BitacoraResumenViajesDetail;


public interface IBitacoraService {
    
    /**
     * filterViajes: Servicio para filtrar viajes.
     * 
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra]
     * @date 2022-12-06
     */
    public List<BitacoraResumenViajesCustom> filterViajes(String fechaInicio, String fechaFin, String idViaje, String noEconomico, int tipoUnidad,
            int estatusViaje, int idEsquema, int idNegociacion, int idClienteinhouse,String idOficinaCliente, String idoficinaDocumenta);
    
    /**
     * getDetalleViaje: Servicio para obtener el detalle del resumen de viaje.
     * 
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra]
     * @date 2022-12-06
     */
    public BitacoraResumenViajesDetail getDetalleViaje(int idNegociacion, int idEsquemaViaje, int idRuta,
            int idCliente, String idOficinaCliente, String idoficinaDocumenta, int idUnidad, int noEconomico);
    }
