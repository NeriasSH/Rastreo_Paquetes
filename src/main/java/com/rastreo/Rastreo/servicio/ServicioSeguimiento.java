package com.rastreo.Rastreo.servicio;


import com.rastreo.Rastreo.modelo.SeguimientoPaquete;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ServicioSeguimiento {
    private static final String URL_API_BASE = "https://api.trackingmore.com/v4/trackings/get";
    private static final String API_KEY = "rzttynnz-pw4m-mx7b-p0y9-que4sivkvklp";

    private final RestTemplate restTemplate;

    public ServicioSeguimiento() {
        this.restTemplate = new RestTemplate();
    }

    private List<SeguimientoPaquete> mapearRespuestaASeguimientos(Map respuesta) {
        List<SeguimientoPaquete> seguimientos = new ArrayList<>();

        List<Map<String, Object>> datos = (List<Map<String, Object>>) respuesta.get("data");

        if (datos != null) {
            datos.forEach(datoSeguimiento -> {
                SeguimientoPaquete seguimiento = new SeguimientoPaquete();
                seguimiento.setId(String.valueOf(datoSeguimiento.get("id")));
                seguimiento.setNumeroSeguimiento(String.valueOf(datoSeguimiento.get("tracking_number")));
                seguimiento.setCodigoTransportista(String.valueOf(datoSeguimiento.get("courier_code")));
                seguimiento.setNumeroOrden(String.valueOf(datoSeguimiento.get("order_number")));
                seguimiento.setEstadoEntrega(String.valueOf(datoSeguimiento.get("delivery_status")));

                seguimientos.add(seguimiento);
            });
        }

        return seguimientos;
    }

    public List<SeguimientoPaquete> obtenerSeguimientosPorTracking(String trackingNumber) {
        String urlCompleta = URL_API_BASE + "?tracking_numbers=" + trackingNumber;

        HttpHeaders encabezados = new HttpHeaders();
        encabezados.set("Accept", "application/json");
        encabezados.set("Content-Type", "application/json");
        encabezados.set("Tracking-Api-Key", API_KEY);

        HttpEntity<String> entidad = new HttpEntity<>(encabezados);

        try {
            ResponseEntity<Map> respuesta = restTemplate.exchange(
                    urlCompleta,
                    HttpMethod.GET,
                    entidad,
                    Map.class
            );

            return mapearRespuestaASeguimientos(respuesta.getBody());

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<SeguimientoPaquete> obtenerSeguimientosPorCourier(String courierCode) {
        String urlCompleta = URL_API_BASE + "?courier_code=" + courierCode;

        HttpHeaders encabezados = new HttpHeaders();
        encabezados.set("Accept", "application/json");
        encabezados.set("Content-Type", "application/json");
        encabezados.set("Tracking-Api-Key", API_KEY);

        HttpEntity<String> entidad = new HttpEntity<>(encabezados);

        try {
            ResponseEntity<Map> respuesta = restTemplate.exchange(
                    urlCompleta,
                    HttpMethod.GET,
                    entidad,
                    Map.class
            );

            return mapearRespuestaASeguimientos(respuesta.getBody());

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<SeguimientoPaquete> obtenerSeguimientos() {
        HttpHeaders encabezados = new HttpHeaders();
        encabezados.set("Accept", "application/json");
        encabezados.set("Content-Type", "application/json");
        encabezados.set("Tracking-Api-Key", API_KEY);

        HttpEntity<String> entidad = new HttpEntity<>(encabezados);

        try {
            ResponseEntity<Map> respuesta = restTemplate.exchange(
                    URL_API_BASE,
                    HttpMethod.GET,
                    entidad,
                    Map.class
            );

            return mapearRespuestaASeguimientos(respuesta.getBody());

        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}