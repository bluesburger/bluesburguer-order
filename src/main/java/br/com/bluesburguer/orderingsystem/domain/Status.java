package br.com.bluesburguer.orderingsystem.domain;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
@RequiredArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = false)
@Embeddable
public class Status implements Serializable {
	private static final long serialVersionUID = -1837328577857983314L;

	@NonNull
	private Step step;
	
	@NonNull
	private Fase fase;
}
