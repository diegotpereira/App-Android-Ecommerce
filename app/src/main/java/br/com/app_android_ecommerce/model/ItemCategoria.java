package br.com.app_android_ecommerce.model;

public class ItemCategoria {

    public int getCategoriaNome;
    private String categoriaNome;
    private int categoriaIconeId;
    private int categoriaIconeCor;

    public ItemCategoria() {
    }

    public ItemCategoria(String categoriaNome, int categoriaIconeId, int categoriaIconeCor) {
        this.categoriaNome = categoriaNome;
        this.categoriaIconeId = categoriaIconeId;
        this.categoriaIconeCor = categoriaIconeCor;
    }

    public String getCategoriaNome() {
        return categoriaNome;
    }

    public void setCategoriaNome(String categoriaNome) {
        this.categoriaNome = categoriaNome;
    }

    public int getCategoriaIconeId() {
        return categoriaIconeId;
    }

    public void setCategoriaIconeId(int categoriaIconeId) {
        this.categoriaIconeId = categoriaIconeId;
    }

    public int getCategoriaIconeCor() {
        return categoriaIconeCor;
    }

    public void setCategoriaIconeCor(int categoriaIconeCor) {
        this.categoriaIconeCor = categoriaIconeCor;
    }

}
