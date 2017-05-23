package br.com.transporte.enumeration;

public enum EnumSexo {
	
	TODOS(""),
	MASCULINO("Masculino"),
	FEMININO("Feminino");
	
	private String descricao;
	
	private EnumSexo(String descricao) {
		this.descricao = descricao;
	}
	
	public String getDescricao() {
		return descricao;
	}
	
	public static EnumSexo fromValue(final String descricao){
		for(final EnumSexo enumSexo : EnumSexo.values()){
			if(enumSexo.getDescricao().equals(descricao)){
				return enumSexo;
			}
		}
		
		throw new IllegalArgumentException("Erro ao converter EnumSexo");
	}
}
