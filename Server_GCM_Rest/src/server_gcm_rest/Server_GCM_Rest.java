/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_gcm_rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

/**
 *
 * @author Eurismar
 */
public class Server_GCM_Rest {

    private static final String ID_DISPOSITIVO_GCM = "APA91bGVd_Ztbihd2iNnC9drgtS-PslKKML-x1oDRJiEIDQ95UeSMid9CYMaxV7mDfSeEpqfwsAs-LoOxK-shRzoZcnq7Sw_Rf1pHJC_x2rSrJp-VFg6c_CgTNwf6A1A9Sk2BtfHnZf5mEtbhU7Hobu3-hoIGMyyo18aS7E7_UuB4glLbeXdZsA";
// Variável com a chave obtida em API ACCESS no Google APIs
    private static final String API_KEY = "AIzaSyAuMLK2n1TQto8xmnSgMtX1ldBvGjdC4X0";

    public static void main(String[] args) throws MalformedURLException, IOException {
        String request = "https://android.googleapis.com/gcm/send";
        URL url = new URL(request);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setAllowUserInteraction(false);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Authorization", "key=AIzaSyAuMLK2n1TQto8xmnSgMtX1ldBvGjdC4X0");
        OutputStream out = conn.getOutputStream();

        String dados = "data.nome=Eurismar Pires Borges&data.mensagem=teste mensagem&data.hoje="+new Date();
        
        String registration_id = "registration_id="+ID_DISPOSITIVO_GCM;              
        String body = dados + "&" + registration_id;
        System.out.println(body);
        Writer writer = new OutputStreamWriter(out, "UTF-8");
        writer.write(body);

        writer.close();
        out.close();

        if (conn.getResponseCode() != 200) {
            System.out.println(conn.getResponseCode() + "-" + conn.getResponseMessage());
        }
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();

        conn.disconnect();

        System.out.println(sb.toString());
    }

}
