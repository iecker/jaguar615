/**
 *
 * Gera o Log das gera��es realizadas
 *
 * Utiliza��o.: Alterar qualquer arquivo texto, inserindo o conte�do definido em um arquivo velocity.
 * 
 * Utilizado a partir de uma <acao> definida no xml de padr�es...
 * 
 * Ex.:
 *  <acao>
 *		<tipo-acao>groovy/comuns/PlcGerarLog</tipo-acao>
 *	</acao>
 * 
 * Onde:
 * <tipo-acao> � o nome do arquivo Groovy sem extens�o
 * 
 * Obs.:
 * N�o esquecer de definir os Pontos de inser��o.
 * Esses pontos, podem variar, dependendo da maneira que o coment�rio se comporta no arquivo escolhido.
 */


import com.powerlogic.gerador.GeradorVelocity;
import com.powerlogic.jcompany.geradordinamico.helper.PlcGeradorHelper;
import groovy.swing.SwingBuilder
import java.awt.FlowLayout as FL
import javax.swing.BoxLayout as BXL


def diretorioArqDestino = acao.getDiretorioArqDestino();
def projetoDestino = padrao.getNomeProjeto();
def date = new Date().getDateTimeString().replaceAll(" ","").replaceAll("/","").replaceAll(":","");

PlcGeradorHelper.getInstance().registraLog("Gravando arquivo de log em: " + gerador.pastaGeracao + File.separator + projetoDestino + diretorioArqDestino + "log_extension_" + date + ".txt");
println("Gravando log: " + gerador.pastaGeracao + File.separator + projetoDestino + diretorioArqDestino + "log_extension_" + date + ".txt");


def s = new SwingBuilder()
s.setVariable('myDialog-properties',[:]) //-- 1 --//
def vars = s.variables //-- 2 --//
def dial = s.dialog(title:'Log de Gera��o',id:'myDialog',modal:true) { //-- 3 --//
    panel() {
        boxLayout(axis:BXL.Y_AXIS)
        panel(alignmentX:0f) {
            flowLayout(alignment:FL.LEFT)
            textArea(id:'name',columns:80,rows:20,text:PlcGeradorHelper.getInstance().getLog("Log de gera��o do extension... ", ""),autoscrolls:true ) //-- 4 --//
        }

        panel(alignmentX:0f) {
            flowLayout(alignment:FL.LEFT)
            button('OK',preferredSize:[80,24],
                   actionPerformed:{
                       vars.dialogResult = 'OK' //-- 5 --//
                       dispose()
            })
        }
    }
}


File file = new File(gerador.pastaGeracao + projetoDestino + diretorioArqDestino + "log_extension_" + date + ".txt");


FileWriter fwriter = new FileWriter(file);
fwriter.write (PlcGeradorHelper.getInstance().getLog("Log de gera��o do extension... ", ""));

PlcGeradorHelper.getInstance().limpaLog();

fwriter.flush();
fwriter.close();

dial.pack()
dial.show()