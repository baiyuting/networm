<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"> 
<title>ECharts</title>
<script src="js/echarts.min.js"></script>
</head>
<body>
<!-- <%
    out.println("Hello World!");
%>
-->
    <div id="main" style="width: 900px;height:400px;"></div>
    <script type="text/javascript">
        
        var myChart = echarts.init(document.getElementById('main'));

        
        var option = {
            title: {
                text: 'Sina 科技热词展示'
            },
            tooltip: {},
            legend: {
                data:['科技热词']
            },
            xAxis: {
                data: ["手机", "iphone", "双", "智能", "ai", "快递", "屏", "单车", "共享", "人工智能"]
            },
            yAxis: {},
            series: [{
                name: '次数',
                type: 'bar',
                data: [89, 87, 52, 47, 43, 43, 40, 40, 38, 38]
            }]
        };

        
        myChart.setOption(option);
    </script>
</body>
</html>