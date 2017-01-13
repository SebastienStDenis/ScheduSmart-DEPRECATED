angular.
	module('uwScheduleBuilderApp').
	controller('AppController', ['$scope', '$location', '$mdSidenav', 'Schedules', function AppController($scope, $location, $mdSidenav, Schedules) {
	  var self = this;
		
	  self.openSideNav = function() {
		  $mdSidenav('left').toggle();
	  };
	  
	  self.loading = true;
	  	  
	  $scope.$on('SearchReady', function() {
		    self.loading = false;
	  });
	  
	  this.sidenavOpen = true;
	  
	  this.schedules = Schedules;
	  
	  this.notSmall = function() {
		  return true;
	  }
	  
	  this.isList = function() {
		  return $location.path() == '/list';
	  }
}]);