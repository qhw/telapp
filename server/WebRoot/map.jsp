<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<% request.setCharacterEncoding("utf-8"); %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=gb2312" />
<title>百度地图的Hello, World</title>
<script type="text/javascript" src="js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" src="http://api.map.baidu.com/api?v=1.3"></script>
<link rel="stylesheet" href="css/style.css">
</head>
<body>
	<div id="wraper">
		<div style="width:80%;height:600px;border:1px solid gray" id="container"></div>
		<div style="float:right;width:18%;border:1px" id = "content"></div>
	</div>
</body>
</html>
<script type="text/javascript">
var map = new BMap.Map("container");            // 创建Map实例
var point = new BMap.Point(108.914518, 34.235076);    // 创建点坐标
var circle;
var currentmarker;
addCurrentMarker(point);
openMapSetting(point);
addMenu();

//获取餐厅信息
$.post("shop.jsp", { latitude: 34.2347831, longitude: 108.9130483 } ,
function(data)
{
	addshops(data);
}); 

//根据请求返回来的数据在地图上增加餐厅图标
function addshops(data)
{
	var json = eval('(' + data + ')'); 
	var content = "";
	for (i = 0; i < json.length; i++)
	{
		var longitude = parseFloat(json[i].shoplng);
		var latitude = parseFloat(json[i].shoplat);	
		var pt = new BMap.Point(longitude, latitude);
		var sContent = 
		"<div width:200;height:200px;border:1px><a target='_blank' href='shopmenu.jsp?shopid=" + json[i].shopid +"'>" +
		"<img style='float:right;margin:4px' id='imgDemo' src='img/logo.gif' " + 
		" width='100' height='80' title='" + json[i].shopname + "'/></a>" + 
		"<p style='margin:0;line-height:1.5;font-size:13px;text-indent:2em'>餐厅: " + 
		json[i].shopname +"电话: " + json[i].shopphone +"距离: " + json[i].shopdistance +"米</p>" + 
		"</div>";
		
		var infoWindow = new BMap.InfoWindow(sContent);  // 创建信息窗口对象
		addMarker(pt, infoWindow);
		content += sContent + "<br/>";
	}
	//文本显示
	document.getElementById("content").innerHTML = content;
}
function trim(str){return   str.replace(/^\s+|\s+$/,   '')}
// 编写自定义函数,创建标注
function addMarker(point, infoWindow){
  var marker = new BMap.Marker(point);
  map.addOverlay(marker);
  marker.addEventListener("click", function(){          
   this.openInfoWindow(infoWindow);
   //图片加载完毕重绘infowindow
   document.getElementById('imgDemo').onload = function (){
       infoWindow.redraw();
   }
});
}
//设置我的位置  标注并平移到该位置
function addCurrentMarker(point)
{
	//创建小狐狸
	var myIcon = new BMap.Icon("http://dev.baidu.com/wiki/static/map/API/img/fox.gif", new BMap.Size(300,157));
  currentmarker = new BMap.Marker(point,{icon:myIcon});  // 创建标注
	map.addOverlay(currentmarker);              // 将标注添加到地图中

	//让小狐狸说话（创建信息窗口）
	//var infoWindow = new BMap.InfoWindow("<p style='font-size:14px;'>我在这里!</p>");
	//currentmarker.addEventListener("click", function(){this.openInfoWindow(infoWindow);});

	addOverlay(point, 1000);
	map.panTo(point);
}

// 编写创建覆盖物
function addOverlay(point, radius)
{
	circle = new BMap.Circle(point,radius);
	map.addOverlay(circle);
}

// 开启map的设置
function openMapSetting(point)
{
	map.centerAndZoom(point,15);                     // 初始化地图,设置中心点坐标和地图级别。
	map.enableScrollWheelZoom();  //启动滚轮             
	map.addControl(new BMap.ScaleControl());                    // 添加比例尺控件
	var opts = {anchor: BMAP_ANCHOR_TOP_RIGHT, offset: new BMap.Size(10, 10)}; 
	map.addControl(new BMap.NavigationControl(opts));              //添加缩略地图控件
}
//添加右键菜单
function addMenu()
{
	var contextMenu = new BMap.ContextMenu();
	var txtMenuItem = [
		{
	   text:'放大',
	   callback:function(){map.zoomIn()}
	  },
	  {
	   text:'缩小',
	   callback:function(){map.zoomOut()}
	  },
	  {
	   text:'标记所在位置',
	   callback:function(p){
	    var marker = new BMap.Marker(p), px = map.pointToPixel(p);
	    map.clearOverlays();
	    //获取餐厅信息
			$.post("shop.jsp", { latitude: p.lat, longitude: p.lng } ,
			function(data)
			{
				addshops(data);
			}); 
	    point = new BMap.Point(p.lng, p.lat);
	    addCurrentMarker(point);
	   }
	  }
	 ];
	 for(var i=0; i < txtMenuItem.length; i++){
	  contextMenu.addItem(new BMap.MenuItem(txtMenuItem[i].text,txtMenuItem[i].callback,100));
	 }
 	map.addContextMenu(contextMenu);
}
</script>