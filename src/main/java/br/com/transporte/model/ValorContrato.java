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
@Table(name="valor_contrato")
public class ValorContrato implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(nullable=false)
	private Long id;
	
	@Column(name="valor_contrato")
	private BigDecimal valorContrato;
	
	@Column(name="valor_contrato_extenso")
	private String valorContratoExtenso;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getValorContrato() {
		return valorContrato;
	}

	public void setValorContrato(BigDecimal valorContrato) {
		this.valorContrato = valorContrato;
	}

	public String getValorContratoExtenso() {
		return valorContratoExtenso;
	}

	public void setValorContratoExtenso(String valorContratoExtenso) {
		this.valorContratoExtenso = valorContratoExtenso;
	}
}
