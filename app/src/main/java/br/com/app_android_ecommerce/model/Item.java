package br.com.app_android_ecommerce.model;

import java.util.ArrayList;

public class Item {

    private String itemID;
    private String itemNome;
    private String itemDescricao;
    private String itemVendedorUID;
    private float  itemPreco;
    private String itemImagem;
    private ArrayList itemImagemLista;
    private boolean itemPedido;
    private boolean itemEscolhido;
    private String latitude;
    private String longitude;
    private String itemEndereco;
    private String itemCategoria;
    private String itemCriacaoHora;
    private String itemCompradorId;

    public Item() {
    }

    public Item(String itemID, String itemNome, String itemDescricao, String itemVendedorUID, float itemPreco, ArrayList itemImagemLista) {
        this.itemID = itemID;
        this.itemNome = itemNome;
        this.itemDescricao = itemDescricao;
        this.itemVendedorUID = itemVendedorUID;
        this.itemPreco = itemPreco;
        this.itemImagemLista = itemImagemLista;
    }

    public Item(String itemNome, String itemDescricao, String itemVendedorUID, float itemPreco, String itemImagem, ArrayList itemImagemLista) {
        this.itemNome = itemNome;
        this.itemDescricao = itemDescricao;
        this.itemVendedorUID = itemVendedorUID;
        this.itemPreco = itemPreco;
        this.itemImagem = itemImagem;
        this.itemImagemLista = itemImagemLista;
    }

    public Item(String itemID, String itemNome, String itemDescricao, String itemVendedorUID,float itemPreco, String itemImagem) {
        this.itemID = itemID;
        this.itemNome = itemNome;
        this.itemDescricao = itemDescricao;
        this.itemVendedorUID = itemVendedorUID;
        this.itemPreco = itemPreco;
        this.itemImagem = itemImagem;
    }

    public Item(String itemID, String itemNome, String itemDescricao, float itemPreco, ArrayList itemImagemLista) {
        this.itemID = itemID;
        this.itemNome = itemNome;
        this.itemDescricao = itemDescricao;
        this.itemPreco = itemPreco;
        this.itemImagemLista = itemImagemLista;
    }

    public String getItemID() {
        return itemID;
    }

    public void setItemID(String itemID) {
        this.itemID = itemID;
    }

    public String getItemNome() {
        return itemNome;
    }

    public void setItemNome(String itemNome) {
        this.itemNome = itemNome;
    }

    public String getItemDescricao() {
        return itemDescricao;
    }

    public void setItemDescricao(String itemDescricao) {
        this.itemDescricao = itemDescricao;
    }

    public String getItemVendedorUID() {
        return itemVendedorUID;
    }

    public void setItemVendedorUID(String itemVendedorUID) {
        this.itemVendedorUID = itemVendedorUID;
    }

    public float getItemPreco() {
        return itemPreco;
    }

    public void setItemPreco(float itemPreco) {
        this.itemPreco = itemPreco;
    }

    public String getItemImagem() {
        return itemImagem;
    }

    public void setItemImagem(String itemImagem) {
        this.itemImagem = itemImagem;
    }

    public ArrayList getItemImagemLista() {
        return itemImagemLista;
    }

    public void setItemImagemLista(ArrayList itemImagemLista) {
        this.itemImagemLista = itemImagemLista;
    }

    public boolean isItemPedido() {
        return itemPedido;
    }

    public void setItemPedido(boolean itemPedido) {
        this.itemPedido = itemPedido;
    }

    public boolean isItemEscolhido() {
        return itemEscolhido;
    }

    public void setItemEscolhido(boolean itemEscolhido) {
        this.itemEscolhido = itemEscolhido;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getItemEndereco() {
        return itemEndereco;
    }

    public void setItemEndereco(String itemEndereco) {
        this.itemEndereco = itemEndereco;
    }

    public String getItemCategoria() {
        return itemCategoria;
    }

    public void setItemCategoria(String itemCategoria) {
        this.itemCategoria = itemCategoria;
    }

    public String getItemCriacaoHora() {
        return itemCriacaoHora;
    }

    public void setItemCriacaoHora(String itemCriacaoHora) {
        this.itemCriacaoHora = itemCriacaoHora;
    }

    public String getItemCompradorId() {
        return itemCompradorId;
    }

    public void setItemCompradorId(String itemCompradorId) {
        this.itemCompradorId = itemCompradorId;
    }

    @Override
    public String toString() {
        return "Item{" +
                "itemID='" + itemID + '\'' +
                ", itemNome='" + itemNome + '\'' +
                ", itemDescricao='" + itemDescricao + '\'' +
                ", itemVendedorUID='" + itemVendedorUID + '\'' +
                ", itemPreco=" + itemPreco +
                ", itemImagem='" + itemImagem + '\'' +
                ", itemImagemLista=" + itemImagemLista +
                ", itemPedido=" + itemPedido +
                ", itemEscolhido=" + itemEscolhido +
                ", latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", itemEndereco='" + itemEndereco + '\'' +
                ", itemCategoria='" + itemCategoria + '\'' +
                ", itemCriacaoHora='" + itemCriacaoHora + '\'' +
                ", itemCompradorId='" + itemCompradorId + '\'' +
                '}';
    }
}