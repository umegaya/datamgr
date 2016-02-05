<%@ page contentType="text/html; charset=utf-8"
%><%@ page trimDirectiveWhitespaces="false"
%><%@ page import="jp.enterquest.manager.core.data.Menu"
%><%@ page import="jp.enterquest.manager.core.data.OperatorMenu"
%><%@ page import="jp.enterquest.system.Array"
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
	// メニュー情報リスト
	final Array<Menu.Row> menus = (Array<Menu.Row>)request.getAttribute("menus");
	// オペレータ別メニュー情報リスト
	final Array<OperatorMenu.Row> operator_menus = (Array<OperatorMenu.Row>)request.getAttribute("operator-menus");
%><!DOCTYPE html>
<html lang="ja">
	<head>
		<meta charset="utf-8"/>
		<title>[<%= environment_alias %>] <%= title_alias %> - メニュー</title>
		<link rel="stylesheet" type="text/css" href="<%= url %>/manager.css"/>
		<link rel="shortcut icon" href="<%= url %>/manager.ico"/>
	</head>
	<body>
		<form action="<%= url %>" method="post" style="white-space: nowrap;">
			<input type="hidden" name="operator" value="<%= operator_name %>"/>
			<span title="タイトル">Title:</span>
			<input class="box odd disabled" type="text" value="<%= title_name %>" title="<%= title_alias %>" style="width: 100px;" disabled/>
			<span title="環境">Environment:</span>
			<input class="box odd disabled" type="text" value="<%= environment_name %>" title="<%= environment_alias %>" style="width: 80px;" disabled/>
			<span title="オペレータ">Operator:</span>
			<input class="box odd disabled" type="text" value="<%= operator_name %>" style="width: 80px;" disabled/>
<%
	if (OperatorMenu.getTable().isAnyReadable(operator_menus))
	{
%>			<span title="メニュー">Menu:</span>
			<select class="box odd" name="menu">
<%
		for (final OperatorMenu.Row operator_menu : operator_menus)
		{
			if (operator_menu.isReadable())
			{
				final String menu_name = operator_menu.getMenu();
				final Menu.Row menu = Menu.getTable().findRow(menus, menu_name);
				final String menu_alias = menu.getAlias();
				final String menu_selected = operator_menu.isSelected() ? " selected" : "";
%>				<option value="<%= menu_name %>" label="<%= menu_name %>" title="<%= menu_alias %>"<%= menu_selected %>/>
<%
			}
		}
%>			</select>
			<input type="submit" name="request-change-menu" value="Select" title="メニュー選択"/>
<%
	}
%>			<input type="submit" name="request-password-view" value="Password" title="パスワード変更"/>
			<input type="submit" name="request-logout" value="Logout" title="ログアウト"/>
		</form>
		<hr/>
	</body>
</html>
