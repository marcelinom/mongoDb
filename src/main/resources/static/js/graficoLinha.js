function graficoLinha(onde, titulo, dados) {
	largura = $(onde).width();
	altura = $(onde).height();
	var margin = {
		top : 20,
		right : 20,
		bottom : 20,
		left : 50
	}, 
	width = largura - margin.left - margin.right, 
	height = altura	- margin.top - margin.bottom;
	
    var x = techan.scale.financetime().range([0, width]).outerPadding(0);
	var y = d3.scaleLinear().range([height, 0]);
	var close = techan.plot.close().xScale(x).yScale(y);
	var xAxis = d3.axisBottom(x);
	var yAxis = d3.axisLeft(y).tickFormat(d3.format(",.3s"));
	
	var timeAnnotation = techan.plot.axisannotation().axis(xAxis).orient('bottom').format(d3.timeFormat("%d/%m/%Y %H:%M:%S")).width(65).translate([ 0, height ]);
	var ohlcAnnotation = techan.plot.axisannotation().axis(yAxis).orient('left').format(d3.format(',.3s'));
	var crosshair = techan.plot.crosshair().xScale(x).yScale(y).xAnnotation(timeAnnotation).yAnnotation([ ohlcAnnotation ]);
	
    var svg = d3.select(onde).append("svg").attr("width", width + margin.left + margin.right).attr("height", height + margin.top + margin.bottom).append("g").attr("transform", "translate(" + margin.left + "," + margin.top + ")");	
	svg.append('g').attr("class", "crosshair ohlc");
    svg.append("g").attr("class", "close");
	svg.append("g").attr("class", "x axis").attr("transform", "translate(0," + height + ")");
	svg.append("g").attr("class", "y axis").append("text").attr("transform", "rotate(-90)").attr("y", 6).attr("dy", ".71em").style("text-anchor", "end").text(titulo);		
	    
    x.domain(dados.map(close.accessor().d));
    y.domain(techan.scale.plot.ohlc(dados, close.accessor()).domain());

    svg.selectAll("g.close").datum(dados).call(close);
    svg.selectAll("g.x.axis").call(xAxis);
    svg.selectAll("g.y.axis").call(yAxis);
	
	svg.select("g.crosshair.ohlc").call(crosshair);
}
