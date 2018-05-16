package com.powerlogic.jcompany.config.collaboration;

/**
 * Padroes de formulário
 */
public enum FormPattern {
	/**
	 * Padrão primário para manutenção de ciclo de vida de entidades quando se deseja que manter todos os seus objetos simultaneamente, ou seja,
	 * em uma mesma transação (tanto do usuário quanto do banco de dados).<p>
	 * 
	 * Esta entidade devem ter população estável e tipicamente menos que 100 objetos, pois estes são recuperados como um todo para o 
	 * formulário e mantidos em caching (memória) automaticamente a partir daí pelo jCompany, que também os disponibiliza de 
	 * forma otimizada para montagem de campos em outros formulários que os referenciam (tipicamente combos dinâmicos). <p>
	 * 
	 * Usar para manter entidades que representam "tabelas básicas" (Ex.: UF, Tipo de Cliente, etc.)
	 */		
	Tab,
	/**
	 * Padrão primário para manutenção de ciclo de vida de agregações simples, quando sua entidade raiz não possua composições 1:N (Em UML 1..*). 
	 * Ou seja, quando um formulário/documento corporativo engloba apenas uma entidade e componentes JPA (em suma, uma tabela relacional).<p>
	 * Uma agregação usada em padrão CRUD pode conter, além da entidade raiz, classes de componentes, arquivo anexado e descendentes.<p>
	 */		
	Man,
	/**
	 * Padrão para consulta e seleção de objetos de agregações já persistidas. Esta solução é segmentada em coleta de argumentos (iniciais de campos
	 * texto, intervalo de datas, etc.), pesquisa em si (envio de argumentos e recuperação de lista) e seleção por parte do usuário 
	 * (paginação pelos registros e clique naquele escolhido para edição ou consulta).<p>
	 * 
	 * Este padrão normalmente acompanha os padrão de manutenção primários CRUD, MESTRE_DETALHE e SUBDETALHE (padrões que mantém a entidade
	 * raiz da agregação)
	 */
	Sel,
	/**
	 * Padrão para manutenção de ciclo de vida de de agregações simples, quando as mesmas possuem poucas propriedades e grande volume 
	 * de objetos para entrada de dados. Tipicamente tratam-se de "registro de eventos" (Ex: registros contábeis, de produção, 
	 * etc., com poucos atributos).<p>
	 * Este padrão mescla comportamentos de três outros: uma entrada de dados "tabular" (várias linhas) típica do TABULAR, com argumentos
	 * típicos da SELECAO e manutenção típica do CRUD (sem manter dados em caching como a TABULAR, por exemplo).
	 */
	Ctb,
	/**
	 * Padrão primário para manutenção de ciclo de vida de agregações cuja entidade raiz possua composições 1:N (Em UML 1..*). Ou seja,
	 * quando um formulário/documento corporativo engloba também detalhes ou, em termos relacionais, mais de uma tabela sendo a de detalhe
	 * composta de várias tuplas.<p>
	 * Uma agregação usada em padrão MESTRE_DETALHE também pode conter, além da entidade raiz e composições, classes de 
	 * componentes, arquivo anexado e descendentes.<p>
	 */
	Mdt,
	/**
	 * Padrão primário similar ao de MESTRE_DETALHE mas incluindo um "detalhe do detalhe", ou seja, em linguagem UML uma composição de
	 * composição com multiplicidade 1..* (1:N). Não é recomendada a manutenção de agregações com maior profundidade que esta, 
	 * dada a complexidade que traria para o usuário. Ao se chegar a este ponto, o projetista deve considerar a combinação de padrões
	 * primários (que mantém a entidade raiz da agregação), com os secundários (usados para manter complementações da agregação como 
	 * sub-subdetalhes ou mesmo detalhes ou sub-detalhes simples quando são mantidos por usuários distintos).
	 */
	Mds,
	/**
	 * Padrão secundário por pressupor que a entidade raiz/mestre de uma agregação já está persistida e que portanto somente determinas
	 * entidades de detalhe (composições UML 1..* que não foram persistidas juntamente com a entidade raiz) deverão ser mantidas.
	 */
	Mad,
	/**
	 * Pequena variação do padrão SELECAO, que neste caso indica variações como a não apresentação do botão "Novo" no formulário de seleção
	 * (pois manutenções secundárias não permitem criação de uma agregação "do zero").
	 */
	Smd,
	/**
	 * Padrão secundário similar ao de MANTEM_DETALHE, para quando o detalhe mantido tem também uma composição 1..* 
	 * (ou seja, detalhe do detalhe). Obs.: A SELECAO_MANTEM_DETALHE também atende ao padrão MANTEM_SUBDETALHE.
	 */
	Mas,
	/**
	 * Padrão de colaboração que engloba mecanismos para captura de argumentos do usuário e seu envio posterior para uma 
	 * engine de relatórios (Ex: Eclipse BIRT) renderizar o relatório no formato tipicamente de PDF, DOC ou XLS. 
	 */
	Rel,
	/**
	 * Padrão similar ao de SELECAO, como duas diferenças principais: não há o último passo de selecionar o resultado pesquisado/consultado;
	 * e não há botão de "Novo" no formulário de seleção
	 */
	Con,
	/**
	 * Um dos dois padrões de colaboração "de manutenção de aplicação", que não são usados para manipular não entidades ou agregações 
	 * "de domínio/negócio", mas sim classes que definem configurações de preferências/parametrizações de usuários para uma aplicação.
	 * Neste padrão, cada usuário possui um objeto/tupla em uma entidade/tabela<p>
	 * 
	 * Usar para prover um formulário onde cada usúario possa definir suas preferências ou configurações dinâmicas que 
	 * afetarão o comportamento da aplicação para ele.
	 */
	Usu,
	/**
	 * Um dos dois padrões de colaboração "de manutenção de aplicação", que não são usados para manipular não entidades ou agregações 
	 * "de domínio/negócio", mas sim classes que definem configurações de preferências/parametrizações de usuários para uma aplicação.
	 * Neste padrão,  um usuário privilegiado (administrador) mantém configurações dinâmicas globais
	 * para uma aplicação. Estas configurações ficam armazenada em um único objeto/tupla de uma entidade/tabela<p>
	 * 
	 * Usar para prover um formulário onde um usuário privilegiado (administrador) possa definir suas preferências 
	 * ou configurações dinâmicas que afetarão o comportamento da aplicação para ele e todos os demais usuários.
	 */
	Apl,
	/**
	 * Este é um padrão minimalista, a ser utilizado quando o desenvolvedor deseja grande flexibilidade (controle) sobre a colaboração/formulário.
	 * Na verdade, ainda é considerado um padrão porque não é uma liberação total, pois provê uma convenção para confecção de XHTML markups
	 * em formulário nos leiautes padrões e um botão de disparo (Gravar), cujo evento é tratado nos controladores padrões.<p>
	 * 
	 * Obs.: para uma liberação total, deve-se simplesmente omitir os metadados do jCompany e URLs padrões
	 */
	Ctl, 
	/**
	 * Padrão a ser utilizado quando se desejar delegar a definição para um Extension. Neste caso, deve informar o padrão do Extension
	 * em colaboracaoPadraoPlx.
	 */
	Plx;
	
	public String sufixoUrl() {
        return this.toString();
    }

}
