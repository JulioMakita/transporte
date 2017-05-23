package br.com.transporte.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;

@Entity
public class Contrato implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(nullable=false)
	private Long id;
	
	@Temporal(value=TemporalType.TIMESTAMP)
	@Column(name = "data_cadastro")
	private Date dataCadastro;
	
	@Temporal(value=TemporalType.TIMESTAMP)
	private Date dataAlteracao;
	
	@Temporal(value=TemporalType.DATE)
	@Column(name = "vigencia_inicio")
	private Date vigenciaInicio;
	
	@Temporal(value=TemporalType.DATE)
	@Column(name = "vigencia_fim")
	private Date vigenciaFim;
	
	@Column(length=2000)
	private String observacao;
	
	@OneToOne
	@JoinColumn(name = "id_valor_contrato")
	@NotNull
	private ValorContrato valorContrato;
	
	@OneToOne
	@JoinColumn(name = "id_parcela_contrato")
	@NotNull
	private ParcelaContrato parcelaContrato;
	
	@OneToOne
	@JoinColumn(name = "id_itinerario_contrato")
	@NotNull
	private ItinerarioContrato itinerarioContrato;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public Date getDataAlteracao() {
		return dataAlteracao;
	}

	public void setDataAlteracao(Date dataAlteracao) {
		this.dataAlteracao = dataAlteracao;
	}

	public Date getVigenciaInicio() {
		return vigenciaInicio;
	}

	public void setVigenciaInicio(Date vigenciaInicio) {
		this.vigenciaInicio = vigenciaInicio;
	}

	public Date getVigenciaFim() {
		return vigenciaFim;
	}

	public void setVigenciaFim(Date vigenciaFim) {
		this.vigenciaFim = vigenciaFim;
	}

	public ValorContrato getValorContrato() {
		return valorContrato;
	}

	public void setValorContrato(ValorContrato valorContrato) {
		this.valorContrato = valorContrato;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

	public ParcelaContrato getParcelaContrato() {
		return parcelaContrato;
	}

	public void setParcelaContrato(ParcelaContrato parcelaContrato) {
		this.parcelaContrato = parcelaContrato;
	}

	public ItinerarioContrato getItinerarioContrato() {
		return itinerarioContrato;
	}

	public void setItinerarioContrato(ItinerarioContrato itinerarioContrato) {
		this.itinerarioContrato = itinerarioContrato;
	}
}
