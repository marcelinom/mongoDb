'use strict';

/* Controller */

nossologControllers.controller('CargaListCtrl', ['$scope', '$rootScope', '$location', '$http', 
  function($scope, $rootScope, $location, $http) {
	$scope.alerts = [];
    $scope.cloud_ok = false;
    $scope.trade = {aberto:false};

	$scope.sair = function() {
	   sessionStorage.removeItem('login');
  	   $location.path('/');
	};
		
	$scope.salvar = function() {
	  $scope.cloud_ok = false;
      $http({
            url: 'http://localhost:8080/TomcatStock/rest/serie/bovespa/temporeal/gravar',
            method: "PUT",
        }).success(function (data, status, headers, config) {
        	$scope.cloud_ok = true;
  	        $scope.alerts.push({type: 'success', msg: 'Dados gravados com sucesso!'});
        }).error(function (data, status, headers, config) {
        	$scope.cloud_ok = true;
	  	    $scope.alerts.push({type: 'danger', msg: data.error});
        });		
	}
			
	$scope.novaCarga = function() {
	  $location.path('/carga/inserir');
	};
	
    $scope.closeAlert = function(index) {
        $scope.alerts.splice(index, 1);
    };	
  	    	
	
  }]);
  
