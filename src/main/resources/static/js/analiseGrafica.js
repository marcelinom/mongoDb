var nomeEmpresa;
var dadosTecnicos = [];

$(function() {
    $("#acao").autocomplete({
		source: "/cripto/codigo/listar",
        minLength: 1
    });
  });
  
function obterDados(onde, opcoes, url, acao, seq, inicio, fim) {
	var parseDate = d3.timeParse("%d/%m/%Y %H:%M:%S");
	d3.json(url+"/"+acao+"/"+seq+"/"+inicio+"/"+fim, function(error, data) {
		nomeEmpresa = data.nome;
		dadosTecnicos[0] = data.bollinger.map(function(d) {
			return {
				date : parseDate(d.data),
				lowerBand : d.lower,
				middleBand : d.middle,
				upperBand : d.upper,
			};
		});

		dadosTecnicos[1] = data.sma20.map(function(d) {
			return {
				date : parseDate(d.data),
				value : d.valor
			};
		});
		dadosTecnicos[2] = data.sma50.map(function(d) {
			return {
				date : parseDate(d.data),
				value : d.valor
			};
		});
		dadosTecnicos[3] = data.sma200.map(function(d) {
			return {
				date : parseDate(d.data),
				value : d.valor
			};
		});

		dadosTecnicos[4] = data.trends.map(function(d) {
			return {
				start : {
					date : parseDate(d.p1.data),
					value : d.p1.valor
				},
				end : {
					date : parseDate(d.p2.data),
					value : d.p2.valor
				}
			};
		});

		dadosTecnicos[5] = data.supstances.map(function(d) {
			if (d.p2.data) {
				return {
					start : parseDate(d.p1.data),
					end : parseDate(d.p2.data),
					value : +d.p1.valor
				};
			} else {
				return {
					start : parseDate(d.p1.data),
					value : +d.p1.valor
				};
			}
		});

		dadosTecnicos[6] = data.cotacoes.map(function(d) {
			return {
				date : parseDate(d.data),
				open : +d.abertura,
				high : +d.maxima,
				low : +d.minima,
				close : +d.fechamento,
				avg : +d.media,
				volume : +d.volume
			};
		});
		
		dadosTecnicos[7] = data.gaps.map(function(d) {
			return {
				date : parseDate(d.data),
				type : "gap",
				quantity: 100,
				price : d.valor
			};
		});

		dadosTecnicos[8] = data.stk.map(function(d) {
			return {
				date : parseDate(d.data),
				middle: 50,
				overbought: 80,
				oversold: 20,
				stochasticD : d.valorA,
				stochasticK : d.valorB
			};
		});

		dadosTecnicos[9] = data.trendsG.map(function(d) {
			return {
				start : {
					date : parseDate(d.p1.data),
					value : d.p1.valor
				},
				end : {
					date : parseDate(d.p2.data),
					value : d.p2.valor
				}
			};
		});

		dadosTecnicos[10] = data.rsi.map(function(d) {
			return {
				date : parseDate(d.data),
				middle: 50,
				overbought: 70,
				oversold: 30,
				rsi : d.valor
			};
		});

		dadosTecnicos[11] = data.macd.map(function(d) {
			return {
				date : parseDate(d.data),
				difference: d.valorA-d.valorB,
				macd: d.valorA,
				signal: d.valorB,
				zero : 0
			};
		});

		dadosTecnicos[12] = data.obv.map(function(d) {
			return {
				date : parseDate(d.data),
				open : d.valor,
				high : d.valor,
				low : d.valor,
				close : d.valor
			};
		});

		dadosTecnicos[13] = data.rsi.map(function(d) {
			return {
				date : parseDate(d.data),
				open : d.valor,
				high : d.valor,
				low : d.valor,
				close : d.valor
			};
		});

		desenhar(onde, opcoes);
	});		
}

function plotar(onde, detalhar) {
	var agora = new Date();
	if (!$('#dfim').val()) {
		var str = agora.getFullYear() + '-'
				+ ('0' + (agora.getMonth() + 1)).slice(-2) + '-'
				+ ('0' + agora.getDate()).slice(-2);
		$('#dfim').val(str);

		var dAux = new Date(agora.getTime());
		dAux.setDate(dAux.getDate() - 100);
		str = dAux.getFullYear() + '-'
				+ ('0' + (dAux.getMonth() + 1)).slice(-2) + '-'
				+ ('0' + dAux.getDate()).slice(-2);
		$('#dini').val(str);
	}

	var dayTrade = $('#daytrade').prop('checked');
	var seq = $('#seq').val();
	var acao = $('#acao').val();
	var inicio = $('#dini').val();
	var fim = $('#dfim').val();
	var opcoes = [$("input[name='trends']:checked").val(), 
				  $('#sr').prop('checked'),
				  $('#boll').prop('checked'),
				  $('#sma').prop('checked'),
				  $('#rsi').prop('checked'),
				  $('#macd').prop('checked'),
				  $('#dica').prop('checked'),
				  $('#stoc').prop('checked'),
				  $('#gap').prop('checked'),
				  $('#obv').prop('checked'),
				  $('#ohlc').prop('checked'),
				  $('#avg').prop('checked')];

	if (inicio >= fim) {
		alert("Período inválido!");
		return;
	} else if (seq<1) {
		alert("Timeframe deve ser maior que zero!");
		return;
	} else if (!acao) {
		alert("Informe o codigo da acao!");
		return;
	}
	
    $(onde[0]).empty();
	$("#spinner").show();
	if (detalhar) desenhar(onde, opcoes);
	else {
		var servico = dayTrade?"/TomcatStock/rest/serie/scalping":"/cripto/temporal";
		obterDados(onde, opcoes, servico, acao, seq, inicio, fim);
	}

}

function criaQuadro(quadro) {
	var dayTrade = $('#daytrade').prop('checked');
	var iDiv = document.createElement('div');
	iDiv.id = quadro.substring(1);
	iDiv.className = 'ui-widget-content';
	document.getElementsByTagName('body')[0].appendChild(iDiv);
	$(quadro).draggable();
	$( function() {
		$(quadro).resizable(
			{
               stop: function (event, ui) {
            	  d3.select(quadro).select("svg").remove();
                  if (quadro==onde[1])
                	  if (dayTrade) graficoLinha(quadro, "Agressão", dadosTecnicos[13])
                	  else graficoRSI(quadro);
                  else if (quadro==onde[2]) graficoMACD(quadro);
                  else if (quadro==onde[3]) graficoStochastic(quadro);
                  else if (quadro==onde[4]) graficoLinha(quadro, dayTrade?"Delta Acum":"OBV", dadosTecnicos[12]);
               }
	        }				
		);
	});
}

function desenhar(onde, opcoes) {
	var dayTrade = $('#daytrade').prop('checked');
	graficoBasico(onde[0], opcoes);
    if (opcoes[4]) {
    	if (!($(onde[1]).length>0)) criaQuadro(onde[1]);
    	else d3.select(onde[1]).select("svg").remove();
  	    if (dayTrade) graficoLinha(onde[1], "Agressão", dadosTecnicos[13])
	    else graficoRSI(onde[1]);
    } else {
    	if (($(onde[1]).length>0)) $(onde[1]).remove();
    }
    if (opcoes[5]) {
    	if (!($(onde[2]).length>0)) criaQuadro(onde[2]);
    	else d3.select(onde[2]).select("svg").remove();
    	graficoMACD(onde[2]);
    } else {
    	if (($(onde[2]).length>0)) $(onde[2]).remove();
    }
    if (opcoes[7]) {
    	if (!($(onde[3]).length>0)) criaQuadro(onde[3]);
    	else d3.select(onde[3]).select("svg").remove();
    	graficoStochastic(onde[3]);
    } else {
    	if (($(onde[3]).length>0)) $(onde[3]).remove();
    }
    if (opcoes[9]) {
    	if (!($(onde[4]).length>0)) criaQuadro(onde[4]);
    	else d3.select(onde[4]).select("svg").remove();
    	graficoLinha(onde[4], dayTrade?"Delta Acum":"OBV", dadosTecnicos[12]);	
    } else {
    	if (($(onde[4]).length>0)) $(onde[4]).remove();
    }
}
