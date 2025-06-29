package com.uy.enRutaBackend.services;

import java.io.IOException;
import java.math.BigDecimal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.uy.enRutaBackend.datatypes.DtPaypal;
import com.uy.enRutaBackend.icontrollers.IServiceIntegracionPaypal;

import okhttp3.Credentials;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class IntegracionPaypalService implements IServiceIntegracionPaypal {
	@Value("${payPal.properties.id}")
	private String payPalId;
	@Value("${payPal.properties.currency}")
	private String payPalCurrency;
	@Value("${payPal.properties.intent}")
	private String payPalIntent;
	@Value("${paypal.properties.secret}")
	private String payPalSecret;
	@Value("${paypal.api.base-url}")
	private String baseUrl;

	public String obtenerTokenDeAcceso() throws IOException {
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create("grant_type=client_credentials",
                MediaType.get("application/x-www-form-urlencoded"));

        Request request = new Request.Builder()
                .url(baseUrl + "/v1/oauth2/token")
                .post(body)
                .header("Authorization", Credentials.basic(payPalId, payPalSecret))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                return new JSONObject(response.body().string()).getString("access_token");
            } else {
                throw new RuntimeException("Error obteniendo token PayPal");
            }
        }
    }
	
	public DtPaypal crearOrdenDePago(BigDecimal amount, String urlRedir, int idVenta) throws IOException {
		
	    String accessToken = new String();
		try {
			accessToken = obtenerTokenDeAcceso();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    
	    OkHttpClient client = new OkHttpClient();
	    JSONObject jsonRequest = new JSONObject();
	    jsonRequest.put("intent", payPalIntent);
	    jsonRequest.put("purchase_units", new JSONArray().put(new JSONObject().put("amount", new JSONObject()
	            .put("currency_code", payPalCurrency)
	            .put("value", amount.toString()))));
	    
	    
	    JSONObject appContext = new JSONObject(); 
	    appContext.put("return_url", urlRedir +"?id_venta="+ idVenta);
	    jsonRequest.put("application_context", appContext);

	    System.out.println(jsonRequest);
	    
	    RequestBody body = RequestBody.create(jsonRequest.toString(), MediaType.get("application/json"));

	    Request request = new Request.Builder()
	            .url(baseUrl + "/v2/checkout/orders")
	            .post(body)
	            .header("Authorization", "Bearer " + accessToken)
	            .header("Content-Type", "application/json")
	            .build();

	    try (Response response = client.newCall(request).execute()) {    	
	        if (response.isSuccessful()) {
	        	DtPaypal paypalDt = crearDtPaypal(response);
	            return paypalDt;
	        } else {
	            throw new RuntimeException("Error creando orden de pago");
	        }
	    }
	}
	
	
	private DtPaypal crearDtPaypal(Response response) throws JSONException, IOException {
		DtPaypal dt = new DtPaypal();
		
		String responseBody = response.body().string();
		System.out.println(responseBody);
		dt.setId_orden(new JSONObject(responseBody).getString("id"));
		JSONArray linksArray = new JSONObject(responseBody).getJSONArray("links");
		String approvalLink = null;

		for (int i = 0; i < linksArray.length(); i++) {
		    JSONObject link = linksArray.getJSONObject(i);
		    if ("approve".equals(link.getString("rel"))) {
		        approvalLink = link.getString("href");
		        break; 
		    }
		}

		dt.setUrlPago(approvalLink);
		System.out.println("URL de pago: " + approvalLink);

		return dt;
	}

	public String capturePayment(String orderId) throws IOException {
	    String accessToken = obtenerTokenDeAcceso();
	    Request request = new Request.Builder()
	            .url(baseUrl + "/v2/checkout/orders/" + orderId + "/capture")
	            .post(RequestBody.create("", null))
	            .header("Authorization", "Bearer " + accessToken)
	            .header("Content-Type", "application/json")
	            .build();
	    OkHttpClient client = new OkHttpClient();
	    try (Response response = client.newCall(request).execute()) {
	        if (response.isSuccessful()) {
	        	String responseBody = response.body().string();
	    		System.out.println(responseBody);
	        	return "OK";
	        } else {
	            throw new RuntimeException("Error capturando pago");
	        }
	    }
	}
}
