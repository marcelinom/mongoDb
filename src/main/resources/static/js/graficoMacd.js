function graficoMACD(onde) {
	largura = $(onde).width();
	altura = $(onde).height();
	var margin = {
		top : 10,
		right : 20,
		bottom : 20,
		left : 45
	}, 
	width = largura - margin.left - margin.right, 
	height = altura	- margin.top - margin.bottom;
	
	var x = techan.scale.financetime().range([ 0, width ]);
	var y = d3.scaleLinear().range([ height, 0 ]);
	var macd = techan.plot.macd().xScale(x).yScale(y);
	var xAxis = d3.axisBottom(x);
	var yAxis = d3.axisLeft(y).tickFormat(d3.format(",.3s"));
	
	var timeAnnotation = techan.plot.axisannotation().axis(xAxis).orient('bottom').format(d3.timeFormat("%d/%m/%Y %H:%M:%S")).width(65).translate([ 0, height ]);
	var crosshair = techan.plot.crosshair().xScale(x).yScale(y).xAnnotation(timeAnnotation);
		
	var svg = d3.select(onde).append("svg").attr("width",	width + margin.left + margin.right).attr("height", height + margin.top + margin.bottom).append("g").attr("transform", "translate(" + margin.left + "," + margin.top + ")");

	svg.append("g").attr("class", "macd");
	svg.append("g").attr("class", "x axis").attr("transform", "translate(0," + height + ")");
	svg.append("g").attr("class", "y axis").append("text").attr("transform", "rotate(-90)").attr("y", 6).attr("dy", ".71em").style("text-anchor", "end").text("MACD");
	svg.append('g').attr("class", "crosshair ohlc");

	var macdData = dadosTecnicos[11];
	x.domain(macdData.map(macd.accessor().d));
	var dominio = techan.scale.plot.macd(macdData).domain();
	var yDom = [-Math.max(Math.abs(dominio[0]),Math.abs(dominio[1])), Math.max(Math.abs(dominio[0]),Math.abs(dominio[1]))];
	y.domain(yDom);

	svg.selectAll("g.macd").datum(macdData).call(macd);
	svg.selectAll("g.x.axis").call(xAxis);
	svg.selectAll("g.y.axis").call(yAxis);
	
	svg.select("g.crosshair.ohlc").call(crosshair);
}
