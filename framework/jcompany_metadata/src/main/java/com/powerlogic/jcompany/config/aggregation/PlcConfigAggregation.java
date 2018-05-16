/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.config.aggregation;

import static java.lang.annotation.ElementType.TYPE;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.powerlogic.jcompany.config.metadata.PlcMetaConfig;
import com.powerlogic.jcompany.config.metadata.PlcMetaConfig.Layer;
import com.powerlogic.jcompany.config.metadata.PlcMetaConfig.Scope;
import com.powerlogic.jcompany.config.metadata.PlcMetaEditor;
import com.powerlogic.jcompany.config.metadata.PlcMetaEditorParameter;

@Documented
@Target(TYPE)
@Retention(RetentionPolicy.RUNTIME)
@PlcMetaConfig(root=true, scope={Scope.MAN, Scope.SEL}, layer=Layer.DOMAIN)
@PlcMetaEditor(label="Agregação", description="Definição de um grafo de classes agregadas envolvidas em uma \"Colaboração\" para serem gerenciadas pelo jCompany.")
/**
 * @since jCompany 5.0 Metadados para definição do grafo de entidades envolvido em uma "Colaboração" (formulário). A entidade principal (mestre ou raiz) da agregação e
 * seus eventuais, detalhes, componentes, referências, arquivos anexados, etc., mantidos ou consultados em um formulário, podem ser aqui
 * definidos, quando se deseja sobrepor o padrão que é obtido por convenção. Por exemplo, através de instrospecção na entidade o jCompany
 * automaticamente configura todos os detalhes, componentes, etc. para serem mantidos, mas em um caso específico um formulário pode manter
 * apenas um dos detalhes ou componentes por vez - isso requer uma configuração explícita que sobreponha a convenção.
 */
public @interface PlcConfigAggregation {

    /**
     * Define a entidade raiz (principal ou mestre) da agregação. Ex: FuncionarioEntity.class 
     */
	@PlcMetaEditorParameter(label="Entidade", description="Entidade (classe de domínio) raiz/mestre da agregação", required=true)
    Class entity() default Object.class;
    
    /**
     * Define a lista de componentes utilizados na agregação. 
     * Ex.: componentes = {@PlcConfigComponente(classe=app.powerlogic.rhdemo.fcls.entidade.Endereco.class, propriedade="endereco")}
     */
	@PlcMetaEditorParameter(label="Componentes", description="Lista de componentes da agregação")
    PlcConfigComponent[] components() default @PlcConfigComponent;
    
    /**
     * Define a lista de descendentes da agregação. 
     * Ex.: descendentes = {@PlcConfigDescendente(classe=app.powerlogic.rhdemo.fcls.entidade.PessoaFisica.class)}
     */
	@PlcMetaEditorParameter(label="Descendentes", description="Lista de descendentes da agregação")
    PlcConfigDescendent[] descendents() default @PlcConfigDescendent;
    
    /**
     * Define a lista de detalhes da agregação.
     * Ex.: 	<pre>	@PlcConfigDetalhe(classe = app.powerlogic.rhdemo.fcls.entidade.DependenteEntity.class,
								nomeColecao = "dependente", numNovos = 4,
								cardinalidade = "0..*", porDemanda = false,
								propReferenciaDesprezar = "nomeDependente")
				</pre>
     */
	@PlcMetaEditorParameter(label="Detalhes", description="Lista de detalhes da agregação")
    PlcConfigDetail[] details() default @PlcConfigDetail;
    
    
    /**
     * Define metadados do padrão 'preferência do usuário'. Este caso de uso padrão é destinado a registrar opções de cada usuário para um sistema
     * @see PlcConfigUserPref
     */
	@PlcMetaEditorParameter(label="Preferência de Usuário", description="Configuração específica para preferência de usuário")
    PlcConfigUserPref userpref() default @PlcConfigUserPref;
    
}
