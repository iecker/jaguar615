<%@ page  contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<!DOCTYPE html
PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<fmt:setBundle basename="jCompanyResources"/>
<%--
																
	    			       Jaguar-jCompany Developer Suite.																		
			    		        Powerlogic 2010-2014.
			    		    
		Please read licensing information in your installation directory.Contact Powerlogic for more 
		information or contribute with this project: suporte@powerlogic.com.br - www.powerlogic.com.br																								

--%>
<html>
	<head>
		<title><fmt:message key="jcompany.error.599.titulo"/></title>
		<meta http-equiv="Content-Type" content="text/html;charset=UTF-8" />
		<link href="<%=request.getContextPath()%>/plc/erros/erros.css" rel="stylesheet" type="text/css" />
	</head>
<body class="erros" marginheight="0" marginwidth="0" style="background-image: url(<%=request.getContextPath()%>/plc/erros/error_500.jpg)">
	<h1><fmt:message key="jcompany.error.599.titulo"/></h1>	
	<p><fmt:message key="jcompany.error.599.saudacao"/></p>
	<h4><c:out value="${requestScope['javax.servlet.error.message']}" escapeXml="false"/></h4>
	<p><c:out value="${requestScope.erro599}" escapeXml="false"/>
		
	<p><fmt:message key="jcompany.error.599.solucao1"/><fmt:message key="jcompany.error.599.solucao2"/></p>
	<hr color="#B70C0C" size="1" noshade width="100%" />
</body>
</html>

