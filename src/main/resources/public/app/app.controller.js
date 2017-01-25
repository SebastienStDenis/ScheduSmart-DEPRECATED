// main controller for the application
angular.
	module('ScheduSmartApp').
	controller('AppController', ['$scope', '$location', '$mdSidenav', '$mdDialog', 'Schedules', function AppController($scope, $location, $mdSidenav, $mdDialog, Schedules) {
	  var self = this;
		
	  // toggles the search sidenav
	  self.openSideNav = function() {
		  $mdSidenav('left').toggle();
	  };
	  
	  self.loading = true;
	  	  
	  // listen for 'SearchReady' signal from search component to hide loading screen
	  $scope.$on('SearchReady', function() {
		    self.loading = false;
	  });
	  
	  self.sidenavOpen = true;
	  
	  self.Schedules = Schedules;
	  
	  // noSchedules returns true when Schedules has no schedules
	  self.noSchedules = function() {
		  if (self.Schedules.schedules.length === 0) {
			  return true;
		  } else {
			  return false;
		  }
	  }
	  
	  // displayIndex returns a string representing the position of the current schedule in Schedules (eg. '12 / 65')
	  self.displayIndex = function() {
		  var schedCount = self.Schedules.schedules.length;
		  if (schedCount === 0) {
			  return 'no schedules';
		  } else {
			  return String(self.Schedules.index+1) + ' / ' + String(schedCount);
		  }
	  }
	  
	  // isList returns true if the view is set to list-view (false for week-view)
	  self.isList = function() {
		  return $location.path() == '/list';
	  }
	  
	  // changeView changes the view from list-view to week-view (or vice-versa)
	  self.changeView = function() {
		  if (self.isList()) {
			  $location.path('/week').replace();
		  } else {
			  $location.path('/list').replace();
		  }
	  }
	  
	  // showHelp shows the help dialog
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
	  
	  // listen for 'ShowHelp' signal from child to show the help dialog
	  $scope.$on('ShowHelp', function() {
		  self.showHelp()
	  });
}]);