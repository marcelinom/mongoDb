'use strict';

/* Controller */

var geoorigem, geodestino;

nossologControllers.controller('CargaInsertCtrl', ['$scope', '$rootScope', '$http', '$location',
  function($scope, $rootScope, $http, $location) {
    $scope.login = sessionStorage.getItem('login');
	if ($scope.login) $scope.login = angular.fromJson($scope.login);
	else $rootScope.loginExpirado('');
	$scope.alerts = [];
    $scope.aguardar = false;
	$scope.carga = {
		"descricao" : "", 
  	    "coleta" : {"lat":0, "lng":0, "descricao": ""},
		"complOrigem" : "",
  	    "destino" : {"lat":0, "lng":0, "descricao": ""},
		"complDestino" : "",
        "unidade" : $scope.login.resposta.preferencias.moeda
  	};
	$scope.tipoPgto = "FIXO";
	$scope.pgtoPor = "T";
	$scope.atualizaUnidade = function() {
	   $scope.carga.unidade = $scope.login.resposta.preferencias.moeda + ($scope.tipoPgto != "FIXO" ? '/' +  $scope.pgtoPor : '');
	};
	
    geoorigem = $scope.carga.coleta;
	geodestino = $scope.carga.destino;
	
	$scope.sair = function() {
	   sessionStorage.removeItem('login');
  	   $location.path('/');
	};
		
    $scope.getLocation = function(val) {
      return $http.get('http://maps.googleapis.com/maps/api/geocode/json', {
        params: {
          address: val,
          sensor: false
        }
      }).then(function(res){
        $scope.addresses = [];
	    $scope.locais = [];
        angular.forEach(res.data.results, function(item){
          $scope.addresses.push(item.formatted_address);
          $scope.locais.push(item.geometry.location);
        });
        return $scope.addresses;
      });
    };
	  
    $scope.plotarOrigem= function() {
      for (var i=0; i<$scope.addresses.length; i++) {
   	  	if ($scope.addresses[i]==$scope.carga.coleta.descricao) {
          $scope.carga.coleta.lat = $scope.locais[i].lat;
          $scope.carga.coleta.lng = $scope.locais[i].lng;
        } 
	  } 
	  geoorigem = $scope.carga.coleta;
	  document.getElementById('mapaOrigemDestino').contentWindow.plotarOrigem();	  
    };	
	  
    $scope.plotarDestino= function() {
      for (var i=0; i<$scope.addresses.length; i++) {
   	  	if ($scope.addresses[i]==$scope.carga.destino.descricao) {
          $scope.carga.destino.lat = $scope.locais[i].lat;
          $scope.carga.destino.lng = $scope.locais[i].lng;
        } 
	  } 
	  geodestino = $scope.carga.destino;
	  document.getElementById('mapaOrigemDestino').contentWindow.plotarDestino();	  
    };	

    $scope.insert= function() {
  	  $scope.novo = {
	    "embarcador" : {"apelido":$scope.login.usuario},
		"descricao" : $scope.carga.descricao, 
  	    "coleta" : {"lat":$scope.carga.coleta.lat, 
				    "lng":$scope.carga.coleta.lng, 
					"descricao": $scope.carga.coleta.descricao},
  	    "destino" : {"lat":$scope.carga.destino.lat, 
				  	 "lng":$scope.carga.destino.lng, 
					 "descricao": $scope.carga.destino.descricao},
        "unidade" : $scope.carga.unidade
  	  };
	  if (!$scope.valida()) return;
	  
      $scope.aguardar = true;
	  if ($scope.carga.complOrigem) 
	  	 $scope.novo.coleta.descricao = $scope.carga.coleta.descricao + ' (' + $scope.carga.complOrigem + ')';
	  if ($scope.carga.complDestino) 
	  	 $scope.novo.destino.descricao = $scope.carga.destino.descricao + ' (' + $scope.carga.complDestino + ')';
	  
      $http({
            url: 'https://nlendpoint.appspot.com/_ah/api/web/v1/frete/cadastrar?sessao='+$scope.login.codigo,
            method: "POST",
            data: $scope.novo
        }).success(function (data, status, headers, config) {
		    $scope.aguardar = false;
  	        $scope.alerts.push({type: 'success', msg: 'Carga cadastrada com sucesso!'});
        }).error(function (data, status, headers, config) {
		    $scope.aguardar = false;
	  	    $scope.alerts.push({type: 'danger', msg: obtemMensagemErro('frete/cadastrar', data.error.message, status)});
        	if (data.error.message=='519') {
			    $rootScope.loginExpirado(data.error.message);
        	}
        });
    };	
	  
    $scope.voltar = function() {
	   history.back();
    };	  
	
    $scope.valida = function() {
	  $scope.alerts = [];
	  if (!$scope.novo.embarcador.apelido) 
	  	  $scope.alerts.push({type: 'danger', msg: "Não foi possível identificar sua conta!"});
	  if (!$scope.novo.descricao) 
	  	  $scope.alerts.push({type: 'danger', msg: "Descrição é obrigatório!"});
	  if (!$scope.novo.unidade) 
	  	  $scope.alerts.push({type: 'danger', msg: "Pagamento é obrigatório!"});
	  if ($scope.novo.coleta.lat == 0 && $scope.novo.coleta.lng == 0 || !$scope.carga.coleta.descricao) 
	  	  $scope.alerts.push({type: 'danger', msg: "Origem é obrigatório!"});
	  if ($scope.novo.destino.lat == 0 && $scope.novo.destino.lng == 0 || !$scope.carga.destino.descricao) 
	  	  $scope.alerts.push({type: 'danger', msg: "Destino é obrigatório!"});
		  
	  return $scope.alerts.length == 0;
    };	  
	
    $scope.closeAlert = function(index) {
      $scope.alerts.splice(index, 1);
    };	
	
  }]);  

