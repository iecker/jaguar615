<%@ page   import="java.util.Map" %>
<%@ page   import="java.util.Iterator" %>

<%@ taglib uri="/WEB-INF/fmt.tld" prefix="fmt" %>
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
		<title><fmt:message key="jcompany.error.597.titulo"/></title>
		<meta http-equiv="Content-Type" content="text/html;" />
		<link href="<%=request.getContextPath()%>/plc/erros/erros.css" rel="stylesheet" type="text/css" />
	</head>
<body class="erros" marginheight="0" marginwidth="0" style="background-image: url(${pageContext.request.contextPath}/plc/erros/error_500.jpg)">
	<h1><fmt:message key="jcompany.error.597.titulo"/></h1>	
	<p><fmt:message key="jcompany.error.597.saudacao"/></p>
	<h4><fmt:message key="jcompany.error.597.solucao1"/></h4>
	<hr color="#B70C0C" size="1" noshade width="100%" />
</body>
</html>