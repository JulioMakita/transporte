package br.com.transporte.model;

import java.io.Serializable;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="itinerario_contrato")
public class ItinerarioContrato implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(nullable=false)
	private Long id;
	
	@Column(name="local_saida")
	private String localSaida;
	
	@Column(name="hora_saida")
	private LocalTime horaSaida;
	
	@Column(name="local_retorno")
	private String localRetorno;
	
	@Column(name="hora_retorno")
	private LocalTime horaRetorno;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLocalSaida() {
		return localSaida;
	}

	public void setLocalSaida(String localSaida) {
		this.localSaida = localSaida;
	}

	public LocalTime getHoraSaida() {
		return horaSaida;
	}

	public void setHoraSaida(LocalTime horaSaida) {
		this.horaSaida = horaSaida;
	}

	public String getLocalRetorno() {
		return localRetorno;
	}

	public void setLocalRetorno(String localRetorno) {
		this.localRetorno = localRetorno;
	}

	public LocalTime getHoraRetorno() {
		return horaRetorno;
	}

	public void setHoraRetorno(LocalTime horaRetorno) {
		this.horaRetorno = horaRetorno;
	}
}
