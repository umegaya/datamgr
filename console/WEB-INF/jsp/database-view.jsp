<%@ page contentType="text/html; charset=utf-8"
%><%@ page trimDirectiveWhitespaces="false"
%><%@ page import="jp.enterquest.manager.core.data.InformationColumn"
%><%@ page import="jp.enterquest.manager.core.data.InformationKey"
%><%@ page import="jp.enterquest.manager.core.data.ManagedColumn"
%><%@ page import="jp.enterquest.manager.core.data.ManagedDatabase"
%><%@ page import="jp.enterquest.manager.core.data.ManagedTable"
%><%@ page import="jp.enterquest.manager.core.data.Menu"
%><%@ page import="jp.enterquest.manager.core.data.OperatorColumn"
%><%@ page import="jp.enterquest.manager.core.data.OperatorDatabase"
%><%@ page import="jp.enterquest.manager.core.data.OperatorMenu"
%><%@ page import="jp.enterquest.manager.core.data.OperatorTable"
%><%@ page import="jp.enterquest.manager.core.data.Relation"
%><%@ page import="jp.enterquest.system.Array"
%><%@ page import="jp.enterquest.system.Data"
%><%@ page import="jp.enterquest.system.Hash"
%><%@ page import="jp.enterquest.system.HtmlEncoder"
%><%@ page import="jp.enterquest.system.SqlOrder"
%><%@ page import="jp.enterquest.system.ConsoleLogger"
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
	// ベースURL
	final String baseUrl = (String)request.getAttribute("baseUrl");
	// オペレータ名
	final String operator_name = (String)request.getAttribute("operator-name");
	// メニュー情報リスト
	final Array<Menu.Row> menus = (Array<Menu.Row>)request.getAttribute("menus");
	// オペレータ別メニュー情報リスト
	final Array<OperatorMenu.Row> operator_menus = (Array<OperatorMenu.Row>)request.getAttribute("operator-menus");
	// データベース情報リスト
	final Array<ManagedDatabase.Row> databases = (Array<ManagedDatabase.Row>)request.getAttribute("databases");
	// オペレータ別データベース情報リスト
	final Array<OperatorDatabase.Row> operator_databases = (Array<OperatorDatabase.Row>)request.getAttribute("operator-databases");
	// テーブル情報リスト
	final Array<ManagedTable.Row> tables = (Array<ManagedTable.Row>)request.getAttribute("tables");
	// オペレータ別テーブル情報リスト
	final Array<OperatorTable.Row> operator_tables = (Array<OperatorTable.Row>)request.getAttribute("operator-tables");
	// 選択中かどうか
	final Boolean selected = (Boolean)request.getAttribute("selected");
	// 選択中のデータベース情報
	final ManagedDatabase.Row selected_database = (ManagedDatabase.Row)request.getAttribute("selected-database");
	// 選択中のオペレータ別データベース情報
	final OperatorDatabase.Row selected_operator_database = (OperatorDatabase.Row)request.getAttribute("selected-operator-database");
	// 選択中のテーブル情報
	final ManagedTable.Row selected_table = (ManagedTable.Row)request.getAttribute("selected-table");
	// 選択中のオペレータ別テーブル情報
	final OperatorTable.Row selected_operator_table = (OperatorTable.Row)request.getAttribute("selected-operator-table");
	// カラム情報リスト
	final Array<ManagedColumn.Row> columns = (Array<ManagedColumn.Row>)request.getAttribute("columns");
	// オペレータ別カラム情報リスト
	final Array<OperatorColumn.Row> operator_columns = (Array<OperatorColumn.Row>)request.getAttribute("operator-columns");
	// システムカラム情報リスト
	final Array<InformationColumn.Row> information_columns = (Array<InformationColumn.Row>)request.getAttribute("information-columns");
	// システムキー情報リスト
	final Array<InformationKey.Row> information_keys = (Array<InformationKey.Row>)request.getAttribute("information-keys");
	// ロウ全件数
	final Integer row_total_count = (Integer)request.getAttribute("row-total-count");
	// ロウ抽出件数
	final Integer row_filter_count = (Integer)request.getAttribute("row-filter-count");
	// ロウリスト
	final Data rows = (Data)request.getAttribute("rows");
	// リレーション情報リスト
	final Array<Relation.Row> relations = (Array<Relation.Row>)request.getAttribute("relations");
	// リレーションカラム情報リスト
	final Array<ManagedColumn.Row> relation_columns = (Array<ManagedColumn.Row>)request.getAttribute("relation-columns");
	// リレーションロウリスト
	final Hash<String,Data> relation_rows = (Hash<String,Data>)request.getAttribute("relation-rows");
	// 読み込み可否
	final boolean readable = (selected && selected_operator_database.isReadable() && selected_operator_table.isReadable());
	// 書き込み可否
	final boolean writable = (readable && selected_operator_database.isWritable() && selected_operator_table.isWritable());
	// ダウンロード可否
	final boolean downloadable = (readable && selected_operator_database.isDownloadable() && selected_operator_table.isDownloadable());
	// アップロード可否
	final boolean uploadable = (writable && selected_operator_database.isUploadable() && selected_operator_table.isUploadable());
%><!DOCTYPE html>
<html lang="ja">
	<head>
		<meta charset="utf-8"/>
		<title>[<%= environment_alias %>] <%= title_alias %> - データベース管理</title>
		<link rel="stylesheet" type="text/css" href="<%= baseUrl %>/css/reset.css"/>
		<link rel="stylesheet" type="text/css" href="<%= baseUrl %>/css/manager.css"/>
<%
		String css_name = "color-product";
		if      (environment_name.equals("local")) css_name = "color-develop";
		else if (environment_name.equals("staging")) css_name = "color-stage";
%>
		<link rel="stylesheet" type="text/css" href="<%= baseUrl %>/css/<%= css_name %>.css"/>
		<link rel="shortcut icon" href="<%= baseUrl %>css/manager.ico"/>
		<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
		<script type="text/javascript">
<%
/*
	====================================================================================================
	====================================================================================================
	====================================================================================================
	====================================================================================================
	====================================================================================================
*/
%>

$(function() {
	$("#show_search_menu").click(function() {
		$("div.search-area").toggle(300);
	});
	var update_index = 0;
	var da = $('div.data-area');
	var wa = $('div.write-area');
	var daf = da.find("form");
	var waf = wa.find("form");
	var daiu = da.find("input[name=request-update]");
	var daid = da.find("input[name=request-delete]");
	var daii = da.find("input[name=request-insert]");
	var iau = $("#all-update");
	var ag = $("#apply-game");
	var ds = $('.dyn-select');
	var hovers = $('[title]');

	daf.bind("jsubmit", function( e, input ) {
		var cinput = $(input);
		var formdata = $(this).serialize();
		cinput.attr('disabled', 'disabled');
		cinput.parents("tr").addClass('update-requesting');
		$.ajax({
			url: this.action,
			data: formdata,
			cache: false,
			processData: false,
			contentType : "application/x-www-form-urlencoded",
			type: 'POST',
			dataType: 'text',
			success: function (data, status, req) {
				console.log('成功:' + data.length);
				cinput.parents("tr").removeClass('update-requesting');
				if (data.length > 0)
				{
					cinput.removeAttr('disabled');
					cinput.parents("tr").removeClass('update-error');
					cinput.parents("tr").removeClass('dirty');
					if (cinput.val() == "Delete")
					{
						cinput.parents("tr").hide('slow', function(){ $(this).remove(); });
					}
				}
				else
				{
					cinput.parents("tr").addClass('update-error');
				}
			},
			error: function (req, status, error) {
				console.log('失敗');
				cinput.parents("tr").removeClass('update-requesting');
			}
		});
	});
	$('div.write-area').find('input[type=text], input[type=checkbox], select, textarea').keydown(function (e) {
		if (e.keyCode != 13) return true;
		if (e.ctrlKey || e.altKey || e.shiftKey) return true;
		e.preventDefault();
		var elms = $(this).parents("tr").find("input:not(:disabled):visible, select, textarea");
		elms[elms.index(this) + 1].focus();
		return false;
	});
	//現在送信したいデータを作業用フォームにコピーする.
	//serialize == trueだとserializeしてくれる.
	function attach_current_form(elem, attach_to, form, serialize, empty_values) {
		form.empty();
		var parent = elem.parents("tr");
		parent.find("input").each(function (){
			if (this.type == "button") return;
			var inp = $(this).clone().val($(this).val());
			form.append(inp);
			if (empty_values && ($(this).attr('type') == 'checkbox')) {
				//checkboxはbooleanとして振舞うように値を設定する.
				empty_values[$(this).attr('name')] = $(this).prop('checked');
			}
		});
		parent.find("select, textarea").each(function (){
			form.append($(this).clone().val($(this).val()));
			if (empty_values && $(this).hasClass('dyn-select') && !$(this).val()) {
				//relationがあり、値が入っていない場合はnullとして扱う必要がある
				empty_values[$(this).attr('name')] = null;
			}
		});
		form.append(attach_to);
		if (serialize) {
			return form.serialize();
		}
		return null;
	}
	function formdata_to_row(formdata, empty_values) {
		var ret = {};
		formdata.replace(/([^=&]+)=([^=&]*)/gm, function (m, k, v) {
			if (k.startsWith("column%3A")) {
				k = k.replace(/^column%3A/, "");
				ret[k] = v;
			}
		});
		//empty_valuesがあれば、それで上書きする.
		if (empty_values) {
			//empty_valuesのkeyはattr('name')なのでurlencodeされていない.
			for (var k in empty_values) {
				var kk = k.replace(/^column:/, "");
				ret[kk] = empty_values[k];
			}
		}
		return ret;
	}
	var no_column_key = ["operator", "database", "table"];
	function row_to_formdata(row) {
		var data = [];
		for (var k in row) {
			var v = row[k];
			//console.log("kv:" + k + "|" + v + "|" + typeof(v));
			if (k.startsWith("request") || no_column_key.indexOf(k) >= 0) {
			} else {
				k = "column:" + k;
			}
			if (v === null) {
				v = "";
			} else if (v === false) {
				//checkboxがoffだったということなのでformdataに含めない.
				continue
			} else if (v === true) {
				v = 1;
			}
			data.push(encodeURIComponent(k)+"="+encodeURIComponent(v));
		}
		return data.join('&');
	}
	function verify_formdata(elem, attach_to, form, successCB, errorCB) {
		var empty_values = {};
		var formdata = formdata_to_row(attach_current_form($(elem), daiu, form, true, empty_values), empty_values);
		if ("<%= selected_operator_database.getDatabase() %>" == "manager") {
			successCB(formdata);
			return;
		}
		//console.log("formdata = " + JSON.stringify(formdata));
		$.ajax({
			url: '<%= url %>',
			contentType : "application/x-www-form-urlencoded",
			type: 'POST',
			dataType: 'text',
			data: {
				"proxy-request":"verify row", 
				"url":"http://cmt:8888/verify", 
				"operator":"<%= operator_name %>", 
				"data": encodeURIComponent(JSON.stringify({
					table:"<%= selected_table.getName() %>",
					row: formdata,
				})),
			},
			success: function (data, status, req) {
				$('.loading').removeClass("show");
				if (data.indexOf("error") >= 0) {
					errorCB(data);
				}
				else {
					successCB(formdata);
				}
			},
			error: function (req, status, error) {
				$('.loading').removeClass("show");
				errorCB(error);
			}
		});		
	}
	var numericRegex = /[0-9\.\b\x25\x26\x27\x28\x09]/;
	da.find('input[type=text], input[type=checkbox], select, textarea').keydown(function (e) {
		if (e.ctrlKey || e.altKey) {
			var fact = 1;
			var elms = null;
			switch (e.keyCode) {
				case 37:
					fact = -1;
				case 39:
					elms = da.find('input[type=text], input[type=checkbox], select, textarea');
					break;
				case 38:
					fact = -1;
				case 40:
					elms = da.find('*[name="' + this.name + '"]');
					break;
			}
			if (elms != null)
			{
				e.preventDefault();
				var i = elms.index(this) + fact;
				if (i >= elms.length) i = 0;
				if (i < 0) i = elms.length - 1;
				elms[i].focus();
				return false;
			}
		}
		var ret = false;
		if (e.altKey || e.shiftKey) ret = true;
		if (e.keyCode != 13) ret = true;
		if ($(this).attr("type") == "button") ret = true;
		if ($(this).hasClass("numeric") && !numericRegex.test(String.fromCharCode(e.keyCode))) {
			console.log("numeric but non numeric input");
			e.preventDefault();
			return false;
		}
		if (ret) return true;

		e.preventDefault();
		if (e.ctrlKey)
		{
			$(this).parents("tr").find("input.update").click();	
		}
		else
		{
			var elms = $(this).parents("tr").find("input:not(:disabled):visible, select, textarea");
			elms[elms.index(this) + 1].focus();
		}
		return false;
	}).on("focus", function (e) {
		$(this).css({
			shadow: '0 0 30px #44f'
		}).animate({
			shadow: '0 0 0 #fff'
		}); 
	}).on("change", function (e, changed) {
		var form = $(this).parents("tr");
		var input = $(form).find("input.update");
		verify_formdata(input, daiu, daf, function (row) {
			form.removeClass('update-error');
			form.addClass('dirty');
			form.removeAttr("title");
		}, function (err) {
			console.log("data verification fails: " + err);
			form.removeClass('dirty');
			form.addClass('update-error');
			form.attr("title", err);
		});
	});
	da.find("input.update").click(function (e) {
		attach_current_form($(this), daiu, daf);
		daf.trigger("jsubmit", this);
	});
	wa.find("input.insert").click(function (e) {
		e.preventDefault();
		var form = $(this).parents("tr");
		verify_formdata($(this), daii, waf, function (row) {
			form.removeClass('update-error');
			form.addClass('dirty');
			form.removeAttr("title");
			row["request-insert"] = "insert";
			row["operator"] = "<%= operator_name %>";
			row["database"] = "<%= selected_database.getName() %>";	
			row["table"] = "<%= selected_table.getName() %>";
			$.ajax({
				url: '<%= url %>',
				data: row_to_formdata(row),
				cache: false,
				processData: false,
				contentType : "application/x-www-form-urlencoded",
				type: 'POST',
				dataType: 'text',
				success: function (data, status, req) {
					console.log('成功:' + data.length);
					location.reload();
				},
				error: function (req, status, error) {
					console.log('失敗');
					location.reload();
				}
			});
		}, function (err) {
			console.log("insert: data verification fails: " + err);
			form.removeClass('dirty');
			form.addClass('update-error');
			form.attr("title", err);
		});
	});
	da.find("input.delete").click(function (e) {
		if (! confirm('Are you sure you want to delete this row?\nこの行を削除してもよろしいですか？')) return;

		attach_current_form($(this), daid, daf);
		daf.trigger("jsubmit", this);
	});
	iau.bind("update_next", function(ev, dady, cb) {
		var btns = $(dady).find("input.update");
		var max = btns.size();
		if (da.find(".update-requesting").size() == 0) {
			if (update_index < max) {
				var btn = $(btns[update_index]);
				btn.click();
				update_index += 1;
			}
			else {
				alert("Update done.");
				cb && cb();
				return;
			}
		}
		setTimeout(function (){ iau.trigger("update_next", [dady, cb]); }, 10);
	});
	iau.click(function() {
		if (! confirm('Are you sure you want to update all row?\n全ての行を保存してもよろしいですか？')) return;
		dady = da.find("tr.dirty");
		update_index = 0;
		$(this).trigger("update_next", [dady]);
	});
	ag.click(function() {
		$.ajax({
			url: '<%= url %>',
			data: '{ "request":"debug", "type":"UpdateCache", "int1":0 }',
			cache: false,
			processData: false,
			contentType : "application/x-www-form-urlencoded",
			type: 'POST',
			dataType: 'text',
			success: function (data, status, req) {
				console.log('成功');
				alert('data update applied/データの更新を反映させました。');
			},
			error: function (req, status, error) {
				console.log('失敗');
			}
		});
	});
	ds.focus(function () {
		var table = $(this).attr("linked_table");
		var nullable = $(this).attr("nullable");
		//console.log("TODO: オプション要素をロードして中に入れ込む:" + table);
		if (table != null) {
			var elem = $(this).children();
			var value = elem.attr("value");
			var options = $("#option_cache_" + table + nullable).children().removeAttr("selected");
			$(this).html(options);
			if (elem.length > 0) {
				var matched = $(this).children("[value=" + value + "]");
				console.log("matchlen:" + matched.length + "|" + value);
				if (matched.length > 0) {
					matched.attr("selected", true);
				}
			}
		}
	})
	ds.blur(function () {
		//console.log("TODO: オプション要素をあんロード");		
		var selected = $(this).children(":selected").clone();
		var table = $(this).attr("linked_table");
		var nullable = $(this).attr("nullable");
		if (table != null) {
			$("#option_cache_" + table + nullable).html($(this).children());
		}
		$(this).html(selected);
	})
	hovers.hover(function () {
		var value = $(this).attr("title")
		if (value != null) {
			var explain = $('#note-' + value).attr("value");
			$(this).attr("title", explain);
			$(this).attr("titleprev", value);
		}
	}, function () {
		var value = $(this).attr("titleprev");
		if (value != null) {
			$(this).attr("title", value);
		}
	})
});

// jsonとcsvを適用する前に確認を行うダイアログを表示
function upload_check(id){
	var file_list = document.getElementById(id).files;

	if(file_list.length == 0){
		alert('ファイルが選択されていません');
		return false;
	}

	var list = "";
	for(var i = 0; i < file_list.length; i++){
		list += file_list[i].name;
	}

	var message = list + 'を適用します、よろしいですか？';
	return confirm(message);
}

function on_pull_request() {
	if (!confirm("現在の修正内容でpull requestを作成します。よろしいですか？")) return;
	$('#pull-request').addClass("disabled");
	$('.loading').addClass("show");
	$.ajax({
		url: '<%= url %>',
		contentType : "application/x-www-form-urlencoded",
		type: 'POST',
		dataType: 'text',
		data: {"proxy-request":"pull request", "url":"http://cmt:8888/commit/<%= operator_name %>", "operator":"<%= operator_name %>"},
		success: function (data, status, req) {
			$('.loading').removeClass("show");
			if (data.match(/^error:/)) {
				alert(data);
			}
			else {
				var match = data.match(/https:\/\/github.com\/.*?\/pull\/[0-9]+/);
				if (match) {
					alert("pull requestが作成されました:" + match[0]);
					window.open(match[0]);
				}
				else {
					alert("error:" + data);
				}
			}
		},
		error: function (req, status, error) {
			$('.loading').removeClass("show");
			console.log('失敗' + error);
		}
	});

}

// サーバー側の更新チェック。更新されていたら未保存分を保存してからページをリロードする.
var updateInfo = {
	version: null,
	error: false,
	confirm: false,	
	onajax: false,
};
setInterval(function () {
	if (!updateInfo.onajax) {
		$.ajax({
			url: '<%= url %>',
			contentType : "application/x-www-form-urlencoded",
			type: 'POST',
			dataType: 'text',
			data: {"proxy-request":"version check", "url":"http://cmt:8888/version", "operator":"<%= operator_name %>"},
			success: function (data, status, req) {
				console.log(JSON.stringify(updateInfo));
				if (!data) {
					console.log("datasource is gone. ignored");
				}
				else if (updateInfo.version === null) {
					updateInfo.version = data;
				}
				//game server changed or recover from link dead
				else if (updateInfo.version != data || updateInfo.error) {
					console.log("need reload:" + updateInfo.version + " => " + data + "|" + updateInfo.error + "|" + updateInfo.confirm);
					var tmp = $('div.data-area').find("tr.dirty");
					var update = false;
					if (tmp.size() > 0) {
						if (confirm("エディタが更新されました。編集中のデータはすべて失われます。更新の前に保存しますか？")) {
							update = true;	
						}
					}
					if (update) {
						$("#all-update").trigger("update_next", [tmp, function () {
							location.reload();
						}]);
					}
					else {
						setTimeout(function () {
							location.reload();
						}, 10);
					}
				}
				updateInfo.onajax = false;
			},
			error: function (req, status, error) {
				console.log('失敗' + error);
				updateInfo.onajax = false;
				updateInfo.error = true;
			}
		});
		updateInfo.onajax = true;
	}
}, 5000);



<%
/*
	====================================================================================================
	====================================================================================================
	====================================================================================================
	====================================================================================================
	====================================================================================================
*/
%>
		</script>
	</head>
	<body>
	  <div class="loading"><div class="caption">リクエスト実行中...</div></div>
	  <div class="header-area deepdark">
	  <div class="main-menu">
		<!-- メインメニューフォーム -->
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
	  </div>
<%
	if (OperatorDatabase.getTable().isAnyReadable(operator_databases))
	{
%>		<!-- サブメニューフォーム -->
	  <div class="sub-menu">
		<form action="<%= url %>" method="post" style="display: inline-block; white-space: nowrap;">
			<input type="hidden" name="operator" value="<%= operator_name %>"/>
			<span title="データベース">Database:</span>
			<select class="box odd" name="database">
<%
		for (final OperatorDatabase.Row operator_database : operator_databases)
		{
			if (operator_database.isReadable())
			{
				final String database_name = operator_database.getDatabase();
				final ManagedDatabase.Row database = ManagedDatabase.getTable().findRow(databases, database_name);
				final String database_alias = database.getAlias();
				final String database_selected = operator_database.isSelected() ? " selected" : "";
%>				<option value="<%= database_name %>" label="<%= database_name %>" title="<%= database_alias %>"<%= database_selected %>/>
<%
			}
		}
%>			</select>
			<input type="submit" name="request-change-database" value="Select" title="データベース選択"/>
		</form>
<%
		if (OperatorTable.getTable().isAnyReadable(operator_tables))
		{
			final String database_name = OperatorDatabase.getTable().findSelectedName(operator_databases);
%>		<form action="<%= url %>" method="post" style="display: inline-block; white-space: nowrap;">
			<input type="hidden" name="operator" value="<%= operator_name %>"/>
			<input type="hidden" name="database" value="<%= database_name %>"/>
			<span title="テーブル">Table: <%= selected_table.getName() %></span>
			<select class="box odd" name="table">
<%
			for (final OperatorTable.Row operator_table : operator_tables)
			{
				if (operator_table.isReadable())
				{
					final String table_name = operator_table.getTable();
					final ManagedTable.Row table = ManagedTable.getTable().findRow(tables, table_name);
					final String table_alias = table.getAlias();
					final String table_selected = operator_table.isSelected() ? " selected" : "";

					String disp_label = "";							// テーブル選択で利用するセレクトボックスのリスト表示用の変数を初期化
					if(false == table_name.equals(table_alias))		// table_nameとtable_aliasが同一(両方ともtable_name)の場合は見た目が悪いので利用しない
					{
						// 同一でない場合のみaliasを代入する
						disp_label = table_alias;
					}

					while(disp_label.length() <= 15)
					{
						// 日本語説明の文字数が特定できないので全角スペースで15文字になるように一旦埋めます。
						// こうすると日本語説明の後にテーブル名を入れると綺麗に日本語説明(15文字)+テーブル名になるので見た目を優先して実装
						// (半角スペースだと何個入れても1個として判断されるため全角スペースで埋めてます)
						disp_label += "　";
					}

					final String search_head_str = table_name.substring(0,2);		// 先頭の2文字ほど切り出して表示の先頭に付加してキーボード入力により各リストへ飛べるようにする検索用文字を取得
					disp_label = search_head_str + ") " + disp_label + table_name;	// 検索文字(2文字)+日本語説明+テーブル名

%>				<option value="<%= table_name %>" label="<%= disp_label %>" title="<%= table_alias %>"<%= table_selected %>/>
<%
				}
			}
%>			</select>
			<input type="submit" name="request-change-table" value="Select" title="テーブル選択"/>
		</form>
	  </div>
<%
		}
%>		<hr/>
	  </div>
<%
	}
	if (readable)
	{
		final String database_name = selected_database.getName();
		final String table_name = selected_table.getName();
%>		<!-- 検索フォーム -->
	<form action="<%= url %>" method="post" style="white-space: nowrap;">
	  <div class="search-area medium">
		<div title="カラムリスト">Table <span name="table_name"><%= table_name %></span> Columns:</div>
			<input type="hidden" name="operator" value="<%= operator_name %>"/>
			<input type="hidden" name="database" value="<%= database_name %>"/>
			<input type="hidden" name="table" value="<%= table_name %>"/>
			<table class="table" rules="all">
				<tr>
					<th class="center" title="カラム名">Name</th>
					<th class="center" title="親カラム名">Parent</th>
					<th class="center" title="カラム型">Type</th>
					<th class="center" title="NULL許可">Null</th>
					<th class="center" title="オートインクリメント">Auto</th>
					<th class="center" title="フィルタ">Filter</th>
					<th class="center" title="ソート">Sort</th>
					<th class="center" title="表示幅">Width</th>
					<th class="center" title="表示高さ">Height</th>
					<th class="center" title="表示可">Visible</th>
				</tr>
<%
		for(final ManagedColumn.Row column : columns)
		{
			final String column_name = column.getName();
			final String column_alias = column.getAlias();
			final OperatorColumn.Row operator_column = OperatorColumn.getTable().findRow(operator_columns, column_name);
			final String filter_checked = (operator_column.isFilterEnabled() ? " checked" : "");
			final String filter_condition = HtmlEncoder.getInstance().encode(operator_column.getFilterCondition());
			final String sort_checked = (operator_column.isSortEnabled() ? " checked" : "");
			final int sort_priority = operator_column.getSortPriority();
			final String sort_order = operator_column.getSortOrder();
			final String asc_selected = (SqlOrder.ASC.equals(sort_order) ? " selected" : "");
			final String desc_selected = (SqlOrder.DESC.equals(sort_order) ? " selected" : "");
			final int width = operator_column.getWidth();
			final int height = operator_column.getHeight();
			final String visible_checked = (operator_column.isVisible() ? " checked" : "");
			final InformationColumn.Row information_column = InformationColumn.getTable().findRow(information_columns, column_name);
			final String auto_increment_checked = (information_column.isAutoIncrement() ? " checked" : "");
			final String nullable_checked = (information_column.isNullable() ? " checked" : "");
%>				<tr>
					<td class="left" title="<%= column_alias %>"><%= column_name %></td>
<%
			if (Relation.getTable().hasRow(relations, table_name, column_name))
			{
				final Relation.Row relation = Relation.getTable().findRow(relations, table_name, column_name);
				final String parent_table_name = relation.getParentTable();
				final String parent_column_name = relation.getParentColumn();
				final ManagedTable.Row parent_table = ManagedTable.getTable().findRow(tables, parent_table_name);
				final ManagedColumn.Row parent_column = ManagedColumn.getTable().findRow(relation_columns, parent_table_name, parent_column_name);
				final String parent_table_alias = parent_table.getAlias();
				final String parent_column_alias = parent_column.getAlias();
%>					<td class="left" title="<%= parent_table_alias %>.<%= parent_column_alias %>"><%= parent_table_name %>.<%= parent_column_name %></td>
<%
			}
			else
			{
%>					<td></td>
<%
			}
%>					<td class="left"><%= information_column.getColumnType() %> <%= information_column.getCollationName() %></td>
					<td class="center">
						<input type="checkbox"<%= nullable_checked %> disabled/>
					</td>
					<td class="center">
						<input type="checkbox"<%= auto_increment_checked %> disabled/>
					</td>
					<td class="left">
						<input type="checkbox" name="filter-enabled:<%= column_name %>"<%= filter_checked %>/>
<%
			if (ManagedTable.getTable().isRenderableRelation(relations, table_name, column_name))
			{
				final Relation.Row relation = Relation.getTable().findRow(relations, table_name, column_name);
				final String parent_table_name = relation.getParentTable();
				final String parent_column_name = relation.getParentColumn();
				final ManagedTable.Row parent_table = ManagedTable.getTable().findRow(tables, parent_table_name);
				final String display_column_name = parent_table.getDisplayColumn();
				final Data parent_rows = relation_rows.get(parent_table_name);
				final String nullable = information_column.isNullable() ? "_null" : "_not_null";
%>						<select class="box odd dyn-select" name="filter-condition:<%= column_name %>" linked_table="<%= parent_table_name %>" nullable="<%= nullable %>">
<%
				boolean has_option = false;
				for (final Data parent_row : parent_rows.asArray())
				{
					final String parent_column_value = HtmlEncoder.getInstance().encode(parent_row.asHash().get(parent_column_name).asString());
					final String display_column_value = HtmlEncoder.getInstance().encode(parent_row.asHash().get(display_column_name).asString());
					if (filter_condition.equals(parent_column_value)) {
							has_option = true;
%>							<option value="<%= parent_column_value %>" label="<%= display_column_value %>" title="<%= parent_column_value %>" selected/>
<%
					}
				}
				if (!has_option) {
%>					<option value="null", label="select", title="select"/>
<%
				}
%>						</select>
<%
			}
			else
			{
				if (information_column.isTinyInt())
				{
					final boolean condition_selected = !filter_condition.equals("0");
					final String true_selected = (condition_selected ? " selected" : "");
					final String false_selected = (!condition_selected ? " selected" : "");
%>						<select class="box odd" name="filter-condition:<%= column_name %>">
							<option value="1" label="true" title="有効"<%= true_selected %>/>
							<option value="0" label="false" title="無効"<%= false_selected %>/>
						</select>
<%
				}
				else if (information_column.isSmallInt() || information_column.isMediumInt() || information_column.isInt() || information_column.isBigInt() || information_column.isFloat() || information_column.isDouble())
				{
%>						<input class="box odd right numeric" type="text" name="filter-condition:<%= column_name %>" value="<%= filter_condition %>" style="width: <%= width %>px;"/>
<%
				}
				else if (information_column.isChar() || information_column.isVarchar() || information_column.isText())
				{
%>						<textarea class="box odd middle" name="filter-condition:<%= column_name %>" style="width: <%= width %>px; height: <%= height %>px; overflow-y: scroll;"><%= filter_condition %></textarea>
<%
				}
				else if (information_column.isDate() || information_column.isTime() || information_column.isDateTime() || information_column.isTimestamp())
				{
%>						<input class="box odd left" type="text" name="filter-condition:<%= column_name %>" value="<%= filter_condition %>" style="width: <%= width %>px;"/>
<%
				}
			}
%>					</td>
					<td class="left">
						<input type="checkbox" name="sort-enabled:<%= column_name %>"<%= sort_checked %>/>
						<input class="box odd right" type="text" name="sort-priority:<%= column_name %>" value="<%= sort_priority %>" style="width: 24px;"/>
						<select class="box odd" name="sort-order:<%= column_name %>">
							<option value="<%= SqlOrder.ASC.getName() %>" label="asc" title="昇順"<%= asc_selected %>/>
							<option value="<%= SqlOrder.DESC.getName() %>" label="desc" title="降順"<%= desc_selected %>/>
						</select>
					</td>
					<td class="center">
<%
			if (!Relation.getTable().hasRow(relations, table_name, column_name) && !information_column.isTinyInt())
			{
%>						<input class="box odd center" type="text" name="width:<%= column_name %>" value="<%= width %>" style="width: 24px;"/>
<%
			}
			else
			{
%>						<input type="hidden" name="width:<%= column_name %>" value="<%= width %>"/>
<%
			}
%>					</td>
					<td class="center">
<%
			if (!Relation.getTable().hasRow(relations, table_name, column_name) && (information_column.isChar() || information_column.isVarchar() || information_column.isText()))
			{
%>						<input class="box odd center" type="text" name="height:<%= column_name %>" value="<%= height %>" style="width: 24px;"/>
<%
			}
			else
			{
%>						<input type="hidden" name="height:<%= column_name %>" value="<%= height %>"/>
<%
			}
%>					</td>
					<td class="center">
						<input type="checkbox" name="visible:<%= column_name %>"<%= visible_checked %>/>
					</td>
				</tr>
<%
		}
%>			</table>
			<div title="キーリスト">Keys:</div>
			<table class="table" rules="all">
				<tr>
					<td class="center" title="キー名">Name</td>
					<td class="center" title="カラム名">Columns</td>
					<td class="center" title="ユニークキー">Unique</td>
				</tr>
<%
		final Array<String> index_names = InformationKey.getTable().getIndexNames(information_keys);
		for (final String index_name : index_names)
		{
			final String column_names = InformationKey.getTable().getColumNames(information_keys, index_name);
			final boolean unique = InformationKey.getTable().isUnique(information_keys, index_name);
			final String unique_checked = (unique ? " checked" : "");
%>				<tr>
					<td class="left"><%= index_name %></td>
					<td class="left"><%= column_names %></td>
					<td class="center">
						<input type="checkbox"<%= unique_checked %> disabled/>
					</td>
				</tr>
<%
		}
%>			</table>
	  </div>
	  <div class="search-form medium">
<%
		final int current_limit = selected_operator_table.getLimit();
		final int current_offset = selected_operator_table.getOffset();
%>			<input type="button" id="show_search_menu" value="△Search menu">
			<span title="全 <%= row_total_count %> 件中 <%= row_filter_count %> 件抽出"><%= row_filter_count %> of <%= row_total_count %> Rows Matched Filters</span>
			<select class="box odd" name="offset">
<%
		if (0 < current_limit)
		{
			for (int offset = 0; offset < row_filter_count; offset += current_limit)
			{
				final int first = offset + 1;
				final int last = Math.min(offset + current_limit, row_filter_count);
				final String label = String.format("%d - %d", first, last);
				final String item_selected = (offset == current_offset) ? (" selected") : ("");
%>				<option value="<%= offset %>" label="<%= label %>"<%= item_selected%>/>
<%
			}
		}
%>			</select>
			<span title="表示件数">Entries Per Page</span>
			<input class="box odd center" type="text" name="limit" value="<%= current_limit %>" style="width: 24px;"/>
			<input type="submit" name="request-select" value="Search" title="検索"/>
			&nbsp;
			<input type="button" id="all-update" value="All Update" title="全ての行を更新"/>
			<input type="button" id="apply-game" value="Apply" title="ゲームに反映"/>
		<hr/>
	  </div>
	</form>
<%
	}
%>

<!-- ================================================================================================ -->
<!-- ================================================================================================ -->
<!-- ================================================================================================ -->
<!-- ================================================================================================ -->
<!-- ================================================================================================ -->

<%
	if (writable)
	{
		final String database_name = selected_database.getName();
		final String table_name = selected_table.getName();
		Hash cached_options = Hash.newInstance();
%>		<!-- 追加フォーム -->
	  <div class="write-area dark">
		<table class="submiter">
		<tr>
		<form action="<%= url %>" method="post" style="white-space: nowrap;">
			<td>
			<input type="hidden" name="operator" value="<%= operator_name %>"/>
			<input type="hidden" name="database" value="<%= database_name %>"/>
			<input type="hidden" name="table" value="<%= table_name %>"/>
			<input class="box odd right disabled" type="text" value="*" title="行番号" style="width: 24px;" disabled/>
			</td>
<%
		for (final ManagedColumn.Row column : columns)
		{
			out.print("<td>");
			final String column_name = column.getName();
			final String column_alias = column.getAlias();
			final OperatorColumn.Row operator_column = OperatorColumn.getTable().findRow(operator_columns, column_name);
			final int width = operator_column.getWidth();
			final int height = operator_column.getHeight();
			final InformationColumn.Row information_column = InformationColumn.getTable().findRow(information_columns, column_name);
			final String defaultValue = information_column.getColumnDefault();
			final String defaultValueExpr = (defaultValue.length() > 0 ? ("value = " + defaultValue) : "");
%>			<div id="note-<%= column_name %>" value="<%= column_name %>:<%= column.getNotes() %>">
<%
			if (ManagedTable.getTable().isRenderableRelation(relations, table_name, column_name))
			{
				final Relation.Row relation = Relation.getTable().findRow(relations, table_name, column_name);
				final String parent_table_name = relation.getParentTable();
				final String parent_column_name = relation.getParentColumn();
				final ManagedTable.Row parent_table = ManagedTable.getTable().findRow(tables, parent_table_name);
				final String display_column_name = parent_table.getDisplayColumn();
				final Data parent_rows = relation_rows.get(parent_table_name);
				final String nullable = information_column.isNullable() ? "_null" : "_not_null";
				if (!cached_options.has(parent_table_name + nullable)) {
					cached_options.set(parent_table_name, true);
%>					<div id="option_cache_<%= parent_table_name %><%= nullable %>" style="display:none;">
<%
					if (information_column.isNullable()) {
%><option value="" label="(null)"/>
<%
					}
					for (final Data parent_row : parent_rows.asArray())
					{
						final String parent_column_value = HtmlEncoder.getInstance().encode(parent_row.asHash().get(parent_column_name).asString());
						final String display_column_value = HtmlEncoder.getInstance().encode(parent_row.asHash().get(display_column_name).asString());
%>						<option value="<%= parent_column_value %>" label="<%= display_column_value %>" title="<%= parent_column_value %>"/>
<%
					}				
%>					</div>
<%
				}

%>				<select class="box odd dyn-select" name="column:<%= column_name %>" title="<%= column_name %>" linked_table="<%= parent_table_name %>" nullable="<%= nullable %>">
<%
				if (information_column.isNullable())
				{
%>					<option value="" label="(null)"/>
<%
				}
				else {
%>					<option value="" label="select"/>
<%
				}

%>				</select>
<%
			}
			else
			{
				if (information_column.isTinyInt())
				{
%>			<input type="checkbox" name="column:<%= column_name %>" title="<%= column_name %>" <%= defaultValueExpr %> />
<%
				}
				else if (information_column.isSmallInt() || information_column.isMediumInt() || information_column.isInt() || information_column.isBigInt() || information_column.isFloat() || information_column.isDouble())
				{
%>			<input class="box odd right numeric" type="text" name="column:<%= column_name %>" style="width: <%= width %>px;" title="<%= column_name %>" <%= defaultValueExpr %> />
<%
				}
				else if (information_column.isChar() || information_column.isVarchar() || information_column.isText())
				{
%>			<textarea class="box odd middle" name="column:<%= column_name %>" style="width: <%= width %>px; height: <%= height %>px; overflow-y: scroll;" title="<%= column_name %>"><%= defaultValue %></textarea>
<%
				}
				else if (information_column.isDateTime() || information_column.isTimestamp())
				{
					if (!selected_table.getInsertTimeColumn().equals(column_name) && !selected_table.getUpdateTimeColumn().equals(column_name))
					{
%>			<input class="box odd left" type="text" name="column:<%= column_name %>" style="width: <%= width %>px;" title="<%= column_name %>" <%= defaultValueExpr %> />
<%
					}
				}
				else
				{
%>			<input class="box odd left" type="text" name="column:<%= column_name %>" style="width: <%= width %>px;" title="<%= column_name %>" <%= defaultValueExpr %> />
<%
				}
			}
			out.print("</td>");
		}
%>		
			<td>
				<input class="insert" type="submit" name="request-insert" value="Add" title="追加"/>
			</td>
		</form>
		</tr>
		</table>
	  </div>
<%
	}
%>
<!-- ================================================================================================ -->
<!-- ================================================================================================ -->
<!-- ================================================================================================ -->
<!-- ================================================================================================ -->
<!-- ================================================================================================ -->
<%
	if (readable)
	{
%>
		<!-- 更新フォーム -->
	  <div class="data-area">
<%
		out.print("<table class=\"submiter\">");
		final String database_name = selected_database.getName();
		final String table_name = selected_table.getName();
		final String disabled = (!writable) ? (" disabled") : ("");
		boolean odd = true;
		int row_number = selected_operator_table.getOffset() + 1;
		boolean is_first = true;
		for (final Data row : rows.asArray())
		{
			if (is_first)
			{
				out.print("<tr class=\"header\">");
				out.print("<th>");
				out.print("</th>");
				for (final ManagedColumn.Row column : columns)
				{
					final String column_name = column.getName();
					final OperatorColumn.Row operator_column = OperatorColumn.getTable().findRow(operator_columns, column_name);
					final InformationColumn.Row information_column = InformationColumn.getTable().findRow(information_columns, column_name);
					final boolean visible = operator_column.isVisible();
					int width = operator_column.getWidth();
					if (information_column.isTinyInt()) width = 20;
					if (Relation.getTable().hasRow(relations, table_name, column_name)) width = 0;
					if (width > 0) {
						out.print("<th style=\"max-width:"+width+"px;\">");
					}
					else {
						out.print("<th>");
					}
					if (visible)
					{
						out.print(column_name);
					}
					out.print("</th>");
				}
				if (writable)
				{
					out.print("<th>");
					out.print("</th><th>");
					out.print("</th>");
				}
				out.print("</tr>");
				is_first = false;
			}
			final String odd_or_even = (odd ? "odd" : "even");
			out.print("<tr class=\"" + odd_or_even + "\">");
			out.print("<td>");
%>		
			<input type="hidden" name="operator" value="<%= operator_name %>"/>
			<input type="hidden" name="database" value="<%= database_name %>"/>
			<input type="hidden" name="table" value="<%= table_name %>"/>
			<input class="box <%= odd_or_even %> right disabled" type="text" value="<%= row_number %>" title="行番号" style="width: 35px;" disabled/>
<%
			out.print("</td>");
			for (final ManagedColumn.Row column : columns)
			{
				out.print("<td>");
				final String column_name = column.getName();
				final String column_alias = column.getAlias();
				final OperatorColumn.Row operator_column = OperatorColumn.getTable().findRow(operator_columns, column_name);
				final int width = operator_column.getWidth();
				final int height = operator_column.getHeight();
				final boolean visible = operator_column.isVisible();
				final InformationColumn.Row information_column = InformationColumn.getTable().findRow(information_columns, column_name);
				final boolean is_json = (column_alias.indexOf("JSON") >= 0);
				final String column_value = HtmlEncoder.getInstance().encode(row.asHash().get(column_name).asString());
				if (visible)
				{
					if (ManagedTable.getTable().isRenderableRelation(relations, table_name, column_name))
					{
						final Relation.Row relation = Relation.getTable().findRow(relations, table_name, column_name);
						final String parent_table_name = relation.getParentTable();
						final String parent_column_name = relation.getParentColumn();
						final ManagedTable.Row parent_table = ManagedTable.getTable().findRow(tables, parent_table_name);
						final String display_column_name = parent_table.getDisplayColumn();
						final Data parent_rows = relation_rows.get(parent_table_name);
						final String nullable = information_column.isNullable() ? "_null" : "_not_null";
%>						<select class="box odd dyn-select" name="column:<%= column_name %>" title="<%= column_name %>" linked_table="<%= parent_table_name %>" nullable="<%= nullable %>">
<%
						boolean dead_link = true;
						if (information_column.isNullable())
						{
							final boolean matched = row.asHash().get(column_name).isNull();
							final String item_selected = (matched ? " selected" : "");
							if (matched)
							{
								dead_link = false;
%>								<option value="" label="(null)"/>
<%							}
						}
						for (final Data parent_row : parent_rows.asArray())
						{
							final String parent_column_value = HtmlEncoder.getInstance().encode(parent_row.asHash().get(parent_column_name).asString());
							final String display_column_value = HtmlEncoder.getInstance().encode(parent_row.asHash().get(display_column_name).asString());
							final boolean matched = column_value.equals(parent_column_value);
							final String item_selected = (matched ? " selected" : "");
							if (matched)
							{
								dead_link = false;
%>								<option value="<%= parent_column_value %>" label="<%= display_column_value %>" title="<%= parent_column_value %>"<%= item_selected %><%= disabled %>/>
<%
							}
						}
						if (dead_link)
						{
%>							<option value="<%= column_value %>" label="(dead-link)" title="<%= column_value %>" selected<%= disabled %>/>
<%
						}
%>			</select>
<%
					}
					else
					{
						if (information_column.isTinyInt())
						{
							final String checked = (!column_value.equals("0") ? " checked" : "");
%>							<input type="checkbox<%= disabled %>" name="column:<%= column_name %>" title="<%= column_name %>"<%= checked %><%= disabled %>/>
<%
						}
						else if (information_column.isSmallInt() || information_column.isMediumInt() || information_column.isInt() || information_column.isBigInt() || information_column.isFloat() || information_column.isDouble())
						{
%>							<input class="box <%= odd_or_even %> right<%= disabled %> numeric" type="text" name="column:<%= column_name %>" value="<%= column_value %>" style="width: <%= width %>px;" title="<%= column_name %>"<%= disabled %>/>
<%
						}
						else if (information_column.isChar() || information_column.isVarchar() || information_column.isText())
						{
%>							<textarea class="box <%= odd_or_even %> middle<%= disabled %>" name="column:<%= column_name %>" style="width: <%= width %>px; height: <%= height %>px; overflow-y: scroll;" title="<%= column_name %>"<%= disabled %>><%= column_value %></textarea>
<%
						}
						else if (information_column.isDateTime() || information_column.isTimestamp())
						{
							if (selected_table.getInsertTimeColumn().equals(column_name) || selected_table.getUpdateTimeColumn().equals(column_name))
							{
%>								<input class="box <%= odd_or_even %> left disabled" type="text" name="column:<%= column_name %>" value="<%= column_value %>" style="width: <%= width %>px;" title="<%= column_name %>" disabled/>
<%
							}
							else
							{
%>								<input class="box <%= odd_or_even %> left<%= disabled %>" type="text" name="column:<%= column_name %>" value="<%= column_value %>" style="width: <%= width %>px;" title="<%= column_name %>"<%= disabled %>/>
<%
							}
						}
						else
						{
%>							<input class="box <%= odd_or_even %> left<%= disabled %>" type="text" name="column:<%= column_name %>" value="<%= column_value %>" style="width: <%= width %>px;" title="<%= column_name %>"<%= disabled %>/>
<%
						}
					}
				}
				else
				{
					if (writable)
					{
						if (!selected_table.getInsertTimeColumn().equals(column_name) && !selected_table.getUpdateTimeColumn().equals(column_name))
						{
%>						<input type="hidden" name="column:<%= column_name %>" value="<%= column_value %>"/>
<%
						}
					}
				}
				out.print("</td>");
			}
			if (writable)
			{
				out.print("<td>");
				for (final InformationKey.Row information_key : information_keys)
				{
					if (information_key.isPrimary())
					{
						final String column_name = information_key.getColumnName();
						final String column_value = HtmlEncoder.getInstance().encode(row.asHash().get(column_name).asString());
%>						<input type="hidden" name="key:<%= column_name %>" value="<%= column_value %>"/>
<%
					}
				}
%>				<input type="button" class="update" value="Update" title="更新"/>
				</td><td>
				<input type="button" class="delete" value="Delete" title="削除"/>
<%
				out.print("</td>");
			}
			odd = !odd;
			row_number++;
			out.print("</tr>");
		}
		out.print("</table>");
%>
		<hr/>
		<div style="display: none;">
			<form action="<%= url %>" method="post" style="white-space: nowrap;">
			</form>
			<input type="hidden" name="request-update" value="Update" />
			<input type="hidden" name="request-delete" value="Delete" />
		</div>
	  </div><!-- data-area -->
<%
	}
%>
	  <div class="footer-area deepdark">
<%
	final String database_name = selected_database.getName();
	final String table_name = selected_table.getName();
	if (writable)
	{
		final String renumbering_column_name = selected_table.getRenumberingColumn();
		if (ManagedColumn.getTable().hasRow(columns, renumbering_column_name))
		{
%>		<!-- リナンバフォーム -->
		<form action="<%= url %>" method="post" style="display: inline; white-space: nowrap;">
			<input type="hidden" name="operator" value="<%= operator_name %>"/>
			<input type="hidden" name="database" value="<%= database_name %>"/>
			<input type="hidden" name="table" value="<%= table_name %>"/>
			<span title="初期値">Start with:</span>
			<input class="box odd right" type="text" name="start" value="10" style="width: 24px;"/>
			<span title="増分値">Increment by:</span>
			<input class="box odd right" type="text" name="increment" value="10" style="width: 24px;"/>
			<input type="submit" name="request-renumber" value="Renumber" title="リナンバ" onClick="return confirm('<%= table_name %>テーブルにRenumberを実行します、よろしいですか？')"/>
		</form>
<%
		}
	}
	if (downloadable)
	{
%>		<!-- ダウンロードフォーム -->
		<form action="<%= url %>" method="post" style="display: inline; white-space: nowrap;">
			<input type="hidden" name="operator" value="<%= operator_name %>"/>
			<input type="hidden" name="database" value="<%= database_name %>"/>
			<input type="hidden" name="table" value="<%= table_name %>"/>
			<input type="hidden" name="format" value="json"/>
			<input type="submit" name="request-download" value="Download" title="ダウンロード"/>
		</form>
<%
	}
	if (uploadable)
	{
%>		<!-- アップロードフォーム -->
		<form action="<%= url %>" method="post" enctype="multipart/form-data" style="display: inline; white-space: nowrap;">
			<input type="hidden" name="operator" value="<%= operator_name %>"/>
			<input type="hidden" name="database" value="<%= database_name %>"/>
			<input type="hidden" name="table" value="<%= table_name %>"/>
			<input type="hidden" name="format" value="json"/>
			<input type="submit" name="request-upload" value="Upload" title="アップロード" onClick="return upload_check('json_upload_files')"/>
			<input type="file" name="file" id="json_upload_files" multiple/>
		</form>
<%
	}
	if (downloadable)
	{
%>		<!-- ダウンロードフォーム -->
		<form action="<%= url %>" method="post" style="display: inline; white-space: nowrap;">
			<input type="hidden" name="operator" value="<%= operator_name %>"/>
			<input type="hidden" name="database" value="<%= database_name %>"/>
			<input type="hidden" name="table" value="<%= table_name %>"/>
			<input type="hidden" name="format" value="csv"/>
			<input type="submit" name="request-download" value="CSV Download" title="CSVダウンロード"/>
		</form>
<%
	}
	if (uploadable)
	{
%>		<!-- アップロードフォーム -->
		<form action="<%= url %>" method="post" enctype="multipart/form-data" style="display: inline; white-space: nowrap;">
			<input type="hidden" name="operator" value="<%= operator_name %>"/>
			<input type="hidden" name="database" value="<%= database_name %>"/>
			<input type="hidden" name="table" value="<%= table_name %>"/>
			<input type="hidden" name="format" value="csv"/>
			<input type="submit" name="request-upload" value="CSV Upload" title="CSVアップロード" onClick="return upload_check('csv_upload_files')"/>
			<input type="file" name="file" id="csv_upload_files" multiple/>
		</form>
<%
	}
	if (writable)
	{
%>		<button id="pull-request", type="button" name="proxy-request" title="Pull Request" onClick="return on_pull_request()">
			Pull Request
		</button>
<%
	}
%>
	  </div>
	</body>
</html>
