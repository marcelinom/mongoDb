'use strict';

/* App Module */

var nossologApp = angular.module('nossologApp', [
  'ngRoute',
  'nossologControllers',
  'ui.bootstrap'
]);

var nossologControllers = angular.module('nossologControllers', []);

nossologApp.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/', {
	    cabecalho: true,
	    rodape: true,
        templateUrl: 'partials/login/edita.html',
        controller: 'RedirecionaCtrl'
      }).
      when('/login', {
	    cabecalho: true,
	    rodape: true,
        templateUrl: 'partials/login/edita.html',
        controller: 'LoginCtrl'
      }).
      when('/esqueci/:usuarioId/:tokenId', {
	    cabecalho: true,
	    rodape: true,
        templateUrl: 'partials/login/esqueci-senha.html',
        controller: 'EsqueciSenhaCtrl'
      }).
      when('/confirmaEmail/:usuarioId/:sessaoId', {
	    cabecalho: true,
	    rodape: true,
        templateUrl: 'partials/login/edita.html',
        controller: 'ConfirmaEmailCtrl'
      }).
      when('/cadastrar', {
	    cabecalho: true,
	    rodape: true,
        templateUrl: 'partials/cadastro/basico.html',
        controller: 'CadastramentoCtrl'
      }).
      when('/cadastrarPE', {
	    rodape: true,
        templateUrl: 'partials/cadastro/perfil-embarcador.html',
        controller: 'CadastraPerfilEmbarcadorCtrl'
      }).
      when('/cadastrarPC', {
	    rodape: true,
        templateUrl: 'partials/cadastro/perfil-caminhoneiro.html',
        controller: 'CadastraPerfilCaminhoneiroCtrl'
      }).
      when('/cadastrarPrefs', {
	    rodape: true,
        templateUrl: 'partials/cadastro/preferencias.html',
        controller: 'CadastraPreferenciaCtrl'
      }).
      when('/fretes', {
	    rodape: true,
        templateUrl: 'partials/frete/lista.html',
        controller: 'FreteListCtrl'
      }).
      when('/frete/exibir/:acordoId', {
	    rodape: true,
        templateUrl: 'partials/frete/detalhe.html',
        controller: 'FreteDetalheCtrl'
      }).
      when('/cargas', {
	    rodape: true,
        templateUrl: 'partials/carga/lista.html',
        controller: 'CargaListCtrl'
      }).
      when('/carga/exibir/:freteId', {
	    rodape: true,
        templateUrl: 'partials/carga/detalhe.html',
        controller: 'CargaDetalheCtrl'
      }).
      when('/carga/inserir', {
	    rodape: true,
        templateUrl: 'partials/carga/novo.html',
        controller: 'CargaInsertCtrl'
      }).
      when('/pedidos', {
	    rodape: true,
        templateUrl: 'partials/pedido/lista.html',
        controller: 'PedidoListCtrl'
      }).
      when('/mini/pedido/:ofertaId', {
        templateUrl: 'partials/pedido/mini-detalhe.html',
        controller: 'PedidoDetalheCtrl'
      }).
      when('/pedido/exibir/:ofertaId', {
	    rodape: true,
        templateUrl: 'partials/pedido/detalhe.html',
        controller: 'PedidoDetalheCtrl'
      }).
      when('/pedido/inserir/:freteId', {
	    rodape: true,
        templateUrl: 'partials/pedido/novo.html',
        controller: 'PedidoInsertCtrl'
      }).
      when('/propostas', {
	    rodape: true,
        templateUrl: 'partials/proposta/lista.html',
        controller: 'PropostaListCtrl'
      }).
      when('/preferencias', {
	    rodape: true,
        templateUrl: 'partials/preferencia/edita.html',
        controller: 'PreferenciaEditCtrl'
      }).
      when('/extrato', {
	    rodape: true,
        templateUrl: 'partials/utilizacao/lista.html',
        controller: 'ExtratoListCtrl'
      }).
      when('/planos', {
	    rodape: true,
        templateUrl: 'partials/comercial/planos.html',
        controller: 'PlanosListCtrl'
      }).
      when('/precos', {
	    rodape: true,
        templateUrl: 'partials/comercial/precos.html',
        controller: 'PrecosListCtrl'
      }).
      when('/voip', {
	    rodape: true,
        templateUrl: 'partials/comercial/aplicativo.html',
        controller: 'AplicativoCtrl'
      }).
      when('/verifica', {
	    rodape: true,
        templateUrl: 'partials/verifica/detalhe.html',
        controller: 'VerificaCtrl'
      }).
      when('/mobile/verifica/:usuarioId/:pais', {
        templateUrl: 'partials/mobile/verifica/detalhe.html',
        controller: 'VerificaMobileCtrl'
      }).
      when('/perfil/embarcador', {
	    rodape: true,
        templateUrl: 'partials/perfil/embarcador-edita.html',
        controller: 'PerfilEmbarcadorEditaCtrl'
      }).
      when('/perfil/caminhoneiro', {
	    rodape: true,
        templateUrl: 'partials/perfil/caminhoneiro-edita.html',
        controller: 'PerfilCaminhoneiroEditaCtrl'
      }).
      when('/home/caminhoneiro/:candidatoId', {
	    rodape: true,
        templateUrl: 'partials/perfil/caminhoneiro.html',
        controller: 'PerfilCaminhoneiroCtrl'
      }).
      when('/home/embarcador/:candidatoId', {
	    rodape: true,
        templateUrl: 'partials/perfil/embarcador.html',
        controller: 'PerfilEmbarcadorCtrl'
      }).
      when('/minix/caminhoneiro/:candidatoId/:estendido', {
        templateUrl: 'partials/perfil/mini-caminhoneiro.html',
        controller: 'PerfilCaminhoneiroCtrl'
      }).
      when('/mini/caminhoneiro/:candidatoId', {
        templateUrl: 'partials/perfil/mini-caminhoneiro.html',
        controller: 'PerfilCaminhoneiroCtrl'
      }).
      when('/mini/veiculo/:usuarioId/:placaId', {
        templateUrl: 'partials/veiculo/mini-veiculo.html',
        controller: 'VeiculoCtrl'
      }).
      when('/mini/embarcador/:candidatoId', {
        templateUrl: 'partials/perfil/mini-embarcador.html',
        controller: 'PerfilEmbarcadorCtrl'
      }).
      when('/qualificacoes/:tipoId/:candidatoId', {
        templateUrl: 'partials/qualificacao/lista.html',
        controller: 'QualificacoesListCtrl'
      }).
      when('/rastreamento/:embarcadorId/:transportadorId/:acordoId/:cargaId', {
	    rodape: true,
        templateUrl: 'partials/frete/rastreamento.html',
        controller: 'FreteRastreamentoCtrl'
      }).
      when('/mobile/qualificacoes/:usuarioId/:regId/:tipoId/:candidatoId', {
        templateUrl: 'partials/mobile/qualificacao/lista.html',
        controller: 'QualificacoesMobileListCtrl'
      }).
      otherwise({
        redirectTo: '/'
      });
  }]);
  
nossologApp.run(['$location', '$rootScope', function($location, $rootScope) {
    $rootScope.$on('$routeChangeSuccess', function (event, current, previous) {
        $rootScope.cabecalho = current.$$route.cabecalho;
        $rootScope.rodape = current.$$route.rodape;
    });
    $rootScope.moedas = [{nome:'REAL', simbolo:'R$'}, {nome:'DOLAR', simbolo:'US$'}];
	$rootScope.simboloMoeda = function(nome) {
      for (var i = 0, len = $rootScope.moedas.length; i < len; i++) {
          if ($rootScope.moedas[i].nome == nome)
		    return $rootScope.moedas[i].simbolo;
      }
	}
	
	$rootScope.loginExpirado = function(mensagem) {
    	sessionStorage.removeItem('login');
        sessionStorage.setItem('expirou', mensagem);
  	    $location.path('/login');
	}
	
}]);  

