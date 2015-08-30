<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="${ctx}/common/jquery-1.11.1.min.js"></script>
<meta http-equiv="expires" content="0">
<meta name=”viewport”
	content=”width=device-width,initial-scale=1.0,maximum-scale=1.0,user-scalable=0;” />
<meta content=”yes” name=”apple-mobile-web-app-capable” />
<meta content=”black” name=”apple-mobile-web-app-status-bar-style” />
<meta content=”telephone=no” name=”format-detection” />
<title>公告通知</title>
<style type="text/css">
html,body{-webkit-touch-callout: none;}
body {
	background-color: #DAE3E0;
	margin: 0px;
	padding: 0px;
}

li {
	list-style: none;
}

.list_a:first-child {
	border-top: none;
}
.list_a:visited{color: white;}
ul {
	margin: 10px auto;
	padding: 0px;
	background-color: #FFFFFF;
	border-radius: 5px;
	border: 1px #21AEB8 solid;
	width: 93%;
	height: 100%;
}

ul li {
	border-bottom: 1px #21AEB8 solid;position: relative;height: 61px;
}

ul li:last-child {
	border-bottom: none;
}
.new{background-image: url(http://images.migu99.com/gamecenter/wangxian/images/new.png);width: 70px;height: 30px;position:absolute;right: 5px;top: 22px;background-size: 48px;background-repeat: no-repeat;}

.list_a {
	width: 100%;
	border-top: 1px #21AEB8 solid;
	height: 60px;
	display: inline-block;
	position: relative;
}

.list_a p {
	margin: 0px;
	padding: 0px;
	font-size: 13px;
}

a {
	text-decoration: none;
}

.list_a p:nth-last-child(2) {
	color: #A2A2A2;
	text-indent: 10px;
	line-height: 24px;
}

.list_a p:nth-last-child(3) {
	color: #3D3D3D;
	text-indent: 10px;
	line-height: 36px;
	font-size: 15px;
	font-weight: bold;
}

.arrow2 {
	width: 12px;
	height: 12px;
	overflow: hidden;
	zoom: 1;
	position: absolute;
	border-right: 2px solid #BFCBCB;
	border-bottom: 2px solid #BFCBCB;
	transform: rotate(-50deg);
	-o-transform: rotate(-50deg);
	-moz-transform: rotate(-50deg);
	-webkit-transform: rotate(-50deg);
	background-color: #FFF;
	position: absolute;
	right: 10px;
	top: 26px;
}

.title {
	width: 245px;
	overflow: hidden;
	white-space: nowrap;
	text-overflow: ellipsis;
}
#wrapper {position:absolute;
	width:100%;
	height:100%;
	overflow:auto;
}

#scroller {
	position:relative;
	/*-webkit-touch-callout:none;*/
	-webkit-tap-highlight-color:rgba(0,0,0,0);
	width:100%;
}


#scroller ul {
	position:relative;
}
.news_content{margin: 0px auto;width: 90%;text-indent: 15px;}
.news_content h2{text-align: center;font-size: 14px;height:40px;line-height:40px;background-image: url(http://images.migu99.com/gamecenter/wangxian/images/notice_top_bg.png);background-repeat:repeat-x;background-position: 0px 38px;}
#page_detail{width: 93%;position: absolute;top:40px;left:11px;box-shadow: 1px 1px 26px #2a3a3e;border-radius:10px;background-color: #edf1f4;z-index: 99999;}
#page_detail p{word-break: break-word;width: 93%;}
.page_detail_close{float: right;background-image: url(http://images.migu99.com/gamecenter/wangxian/images/notice_close.png);width: 29px;height: 29px;background-size: 29px;margin: 6px 0px 0px 0px;}
.page_detail_close:hover{background-position: 0px -34px;}
.mask_div{opacity: 0.9;background: none repeat scroll 0 0 rgba(130, 130, 130, 0.5);width: 100%;height:100%;z-index: 9999;position: absolute;left: 0px;top: 0px;display:none; }
</style>
</head>
<body>
<!-- 隐藏层 -->
	<div class="mask_div" id="mask_div"></div>
	<c:if test="${empty noticelList}">
		<tr>
			<td colspan="10">
				<h4
					style="color: #90A5A7; font-size: 16px; text-align: center; line-height: 200px">暂无公告通知!</h4>
			</td>
		</tr>
	</c:if>
	<c:if test="${not empty noticelList}">
		<div id="scroller">
			<ul id="notice_ul">
			<c:set var="substr" value="http://" />
			
				<c:forEach var="item" items="${noticelList}" varStatus="status">
					<li>
					
						<c:choose>
						  <c:when test="${!empty item.url && fn:containsIgnoreCase(item.url, substr) }">
						      <a href="${ctx}/mob/v1/more/urlDetail?id=${item.id }&token=${token}&noticeId=${item.noticeId }" class="list_a" >
						  </c:when>
						
						  <c:otherwise>
						<a href="#page_detail" class="list_a list_page" message='${item.id }' notice='${item.noticeId }'>
						  </c:otherwise>
						</c:choose>
							<p class="title">${item.title}</p>
							<p>
								来自:站内信&nbsp;
									<c:if test="${item.status == 0 }"><span class="new"  click_id='${item.id}'></span></c:if>
							</p>
							<div class="arrow2"></div>
					</a></li>
				</c:forEach>
			</ul>
		</div>
	</c:if>
	<div id="page_detail" style="display:none;">
		<div class="news_content">
		<a class="page_detail_close" id="detail_close"></a>
 			<h2></h2>
 			<p></p>
 		</div>
	</div>
<script type="text/javascript">

var pageClick = function(){
		$(this).find("span").removeClass('new');
		var id=$(this).attr('message');
		var noticeId=$(this).attr('notice');
		$.ajax({
			url:'${ctx}/mob/v1/more/detail?id='+id + '&token=${token}' + '&noticeId=' + noticeId,
			data:'',
			async:true,
			success:function(data){
				var code=data['code'];
				if(code==1){
					var message=data['content'];
					$('#page_detail').find('h2').text(message['title']);
					$('#page_detail').find('p').html(message['content']);
					var top = 40 + $(window).scrollTop();
					$('#page_detail').css("top",top); 
					$('#page_detail').show();
					$('#mask_div').height($(document).height());
					$('#mask_div').show();
				}
			}
		});
		return false;
	}
	
$(function(){
	$('a.list_page').bind('click', pageClick);
	$('#detail_close').click(function(){
		$('#page_detail').hide();
		$('#mask_div').hide();
	});
	
     		var range = 50;             //距下边界长度/单位px   
	        var totalheight = 0;   
	        var pageNo=${pageNo};
	        var totalPages = ${page.totalPages};
	        var pageSize='${pageSize}';
	        var gameId='${gameId}';
	        var token='${token}';
	        var isLoading = false;
	            	
	        $(window).scroll(function(){  
	           
	            if((range + $(window).scrollTop()) >= ($(document).height() - $(window).height()) && !isLoading && pageNo < totalPages) {
	            	pageNo=pageNo+1;
	            	isLoading = true;
	            	$.ajax({
	            		url:'${ctx}/mob/v1/more/getNextNotice?token='+token+'&pageNo='+pageNo+'&pageSize='+pageSize+'&gameId=' +gameId,
	            		data:'',
	            		async:true,
	            		success:function(data){
	            			var html = '';
	            			for (var i=0; i < data.length; i++) {
	            				html += '<li>';
	            				var idStri = '';
	            				if (data[i].id != null) {
	            					idStri = data[i].id
	            				}
	            				if(data[i].url !=null && data[i].url != '' ){
	            					html += '<a href="${ctx}/mob/v1/more/urlDetail?id=' + idStri + '&token=' + token + '&noticeId='+ data[i].noticeId +'" class="list_a" >';
								}else {
									html += '<a href="#page_detail" class="list_a list_page" message="' + idStri + '" notice="' + data[i].noticeId + '">';
								}
	            				
	            				html += '<p class="title">' + data[i].title+ '</p>';
	            				html += '<p>来自:站内信&nbsp;';
	            				if (data[i].status == 0) {
	            				html += '<span class="new"  click_id="'+ data[i].id +'"></span>';
	            				}
	            				html += '</p><div class="arrow2"></div></a></li>';
	            			}
	            			 $('#notice_ul').append(html);
	            			 isLoading = false;
	            			 $('a.list_page').bind('click', pageClick);
	            		}
	            	});
	                }
	        });   
});
</script>  
</body>
</html>