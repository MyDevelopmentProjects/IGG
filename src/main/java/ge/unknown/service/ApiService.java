package ge.unknown.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import ge.unknown.DTO.PolicySuccessDTO;
import ge.unknown.DTO.Transaction;
import ge.unknown.data.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.HashMap;

import static ge.unknown.utils.constants.Constants.BASE_URL;

@Service
@Slf4j
public class ApiService {

    public HashMap get_transaction_result(String transId) {
        try {
            transId = transId.replaceAll("/", "_");

            HttpResponse<String> response = Unirest.post("http://178.128.198.46:8090/index.php/check?transId=" + URLEncoder.encode(transId, "UTF-8"))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("cache-control", "no-cache")
                    .header("Postman-Token", "51e8d8e2-1110-4857-b50e-2c270b5106d2")
                    .asString();

            System.out.println("SENDING CHECK TRANSACTION CALL TO TBC ------ " + "http://localhost:8090/index.php/check/" + URLEncoder.encode(transId, "UTF-8"));
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.serializeNulls().create();
            System.out.println("ANSWER RECEIVED FROM TBC SERVICE ------ " + response.getBody());
            return gson.fromJson(response.getBody(), HashMap.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new HashMap();
    }

    public PolicySuccessDTO sendJson(String json) {
        try {

            System.out.println("JSON DATA TO BE SENT TO DATO'S SERVICE ------ " + json);

            HttpResponse response = Unirest.post(BASE_URL.replaceAll("data=", ""))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .body("data=" + URLEncoder.encode(json))
                    .asString();

            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.serializeNulls().create();
            System.out.println("ANSWER RECEIVED FROM DATO'S SERVICE ------ " + response.getBody().toString());
            return gson.fromJson(response.getBody().toString(), PolicySuccessDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return PolicySuccessDTO.builder().result(0).build();
    }

    public PolicySuccessDTO send(Object o) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            HttpResponse response = Unirest.post(BASE_URL.replaceAll("data=", ""))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .body("data=" + URLEncoder.encode(mapper.writeValueAsString(o)))
                    .asString();

            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.serializeNulls().create();
            return gson.fromJson(response.getBody().toString(), PolicySuccessDTO.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return PolicySuccessDTO.builder().result(0).build();
    }

    public Transaction createTransaction(Double amt) {
        try {
            String _amt = String.valueOf(amt) + "0";
            if (_amt.startsWith("0")) {
                _amt = _amt.substring(_amt.indexOf('.') + 1);
            } else {
                _amt = _amt.replaceAll("\\.", "");
            }
            HttpResponse response = Unirest.get("http://178.128.198.46:8090/index.php/pay/" + _amt).asJson();
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.serializeNulls().create();
            return gson.fromJson(response.getBody().toString(), Transaction.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Transaction.builder().transId("").build();
    }

    public String getschedule(String dt, String doctorId) {
        try {
            return Unirest.get(BASE_URL + URLEncoder.encode("{oper:\"getschedule\",fordate:\"" + dt + "\",doctorid:" + doctorId + "}", "UTF-8")).asJson().getBody().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public void syncProducts() {
        ProductsSyncResponse myObject = generateResp("syncproduct", ProductsSyncResponse.class);
        if (myObject != null) {
            AllProductsSingleton.getInstance().setProducts(myObject.getProducts());
        }
    }

    public void syncDoctors() {
        DoctorSyncResp myObject = generateResp("syncdoctor", DoctorSyncResp.class);
        if (myObject != null) {
            AllDoctorsSingleton.getInstance().setDoctors(myObject.getDoctors());
        }
    }

    private <T> T generateResp(String param, Class<T> tClass) {
        log.info("Fetching " + tClass.getCanonicalName());
        try {
            HttpResponse<JsonNode> request = Unirest.get(BASE_URL + URLEncoder.encode("{\"oper\":\"" + param + "\"}", "UTF-8")).asJson();
            String responseJSONString = request.getBody().toString();
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.serializeNulls().create();
            return gson.fromJson(responseJSONString, tClass);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Failed to fetch " + tClass.getCanonicalName());
        }
        return null;
    }

}
