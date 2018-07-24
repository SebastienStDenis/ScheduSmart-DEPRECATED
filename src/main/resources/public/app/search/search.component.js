// the search component is used for selecting courses and schedule preferences
angular.
	module('search').
	component('search', {
		templateUrl: 'app/search/search.template.html',
		controller: ['$scope', '$http', '$mdSidenav', '$mdDialog', 'Schedules',
			function SearchController($scope, $http, $mdSidenav, $mdDialog, Schedules) {
				var self = this;
								
				// terms is an array of {name, code, courses} objects, where name is the term name (eg. W17), code is 
				//    the term code (eg. 1171), and courses is an array of course names
				self.terms = []; 
				self.termInd = 0;
				
				self.Schedules = Schedules;

                var schedusmartApi;

                for (var stack of JSON.parse(stackData).Stacks) {
                  if (stack.StackName === "ScheduSmartApi") {
                    var found = false;
                    for (var output of stack.Outputs) {
                      if (output.OutputKey == "ScheduSmartApiUrl") {
                        schedusmartApi = output.OutputValue;
                        found = true;
                        break;
                      }
                    }

                    if (found) {
                      break;
                    }
                  }
                }

				$http.get(schedusmartApi + '/api/v1/allcourses').success(function (data) { // get course names for all available terms
					self.terms = data;
					$scope.$emit('SearchReady')
				}).error(function (data, status) {					
					$mdDialog
			        .show(
			        		$mdDialog.alert({
					            title: 'Error',
					            htmlContent: data + "<BR/><BR/>Please try again later.  Click <a href='https://github.com/SebastienStDenis/ScheduSmart/issues'>here</a> to report an issue.",
					            ok: 'Close'
			          }));
				});
				
				self.courses = []; // currently selected courses
				
				// addCourse adds the simple name version (eg. CS 241) of self.selectedItem to courses
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
				
				// noCourses returns true if no courses have been selected
				self.noCourses = function () {
					return self.courses.length === 0;
				}
				
				// resetCourses resets all selected courses
				self.resetCourses = function () {
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
				
				// getSchedules uses the Schedules service to generate schedules
				//    based on current courses and preferences
				self.getSchedules = function () {
					path = schedusmartApi + '/api/v1/schedules?';

					var term = self.terms[self.termInd].code
					path += 'term=' + term +
					 '&courses=' + self.courses.map(c => c.replace(/\s+/g, '')).join(',') +
					 '&ignore=' + self.sections.filter(sec => sec.selected).map(sec => sec.name).join(',') +
					 '&classtime=' + self.classTime +
					 '&daylength=' + self.dayLength +
					 '&omitclosed=' + (self.omitClosed ? '1' : '0');
					  
					//path = '/api/v1/schedules?term=1171&courses=CS240&courses=CS241&courses=CS251&courses=STV205&classtime=1&daylength=2&omitclosed=0';
					
					var year = '20' + term.substring(1, 3);
					
					Schedules.getSchedules(path, term.charAt(3), year, $mdSidenav('left').toggle); // close search menu if successful
				}
				
				// return the set of courses that match the string search
				self.filterCourses = function(search) {
					var sanitize = (str => str.replace(/ |-|'/g, '').toLowerCase())
					var searchClean = sanitize(search)
					return self.terms[self.termInd].courses.filter(name => (sanitize(name).includes(searchClean)))
				}
				
				// showHelp opens the help dialog
				self.showHelp = function() {
					$scope.$emit('ShowHelp')
				}
		}
	]
});