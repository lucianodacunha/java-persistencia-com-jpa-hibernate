package io.github.lucianodacunha;

import com.github.javafaker.Faker;
import io.github.lucianodacunha.dao.CategoriaDAO;
import io.github.lucianodacunha.dao.ProdutoDAO;
import io.github.lucianodacunha.model.Categoria;
import io.github.lucianodacunha.model.Categorias;
import io.github.lucianodacunha.model.Produto;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;

public class Main {

    private static final Faker faker = new Faker();
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);

        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();

        CategoriaDAO categoriaDAO = new CategoriaDAO(em);
        ProdutoDAO produtoDao = new ProdutoDAO(em);

        // inserts
        getProdutos(10).forEach(p -> {
            categoriaDAO.cadastrar(p.getCategoria());
            produtoDao.cadastrar(p);
        });
        em.flush();

        // consulta todos
        listarProdutos(produtoDao.buscarTodos());

        // consulta por limite de preco
        System.out.println();
        listarProdutos(produtoDao.buscarProdutoPorPreco(BigDecimal.valueOf(300.0)));

        // buscar por Id
        System.out.println();
        listarProduto(produtoDao.buscarPorId(5L));

        // remove por Id
        produtoDao.remover(produtoDao.buscarPorId(5L));

        // listar todos
        System.out.println();
        listarProdutos(produtoDao.buscarTodos());

        // atualizar o preco de todos os produtos.
//        produtoDao.atualizarTodosOsPrecos(new BigDecimal("0.10"));
//
//        System.out.println();
//        listarProdutos(produtoDao.buscarTodos());

        em.getTransaction().commit();
        em.close();
    }

    static void listarProduto(Produto produto){
        System.out.println(
                "%-3s %-30s %-7s %-10s %s"
                        .formatted(
                                "ID", "NOME", "PREÇO $", "DATA", "CATEGORIA"));
        System.out.println(produto);
    }

    static void listarProdutos(List<Produto> produtos){
        System.out.println(
                "%-3s %-30s %-7s %-10s %s"
                        .formatted(
                                "ID", "NOME", "PREÇO $", "DATA", "CATEGORIA"));
        produtos.forEach(System.out::println);
    }

    static List<Produto> getProdutos(int quantidade){
        List<Produto> produtos = new ArrayList<>();
        for(int i = 0; i < quantidade; i++){
            produtos.add(getProduto());
        }

        return produtos;
    }

    static Produto getProduto(){
        return new Produto(
                faker.commerce().productName(),
                faker.commerce().material(),
                BigDecimal.valueOf(faker.number().randomDouble(2, 10, 1000)),
                getCategoria());
    }

    static Categoria getCategoria(){
        return new Categoria(
                (faker.options().option(Categorias.values())).toString());
    }
}