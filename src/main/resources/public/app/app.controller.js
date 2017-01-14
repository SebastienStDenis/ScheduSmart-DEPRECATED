angular.
	module('uwScheduleBuilderApp').
	controller('AppController', ['$scope', '$location', '$mdSidenav', '$mdDialog', 'Schedules', function AppController($scope, $location, $mdSidenav, $mdDialog, Schedules) {
	  var self = this;
		
	  self.openSideNav = function() {
		  $mdSidenav('left').toggle();
	  };
	  
	  self.loading = true;
	  	  
	  $scope.$on('SearchReady', function() {
		    self.loading = false;
	  });
	  
	  self.sidenavOpen = true;
	  
	  self.schedules = Schedules;
	  
	  self.notSmall = function() {
		  return true;
	  }
	  
	  self.isList = function() {
		  return $location.path() == '/list';
	  }
	  
	  self.showHelp = function() {
		  $mdDialog.show({
              controller: function DialogController($scope, $mdDialog) {
            	  $scope.closeDialog = function() {
                      $mdDialog.hide();
                  }
              },
              templateUrl: 'assets/html/helpDialog.html',
              parent: angular.element(document.body),
              clickOutsideToClose: false,
              fullscreen: false,
            })
	  }
	  
	  $scope.$on('ShowHelp', function() {
		  self.showHelp()
	  });
}]);