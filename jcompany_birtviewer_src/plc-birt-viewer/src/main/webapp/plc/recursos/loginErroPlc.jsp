<%@ page language="java" session="true" isThreadSafe="false" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8" %>

<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt"%>
<fmt:setBundle basename="jCompanyResources"/>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%--
																
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								

--%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="pt-BR" lang="pt-BR">
<head>
<title>jCompany - Visualizador de Relatório</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
<link rel="StyleSheet" href="<%=request.getContextPath()%>/plc/css/PlcLogin.css" type="text/css" charset="UTF-8" />
<script type="text/javascript">
	function testaCampos()
	{
		var numForms = document.forms.length
		var numElements = 0;
		for(i=0; i < numForms; i++)
		{
			numElements = document.forms[i].elements.length
			for(j=0; j < numElements; j++)
			{
				if(document.forms[i].elements[j].type=="text")
				{
					document.forms[i].elements[j].focus();
					i = numForms;
					j = numElements;
				}
			}
		}
	}
</script>
</head>
<body scroll="no" onload="testaCampos();">
<table width="100%" height="100%" border="0" cellpadding="0" cellspacing="0" style="margin-top:120px;">
	<tr>
		<td valign="middle" align="center">
		<form method="post" action="<%=response.encodeURL("j_security_check")%>">
			<div id="marca">jCompany - Visualizador de Relatório</div>

			<div class="container">
				<img class="alerta" alt="Erros nesta página" src="<%=request.getContextPath()%>/plc/midia/ico.gif"/>
				<div class="erro">
					<fmt:message key="jcompany.erro.senhasNaoConferem"/>
				</div>
			</div>
			<br/>
			<div class="loginContainer">
				<table width="100%" border="0" cellpadding="0" cellspacing="0">				
					<tr>
						<td class="label">
							<fmt:message key="jcompany.login.usuario"/>
						</td>
						<td>
							<input class="loginTexto" name="j_username" type="text">
						</td>
					</tr>
					<tr>
						<td class="label">
							<fmt:message key="jcompany.login.senha"/>
						</td>
						<td>
							<input class="loginTexto" type="password" name="j_password">
						</td>
					</tr>
					<tr>
						<td class="label">
							&nbsp;														
						</td>
						<td>
							<input class="loginBotao" name="btnLogin" type="submit" value="Entrar">
						</td>
					</tr>
				</table>
			</div>
		</form>
		</td>
	</tr>
</table>
</body>
</html>
