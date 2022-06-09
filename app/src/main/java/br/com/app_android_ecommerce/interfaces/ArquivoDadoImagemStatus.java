package br.com.app_android_ecommerce.interfaces;

import android.net.Uri;

public interface ArquivoDadoImagemStatus {

    void comSucesso(Uri uri);

    void comErro(String e);
}
