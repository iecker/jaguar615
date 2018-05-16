/**
 * Arquivo geral de funções do extension Many-to-Many Matrix.
 * @author Mauren Ginaldo Souza
 * @version 1.0
 * @since out/2010 jCompany 6.0
 * 
 */

	plc.jq(function(){
		
		var listaEntidades = {}, listaEntidadesId = {}, hash_acoes = {};
		
		/**
		 * Realiza a inclusão / marcação de um objeto na lista de acções
		 */
		function marcaMatrix() {
			classe = plc.jq(this).attr('class');
			if (classe == 'marcado') {
				classe = 'desmarcando';
				hash_acoes[this.id] = false;
			} else if (classe == 'desmarcando') {
				classe = 'marcado';
				hash_acoes[this.id] = true;
			} else if (classe == 'desmarcado') {
				classe = 'marcando';
				hash_acoes[this.id] = true;
			} else if (classe == 'marcando') {
				classe = 'desmarcado';
				hash_acoes[this.id] = false;
			}	
			plc.jq(this).attr('class', classe);
		}

		/**
		 * Verifica se existe um objeto dentro da lista
		 */
		function listaSearch(objeto, lista){
			for(var i in lista){
				if(i==objeto) {
					return true;
				}    
			}
			return false;
		}
		
		/**
		 * Retorna o Caso de Uso
		 */
		function retornaCasoDeUso() {
			return plc.jq('#casodeuso').val() + 'mmm';
		}	

		/**
		 * Monta a lista final com as associações/desassociações a realizar
		 */
		function confirmaMatrix() {
			plc.partialLoading();
			
			//preenche a lista com as alterações
			var associacoes = [];
			for (var val in hash_acoes) {
				if (hash_acoes[val] == true) {
					// se ja existir na lista desconsidera!
					if (!listaSearch(val, listaEntidades)) {
						associacoes.push('associacao='+ val + ',true');  
					}	
				} else {
					// se nao tiver na lista desconsidera!
					if (listaSearch(val, listaEntidades)) {
						associacoes.push('associacao='+ listaEntidadesId[val] + ',false');
					}
				}
			}
			
			//Pega os parametros de filtro
			//Preciso deles para apos a gravacao realizar a montagem da tabela
			var params = recuperaObjetoFiltro();

			//realiza a chamada Rest para atualizar os dados
			plc.jq.ajax({
				url:  plcGeral.contextPath + '/soa/service/matrix(' + retornaCasoDeUso() +')?'+associacoes.join('&') + '&' + params.join('&')
				,type:'PUT' 
				,dataType:'json'	
				,success:function(resultado){
					plc.partialLoading(false);
					// Montagem e marcação da tabela
					montaTabela(resultado)
					marcaTabela(resultado.entidadeAssociativa);
				}
				,error: function(){
					plc.partialLoading(false);
					plc.mensagem("erro","Erro ao atualizar dados.");
				}		
			});
			// Limpa o hash de acoes
			hash_acoes = {};
		}
		
		/**
		 * Realiza a montagem da tabela, linha, coluna e marcações no interior
		 */
		function montaTabela(resultado) {

			var listaEntidade1 = resultado.entidade1;
			var listaEntidade2 = resultado.entidade2;
			var texto = '';
			texto = texto +	'<tr><td class="colunaVertical"><span class="vertical">' /**+ resultado.tituloEntidade1 + ' \ ' + resultado.tituloEntidade2*/ + '</span></td>';
			// verificando se existe dados nas listas
			if (listaEntidade1.length == 0 || listaEntidade2.length == 0) {
				texto = texto +	'</tr>';
			} else {
				for (var s=0; s<listaEntidade2.length; s++){
					texto = texto +	'<td class="colunaVertical"><span class="vertical">' + listaEntidade2[s].lookup + '</span><br/></td>';
				}
				texto = texto +	'</tr>';
				for (t=0; t<listaEntidade1.length; t++){
					texto = texto +	'<tr><td>' + listaEntidade1[t].lookup + '</td>';
					var id_checkbox;
					for (s=0; s<listaEntidade2.length; s++){
						id_marcacao = listaEntidade1[t].id + '_' + listaEntidade2[s].id;
						texto = texto +	'<td align="center"><span class="desmarcado"  id="' + id_marcacao + '" \></td>';
					}
					texto = texto +	'</tr>';
				}
			}	
			tabela = document.getElementById('mm_matrix');
			plc.jq('#mm_matrix').empty();
			plc.jq('#mm_matrix').append(texto);
			plc.jq('#mm_matrix span').click(marcaMatrix);
		}	
		
		/**
		 * Realiza a marcação no interior da tabela dos itens associados
		 */
		function marcaTabela(entidadeAssociativa) {
			listaEntidades = [];
			listaEntidadesId = [];
			
			for (i=0; i<entidadeAssociativa.length; i++){
				listaEntidades[entidadeAssociativa[i].lookup] = true;
				listaEntidadesId[entidadeAssociativa[i].lookup] = entidadeAssociativa[i].id;
				var id = '#' + entidadeAssociativa[i].lookup;
				//plc.jq(id).attr('class', marcado);
				plc.jq(id).removeClass("desmarcado");
				plc.jq(id).addClass("marcado");
			}	
		}
		
		/**
		 * Monta um objeto com os parametros de filtro do formulario
		 */
		function recuperaObjetoFiltro() {
		
			var params = [];
			//captura os dados do form
			var formData = plc.form2json('#corpo\\:formulario', 'corpo:formulario:');
			plc.partialLoading();
			
			// monta os parâmetros
			plc.jq.each(formData, function(k, v){
				params.push(escape(k)+'='+escape(v));
			});
			return params;
		}
		
		/**
		 * Realiza a filtragem dos dados buscando as listas via rest
		 */
		function filtraMatrix() {

			var params = recuperaObjetoFiltro();
			// faz a chamada buscando a lista
			plc.jq.ajax({
				url: plcGeral.contextPath + '/soa/service/matrix(' + retornaCasoDeUso() + ')'
				,type:'GET'
				,data: params.join('&')
				,dataType:'json'	
				,success:function(resultado){
					plc.partialLoading(false);
					plc.mensagem(false);
					// Montagem e marcação da tabela
					montaTabela(resultado);
					marcaTabela(resultado.entidadeAssociativa);
				}	
				,error: function(){
					plc.partialLoading(false);
					plc.mensagem("erro", "Erro ao processar pesquisa.");
				}	
			});
		}
		
		plc.jq('#filtra').click(filtraMatrix);
		plc.jq('#confirma').click(confirmaMatrix);
		
	});