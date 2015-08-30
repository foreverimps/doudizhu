var ws =null;
var web_socket_url = "ws://localhost:8080/platform-gamecenter2/web_game";
var connected = false;
var token,curr_user_id,curr_account,yuanbao,jinbi,curr_place,curr_room_code,curr_bottom_note="";

var socket_msg = {id:1,content:{},token:""};

init_conn();
function init_conn(){
	 ws = new WebSocket(web_socket_url);
	 ws.onopen = function () {
		 connected=true;
		 socket_msg.id = 1000;
		 send($.toJSON(socket_msg));
	 };
	 ws.onmessage = function (event) {
	     var msg = event.data;
	     console.log(msg);
	     process_msg($.evalJSON(msg));
	 };
	 ws.onclose = function (event) {
		 connected = false;
		 console.log("is colse");
	 };
}

function send(msg){
	if(connected && ws != null){
		ws.send(msg);
	}else{
		console.log("socket is close ..");
	}
}

function gen_json_send(id,form){
	var obj = {};
	$(form).find("input[prop='true']").each(
		function(){
			var attr=$(this).attr("name");
			var value = $(this).val();
			if($(this).attr("type") == "password"){
				value = $.md5(value);
			}
			obj[attr]=value;
		}
	);
	socket_msg.id = id;
	socket_msg.content=obj;
	socket_msg.token=token;
	send($.toJSON(socket_msg));
}

function process_msg(data){
	var id = data.id;
	var code = data.code;
	if(id == 1010 || id == 1020){//注册，或者登陆
		if(code == 1){
			token = data.content.token;
			curr_account = data.content.account;
			curr_user_id = data.content.id;
			yuanbao = data.content.ingotAccountAmount;
			jinbi = data.content.moneyAccountAmount;
			$("#index_div").show();
			$("#login_div").hide();
		}
		return;
	}
	
	if(id == 1140){//创建游戏
		if(code == 1){
			$("#index_div").hide();
			$("#room_div").show();
			
			var place = 1;
			curr_place = place;
			curr_room_code = data.content.code;
			curr_bottom_note = data.content.bottomNote;
			setPlace(place,data.content.personNum);
			var _div = $("div[place='"+place+"']");
			$(_div).find("h2[name='account']").html(curr_account);
			$(_div).find("span[name='yuanbao']").html(yuanbao+" 元宝");
			$(_div).find("span[name='jinbi']").html(jinbi+" 金币");
		}
		return;
	}
	
	if(id == 1150){//玩游戏
		if(code == 1){
			$("#index_div").hide();
			$("#room_div").show();
			playGame(data.content);
		}
		return;
	}
	
	if(id == 1145){//快速开始
		if(code == 1){
			$("#index_div").hide();
			$("#room_div").show();
			viewGame(data);
		}
		return;
	}
}

function playGame(data){
	if(data.type == 5){//进入
		var place = data.place;
		curr_bottom_note = data.bottomNote;
		var _div = $("div[place='"+place+"']");
		if(data.userId != curr_user_id){
			$(_div).find("h2[name='account']").html(data.account);
			$(_div).find("span[name='yuanbao']").html(data.ingotAccountAmount+" 元宝");
			$(_div).find("span[name='jinbi']").html(data.moneyAccountAmount+" 金币");
		}else{
			curr_place = place;
		}
	}else if(data.type == 10){//准备
		var place = data.place;
		if(data.userId != curr_user_id){
			var _div = $("div[place='"+place+"']");
			$(_div).attr("opt_type",data.type);
			$(_div).find("h2").html(data.account + " 准");
		}
	}else if(data.type == 15){//发牌
		$("div[opt_type='10']").each(
			function(){
				var content = $(this).find("h2").html();
				$(this).find("h2").html(content+" 发");
				$(this).attr("opt_type",data.type);
			}
		);
	}else if(data.type == 25){//跟注
		var place = data.place;
		var _div = $("div[place='"+place+"']");
		var content = $(_div).find("h2").html();
		$(_div).find("h2").html(content+" 跟");
		
	}else if(data.type == 30){//加注
		var place = data.place;
		curr_bottom_note = data.bottomNote;
		var _div = $("div[place='"+place+"']");
		var content = $(_div).find("h2").html();
		$(_div).find("h2").html(content+" 加");
	}else if(data.type == 20){//看牌
		var place = data.place;
		var cards = data.cards;
		var userId = data.userId;
		var _div = $("div[place='"+place+"']");
		var content = $(_div).find("h2").html();
		if(userId == curr_user_id){
			if(typeof(cards) != 'undefined'){
				$(_div).find("h2").html(content+" "+cards);
			}
		}else{
			$(_div).find("h2").html(content+" 看");
		}
	}else if(data.type == 37){//比较
		var place = data.place;
		var userId = data.userId;
		var winerUserId = data.winerUserId;
		var winPlace = place;
		var lowPlace = place;
		if(userId != winerUserId){
			winPlace = data.toPlace;
			lowPlace = place;
		}else{
			winPlace = place;
			lowPlace = data.toPlace;
		}
		
		var _div2 = $("div[place='"+winPlace+"']");
		var content = $(_div2).find("h2").html();
		$(_div2).find("h2").html(content + " 成功");
		
		var _div3 = $("div[place='"+lowPlace+"']");
		var content = $(_div3).find("h2").html();
		$(_div3).find("h2").html(content + " 失败");
		
	}else if(data.type == 45){//跟注
		$("div[opt_type='10']").each(
			function(){
				var content = $(this).find("h2").html();
				$(this).find("h2").html(content+" 发");
				$(this).attr("opt_type",data.type);
			}
		);
	}else if(data.type == 50){//加注
		$("div[opt_type='10']").each(
			function(){
				var content = $(this).find("h2").html();
				$(this).find("h2").html(content+" 发");
				$(this).attr("opt_type",data.type);
			}
		);
	}
}

function viewGame(data){
	var players = data.content.playerList;
	var personNum = data.content.personNum;
	curr_room_code = data.content.code;
	for(var i=0;i<players.length;i++){
		var place = players[i].place;
		if(curr_user_id == players[i].userId){
			curr_place = place;
			setPlace(place,personNum);
			continue;
		}
	}
	
	for(var i=0;i<players.length;i++){
		var place = players[i].place;
		var _div = $("div[place='"+place+"']");
		$(_div).find("h2[name='account']").html(players[i].account);
		$(_div).find("span[name='yuanbao']").html(players[i].ingotAccountAmount+" 元宝");
		$(_div).find("span[name='jinbi']").html(players[i].moneyAccountAmount+" 金币");
	}
}

function setPlace(place,personNum){
	personNum = 5;
	var middle = 3;
	var start = middle + place;
	if(start > personNum){
		start = 1;
	}
	for(var i=0;i<personNum;i++){
		$("#site"+(i+1)).attr("place",start);
		start++;
		if(start > personNum){
			start = 1;
		}
	}
}
