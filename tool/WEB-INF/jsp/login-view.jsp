<%@ page contentType="text/html; charset=utf-8"
%><%@ page trimDirectiveWhitespaces="false"
%><%
	// タイトル名
	final String title_name = application.getInitParameter("title-name");
	// タイトル別名
	final String title_alias = application.getInitParameter("title-alias");
	// 環境名
	final String environment_name = application.getInitParameter("environment-name");
	// 環境別名
	final String environment_alias = application.getInitParameter("environment-alias");
	// 管理ツールURL
	final String url = (String)request.getAttribute("url");
	// エラーが発生したか
	final boolean has_error = (request.getAttribute("has-error") != null);
%><!DOCTYPE html>
<html lang="ja">
	<head>
		<meta charset="utf-8"/>
		<title>[<%= environment_alias %>] <%= title_alias %> - ログイン</title>
		<link rel="stylesheet" type="text/css" href="<%= url %>/manager.css"/>
		<link rel="shortcut icon" href="<%= url %>/manager.ico"/>
	</head>
	<body>
		<form action="<%= url %>" method="post">
			<div title="タイトル">Title:</div>
			<input class="box odd disabled" type="text" value="<%= title_name %>" title="<%= title_alias %>" disabled/>
			<div title="環境">Environment:</div>
			<input class="box odd disabled" type="text" value="<%= environment_name %>" title="<%= environment_alias %>" disabled/>
			<div title="オペレータ">Operator:</div>
			<input class="box odd" type="text" name="operator"/>
			<div title="パスワード">Password:</div>
			<input class="box odd" type="password" name="password"/>
			<div>
				<input type="submit" name="request-login" value="Login" title="ログイン"/>
			</div>
<%
	if (has_error)
	{
%>			<div class="red" title="ログインに失敗しました。">Login failed.</div>
<%
	}
%>		</form>
	</body>
</html>
