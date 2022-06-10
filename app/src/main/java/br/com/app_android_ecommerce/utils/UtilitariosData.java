package br.com.app_android_ecommerce.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UtilitariosData {

    public String getCurrentTimeInMillis() {
        String atualHora = String.valueOf(System.currentTimeMillis());

        return atualHora;
    }
    public String getdataEhHora(String hora) {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd, MM, yyyy HH:mm z");
        String dataString = simpleDateFormat.format(new Date(Long.parseLong(hora)));

        return dataString;
    }
}
