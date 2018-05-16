import com.powerlogic.jcompany.geradordinamico.helper.*;
import net.sf.json.*;
import com.powerlogic.jcompany.ide.geradordinamico.*;
import org.apache.commons.lang.StringUtils;
/**
 * 
 * 
 * Recupera informações sobre a entidade para popular o grid
 * 
 * Utilizado internamente pelo plugin dinâmico para varrer a entidade informada pelo usuário, retornando todos os atributos para 
 * geração do Grid com essas entidades.
 * 
 * Utilização.:
 * 		Pode ser utilizada após a definição de uma página que vai conter dados da entidade.
 * 
 * Ex.:
 * 
 * Verificar o arquivo tabular.padrao.xml
 * Na definição da 2 página:
 * 		<pagina titulo="Página 2 - Definição componentes da tela">
 * 
 * Existe a declaração de um campo com o domínio GRID
 * 		<dominio>GRID</dominio>
 * 
 * No final da declaração deste campo, existe a seguinte definição: 
 * 		 <script>groovy/comuns/PlcRecuperaPropsClasse</script>
 * Essa definição informa à página que estará utilizado o PlcRecuperaPropsClasse para varrer a entidade espeficicada pelo usuário 
 * e preencher o grid definido acima.
 *
 * 
*/
public class PlcRecuperaPropsClasse {
	
	void run(contexto) {
		
		def entidade = PlcGeradorHelper.getInstance().substituiTokens(contexto.padrao, '${entidade}');
		
		println 'Recuperando propriedades de ' + entidade;
		def listaProps = PlcGeradorHelper.getInstance().montaListaPropriedade(entidade);
		def gridLinha = new JSONArray();
		
		def ultimasProps = [];
		
		listaProps.each { field ->
			
			def gridColuna = new JSONArray();
			def info = field.getElementInfo(); 
			def tipo = new String (info.getTypeName());
			def nomeProp = field.getElementName();
			def isEnum = field.getSource().contains("@Enumerated");
						
			def gerar = "S";
			
			if (nomeProp.equals('versao') || nomeProp.equals('dataUltAlteracao') || nomeProp.equals('usuarioUltAlteracao'))
				gerar = "N";
			
			if ('Long'.equals(tipo)){
				tipo = "longo";
			} else if ('BigDecimal'.equals(tipo)){
				tipo = "numerico";
			} else if ('int'.equals(tipo)){
				tipo = "inteiro";
			} else if ('Date'.equals(tipo)){
				tipo = "data";
			} else if ('String'.equals(tipo)){
				tipo = "texto";
			} else if (isEnum) {
				tipo = "radio";
			}
			
			gridColuna.add(nomeProp);
			gridColuna.add(gerar);
			gridColuna.add(tipo);
			gridColuna.add(StringUtils.capitalize(nomeProp));
		
			if (!("versao".equals(nomeProp) || "dataUltAlteracao".equals(nomeProp) || "usuarioUltAlteracao".equals(nomeProp))){
				gridLinha.add(gridColuna);
			} else {
				ultimasProps.add(gridColuna);
			}
		
		}
		
		ultimasProps.each{ gridColuna ->
			gridLinha.add(gridColuna);
		}
		
		//println gridLinha.toString();
		
		contexto.campo.valorDefault = gridLinha.toString();
		
	}
}
