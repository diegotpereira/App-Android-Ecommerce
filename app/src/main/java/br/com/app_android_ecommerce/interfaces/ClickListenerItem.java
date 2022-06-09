package br.com.app_android_ecommerce.interfaces;

import android.view.View;

import br.com.app_android_ecommerce.model.Item;

public interface ClickListenerItem {

    abstract void onClick(View view, Item item);
}
