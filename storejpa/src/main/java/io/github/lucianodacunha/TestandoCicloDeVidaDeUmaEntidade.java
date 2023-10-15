package io.github.lucianodacunha;

import com.github.javafaker.Faker;
import io.github.lucianodacunha.model.Categoria;
import io.github.lucianodacunha.model.Categorias;
import io.github.lucianodacunha.model.Produto;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.Locale;
import java.util.logging.Level;

public class TestandoCicloDeVidaDeUmaEntidade {
    private static final Faker faker = new Faker();
    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
        testDetached();
    }

    static Produto getProduto(){
        return new Produto(
                faker.commerce().productName(),
                faker.commerce().material(),
                BigDecimal.valueOf(faker.number().randomDouble(2, 100, 10000)),
                getCategoria()); // transient
    }

    static Categoria getCategoria(){
        return new Categoria(
                (faker.options().option(Categorias.values())).toString());
    }

    static void testTransient(){
        var produto = getProduto();
        // id é null, transient.
        System.out.println(produto);
        produto.setDescricao("Nova descrição");
        // id ainda é null.
        System.out.println(produto);

        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();
        em.persist(produto.getCategoria());
        em.persist(produto); // insert
        // id é igual a 1, objeto passa a ser managed.
        System.out.println(produto);
        produto.setDescricao("Nova descrição 2"); // objeto marcado como alterado.
        produto.setDescricao("Nova descrição 3"); // porém, o update não ocorre no db.
        produto.setDescricao("Nova descrição 4"); // essa será a alteração enviada para o db.
        em.getTransaction().commit(); // update.
        em.close();
    }

    static void testDetached(){
        var categoria = getCategoria();
        EntityManager em = JPAUtil.getEntityManager();
        em.getTransaction().begin();

        em.persist(categoria); // insert
        // id é igual a 1, objeto passa a ser managed.
        System.out.println(categoria);
        em.detach(categoria);
//        em.getTransaction().commit(); // update.
        categoria = em.merge(categoria); // attach
        em.detach(categoria);
        categoria = em.find(Categoria.class, 1L); // attach
        categoria.setNome("Nova categoria 2"); // objeto marcado como alterado.
        System.out.println(categoria);
        em.flush(); // update.
        categoria.setNome("Nova categoria 3"); // objeto marcado como alterado.
        System.out.println(categoria);
        var categoria1 = em.find(Categoria.class, 1L); // attach
        System.out.println(categoria1);
        em.getTransaction().commit();
        em.close();
    }
}