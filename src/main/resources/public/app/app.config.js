// configure the main application
angular.
	module('ScheduSmartApp').
	config(['$mdThemingProvider', '$locationProvider', '$routeProvider', '$mdGestureProvider',
		function config($mdThemingProvider, $locationProvider, $routeProvider, $mdGestureProvider) {
			// set color palette
			$mdThemingProvider.theme('default')
			    .primaryPalette('grey')
			    .accentPalette('amber')
			    .warnPalette('orange');
			
			$locationProvider.hashPrefix('!');
			
			// set up routes for list-view and week-view, default to week-view
			$routeProvider.
				when('/list', {
					template: '<list-view></list-view>'
				}).
				when('/week', {
					template: '<week-view></week-view>'
				}).
				otherwise('/week');
			
			$mdGestureProvider.skipClickHijack();
			
	}
]);