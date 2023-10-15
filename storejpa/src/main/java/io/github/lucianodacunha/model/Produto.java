package io.github.lucianodacunha.model;

import lombok.*;
import net.bytebuddy.asm.Advice;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "Produtos")
public class Produto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // nao tem sequence no h2.
    private Long id;
    @NonNull
    private String nome;
    @NonNull
    private String descricao;
    @NonNull
    private BigDecimal preco;
    private LocalDate dataCadastro = LocalDate.now();
    @NonNull
    @ManyToOne
    private Categoria categoria;

    @Override
    public String toString(){
        return "%03d %-30s %7.2f %10s %-10s"
                .formatted(
                        this.id, this.nome, this.preco, this.dataCadastro,
                        this.categoria.getNome());
    }
}
