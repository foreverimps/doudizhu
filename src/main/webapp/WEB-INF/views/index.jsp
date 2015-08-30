<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel='stylesheet' href='${ctx}/common/css/base.css' />
<link rel='stylesheet' href='${ctx}/common/css/index.css' />
<script type="text/javascript" src="${ctx}/common/jquery-1.11.1.min.js"></script>
<script type="text/javascript" src="${ctx}/common/jquery.json-2.2.js"></script>
<script type="text/javascript" src="${ctx}/common/jquery.md5.js"></script>
<script type="text/javascript" src="${ctx}/common/game.js"></script>
<title></title>
</head>
<body>
	<div id="reg_div">
		<p>注册</p>
		<br>
		<form action="" name="regForm" id="regForm">
			账号：<input name="account" prop="true"/> 密码：<input name="password" prop="true" type="password"/><br>
			<br> 确认密码：<input name="confirm_password" type="password"> 验证码：<input name="checkCode" prop="true"/><br>
			<br> <input type="button" name="已有账号，去登录" value="已有账号，去登录" id="go_login" /> <input
				type="button" name="注册" value="注册" id="register" />
		</form>
		<br>
	</div>
	<div id="login_div" style="display: none;">
		<p>登录</p>
		<br>
		<form action="" name="loginForm" id="loginForm">
			账号：<input name="account" prop="true"/> 密码：<input name="password" prop="true" type="password"/><br>
			<input
				type="button" name="登陆" value="登陆" id="login"/>
		</form>
	</div>
	<div id="index_div" style="display: none;">
		<input type="button" name="标准" value="标准" id="bz"/>&nbsp;&nbsp;&nbsp;
		<input type="button" name="富豪" value="富豪" id="fh"/>&nbsp;&nbsp;&nbsp;
		<input type="button" name="土豪" value="土豪" id="th"/>&nbsp;&nbsp;&nbsp;<br><br><br>
		<input type="button" name="快速开始" value="快速开始" id="start"/>&nbsp;&nbsp;&nbsp;
		<input type="button" name="创建游戏" value="创建游戏" id="create"/>
	</div>
	<div class="page1" id="room_div" style="display: none;">
		<div class="tabs">
			<span class="ready" id="ready">准备</span>
			<span class="all_money" id="all_money">总下注:2000</span>
			<span class="remaining_time" id="remaining_time">9秒</span>
			<div class="site" id="site1"  place>
	      		<h2 name="account"></h2>
	      		<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
	      		<span name="yuanbao"></span> <span name="jinbi"></span>
	      </div>  
	      <div class="site" id="site2"  place>
	      		<h2 name="account"></h2>
	      		<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
	      		<span name="yuanbao"></span> <span name="jinbi"></span>
	      </div>  
	      <div class="site" id="site3"  place>
	      		<h2 name="account"></h2>
	      		<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
	      		<span name="yuanbao"></span> <span name="jinbi"></span>
	      </div>  
	      <div class="site" id="site4"  place>
	      		<h2 name="account"></h2>
	      		<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
	      		<span name="yuanbao"></span> <span name="jinbi"></span>
	      </div>  
	      <div class="site" id="site5"  place>
	      		<h2 name="account"></h2>
	      		<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>
	      		<span name="yuanbao"></span> <span name="jinbi"></span>
				<br>
				<span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span><span color="red"></span>
	      </div>    
		</div>

	    <span class="fllow_last"><input type="checkbox" id="call_over">跟到底</span>
	   	<span class="quite index_button">退出</span>
	   	<span class="discard index_button" id="give_up">弃牌</span>
	   	<span class="look index_button" id="look">看牌</span>
	   	<span class="match index_button" id="compare">比牌</span>
	   	<span class="pull index_button" id="add_call">加注</span>
	   	<span class="filling index_button" id="call">跟注</span>
	   	<span class="receive index_button" id="get">领取奖励</span>
	  </div>
	  <form action="" id="game_form" name="game_form">
	  	<input type="hidden" id="place" name="place" prop='true'/>
	  	<input type="hidden" id="toUserId" name="toUserId" prop='true'/>
	  	<input type="hidden" id="type" name="type" prop='true'/>
	  	<input type="hidden" id="amount" name="amount" prop='true'/>
	  	<input type="hidden" id="toPlace" name="toPlace" prop='true'/>
	  	<input type="hidden" id="roomCode" name="roomCode" prop='true'/>
	  </form>
</body>
<script type="text/javascript">
	$("#go_login").click(
		function(){
			$("#reg_div").hide();
			$("#login_div").show();
		}		
	);
	
	$("#register").click(
		function(){
			gen_json_send(1020,$("#regForm"));
		}		
	);
	
	$("#login").click(
		function(){
			gen_json_send(1010,$("#loginForm"));
		}		
	);
	
	$("#create").click(
		function(){
			var dto = {personNum:5,type:1,isDoubleOpen:0,bottomNote:20,password:"123123"};
			var in_msg = {id:1140,content:dto,code:"",token:token};
			send($.toJSON(in_msg));
		}		
	);
	
	$("#start").click(
		function(){
			gen_json_send(1145,$(this));
		}		
	);
	
	$("#ready").click(
		function(){
			$("#game_form #type").val(10);
			$("#game_form #roomCode").val(curr_room_code);
			var _div = $("div[place='"+curr_place+"']");
			$(_div).attr("opt_type",10);
			$(_div).find("h2").html(curr_account + " 准");
			gen_json_send(1150,$("#game_form"));
		}		
	);
	
	$("#give_up").click(
		function(){
			$("#type").val(4);
			var place = $("#place").val();
			var span = $("span[place='"+place+"']");
			var text =  $("span[place='"+place+"']").text();
			$(span).text(text+" 弃牌");
			gen_json_send(1150,$("#game_form"));
		}		
	);
	
	$("#look").click(
		function(){
			$("#type").val(20);
			gen_json_send(1150,$("#game_form"));
		}		
	);
	
	$(".site").click(
		function(){
			var place = $(this).attr("place");
			$("#toPlace").val(place);
		}		
	);
	
	$("#compare").click(
		function(){
			if($("#toPlace").val() == ""){
				alert("请选择您要对比的玩家");
				return false;
			}
			$("#type").val(37);
			//把游戏中的玩家的背景变成可点击状态，然后点击，然后发送比较请求
			gen_json_send(1150,$("#game_form"));
		}		
	);
	
	$("#call").click(
		function(){
			$("#game_form #type").val(25);
			gen_json_send(1150,$("#game_form"));
		}		
	);
	
	$("#add_call").click(
		function(){
			$("#game_form #type").val(30);
			//有个控制条让用户选择加多少注
			$("#game_form #amount").val(parseInt(curr_bottom_note)+30);
			curr_bottom_note = curr_bottom_note +30;
			gen_json_send(1150,$("#game_form"));
		}		
	);
	
	$("#get").click(
		function(){
			$("#type").val(1);
			gen_json_send(1150,$("#game_form"));
		}		
	);
	
	$("#call_over").click(
		function(){
			$("#type").val(7);
			var place = $("#place").val();
			var span = $("span[place='"+place+"']");
			var text =  $("span[place='"+place+"']").text();
			$(span).text(text+" 跟到底");
			gen_json_send(1150,$("#game_form"));
		}		
	);
</script>
</html>