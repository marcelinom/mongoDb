  function graficoBasico(onde, opcoes) {
    var largura = $(onde).width();
    var altura = $(onde).height();
	var margin = {top : 20,	right : 20,	bottom : 20, left : 45}, 
		width = largura - margin.left - margin.right, 
		height = altura - margin.top - margin.bottom;

	var timeFormat = d3.timeFormat("%d/%m/%Y %H:%M:%S"), 
		valueFormat = d3.format(',.2f');
	
	var x = techan.scale.financetime().range([ 0, width ]);
	var y = d3.scaleLinear().range([ height, 0 ]);
	var yVolume = d3.scaleLinear().range([ y(0), y(0.2) ]);
	var candlestick = techan.plot.candlestick().xScale(x).yScale(y);
	var volume = techan.plot.volume().accessor(candlestick.accessor()).xScale(x).yScale(yVolume);
    var plotAvgLine = d3.line().defined(function(d) {return !isNaN(d.avg);}).x(function(d) {return x(d.date);}).y(function(d) {return y(d.avg);});
	var xAxis = d3.axisBottom(x);
	var yAxis = d3.axisLeft(y);
	var volumeAxis = d3.axisRight(yVolume).ticks(3).tickFormat(d3.format(",.3s"));
	var timeAnnotation = techan.plot.axisannotation().axis(xAxis).orient('bottom').format(d3.timeFormat("%d/%m/%Y %H:%M:%S")).width(65).translate([ 0, height ]);
	var ohlcAnnotation = techan.plot.axisannotation().axis(yAxis).orient('left').format(d3.format(',.2f'));
	var volumeAnnotation = techan.plot.axisannotation().axis(volumeAxis).orient('right').width(35);
	var crosshair = techan.plot.crosshair().xScale(x).yScale(y).xAnnotation(timeAnnotation).yAnnotation([ ohlcAnnotation, volumeAnnotation ]);
	var supstance = techan.plot.supstance().xScale(x).yScale(y);
	var trendline = techan.plot.trendline().xScale(x).yScale(y);
	var bollinger = techan.plot.bollinger().xScale(x).yScale(y);
	var tradearrow = techan.plot.tradearrow().xScale(x).yScale(y).orient(function(d) { return d.type.startsWith("buy") ? "up" : "down"; });
	var sma0 = techan.plot.sma().xScale(x).yScale(y);
	var sma1 = techan.plot.sma().xScale(x).yScale(y);
	var sma2 = techan.plot.sma().xScale(x).yScale(y);
	var svg = d3.select(onde).append("svg").attr("width",	width + margin.left + margin.right).attr("height", height + margin.top + margin.bottom);
	var valueText = svg.append('text').style("text-anchor", "end").attr("class", "coords").attr("x", width - 5).attr("y", 15);

	var defs = svg.append("defs");
	defs.append("clipPath").attr("id", "ohlcClip").append("rect").attr("x", 0).attr("y", 0).attr("width", width).attr("height", height);
	defs.append("clipPath").attr("id", "supstanceClip").append("rect").attr("x", -margin.left).attr("y", 0).attr("width", width + margin.left).attr("height", height);
	svg = svg.append("g").attr("transform",	"translate(" + margin.left + "," + margin.top + ")");
	svg.append('text').attr("class", "symbol").attr("x", 30).text(nomeEmpresa);
	
    svg.append("g").attr("id", "avg_line").append("path").attr("class", "line");
	svg.append("g").attr("class", "x axis").attr("transform", "translate(0," + height + ")");
	svg.append("g").attr("class", "y axis").append("text").attr("transform", "rotate(-90)").attr("y", 6).attr("dy",	".71em").style("text-anchor", "end").text("Price ($)");
	
	svg.append("g").attr("class", "candlestick");
	svg.append("g").attr("class", "supstances").attr("clip-path", "url(#supstanceClip)");
	svg.append("g").attr("class", "trendlines");
	svg.append("g").attr("class", "bollinger");
	svg.append("g").attr("class", "volume axis");
	svg.append('g').attr("class", "crosshair ohlc");
	svg.append("g").attr("class", "indicator sma ma-0").attr("clip-path", "url(#ohlcClip)");
	svg.append("g").attr("class", "indicator sma ma-1").attr("clip-path", "url(#ohlcClip)");
	svg.append("g").attr("class", "indicator sma ma-2").attr("clip-path", "url(#ohlcClip)");
    svg.append("g").attr("class", "tradearrow");

	var bL = techan.scale.plot.bollinger(dadosTecnicos[0]).domain();
	var cL = techan.scale.plot.ohlc(dadosTecnicos[6], candlestick.accessor()).domain();
	var yLim = [bL[0]?Math.min(bL[0], cL[0])*0.97:cL[0]*0.97, bL[1]?Math.max(bL[1], cL[1]):cL[1]];
	
	yVolume.domain(techan.scale.plot.volume(dadosTecnicos[6]).domain());
	x.domain(dadosTecnicos[6].map(candlestick.accessor().d));
	y.domain(yLim);
	
	svg.selectAll("g.x.axis").call(xAxis);
	svg.selectAll("g.y.axis").call(yAxis);
	svg.selectAll("g.volume.axis").call(volumeAxis);
	if (opcoes[11]) svg.select("g#avg_line path").datum(dadosTecnicos[6]).attr("d", plotAvgLine);
	if (opcoes[10]) svg.selectAll("g.candlestick").datum(dadosTecnicos[6]).call(candlestick);
	svg.selectAll("g.volume").datum(dadosTecnicos[6]).call(volume);

	if (opcoes[0]=='M') svg.selectAll("g.trendlines").datum(dadosTecnicos[4]).call(trendline);
	else if (opcoes[0]=='G') svg.selectAll("g.trendlines").datum(dadosTecnicos[9]).call(trendline);
	else if (opcoes[0]=='N') svg.selectAll("g.trendlines").datum([]).call(trendline);
	if (opcoes[1]) svg.selectAll("g.supstances").datum(dadosTecnicos[5]).call(supstance);
	if (opcoes[2]) svg.selectAll("g.bollinger").datum(dadosTecnicos[0]).call(bollinger);
	if (opcoes[3]) {
		svg.selectAll("g.sma.ma-0").datum(dadosTecnicos[1]).call(sma0);
        svg.selectAll("g.sma.ma-1").datum(dadosTecnicos[2]).call(sma1);
        svg.selectAll("g.sma.ma-2").datum(dadosTecnicos[3]).call(sma2);
	}
	if (opcoes[8]) svg.selectAll("g.tradearrow").datum(dadosTecnicos[7]).call(tradearrow);
	if (opcoes[6]) svg.selectAll("g.tradearrow").datum(dadosTecnicos[12]).call(tradearrow);
	
	svg.select("g.crosshair.ohlc").call(crosshair);
    $("#spinner").hide();
}
