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
	// 管理画面URL
	final String url = (String)request.getAttribute("url");
	// オペレータ名
	final String operator_name = (String)request.getAttribute("operator-name");
	// エラーが発生したか
	final boolean has_error = (request.getAttribute("has-error") != null);
%><!DOCTYPE html>
<html lang="ja">
	<head>
		<meta charset="utf-8"/>
		<title>[<%= environment_alias %>] <%= title_alias %> - パスワード変更</title>
		<link rel="stylesheet" type="text/css" href="<%= url %>/manager.css"/>
		<link rel="shortcut icon" href="<%= url %>/manager.ico"/>
	</head>
	<body>
		<form action="<%= url %>" method="post">
			<input type="hidden" name="operator" value="<%= operator_name %>"/>
			<div title="タイトル">Title:</div>
			<input class="box odd disabled" type="text" value="<%= title_name %>" title="<%= title_alias %>" disabled/>
			<div title="環境">Environment:</div>
			<input class="box odd disabled" type="text" value="<%= environment_name %>" title="<%= environment_alias %>" disabled/>
			<div title="オペレータ">Operator:</div>
			<input class="box odd disabled" type="text" value="<%= operator_name %>" disabled/>
			<div title="パスワード">Password:</div>
			<input class="box odd" type="password" name="password"/>
			<div title="新しいパスワード">New Password:</div>
			<input class="box odd" type="password" name="new-password"/>
			<div title="新しいパスワード(確認)">New Password (Confirmation):</div>
			<input class="box odd" type="password" name="new-password-confirmation"/>
			<div>
				<input type="submit" name="request-change-password" value="Change" title="パスワード変更"/>
				<input type="submit" name="request-menu-view" value="Back" title="戻る"/>
			</div>
<%
	if (has_error)
	{
%>			<div class="red" title="パスワード変更に失敗しました。">Change password failed.</div>
<%
	}
%>		</form>
	</body>
</html>
