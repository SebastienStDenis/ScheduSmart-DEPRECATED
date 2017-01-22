angular.
	module('search').
	component('search', {
		templateUrl: 'app/search/search.template.html',
		controller: ['$scope', '$http', '$mdSidenav', '$mdDialog', 'Schedules',
			function ListViewController($scope, $http, $mdSidenav, $mdDialog, Schedules) {
				var self = this;
								
				self.terms = [];
				self.termInd = 0;
				
				self.Schedules = Schedules;
				
				$http.get('api/v1/allcourses').success(function (data) {
					self.terms = data;
					$scope.$emit('SearchReady')
				}).error(function (data, status) {					
					$mdDialog
			        .show(
			        		$mdDialog.alert({
					            title: 'Error',
					            textContent: 'Cannot access service. Please try again later.',
					            ok: 'Close'
			          }));
				});
				
				self.courses = [];
				
				self.addCourse = function() {
					  if (self.selectedItem == null) {
						  return
					  }
					  
					  var shortName = self.selectedItem.split(" - ")[0]
					  if (self.searchText && self.courses.indexOf(shortName) === -1) {
						  self.courses.push(shortName);
					  }
				
					  self.searchText = '';
					  self.selectedItem = '';
				  }
				
				self.noCourses = function() {
					return self.courses.length === 0;
				}
				
				self.changeTerm = function () {
					  self.courses = [];
				}
				
				self.sections = [
					  {name: 'LEC', selected: false},
					  {name: 'TUT', selected: false},
					  {name: 'TST', selected: false},
					  {name: 'LAB', selected: false},
					  {name: 'SEM', selected: false},
					  {name: 'PRJ', selected: false},
				];
				
				self.classTime = '0';
				
				self.dayLength = '0';
				
				self.omitClosed = false;
				
				self.getSchedules = function () {
					path = '/api/v1/schedules?';
					
					var term = self.terms[self.termInd].code
					path += 'term=' + term;
					
					for (var i = 0; i < self.courses.length; ++i) {
						path += '&courses=' + self.courses[i].replace(/\s+/g, '');
					}
					
					for (var i = 0; i < self.sections.length; ++i) {
						var section = self.sections[i];
						if (section.selected) {
							path += '&ignore=' + section.name;
						}
					}
					
					path += '&classtime=' + self.classTime;
					path += '&daylength=' + self.dayLength;
					path += '&omitclosed=' + (self.omitClosed ? '1' : '0');
					  
					//path = '/api/v1/schedules?term=1171&courses=CS240&courses=CS241&courses=CS251&courses=STV205&classtime=1&daylength=2&omitclosed=0';
					
					var year = '20' + term.substring(1, 3);
					
					Schedules.getSchedules(path, term.charAt(3), year, $mdSidenav('left').toggle);
				}
				
				
				self.showHelp = function() {
					$scope.$emit('ShowHelp')
				}
		}
	]
});