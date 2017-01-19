angular.
	module('uwScheduleBuilderApp').
	config(['$mdThemingProvider', '$locationProvider', '$routeProvider',
		function config($mdThemingProvider, $locationProvider, $routeProvider) {
			$mdThemingProvider.theme('default')
			    .primaryPalette('grey')
			    .accentPalette('amber')
			    .warnPalette('orange');
			
			$locationProvider.hashPrefix('!');
			
			$routeProvider.
				when('/list', {
					template: '<list-view></list-view>'
				}).
				when('/week', {
					template: '<week-view></week-view>'
				}).
				otherwise('/week');
			
	}
]);