function graficoStochastic(onde) {
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
	
	var x = techan.scale.financetime().range([0, width]);
	var y = d3.scaleLinear().range([height, 0]);
	var stochastic = techan.plot.stochastic().xScale(x).yScale(y);
	var xAxis = d3.axisBottom(x);
	var yAxis = d3.axisLeft(y).tickFormat(d3.format(",.3s"));

	var timeAnnotation = techan.plot.axisannotation().axis(xAxis).orient('bottom').format(d3.timeFormat("%d/%m/%Y %H:%M:%S")).width(65).translate([ 0, height ]);
	var crosshair = techan.plot.crosshair().xScale(x).yScale(y).xAnnotation(timeAnnotation);
	
	var svg = d3.select(onde).append("svg").attr("width", width + margin.left + margin.right).attr("height", height + margin.top + margin.bottom).append("g").attr("transform", "translate(" + margin.left + "," + margin.top + ")");
	svg.append('g').attr("class", "crosshair ohlc");
    svg.append("g").attr("class", "stochastic");
	svg.append("g").attr("class", "x axis").attr("transform", "translate(0," + height + ")");
	svg.append("g").attr("class", "y axis").append("text").attr("transform", "rotate(-90)").attr("y", 6).attr("dy", ".71em").style("text-anchor", "end").text("Stochastic Oscillator");
		
    var stochasticData = dadosTecnicos[8];
    x.domain(stochasticData.map(stochastic.accessor().d));
    y.domain(techan.scale.plot.stochastic().domain());
    svg.selectAll("g.stochastic").datum(stochasticData).call(stochastic);
    svg.selectAll("g.x.axis").call(xAxis);
    svg.selectAll("g.y.axis").call(yAxis);
    
	svg.select("g.crosshair.ohlc").call(crosshair);
}
