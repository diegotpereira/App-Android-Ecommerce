package br.com.app_android_ecommerce.item;

import java.util.ArrayList;

public class Categorias {

    public ArrayList<String> getItemCategoriaLista() {

        ArrayList<String> categoriaNomes = new ArrayList<String>();

        categoriaNomes.add("Eletrodomésticos");
        categoriaNomes.add("Automóveis");
        categoriaNomes.add("Eletrônicos");
        categoriaNomes.add("Moda");
        categoriaNomes.add("Brindes");
        categoriaNomes.add("Móveis");
        categoriaNomes.add("Casa & jardim");
        categoriaNomes.add("Filmes & música");
        categoriaNomes.add("Escritório");
        categoriaNomes.add("Outros");
        categoriaNomes.add("Esportes");
        categoriaNomes.add("Brinquedos & Jogos");

        return categoriaNomes;
    }
    public ArrayList<String> getItemCategoriaResource(String itemCategoria) {

        ArrayList<String> categoriaResource = new ArrayList<String>();

        switch (itemCategoria) {
            case "Eletrodomésticos":
                categoriaResource.add("ic_cat_appliances");
                categoriaResource.add("md_orange_500");
                break;

            case "Automóveis":
                categoriaResource.add("ic_cat_automobiles");
                categoriaResource.add("md_red_200");
                break;

            case "Eletrônicos":
                categoriaResource.add("ic_cat_electronics");
                categoriaResource.add("md_purple_300");
                break;

            case "Moda":
                categoriaResource.add("ic_cat_fashion");
                categoriaResource.add("md_pink_200");
                break;

            case "Brindes":
                categoriaResource.add("ic_cat_freebies");
                categoriaResource.add("colorPrimary");
                break;

            case "Móveis":
                categoriaResource.add("ic_cat_furniture");
                categoriaResource.add("md_brown_200");
                break;

            case "Casa & jardim":
                categoriaResource.add("ic_cat_home");
                categoriaResource.add("md_blue_grey_200");
                break;

            case "Filmes & música":
                categoriaResource.add("ic_cat_movies");
                categoriaResource.add("md_lime_200");
                break;

            case "Escritório":
                categoriaResource.add("ic_cat_office");
                categoriaResource.add("md_teal_200");
                break;

            case "Outros":
                categoriaResource.add("ic_cat_sports");
                categoriaResource.add("md_amber_200");
                break;

            case "Esportes":
                categoriaResource.add("ic_cat_toys");
                categoriaResource.add("md_light_blue_100");
                break;

            case "Brinquedos & Jogos":
                categoriaResource.add("ic_cat_other");
                categoriaResource.add("md_grey_400");
                break;
        }
        return categoriaResource;
    }
}
