package com.grupocastores.bitacoras.resumen.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.grupocastores.bitacoras.resumen.DTO.AsistenciaOperadorDTO;
import com.grupocastores.bitacoras.resumen.DTO.BitacoraResumenGuiaDetail;
import com.grupocastores.bitacoras.resumen.DTO.BitacoraResumenTalonDetail;
import com.grupocastores.bitacoras.resumen.DTO.BitacoraResumenViajesCustom;
import com.grupocastores.bitacoras.resumen.DTO.BitacoraResumenViajesDetail;
import com.grupocastores.bitacoras.resumen.DTO.BitacoraViajesDetalleVales;
import com.grupocastores.bitacoras.resumen.DTO.BitacoraViajesRequestDetail;
import com.grupocastores.bitacoras.resumen.DTO.HorarioOperador;
import com.grupocastores.bitacoras.resumen.DTO.IncidenciasDTO;
import com.grupocastores.bitacoras.resumen.DTO.OperadoresSecundariosRequest;
import com.grupocastores.bitacoras.resumen.DTO.TalonCustomResponse;
import com.grupocastores.bitacoras.resumen.DTO.UnidadOperadorRequest;
import com.grupocastores.bitacoras.resumen.service.IBitacoraService;


@RestController
@RequestMapping(value ="/viajes")
public class BitacorasController {
    
    @Autowired
    IBitacoraService bitacoraService;
    
    /**
     * filterViajes: Servicio para filtrar viajes.
     * 
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra]
     * @return List<BitacoraResumenViajesCustom>
     * @date 2022-12-06
     */
    @RequestMapping("/filterViajes/{fechaInicio}/{fechaFin}/{idViaje}/{noEconomico}/{tipoUnidad}/{estatusViaje}/{idEsquema}/{idNegociacion}/{idCliente}/{idOficinaCliente}/{idoficinaDocumenta}")
    public ResponseEntity<List<BitacoraResumenViajesCustom>> filterViajes(
            @PathVariable("fechaInicio") String fechaInicio, 
            @PathVariable("fechaFin") String fechaFin,
            @PathVariable("idViaje") String idViaje,
            @PathVariable("noEconomico") String noEconomico,
            @PathVariable("tipoUnidad") int tipoUnidad,
            @PathVariable("estatusViaje") int estatusViaje,
            @PathVariable("idEsquema") int idEsquema,
            @PathVariable("idNegociacion") int idNegociacion,
            @PathVariable("idCliente") int idCliente,
            @PathVariable("idOficinaCliente") String idOficinaCliente,
            @PathVariable("idoficinaDocumenta") String idoficinaDocumenta
          
            ) throws Exception{
        List<BitacoraResumenViajesCustom> response = bitacoraService.filterViajes(fechaInicio, fechaFin, idViaje, noEconomico, tipoUnidad, estatusViaje, idEsquema, idNegociacion, idCliente, idOficinaCliente, idoficinaDocumenta);
       
        
        return ResponseEntity.ok(response);
        
    } 
    
    /**
     * getDetalleViaje: Servicio para obtener el detalle del resumen de viaje.
     * 
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra]
     * @return BitacoraResumenViajesDetail
     * @date 2022-12-06
     */
    @GetMapping("/getDetalleViaje/{idNegociacion}/{idEsquemaViaje}/{idRuta}/{idCliente}/{idOficinaCliente}/{idoficinaDocumenta}/{idUnidad}/{noEconomico}")
    public ResponseEntity<BitacoraResumenViajesDetail> getDetalleViaje(
            @PathVariable("idNegociacion") int idNegociacion,
            @PathVariable("idEsquemaViaje") int idEsquemaViaje,
            @PathVariable("idRuta") int idRuta,
            @PathVariable("idCliente") int idCliente,
            @PathVariable("idOficinaCliente") String idOficinaCliente,
            @PathVariable("idoficinaDocumenta") String idoficinaDocumenta,
            @PathVariable("idUnidad") int idUnidad,
            @PathVariable("noEconomico") int noEconomico) throws Exception{
        BitacoraResumenViajesDetail response = bitacoraService.getDetalleViaje(idNegociacion, idEsquemaViaje, idRuta,  idCliente, idOficinaCliente, idoficinaDocumenta, idUnidad, noEconomico);
       
        
        return ResponseEntity.ok(response);
        
    } 
    
    /**
     * getDetalleViaje: Servicio para obtener talones por viaje.
     * 
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra]
     * @return List<TalonCustomResponse>
     * @date 2022-12-06
     */
    @GetMapping("/getTalonesByViaje/{idoficinaDocumenta}/{idViaje}")
    public ResponseEntity<List<TalonCustomResponse>> getTalonesByViaje(    
            @PathVariable("idoficinaDocumenta") String idoficinaDocumenta,
            @PathVariable("idViaje") int idViaje) throws Exception{
        List<TalonCustomResponse> response = bitacoraService.getTalonesByViaje(idoficinaDocumenta, idViaje);
       
        
        return ResponseEntity.ok(response);
        
    } 
    
    /**
     * detTalonDetail: Servicio para obtener el detalle del resumen de talones.
     * 
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra]
     * @return List<TalonCustomResponse>
     * @date 2022-12-13
     */
    @GetMapping("/getDetalleTalon/{claTalon}/{idoficinaDocumenta}")
    public ResponseEntity<List<BitacoraResumenTalonDetail>> getTalonDetail(    
            @PathVariable("claTalon") String claTalon,
            @PathVariable("idoficinaDocumenta") String idoficinaDocumenta) throws Exception{
        List<BitacoraResumenTalonDetail> response = bitacoraService.getTalonDetail(claTalon, idoficinaDocumenta);
       
        
        return ResponseEntity.ok(response);
        
    } 
    
    /**
     * getDetalleGuia: Servicio para obtener el detalle de guia.
     * 
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra]
     * @return BitacoraResumenGuiaDetail>
     * @date 2022-12-13
     */
    @GetMapping("/getDetalleGuia/{noGuia}/{tabla}/{idoficinaDocumenta}")
    public ResponseEntity<BitacoraResumenGuiaDetail> getDetalleGuia(    
            @PathVariable("noGuia") String noGuia,
            @PathVariable("tabla") String tabla,
            @PathVariable("idoficinaDocumenta") String idoficinaDocumenta) throws Exception{
        BitacoraResumenGuiaDetail response = bitacoraService.getDetalleGuia(noGuia, tabla,  idoficinaDocumenta);
       
        
        return ResponseEntity.ok(response);
        
    } 
    
    /**
     * getDetalleRuta: Servicio para obtener el detalle de ruta.
     * 
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra]
     * @return BitacoraResumenGuiaDetail>
     * @date 2022-12-18
     */
    @GetMapping("/getDetalleRuta/{idViaje}/{idOficinaCliente}/{idoficinaDocumenta}")
    public ResponseEntity<List<BitacoraViajesRequestDetail>> getDetalleRuta(    
            @PathVariable("idViaje") int idViaje,
            @PathVariable("idOficinaCliente") String idOficinaCliente,
            @PathVariable("idoficinaDocumenta") String idoficinaDocumenta) throws Exception{
        List<BitacoraViajesRequestDetail> response = bitacoraService.getDetalleRuta(idViaje, idOficinaCliente,  idoficinaDocumenta);
       
        
        return ResponseEntity.ok(response);
        
    } 
    
    /**
     * getVales: Servicio para obtener los vales por folio.
     * 
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra]
     * @return BitacoraResumenGuiaDetail
     * @date 2022-12-19
     */
    @GetMapping("/getVales/{folioViaje}")
    public ResponseEntity<List<BitacoraViajesDetalleVales>> getValesByViaje(    
            @PathVariable("folioViaje") String folioViaje
           ) throws Exception{
        List<BitacoraViajesDetalleVales> response = bitacoraService.getVales(folioViaje);
       
        
        return ResponseEntity.ok(response);
        
    } 
    


    /**
     * obtenerInsidencias: Obtiene incidencias o evidencias por talon.
     * 
     * @param claTalon String
     * @param tipo int
     * @version 0.0.1
     * @author Oscar Eduardo Guerra Salcedo [OscarGuerra] 
     * @return Coordenada
     * @date 2022-09-26
     */

    @GetMapping("/obtenerIncidencias/{claTalon}/{tipo}")
    public ArrayList<IncidenciasDTO> obtenerInsidencias(
            @PathVariable("claTalon") String claTalon,
            @PathVariable("tipo") int tipo) {
        ArrayList<IncidenciasDTO> response = bitacoraService.obtenerIncidencias(claTalon, tipo);

        return response;      
        
     }
    
    
    
    
    
    
    
    
    
    /**
     * Consulta asistencias por fechas.
     *
     * @param fecha inicio
     * @param fecha final
     * @return el InhouseViajes
     */
    
    @GetMapping(value = "/filter/{fechaInicio}/{fechaFinal}")
    @ResponseBody
    public Collection<AsistenciaOperadorDTO> filterAsistencias(
            @PathVariable String fechaInicio, 
            @PathVariable String fechaFinal)  
    {
        
      List<AsistenciaOperadorDTO> listAsistenciaOperador = new ArrayList<>();  
      List<AsistenciaOperadorDTO> asistenciaOperador =  bitacoraService.filterAsistencias(fechaInicio, fechaFinal);
      if( asistenciaOperador.isEmpty() ) 
          return listAsistenciaOperador;
      
      int size = asistenciaOperador.size();
      for (int i = 0; i< size; i ++) {
          listAsistenciaOperador.add(asistenciaOperador.get(i));
      }
        return listAsistenciaOperador;
    }
    
    
    /**
     * Consulta asistencias por fechas y idoperador.
     *
     * @param fecha inicio
     * @param fecha final
     * @return el InhouseViajes
     */
    @GetMapping(value = "/filter/{fechaInicio}/{fechaFinal}/{idOperador}")
    @ResponseBody
    public Collection<AsistenciaOperadorDTO> filterAsistenciasByIdoperador(
            @PathVariable String fechaInicio, 
            @PathVariable String fechaFinal,
            @PathVariable int idOperador) 
    {
        List<AsistenciaOperadorDTO> listAsistenciaOperador = new ArrayList<>();  
        List<AsistenciaOperadorDTO> asistenciaOperador =  bitacoraService.filterAsistencias(fechaInicio, fechaFinal, idOperador);
        if( asistenciaOperador.isEmpty() ) 
            return listAsistenciaOperador;
        
        int size = asistenciaOperador.size();
        for (int i = 0; i< size; i ++) {
            listAsistenciaOperador.add(asistenciaOperador.get(i));
        }
          return listAsistenciaOperador;
    }
    
    /**
     * Consulta asistencias por fechas y idoperador.
     *
     * @param fecha inicio
     * @param fecha final
     * @return el InhouseViajes
     */
    @GetMapping(value = "/filterHorario/{idUnidad}/{tipoOperador}/{idOperador}")
    @ResponseBody
    public Collection<HorarioOperador> filterAsistenciasByIdoperador(
            @PathVariable int idUnidad, 
            @PathVariable int tipoOperador,
            @PathVariable int idOperador) 
    {
        List<HorarioOperador> listaHorarioOperador = new ArrayList<>();  
       List<HorarioOperador> horarioOperador = bitacoraService.filterHorario(idUnidad, tipoOperador, idOperador);
       if( horarioOperador.isEmpty() ) 
           return listaHorarioOperador;
       
       int size = horarioOperador.size();
       for (int i = 0; i< size; i ++) {
           listaHorarioOperador.add(horarioOperador.get(i));
       }
         return listaHorarioOperador;
    }
    
    /**
     * getUnidadesCliente: Obtiene los esquemas de pago del catálogo
     * 
     * @return List<EsquemasPago>
     * @author Cynthia Fuentes Amaro
     * @date 2023-05-26
     */
    @GetMapping(value = "/getUnidadesCliente/{idClienteInhouse}/{idTipoUnidad}")
    public ResponseEntity<List<UnidadOperadorRequest>> getUnidadesCliente(@PathVariable("idClienteInhouse") int idClienteInhouse, @PathVariable("idTipoUnidad") int idTipoUnidad) {
        List<UnidadOperadorRequest> lstUnidades = bitacoraService.getUnidadesCliente(idClienteInhouse, idTipoUnidad);
        if (lstUnidades == null || lstUnidades.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lstUnidades);
    }
    
    
    /**
     * getUnidadesCliente: Obtiene los esquemas de pago del catálogo
     * 
     * @return List<EsquemasPago>
     * @author Cynthia Fuentes Amaro
     * @date 2023-05-26
     */
    @GetMapping(value = {"/getOperadoresAsignados/{idUnidad}"})
    public ResponseEntity<List<OperadoresSecundariosRequest>> getOperadoresAsignados(@PathVariable("idUnidad") int idUnidad) {
        List<OperadoresSecundariosRequest> lstOperadores = bitacoraService.getOperadoresAsignados(idUnidad);
        if (lstOperadores == null || lstOperadores.isEmpty())
            return ResponseEntity.noContent().build();
        return ResponseEntity.ok(lstOperadores);
    }
    
   
   

      
}
