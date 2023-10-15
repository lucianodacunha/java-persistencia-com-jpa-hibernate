package io.github.lucianodacunha.dao;

import io.github.lucianodacunha.model.Produto;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.util.List;

public class ProdutoDAO {

    private EntityManager em;

    public ProdutoDAO(EntityManager em){
        this.em = em;
    }

    public void cadastrar(Produto produto){
        this.em.persist(produto);
    }

    public void atualizar(Produto produto) {
        this.em.merge(produto);
    }

    public void remover(Produto produto){
        produto = em.merge(produto);
        em.remove(produto);
    }

    public List<Produto> buscarTodos(){
        String jpql = "SELECT p FROM Produto p";
        return em.createQuery(jpql, Produto.class)
                .getResultList();
    }

    public Produto buscarPorId(Long id){
        return em.find(Produto.class, id);
    }

    public List<Produto> buscarProdutoPorPreco(BigDecimal preco){
        String jpql = "SELECT p FROM Produto p WHERE p.preco < :preco";
        return em.createQuery(jpql, Produto.class)
                .setParameter("preco", preco)
                .getResultList();
    }

    // TODO: Verificar
//    public void atualizarTodosOsPrecos(BigDecimal percentualDeAtualizacao) {
//        String jpql = "UPDATE Produto p " +
//                "SET p.preco = :percentualDeAtualizacao";
//        em.createQuery(jpql, Produto.class)
//                .setParameter("percentualDeAtualizacao", percentualDeAtualizacao)
//                .executeUpdate();
//    }
}