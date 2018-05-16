/*  																													
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								
 */ 
package com.powerlogic.jcompany.commons;



/**
 * Constantes utilizadas somente na camada controle.
 * @since jCompany 3.0
 */
public interface PlcConstants {

	/**
	 *   
	 *   Alguns constantes muito usadas frequentemente estão fora de sub-interfaces para facilitar compatibilização.
	 *   
	 **/

    /**
     * Nome do atributo de aplicação para a string contendo a versao e o build da aplicação
     */
    String VERSAO = "Implementation-Version";
    
    /**
     * Nome do atributo de aplicação para a string contendo o Código do Produto
     */
    String CODIGO_PRODUTO = "PrjCodigo-Produto";
    
    /**
	 * Evitar usar Entidade em requisicao somente para jMonitor
	 */
	String ENTITY_OBJECT = "jMonitor_EntityObject";
    
    /**
     * Devolve o VO de usuário da sessão. <br>
     * Ex: AppPerfilUsuarioVO usu = (AppPerfilUsuarioVO)
     * request.getession().getAttribute( <constante>); (Pode-se usar em
     * SEGURANCA também, mas foi mantido na raiz para maior comodidade)
     */
    String USER_INFO_KEY 		= "USER_INFO";
    String BROWSER_INFO_KEY 		= "BROWSER_INFO";
    String SESSION_CACHE_KEY 	= "SESSION_CACHE";
    String SIM 					= "S";
    String NAO 					= "N";

    /**
     * Se desejado, utilizar "S" diretamente
     */
    String EXIBIR = "S";

    /**
     * Se desejado, utilizar "N" diretamente
     */
    String NAO_EXIBIR = "N";

    
    /**
     * Utilizado como sufixo para guardar estado de uma lista(List) de objetos
     * entre leitura e gravação. O padrão é PREFICO_OBJ+ <nome da classe sem
     * package>+SUFIXO_LISTA"
     */
    String SUFIXO_LISTA 			= "itensPlcAnterior";
    String VISUALIZA_DOCUMENTO_PLC 	= "visualizaDocumentoPlc";
    String UNIVERSAL_COM_NAVEGADOR 	= "universalComNavegadorPlc";
	String EXIBE_ED_DOC_PLC 		= "exibeEdDocPlc";
    String MSG_COMPLEMENTAR 		= "msgCompl";
   
	interface ENTIDADE {

        /**
         * Utilizado como prefixo para guardar estado de objetos entre leitura e gravação. 
         * O padrão é PREFIXO_OBJ+ <nome da classe sem package>"
         */
        String PREFIXO_OBJ = "objeto";
        
        String ENTIDADE = "VALUE_OBJECT";

    }
    
    /**
     * Constantes para referência a parâmetros iniciais (context-params) declarados no web.xml 
     * Cada constante referencia uma variável context-param declarada no web.xml e que pode ser capturada com
     * um comando como String valor = PlcMetaDadoAtributoUtil.getAtributo(PlcConstantes.CONTEXT_PARAM.&lt;variavel&gt;);
     */
    interface CONTEXTPARAM {
        /**
         * Versão do sistema no seguinte padrão [versão].[release].[build]. A
         * versão deve ser incrementada sempre que o sistema sofrer grandes
         * manutenções evolutivas O release deve ser incrementado sempre uma
         * nova liberação do sistema contiver manutenções corretivas e
         * adaptativas e evolutivas de menor monta. O build deve ser
         * incrementado a cada liberação intermediária, normalmente provocada
         * por acertos emergenciais.
         */
        String INI_VERSAO = "versao";
        
        /**
         * D-Desenvolvimento, T-Teste, P-Produção.
         */
        String INI_MODO_EXECUCAO = "modoExecucao";

        /**
         * Indica que o jCompany dever fazer verificação de alteração de dados e enviar alerta
         * O default é N.
         */
        String INI_ALERTA_ALTERACAO_USA = "alertaAlteracaoUsa";
        
        /**
         * Indica que o jCompany dever fazer verificação de exclusão de detalhdes/subdetalhes ao gravar registro. 
         * O default é N.
         */
        String INI_ALERTA_EXCLUSAO_DETALHE_USA = "alertaExclusaoDetalheUsa";

      }

    /**
     * jCompany 2.7. Constantes para referência a interfaces (layouts e componentes)
     */
    interface GUI {

    	/**
    	 * Componente de MSG Universal
    	 */
    	interface MSGUNIVERSAL {
    		
    		String BONECO = "BONECO_PLC";
    		
    		String MSG = "MSG_PLC";
    		
    		String IND_DINAMICO = "DINAMICO_PLC";
    		
    	}
    	
    	/**
    	 * Componente de Exploração de dados genérico
    	 */
    	interface EXPLORER {
    		
    		String USA_PLC = "explorerUsaPlc";
    		
    		String TREEVIEW_PLC = "explorerTreeviewPlc";

			String LINK_PLC = "explorerLinkPlc";

    	}
    	
        /**
         * Constantes relacionadas aos layouts principais e peles
         */
        interface GERAL {

            /**
             * Um valor igual a S com nesta chave no request (atributo ou
             * parametro na url), exibe somente o corpo de qualquer layout
             */
            String LAYOUT_SOMENTE_CORPO = "laysc";

        }

        /**
         * Constantes realacionadas às lógicas de uso de peles (CSS)
         */
        interface PELE {

        	/**
        	 * Request parameter para troca de pele dinâmica
        	 */
        	String PELE_PLC = "pelePlc";
        	
            /**
             * Mantém como atributo de sessão o CSS específico da aplicação.
             */
            String CSS_ESPECIFICO = "cascadeStyleSheet";

        }
        
        /**
         * Constantes relacionadas ao layout universal automatizado
         */
        interface UNIVERSAL {

            /**
             * Indica na sessão que uma selação está aberta em modo popup
             */
            String IND_POPUP = "popup";
            
        }

        /**
         * Constantes relacionadas às lógicas com layout navegador
         */
        interface NAVEGADOR {

            /**
             * Total de registros resultantes de uma pesquisa
             */
            String PORTLET_NAV_TOT_REG = "totPlc";

            /**
             * Indica o início que a consulta vai ser chamada
             */
            String PORTLET_NAV_INI = "pAcIniNav";

            /**
             * Mantém como atributo de sessão o número de início corrente da consulta
             */
            String PORTLET_NAV_DE = "dePlc";

            /**
             * Mantém como atributo de sessão o número limite de registros para a consulta
             */
            String PORTLET_NAV_ATE = "atePlc";

            /**
             * Chave da URL que contém o índice inicial da próxima paginação do navegador
             */
            String PORTLET_NAV_INI_PROX = "pAcIniProxPlc";

            /**
             * Chave da URL que contém o índice inicial da paginação anterior do navegador
             */
            String PORTLET_NAV_INI_ANT = "pAcIniAntPlc";

            /**
             * Chave da URL que contém o índice final da próxima paginação do navegador
             */
            String PORTLET_NAV_INI_FIM = "pAcIniFimPlc";
            
			String TOPO_POSICAO = "topoPosicaoPlc";

			String TOPO_ESTILO = "topoEstiloPlc";

			String NUM_REG_DINAMICO = "numeroRegistroDinamico";

        }

        /**
         * Constantes relacionadas às lógicas com layout tab-folder
         */
        interface TABFOLDER {

        	/**
        	 * Prefixo que fica no Map de componentes para nao exibiçao, para abas de tab-folder
        	 */
        	String TOKEN_FOLDER = "TOKEN_FOLDER>";
        	
        	/**
        	* Define campos para tabulação para próxima aba do tab folder quando clita tecla TAB com foco no campo
        	*/
        	String CAMPOS_FOCO = "tabFolderCamposFocoPlc";
        	/**
        	* Define campos para tabulação para próxima aba do tab folder quando clita tecla TAB com foco no campo
        	*/
        	String STRING_CAMPOS_FOCO = "stringTabFolderCamposFocoPlc";
            
        }

        /**
         * Constantes relacionadas às portlets (componentes expansíveis)
         */
        interface PORTLET {
            /**
             * Chave padrão na URL para indicar os eventos(ações) do portlet
             */
            String EVT_PORTLET = "acao";

            /**
             * Identifica o evento de expandir um portlet
             */
            String EVT_PORTLET_EXPANSAO = "expande";

            /**
             * Identifica o evento de retrair um portlet
             */
            String EVT_PORTLET_RETRACAO = "retrai";

        }

        /**
         * Map no request para  lógica de segurança visual
         */
		String MAPA_SEGURANCA = "MAPA_SEGURANCA";
		
		/**
		 * Indica na url e na sessao que esta em modo de radiografia de layouts (1,2,3 ou N)
		 */
		String LAY_SPY_PLC = "laySpyPlc";

    }


    /**
     * jCompany 2.7. Constantes para lógicas de lidar com erros.
     */
    interface ERRO {

        /**
         * jcompany 2.7.1. Chave de área de caching que mantém erros de inicialização da aplicação
         */
        String ERRO_INICIALIZACAO_CHAVE_CACHE = "ERRO_INI_PLC";
        /**
         * Mantém as mensagens de erro de validação enviadas via exceções
         */
        String PLC_ERROR_KEY = "PLC_ERROR_KEY";

		/**
		 * Stack Trace de msg inesperadas no request
		 */
		String STACK_TRACE_MSG = "STACK_TRACE_MSG";

    }

    /**
     * jCompany 2.7. Constantes para lógicas de lidar com erros.
     */
    interface MSG {

        String PROP_TITULO_AUTOMATICO = "###propTituloAutomaticoPlc###";

    }

    /**
     * jCompany 2.7. Constantes para referência a botões e eventos gerais do
     * jCompany
     */
    interface ACAO {

        /**
         * Chave padrão na URL para indicar os eventos (acões) como requerido
         * pelo LookupDispatchAction
         */
        String EVENTO = "evento";

        /**
         * Identifica o evento de incluir um novo registro
         */
        String EVT_INCLUIR = "Novo";

        /**
         * Identifica o evento de incluir um novo item do detalhe
         */
        String EVT_INCLUIR_DETALHE = "Novo";

        /**
         * Identifica o evento de gravar um registro
         */
        String EVT_GRAVAR = "Gravar";

        /**
         * Identifica o evento de excluir um registro
         */
        String EVT_EXCLUIR = "Excluir";

        /**
         * Identifica o evento de clonar um registro
         */
        String EVT_CLONAR = "Clonar";

        /**
         * Identifica o evento de abrir. Por exemplo, uma página de seleção
         */
        String EVT_ABRIR = "Abrir";

        /**
         * Identifica o evento que realiza uma pesquisa
         */
        String EVT_PESQUISAR = "Pesquisar";

        /**
         * Identifica o evento para gerar um relatório
         */
        String EVT_GERAR_RELATORIO = "Gerar Relatório";

        /**
         * Identifica o evento para desconectar
         */
        String EVT_DESCONECTAR = "Desconectar";

        /**
         * Identifica o evento de impressão
         */
        String EVT_IMPRIMIR = "Imprimir";

        /**
         * Identifica o evento para editar um registro
         */
        String EVT_EDITAR = "Editar";

        /**
         * Identifica o evento de upload
         */
        String EVT_UPLOAD = "upload";

        /**
         * Identifica o evento de download
         */
        String EVT_DOWNLOAD = "download";

        /**
         * Identifica o evento de limpar
         */
        String EVT_LIMPAR = "limpar";

        /**
         * Indica, com valor "S" no request, que é para exibir o botão correspondente. O default é não exibir.
         */
        String EXIBE_BT_CANCELAR = "exibeCancelarPlc";

        /**
         * Indica, com valor "S" no request, que é para exibir o botão correspondente. O default é não exibir.
         */
        String EXIBE_BT_CANCELAR_POPUP = "exibeCancelarPopupPlc";
        /**
         * Indica, com valor "S" no request, que é para exibir o botão correspondente. O default é não exibir.
         */
        String EXIBE_BT_INCLUIR = "exibeIncluirPlc";
        /**
         * Indica, com valor "S" no request, que é para exibir o botão correspondente. O default é não exibir.
         */
        String EXIBE_BT_GRAVAR = "exibeGravarPlc";
        /**
         * Indica, com valor "S" no request, que é para exibir o botão correspondente. O default é não exibir.
         */
        String EXIBE_BT_EXCLUIR = "exibeExcluirPlc";
        /**
         * Indica, com valor "S" no request, que é para exibir o botão correspondente. O default é não exibir.
         */
        String EXIBE_BT_ABRIR = "exibeAbrirPlc";
        /**
         * Indica, com valor "S" no request, que é para exibir o botão correspondente. O default é não exibir.
         */
        String EXIBE_BT_PESQUISAR = "exibePesquisarPlc";
        
        /**
         * Indica, com valor "S" no request, que é para exibir o botão correspondente. O default é não exibir.
         */
        String EXIBE_BT_PESQUISAR_RSS = "exibePesquisarRssPlc";
        /**
         * Indica, com valor "S" no request, que é para exibir o botão correspondente. O default é não exibir.
         */
        String EXIBE_BT_GERAR_RELATORIO = "exibeGerarRelatorioPlc";
        /**
         * Indica, com valor "S" no request, que é para exibir o botão correspondente. O default é não exibir.
         */
        String EXIBE_BT_IMPRIMIR = "exibeImprimirPlc";
        /**
         * Indica, com valor "S" no request, que é para exibir o botão correspondente. O default é não exibir.
         */
        String EXIBE_BT_CLONAR = "exibeClonarPlc";
        /**
         * Indica, com valor "S" no request, que é para exibir o botão correspondente. O default é não exibir.
         */
        String EXIBE_BT_PESQUISAR_DETALHE = "exibePesquisarDetalhePlc";
        /**
         * Indica, com valor "S" no request, que é para exibir o botão correspondente. O default é não exibir.
         */
        String EXIBE_BT_EXPORTA = "exibeExportarPlc";
        /**
         * Indica, com valor "S" no request, que é para exibir o botão correspondente. O default é não exibir.
         */
        String EXIBE_BT_LIMPAR = "exibeLimparPlc";
        /**
         * Indica, com valor "S" no request, que é para exibir o botão correspondente. O default é não exibir.
         */
        String EXIBE_BT_UPLOAD = "exibeUploadPlc";
        /**
         * Indica, com valor "S" no request, que é para exibir o botão correspondente. O default é não exibir.
         */
        String EXIBE_BT_AJUDA = "exibeAjudaPlc";
        /**
         * Indica, com valor "S" no request, que é para exibir o botão correspondente. O default é não exibir.
         */
        String EXIBE_BT_ASSISTENTE_INICIALIZA = "exibeAssistInicializarPlc";
        /**
         * Indica, com valor "S" no request, que é para exibir o botão correspondente. O default é não exibir.
         */
        String EXIBE_BT_ASSISTENTE_PROXIMO = "exibeAssistProximoPlc";
        /**
         * Indica, com valor "S" no request, que é para exibir o botão correspondente. O default é não exibir.
         */
        String EXIBE_BT_ASSISTENTE_ANTERIOR = "exibeAssistAnteriorPlc";
        /**
         * Indica, com valor "S" no request, que é para exibir o botão correspondente. O default é não exibir.
         */
        String EXIBE_BT_APROVA = "exibeAprovarPlc";
        /**
         * Indica, com valor "S" no request, que é para exibir o botão correspondente. O default é não exibir.
         */
        String EXIBE_BT_APROVA_PESQUISA = "exibePesquisarAprovarPlc";
        /**
         * Indica, com valor "S" no request, que é para exibir o botão correspondente. O default é não exibir.
         */
        String EXIBE_BT_REPROVA = "exibeReprovarPlc";
        /**
         * Indica, com valor "S" no request, que é para exibir o botão correspondente. O default é não exibir.
         */
        String EXIBE_BT_APROVA_TODOS = "exibeAprovarTodosPlc";
        /**
         * Indica, com valor "S" no request, que é para exibir o botão correspondente. O default é não exibir.
         */
        String EXIBE_BT_GRAVA_VERSAO = "exibeGravarVersaoPlc";
        /**
         * Indica, com valor "S" no request, que é para exibir o botão correspondente. O default é não exibir.
         */
        String EXIBE_BT_PUBLICA_VERSAO = "exibePublicarVersaoPlc";
        /**
         * Indica, com valor "S" no request, que é para exibir o botão correspondente. O default é não exibir.
         */
        String EXIBE_BT_ICONE_APROVACAO = "exibeIconeAprovacaoPlc";
        /**
         * Indica, com valor "S" no request, que é para exibir o botão correspondente. O default é não exibir.
         */
        String EXIBE_BT_GERAR_ESQUEMA = "exibeGerarEsquemaPlc";
        /**
         * Indica, com valor "S" no request, que é para exibir o botão correspondente. O default é não exibir.
         */
        String EXIBE_BT_RETORNAR_MULTI_SEL = "exibeRetornarMultiSelPlc";
        /**
         * Indica, com valor "S" no request, que é para exibir o botão correspondente. O default é não exibir.
         */
        String EXIBE_BT_VISUALIZA_DOCUMENTO = "exibeVisualizarDocumentoPlc";
        /**
         * Indica, com valor "S" no request, que é para exibir o botão correspondente. O default é não exibir.
         */
        String EXIBE_BT_EDITA_DOCUMENTO = "exibeEditarDocumentoPlc";
        /**
         * Indica, com valor "S" no request, que é para exibir o botão correspondente. O default é não exibir.
         */
        String EXIBE_BT_GERAR_DOCUMENTO_OFFICE = "exibeGerarDocumentoOfficePlc";
        
        /**
         * Indica que não é para verificar detalhe por demanda
         */
        String IND_NAO_VERIFICAR_DETALHE_DEMANDA = "naoVerificarDetalheDemanda";

        String EXIBE_JCOMPANY_EVT_INCLUIR_SUBDETALHE = "exibe_jcompany_evt_incluir_subdetalhe";
        
        String EXIBE_JCOMPANY_EVT_INCLUIR_DETALHE = "exibe_jcompany_evt_incluir_detalhe";
        
    }

    /**
     * jCmopany 2.7 Constantes para manipulação de classes do lookup
     */
    interface LOOKUP {

        /**
         * Constante que prefixa classes de lookup no caching
         */
        String PREFIXO_LOOKUP = "listaSel";
        
        /**
         * Constante que sufixa as classes de lookup no caching em formato JSF. Ex: ufEntityListaSel
         */
        String SUFIXO_JSF_LOOKUP = "Items";
    }


    /**
     * jCompany 2.7. Constantes utilizadas em lógicas de manipulação de cookies
     */
    interface COOKIE {

        /**
         * Chave do cookie e sessão, que guarda últimas edições
         */
        String COOKIE_ULT_EDICOES = "ultimasEdicoesPlc";

        /**
         * Indica se a requisição vem de um redirecionamento. Qualquer valor
         * pode ser atribuido ao parâmetro de request. Utilizado para preservar
         * gravação de cookies após um redirect.
         */
        String REDIRECT = "redirect";

        /**
         * Identifica o cookie relacionado ao idioma no objeto Response
         */
        String COOKIE_IDIOMA = "cookieIdioma";

        /**
         * Identifica o cookie relacionado a pele
         */
        String COOKIE_PELE = "cookiePele";
        
        /**
         * Identifica o cookie relacionado a personalizacao de formulário
         */
        String COOKIE_FORM = "cookieForm";

        /**
         * Identifica o cookie relacionado ao layout no objeto Response, por exemplo classico
         */
        String COOKIE_LAYOUT = "cookieLayout";

        /**
         * Identifica a resolução de vídeo do cliente.
         */
        String RESOLUCAO_VIDEO = "resolucaoPlc";
        
        /**
         * Identifica se a verificacao inicial de cookies do layout ja foi processado
         */
        
        String COOKIES_LAYOUT_PROCESSADOS = "cookiesLayoutProcessados";
    }

    /**
     * jCompany 2.7. Constantes em lógicas de arquivo anexado
     */
    interface ARQUIVO {
        /**
         * Indica com S no request que a lógica vai entrar em modo de arquivo anexado, para que form seja multipart neste caso
         */
        String IND_ARQ_ANEXADO = "IND_ARQ_ANEXADO";
        
        /**
         * Indica "S" para quando o arquivo anexado for uma imagem.
         */
        String IND_IMAGEM  = "IND_IMAGEM";

    }
    
    /**
     * jCompany 2.7 Constantes para manipulação de arquivos de javascript pelos
     * layout
     */
    public interface JAVASCRIPT {
        /**
         * Chaves para conter endereços de Arquivos de Recursos do lado cliente,
         * que ficam armazenados em escopo da sessão. São utilizados nos layouts
         * do jCompany e outros flags para JSPs.
         */
        String JAVASCRIPT_ESPECIFICO = "JAVASCRIPT_ESPECIFICO";

        String JAVASCRIPT_JCOMPANY = "JAVASCRIPT_JCOMPANY";
    }
    
    /**
     * jCompany 2.7. Constantes utilizadas em lógicas de relação com janelas
     * popup
     */
    interface POPUP {
        /**
         * Indica se uma página está sendo aberta de forma Popup, para incluir
         * CSS e Javascript no VBoxManSelLayout, sem os layouts principais
         * (EcpGeralLayout)
         */
        String SELECAO_POPUP = "SELECAO_POPUP";

        /**
         * Modo de abertura passado na url para que uma seleção funcione como popup
         */
        String URL_MODO_POPUP = "modoJanelaPlc";

    }
 

    
    /**
     * jCompany 2.7. Constantes utilizadas para lógicas de teste de qualidade
     * automatizados (Quality Assurance). Quando rodando em modo de teste algumas funções
     * de GUI como Ajax e Expansão/Retração de portlets são desabilitadas
     */
    interface QA {

        /**
         * Indica na sessão que estamos em modo de teste, forçando com que todos
         * os componentes (portlets) apareçam expandidos
         */
        String MODO_TESTE = "modoTestePlc";
        
        String NOME = "JCOMPANY_QA";
        
        String LOGGING = "loggingQA";

    }
    
    /**
     * POJO de Context, que contem informações da camada cliente
     */
	String CONTEXT = "contextPlc";

	/**
	 * Chave do google map
	 */
	String GOOGLE_MAP_KEY = "googleMapKey";

	/**
	 * Indica se a pesquisa RESTful esta habilitada
	 */
	String PESQUISA_RESTFUL_USA = "pesquisaRestfulUsa";

	String STARTUP = "startupPlc";

    /**
     * jCompany 2.7. Constantes utilizadas em lógicas de profiling, como registro de filtro de segurança
     * por usuário e controle de tentativa de logins
     */
    interface PROFILE {
        /**
         * Guarda em um MAP a lista de URLs que não enviarão filtro de queries declarados.
         */
        String FILTRO_EXCECOES = "FiltroExcecoes";

        /**
         * Constante no padrão JSR 168 para uso em JSTL. Com esta chave pega-se
         * o VO de perfil de usuário da sessão <br>
         * Exemplo: AppPerfilUsuarioVO usu = (AppPerfilUsuarioVO)
         * request.getSession().getAttribute( <constante>);
         */
        String USER_INFO_KEY = "USER_INFO";
        
    }
    

    /**
     * jCompany 2.7. Constantes utilizadas em lógicas de impressão
     */
    interface IMPRESSAO {

        /**
         * Indica que a lógica corrente possui regras no momento da impressão
         */
        String IND_LOGICA_IMPRESSAO_KEY = "indImp";

    }


    /**
     * Especializações relacionadas a Form-Beans
     */
    interface FORM {


        /**
         * Campos incluidos automaticamente se for formBeanAutomatico
         */
        interface AUTOMACAO {
        	
            /**
             * Campo que indica o modo em que se encontra uma transação
             */
            String MODO = "modoPlc";

            /**
             * Relacionados a detalhes de lógicas mestre-detalhe
             */
            interface DETALHES {

                /**
                 * Campo que indica para exibir caixa de exclusão de detalhe
                 */
                String EXIBE_IND_EXC_DET_PLC = "exibeIndExcDetPlc";

                /**
                 * Campos que mantem relacao de detalhes por demanda nao inicializados
                 */
				String DETALHE_POR_DEMANDA = "detalhePorDemandaPlc";
            }

        }

        
    }
    
    
    /**
     * jCompany 2.7. Constantes reservadas para uso interno, em utilitários do
     * jCompany.
     */
    interface INTERNOS {
   
        /**
         * Constantes utilizadas no utilitário de aviso on-line
         */
        interface AVISO {

            /**
             * Chave do caching em nível de aplicação para aviso
             */
            String CACHE_AVISO_ONLINE_KEY = "CACHE_AVISO_ONLINE_KEY";

            /**
             * Indica se é para exibir mensagem online para o usuário
             */
            String EXIBE_AVISO_ONLINE = "EXIBE_AVISO_ONLINE";

            /**
             * Indica se exibe mensagem online
             */
            String EXISTE_AVISO_ONLINE = "EXISTE_AVISO_ONLINE";

            /**
             * Guarda a data de expiração gravada no cookie
             */
            String AVISO_ONLINE_EXPIRACAO = "AVISO_ONLINE_EXPIRACAO";

        }

    }
    
    /**
     * jCompany 2.7. Declarações utilizadas em especializações diversas do
     * Validator
     */
    interface VALIDACAO {

        /**
         * Variável de request que guarda o título de cada detalhe, no momento da validação, para exibição
         */
        String TITULO_DETALHE = "TITULO_DETALHE";
    }
    
    /**
     * jCompany 2.7. Declarações utilizadas em lógicas padrões nos
     * Action-Mappings
     */
    interface LOGICAPADRAO {
        
        /**
         * Declarações utilizadas em lógicas padrões de manutenção
         */
        interface MANUTENCAO {

            /**
             * Declara quantas páginas JSPs existem para representar o
             * componente de Mestre, de modo a orientar o layout universal sobre
             * quantos passos totais gerar no assistente de entrada de dados.
             */
            String MESTRE_TOTAL_JSPS = "mestreTotalJspsPlc";
            
            String ESTILO_APRESENTACAO = "estiloApresentacaoPlc";

        }
              

        interface GERAL {

            /**
             * Declaração de action-mapping e variável de request que indica um
             * diretório raiz para páginas jsps referenciadas pelo layout universal
             */
            String DIR_BASE_FCLS = "dirBaseFacelets";

            /**
             * Se o layout do jCompany (PlcVBoxManSelLayout) deve gerar tags de inicio e fim do bloco Ajax.
             * O default é N.
             */
            String AJAX_AUTOMATICO = "ajaxAutomatico";

            /**
             * Se será utilizado Ajax para a lógica corrente. O default é N
             */
            String AJAX_USA = "ajaxUsa";
            
            String IMPRESSAO_INTELIGENTE = "impressaoInteligente";

			
			/**
			 * jCompany 3.0 Modalidade da lógica padrão. Indicar comportamento diferenciado importante dos Application Patterns.
			 * Na versão 3.x somente é utilizado para crudtabular A ou B
			 */
			String MODALIDADE = "modalidade";

            /**
             * Valores possível para modoJanela: <br>- <b>normal </b>: com
             * topos e rodapés <br>- <b>popup </b>: sem topos e rodapés e sem
             * opção de inclusão <br>- <b>popupEdicao </b>: sem topos e rodapés
             * e com opção de manutenção <br>
             */
            interface MODO_JANELA_KEY_VALORES {

                /**
                 * Valor de Indicador de Modo de Abertura de Janela, quando for
                 * Popup. Uso: if (plcMapping.getModoJanela().equals(
                 * <constante>)) {
                 */
                String MODO_JANELA_POPUP = "popup";

                /**
                 * Valor de Indicador de Modo de Abertura de Janela, quando a
                 * janela for Popup e aceitar inclusão de registros neste modo.
                 * Uso: if (plcMapping.getModoJanela().equals( <constante>)) {
                 */
                String MODO_JANELA_POPUP_EDICAO = "popupedicao";


            }
        }
    }
    
    interface PlcJsfConstantes {
    	
     
    	String PLC_MANAGED_BEAN_KEY = "plcManagedBeanKey";
    	String PLC_ACTION_KEY = "plcActionKey";
    	String PLC_MB = "plcAction";
    	String PLC_ENTIDADE = "plcEntidade";
    	String PLC_LOGICA_ITENS = "plcLogicaItens";
    	String PLC_LOGICA_ITENS_ANTERIOR = "plcLogicaItensAnterior";
    	String PLC_ITENS_LINHA = "plcItensLinha";
    	String PLC_ITENS_STATUS = "plcItensStatus";
    	String PLC_LOGICA_ITENS_ARGUMENTO = "argumentos";    	    	
    	String PLC_ITENS_ITEM = "item";
    	String PLC_ITENS_PLC_ITEM = "itensPlc";
    	String PLC_DOMINIOS = "plcDominios";
    	String PLC_CONFIG_URL_COLABORACAO = "plcConfigUrlColaboracao";
    	String PLC_CONTROLE_CONVERSACAO = "plcControleConversacao";
    	String PLC_CONTROLE_REQUISICAO = "plcControleRequisicao";
    	String PLC_CONVERSACAO_ENCERRA_PARAM = "ecPlc";
    	String PLC_LIMPA_CAIXA_EXCLUSAO = "PLC_LIMPA_CAIXA_EXCLUSAO";
    	  
    	/**
    	 * Constantes para Detalhe Paginado
    	 */
    	String DETALHE_PAGINADO = "detalhePaginado";
    	String DETALHE_FILTRO = "detalheFiltro";
    	String PLC_CONTROLE_DETALHE_PAGINADO = "plcControleDetalhePaginado";
    	String PLC_NOME_COLECAO_DETALHE_POSSUI_PAGINACAO = "nomeColecaoDetalheTotalPaginasPlc";
    	
    	/**
    	 * Constantes para Paginaçãod e seleção
    	 */
    	String PLC_CONTROLE_PAGINACAO = "plcControlePaginacao";
    	
    	/**
    	 * Constante colocada no Request com o prefixo da colaboração (sem o sufixo).
    	 */
    	String PLC_PREFIXO_ACTION = "plcAliasPrefixoAction";
    	String PLC_SUFIXO_ACTION = "plcAliasSufixoAction";
    	String PLC_SUFIXO_ENTIDADE_CORRENTE = "plcAliasSufixoEntidadeCorrente";
    	
    	/**
    	 * Constante colocada no Request com o action diferencial incluindo barra inicial.
    	 */
    	String URL_COM_BARRA = "plcURLComBarra";
    	
    	/**
    	 * Usado pela nova logica de layout, como associacao da url com o nome de layout
    	 */
    	String URL_SEM_BARRA = "plcURLSemBarra";

        /**
         * Chave de sessão que mantém a url sem barra inicial, para uso
         * em tag files para links de JSPs de seleção reutilizáveis.
         */
        String PATH_URL_ULT_MANUTENCAO_SEM_BARRA = "actionUltManPlc";    

    	
    	String CONVERSATION_ID_PLC= "conversationIdPlc";    	
    	  	
    	/**
    	 * Constantes de validação
    	 */
    	interface VALIDACAO {
    		//String EVITA_VALIDACAO = "plcEvitaValidacao";
    		String EVITA_VALIDACAO_TABULAR 	= "plcEvitaValidacaoTabular";
    		String EVITA_VALIDACAO_EVENTO_Y = "plcEvitaValidacaoEventoY";
			String FALHA_VALIDACAO	= "falhaValidacao";
			
    	}
    	 
    	interface NAVEGACAO {
    		String NOVO = "novo";
    		String EDITA = "edita";
    		String INICIAL = "inicial";
    		String LOGOUT = "logout";
    	}
    	
    	String PLC_COLECAO_DETALHE_NOME 		= "colecaoNomeDetPlc";
    	
    	String PLC_COLECAO_SUB_DETALHE_NOME 	= "colecaoNomeSubDetPlc";
    	
    	String PLC_IMAGEM_DOWNLOAD 				= "PLC_IMAGEM_DOWNLOAD";
    	
    	/**
    	 * Arquivo padrão para i18n para os componentes
    	 */
    	String BUNDLE_PADRAO_TAGS 		= "ApplicationResources";
		
		String TAB_FOLDER_CAMPOS_FOCO 	= "tabFolderCamposFocoPlc";
		/**
		 * Parâmetro que indica o Módulo Corrente
		 */
		String PLC_MODULO_CORRENTE 		= "plcModuloCorrente";
		
		interface PROPRIEDADES {
			
			String  AUTO_RECUPERACAO_CLASSE_KEY 		= "autoRecuperacaoClasse";
			String  AUTO_RECUPERACAO_PROPRIEDADE_KEY 	= "autoRecuperacaoPropriedade";
			String  AUTO_RECUPERACAO_PROPRIEDADE_VALUE 	= "autoRecuperacaoPropriedadeValue";
			String  PROPS_CHAVE_NATURAL_PLC 			= "propsChaveNaturalPlc";
			String  LOOKUP_VALUE_KEY 					= "lookupValue";
			String  VALUE_KEY 							= "value";
			
				
		}

		
		interface COMPONENTES{
			 String PLC_VINCULADO_TITULO_BOTAO_SEL_POP 		= "jcompany.componente.vinculado.label.popup";
			 String PLC_VINCULADO_TITULO_BOTAO_LIMPAR	 	= "jcompany.componente.vinculado.label.limpar";
		}
		
		interface ACAO{
			/**
			 * Constantes que indicam qual foi a ação realizada
			 */
			 String PLC_IND_ACAO_NOVO 						= "plcIndAcaoNovo";
			 String PLC_IND_ACAO_NOVO_SUBDET 				= "plcIndAcaoNovoSubDetalhe";
 			 String PLC_IND_ACAO_NOVO_DET 					= "plcIndAcaoNovoDetalhe";			 
			 String PLC_IND_ACAO_EXCLUIR 					= "plcIndAcaoExcluir";
			 String PLC_IND_ACAO_EXCLUIR_DET 				= "plcIndAcaoExcluirDetalhe";
			 String PLC_OBJ_AUTO_RECUPERACAO 				= "plcObjAcaoAutoRecuperacao";
			 String PLC_IND_AUTO_RECUPERACAO 				= "plcIndAcaoAutoRecuperacao";
			 String PLC_IND_NAO_VERIFICAR_DETALHE_DEMANDA 	= "naoVerificarDetalheDemanda";
		}
		
		/**
		 * Constante que indica o uso do Google Analytics
		 */
		String GOOGLE_ANALYTICS_USA = "usaGoogleAnalytics";
		
		/**
		 * Constante que indica o uso do Google Analytics
		 */
		String GOOGLE_ANALYTICS_KEY = "keyGoogleAnalytics";
		
		/**
		 * Constante para multiplos arquivos anexados
		 */
		String PLC_NOME_COLECAO_MULTIPLOS_ARQUIVOS = "nomeColecaoMultiplosArquivos";
		
		/**
		 * Constante que indica que a requisição é uma requisição de autorecuperação de vinculado.
		 */
		String PLC_PARTIAL_VINCULADO = "partialVinculado";
    }
    
    /**
     * Constantes de combo aninhado
     */
    interface COMBOANINHADO {
    	String  MAPA		= "mapaComboAninhadoPlc";

    }    
    /**
     * Constantes de opcao de personalizacao de formulario, pele e layout
     */
    interface PERSONALIZA {
    	String PELE_LISTA = "peleListaPlc";
    	String LAYOUT_LISTA = "layoutListaPlc";
    }

	/**
	 * Nome do pocote raiz das Anotações de pacote
	 */
	String PLC_PACOTE_CONFIG_BASE = "com.powerlogic.jcompany.config";

    
    /**
     * Token a ser usado para retornar valor quando um tratamento de erro não precisa ser feito
     */
    String ERRO_SILENCIOSO_PLC = "erroSilPlc";

    String TOTAL_DETALHES_PLC = "totalDetalhes";
    
    /**
     * Valores possíveis para eventos originários da camada controle (Ação Original)
     */
    interface EVENTOS {

        /**
         * Ação de download
         */
        String DOWNLOAD = "download";
        
        /**
         * Desconecta
         */
        String DESCONECTA = "desconecta";

		/**
		 * Clonagem
		 */
		Object CLONA = "clona";

    }
    
    /**
     * jCompany 2.7. Constantes utilizadas em lógicas de fluxo de aprovação
     */
    interface WORKFLOW {

        /**
         * Indica, no request, a ação original (nome do método que inicialmente
         * recebeu a requisição)
         */
        String IND_ACAO_ORIGINAL = "indAcaoOriginal";

        /**
         * Indica que uma operação de exclusão ocorreu ok, no request
         */
        String EXCLUI_ENCERROU_OK = "encerrouExcluiOk";
		
		 /**
         * Indica que uma operação de pequisa ocorreu ok, no request
         */
		String RECUPERA_POR_DEMANDA_ENCERROU_OK = "encerrouRecuperaPorDemandaOk";

        /**
         * Indica que uma ocorreu ok, no request, para qualquer evento
         */
        String ENCERROU_EVENTO_OK = "encerrouEventoOk";

    }


    /**
     * jCompany 2.7. Constantes utilizadas em lógicas de manipulação de Value
     * Objects
     */
    interface VO {

    	/**
    	 * Propriedade que deve ser criada para conter "A"-Ativo, "I"-Invativo ou "P"-Pendente em lógicas
    	 * de aprovação, exclusão lógica ou versionamento.
    	 */
    	String SIT_HISTORICO_PLC = "sitHistoricoPlc";
   		String SIT_INATIVO = "I";
       	String SIT_ATIVO = "A";

       	/**
    	 * Propriedades de auditoria
    	 */
    	String DATA_ULT_ALTERACAO = "dataUltAlteracao";
    	String DATA_CRIACAO = "dataCriacao";
      	String USUARIO_CRIACAO = "usuarioCriacao";
      	String USUARIO_ULT_ALTERACAO = "usuarioUltAlteracao";
    	
    	/**
    	 * Propriedades de auditoria  jALM 
    	 */
      	String[] ALM_AUDIT = {"almClientCompany" , "version" , "lastModified" , "lastModifiedByUserLogin" , "internalState" , "internalStateActiveReference", "createdDateTime", "createdByUserLogin"};
    	String[] ALM_AUDIT_DATE = {"lastModified" , "createdDateTime"};
 

        /**
         * Propriedade utilizada para controle de concorrencia otimista e versionamento
         */
		String VERSAO = "versao";
    	
        /**
         * Utilizado como prefixo para guardar estado de objetos entre leitura e gravação. 
         * O padrão é PREFIXO_OBJ+ <nome da classe sem package>"
         */
        String PREFIXO_OBJ = "objeto";
        
        
        /**
         * Utilizado como sufixo para indicar que um campo tem um atributo transient
         * para trabalhar seu formato 
         */
        String SUFIXO_PROPERTY_TRANSIENT = "Converter";
        /*
         * indExcPlc
         */
        String IND_EXC_PLC = "indExcPlc";
    }
    
    /**
     * Chave do padrão de formulário mantido no request para ajustes visuais
     */
    String FORM_PATTERN = "formPattern";
    
    interface EXCLUSAO {

        String MODO_FISICA = "FISICA";
        

        String MODO_LOGICA = "LOGICA";
        
    }

    /**
     * jCompany 2.7 Constantes para modos diversos
     */
    interface MODOS {
        /**
         * Chave utilizada para lembrar o modo corrente. através de campos
         * hidden nos layouts. Este campo deve ser declarado em todo form-bean.
         */
        String MODO = "modoPlc";

        /**
         * Valor que indica que o modo corrente é de consulta
         */
        String MODO_CONSULTA = "consultaPlc";

        /**
         * Valor que indica que o modo corrente é de edição (ou alteração) de objetos
         */
        String MODO_EDICAO = "alteracaoPlc";

        /**
         * Valor que indica que o modo corrente é de criação de novos objetos
         */
        String MODO_INCLUSAO = "inclusaoPlc";

        /**
         * Valor que indica que o modo corrente é de exclusão de objetos
         */
        String MODO_EXCLUSAO = "exclusaoPlc";

        /**
         * Valor que indica que o modo corrente é de consulta para seleção de registros para a manutenção.
         */
        String MODO_SELECAO_MANUTENCAO = "selmanutencao";

        /**
         * Valor que indica que o modo corrente é de impressão
         */
        String MODO_IMPRESSAO = "impressao";

        String MODO_DOCUMENTACAO = "documentacao";

        String MODO_ASSISTENTE = "assistente";

        /**
         * Valor que indica que o modo corrente é de consulta para seleção de registros
         */
        String MODO_SELECAO_SIMPLES = "selsimples";
    }
    
    
    /**
     * jCompany 3. Anotações Padrões
     */
    interface ANOTACAO {

        String SEPARADOR_QUERY = ".";

        String SUFIXO_QUERYSEL_PADRAO 			= "querySel";
        String SUFIXO_QUERYTREEVIEW_PADRAO 		= "queryTreeView";
        String SUFIXO_QUERYSEL_QBE_PADRAO 		= "querySelQBE";
        String SUFIXO_NAO_DEVE_EXISTIR_PADRAO 	= "naoDeveExistir";
        String SUFIXO_RELATORIO_PADRAO 			= "querySelRel";
        String SUFIXO_QUERYSEL_LOOKUP 			= "querySelLookup";
        String SEPARADOR_FILTRO 				= "_";
		String SUFIXO_QUERYEDITA_PADRAO 		= "queryEdita";

    }
    
    /**
     * 
     */
    interface LOGGERs {

    	String JCOMPANY_QA_PROFILING 				= "com.powerlogic.jcompany_qa.profiling";
    	String JCOMPANY_DOC_AUTOMATIZADA			= "com.powerlogic.jcompany_doc.automatizada.profiling";
    	String JCOMPANY_VISAO						= "com.powerlogic.jcompany.log.visao";
    	String JCOMPANY_CONTROLE					= "com.powerlogic.jcompany.log.controller";
    	String JCOMPANY_MODELO						= "com.powerlogic.jcompany.log.model";
    	String JCOMPANY_PERSISTENCIA				= "com.powerlogic.jcompany.log.persistence";
    	String JCOMPANY_ADVERTENCIA_DESENVOLVIMENTO = "com.powerlogic.jcompany.advertencia";
    
    }
    
}
