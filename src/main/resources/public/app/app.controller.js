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
	  
	  self.Schedules = Schedules;
	  
	  self.noSchedules = function() {
		  console.log(self.Schedules.schedules.length);
		  if (self.Schedules.schedules.length === 0) {
			  return true;
		  } else {
			  console.log('return false');
			  return false;
		  }
	  }
	  
	  self.displayIndex = function() {
		  var schedCount = self.Schedules.schedules.length;
		  if (schedCount === 0) {
			  return 'no schedules';
		  } else {
			  return String(self.Schedules.index+1) + ' / ' + String(schedCount);
		  }
	  }
	  
	  self.isList = function() {
		  return $location.path() == '/list';
	  }
	  
	  self.changeView = function() {
		  if (self.isList()) {
			  $location.path('/week').replace();
		  } else {
			  $location.path('/list').replace();
		  }
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