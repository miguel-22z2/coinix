package model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class DespesaBean {
	private int id;
    private String descricao;
    private BigDecimal valor;
    private String categoria;
    private LocalDate data;

    public DespesaBean() {}

    public DespesaBean(int id, String descricao, BigDecimal valor, String categoria, LocalDate data) {
        this.id = id;
        this.descricao = descricao;
        this.valor = valor;
        this.categoria = categoria;
        this.data = data;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public LocalDate getData() { return data; }
    public void setData(LocalDate data) { this.data = data; }
}
