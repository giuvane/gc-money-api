package br.edu.utfpr.gcmoney.api.model.sr;

import br.edu.utfpr.gcmoney.api.model.Usuario;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AgroApiKey.class)
public abstract class AgroApiKey_ {

	public static volatile SingularAttribute<AgroApiKey, Long> codigo;
	public static volatile SingularAttribute<AgroApiKey, Boolean> ativo;
	public static volatile SingularAttribute<AgroApiKey, String> apikey;
	public static volatile SingularAttribute<AgroApiKey, String> name;
	public static volatile SingularAttribute<AgroApiKey, Usuario> usuario;

	public static final String CODIGO = "codigo";
	public static final String ATIVO = "ativo";
	public static final String APIKEY = "apikey";
	public static final String NAME = "name";
	public static final String USUARIO = "usuario";

}

