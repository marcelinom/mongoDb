'use strict';

/* Controller */

nossologControllers.controller('CargaDetalheCtrl', ['$scope', '$rootScope', '$routeParams', '$http', '$modal', '$location', '$window',
  function($scope, $rootScope, $routeParams, $http, $modal, $location, $window) {
    $scope.login = sessionStorage.getItem('login');
	if ($scope.login) $scope.login = angular.fromJson($scope.login);
	else $rootScope.loginExpirado('');
	$scope.alerts = [];
    $scope.cloud_ok = false;
  	var chave = $routeParams.freteId;
    $http.get('https://nlendpoint.appspot.com/_ah/api/web/v1/frete/buscar?codigo='+chave+'&usuario='+$scope.login.usuario+'&embarcador='+$scope.login.usuario+'&sessao='+$scope.login.codigo)
	  .success(function(data) {
        $scope.cloud_ok = true;
        $scope.carga = data.resposta;
		$scope.distancia = haversine($scope.carga.coleta.lat, $scope.carga.coleta.lng, $scope.carga.destino.lat, $scope.carga.destino.lng);
      }).error(function (data, status, headers, config) {
        $scope.cloud_ok = true;
   	    $scope.alerts.push({type: 'danger', msg: obtemMensagemErro('frete/buscar', data.error.message, status)});
		if (data.error.message=='519') {
		    $rootScope.loginExpirado(data.error.message);
		}
      });
	
	$scope.exibePedido = function() {
	  $scope.filtroPedido = {'tipo': 'carga', 'valor': $scope.carga.codigo};
	};
	
	$scope.exibeProposta = function() {
	  $scope.filtroProposta = {'tipo': 'carga', 'valor': $scope.carga.codigo};
	};
	
	$scope.exibeFrete = function() {
	  $scope.filtroFrete = {'tipo': 'carga', 'valor': $scope.carga.codigo};
	};
	
	$scope.sair = function() {
	   sessionStorage.removeItem('login');
  	   $location.path('/');
	};
	
	$scope.verPerfil = function(transportador) {
	   var sw = $window.screen.availWidth/3;
	   var sh = $window.screen.availHeight*2/3;
	   var st = ($window.screen.availHeight-sh)/2;
	   var sl = ($window.screen.availWidth-sw)/2;
	   var urlPerfil = $location.absUrl().replace($location.path(),'/mini/caminhoneiro/'+transportador);
	   $window.open(urlPerfil, '_blank', 'width='+sw+',height='+sh+',top='+st+',left='+sl);
	};
			
    $scope.perguntas_ok = false;
    $scope.buscarPerguntas = function() {
      $http.get('https://nlendpoint.appspot.com/_ah/api/web/v1/frete/perguntas/listar?codigo='+chave+'&usuario='+$scope.login.usuario+'&sessao='+$scope.login.codigo)
	    .success(function(data) {
    	  $scope.perguntas_ok = true;
          $scope.perguntas = data.resposta;
        }).error(function (data, status, headers, config) {
          $scope.cloud_ok = true;
   	      $scope.alerts.push({type: 'danger', msg: obtemMensagemErro('frete/perguntas/listar', data.error.message, status)});
		  if (data.error.message=='519') {
		    $rootScope.loginExpirado(data.error.message);
		  }
        });
	};
	
	if (!$scope.perguntas_ok) {
	   $scope.buscarPerguntas();
	};
	
    $scope.rastrear = function(acordo, frete, transportador, embarcador) {
	   $location.path('/rastreamento/' +embarcador + '/' + transportador + '/' + acordo + '/' + frete);
    };
	
    $scope.voltar = function() {
	   history.back();
    };
	
	$scope.novoAnuncio = function() {
	  $location.path('/pedido/inserir/' + $routeParams.freteId);
	};
	
    $scope.closeAlert = function(index) {
      $scope.alerts.splice(index, 1);
    };			

	$scope.callbackExcluirOk = function(codigo) {
      $scope.aguardar = true;
      $http({
            url: 'https://nlendpoint.appspot.com/_ah/api/web/v1/frete/remover?codigo='+codigo+'&usuario='+$scope.login.usuario+'&sessao='+$scope.login.codigo,
            method: "DELETE"
        }).success(function (data, status, headers, config) {
            $scope.aguardar = false;
		    $scope.voltar();
        }).error(function (data, status, headers, config) {
            $scope.aguardar = false;
	  	    $scope.alerts.push({type: 'danger', msg: obtemMensagemErro('frete/remover', data.error.message, status)});
   		    if (data.error.message=='519') {
			    $rootScope.loginExpirado(data.error.message);
    		}
        });
    };
	
 	$scope.sort = {
		column: '',
        descending: false
    };    
	
    $scope.changeSorting = function(column) {
        var sort = $scope.sort;
        if (sort.column == column) {
           sort.descending = !sort.descending;
        } else {
           sort.column = column;
           sort.descending = false;
        }
    };
	
	$scope.callbackResponderOk = function(selectedItem) {
		$scope.buscarPerguntas();
	}	

	$scope.callbackResponderErro = function(origem, mensagem, status) {
      $scope.alerts.push({type: 'danger', msg: obtemMensagemErro(origem, mensagem, status)});
   	  if (mensagem == '519') {
	    $rootScope.loginExpirado(mensagem);
   	  }
	}	
	
  }]);




