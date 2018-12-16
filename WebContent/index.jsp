<%@page import="org.apache.catalina.Session"%>
<%@ include file="/include.jsp"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%-- <spring:message code=""/> --%>
<html>
<head>
<title>Welcome to Festival Event Registration System</title>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<link rel="StyleSheet" href="css/struts2.css" type="text/css" />
<script language="JavaScript">
<!--
var nHist = window.history.length;
if(window.history[nHist] != window.location)
  window.history.forward();
//-->
</script>

<script type="text/javascript">
function validateForm()
{
var uname=document.forms["logForm"]["USERNAME"].value;
var password=document.forms["logForm"]["PASSWORD"].value;
if (uname==null || uname=="")
{
alert("Please provide Username");
return false;
}
if (password==null || password=="")
{
alert("Please provide Password");
return false;
}
}
</script>
</head>
<style>
.error {
	color: red;
	font-size: 13px; 
	font-weight: bold;
}
</style>

<body>
<br/><br/><br/><br/><br/><br/>
<%
	HttpSession session=request.getSession(true);
	session.invalidate();
%>
<form method="post" name="logForm" action="searchVisitor.htm" onsubmit="return validateForm()">
<table width="80%" align="center" border="2" bordercolor="#339999">
	<tr>
		<td align="Center">
		<div id="header">&nbsp;
		<div align="center"><spring:message code="app.lang.title"/></div>
		</div>
		<!-- header end -->
		<br/>
		<table>
			<tr>
				<!--content begin -->
				<td colspan="2" align="center">
				<div id="content">
				<h3><spring:message code="app.lang.portal"/></h3>
				</div>
				</td>
			</tr>
			<tr>
				<td><spring:message code="app.lang.visitor"/></td>
				<td><input type="text" name="USERNAME"></td>
			</tr>
			<tr>
				<td><spring:message code="app.lang.pass"/></td>
				<td><input type="password" name="PASSWORD">
				</td>
			</tr>
			<tr>
				<td colspan="2" align="right"><input type='submit' value="<spring:message code="app.lang.submit"/>"></input> <br />
				</td>
			</tr>
			<tr>
				<td colspan="2" align="center"><span class="error">${ERROR}</span></td>
			</tr>
			<tr>
				<td></td>
			</tr>
			<tr>
				<td></td>
			</tr>
			<tr>
				<td>
				<div id="content"><spring:message code="app.lang.newvisitor"/>:</div>
				<div id="content"><a href="/FestivalPortalR2_Participant/registration.jsp"><spring:message code="app.lang.registerhere"/></a></div>
				</td>
			</tr>
		</table>
		<br />
		</td>
	</tr>
</table>
<center><a href="?lang=en"><spring:message code="app.lang.english"/></a> <a href="?lang=hi"><spring:message code="app.lang.hindi"/></a> <a href="?lang=cn"><spring:message code="app.lang.chinese"/></a></center>
</form>

</body>

</html>
