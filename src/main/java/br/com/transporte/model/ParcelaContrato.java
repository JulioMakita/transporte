package br.com.transporte.model;

import java.io.Serializable;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="parcela_contrato")
public class ParcelaContrato implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(nullable=false)
	private Long id;
	
	@Column(name="quantidade_parcela")
	private Integer quantidadeParcela;
	
	@Column(name="valor_mensal")
	private BigDecimal valorMensalParcela;
	
	@Column(name="valor_extenso")
	private String valorExtensoParcela;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getQuantidadeParcela() {
		return quantidadeParcela;
	}

	public void setQuantidadeParcela(Integer quantidadeParcela) {
		this.quantidadeParcela = quantidadeParcela;
	}

	public BigDecimal getValorMensalParcela() {
		return valorMensalParcela;
	}

	public void setValorMensalParcela(BigDecimal valorMensalParcela) {
		this.valorMensalParcela = valorMensalParcela;
	}

	public String getValorExtensoParcela() {
		return valorExtensoParcela;
	}

	public void setValorExtensoParcela(String valorExtensoParcela) {
		this.valorExtensoParcela = valorExtensoParcela;
	}
}
