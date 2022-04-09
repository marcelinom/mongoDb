'use strict';

/* Controller */

nossologControllers.controller('TabelaCargaCtrl', ['$scope', '$route', '$location', '$http', '$interval', 
  function($scope, $route, $location, $http, $interval) {
	$scope.alerts = [];    
    $scope.cloud_ok = false;

	$scope.carregar = function() {
	    $http.get('http://localhost:8080/TomcatStock/rest/serie/scalping/'+$scope.trade.aberto)
		  .success(function(data) {
	        $scope.cloud_ok = true;
	        $scope.cargas = data;
	      }).error(function (data, status, headers, config) {
	        $scope.cloud_ok = true;
	   	    $scope.alerts.push({type: 'danger', msg: data.error});
	      });
	};		
	$scope.carregar();

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
    
    $scope.closeAlert = function(index) {
        $scope.alerts.splice(index, 1);
      };	
  	    
    $interval($scope.carregar, 40000);
	
  }]);
  
