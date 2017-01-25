// the schedules service is used to retrieve and store schedule information retrieved from the ScheduSmart backend
angular.
	module('schedules').
	factory('Schedules', ['$http', '$mdDialog', function($http, $mdDialog) {
		var today = new Date();
		var month = today.getMonth() + 1;
		var currYear = today.getFullYear();
		
		var monthChar;
		if (month < 5) {
			monthChar = '1';
		} else if (month < 9) {
			monthChar = '5';
		} else {
			monthChar = '9';
		}
		
		return {		
			schedules: [],
			index: 0,
			firstMonth: monthChar, // first month of the term (1, 5 or 9)
			year: currYear,
			
			observerCallbacks: [], // observer pattern for notifying of schedule changes

			registerObserverCallback: function(callback){
			    this.observerCallbacks.push(callback);
			},

			notifyObservers: function(){
				for (var i = 0; i < this.observerCallbacks.length; ++i) {
					this.observerCallbacks[i]();
				}
			},
			
			// getSchedules calls the ScheduSmart backend at path to generate and store schedules.
			//    firstMonth and year correspond to the selected term for the schedules, and
			//    successFunc is called if the call succeeds.
			getSchedules: function (path, firstMonth, year, successFunc) {
				var self = this;
				
				$mdDialog.show({
	                controller: 'AppController',
	                templateUrl: 'assets/html/progressDialog.html',
	                parent: angular.element(document.body),
	                clickOutsideToClose: false,
	                fullscreen: false,
	                escapeToClose: false,
	                onComplete: generateSchedules, // wait for progress to show, then make call to backend
	              })
	              
				function generateSchedules() {
					$http.get(path).success(function (data) {
						self.schedules = data;
						self.index = 0;
						self.firstMonth = firstMonth;
						self.year = year;
						
						$mdDialog.cancel(); // cancel progress dialog
						
						self.notifyObservers();
						
						if (self.schedules.length === 0) {
							$mdDialog.show(
					        		$mdDialog.alert({
							            title: 'Notice',
							            textContent: 'No valid schedules exist. Try again with different options.',
							            ok: 'Close'
					          }));
						} else {
							successFunc();
						}					
						
					}).error(function (data, status) {
						$mdDialog.cancel(); // cancel progress dialog
						
						var msg = 'Cannot access service. Please try again later.';
						if (status === 400) {
							msg = data;
						}
						
						$mdDialog.show(
				        		$mdDialog.alert({
						            title: 'Error',
						            textContent: msg,
						            ok: 'Close'
				          }));
					});
				}				
			},
			
			// getInstructors returns a shortened and formatted string
			//     representing the instructors for component
			getInstructors: function (component) {
				var instr = 'TBA';
				
				for (var i = 0; i < component.blocks.length; ++i) {
					var instructors = component.blocks[i].instructors;
					
					if (instructors != null) {
						for (var j = 0; j < instructors.length; ++j) {							
							var lastFirst = instructors[j].split(',');
							var name = 'TBA';
							
							if (lastFirst.length === 1 && lastFirst[0] !== '') {
								name = lastFirst[0];
							} else if (lastFirst.length > 1)  {
								name = lastFirst[1] + ' ' + lastFirst[0];
							}
							
							if (instr === 'TBA') {
								instr = name;
							} else if (instr !== name) { // multiple instructors, just add (+)
								instr += ' (+)';
								return instr;
							}
						}
					}
				}
				return instr;
			},
			
			// getLocations returns a shortened and formatted string
			//     representing the locations for component
			getLocations: function (component) {
				var loc = 'TBA';
				
				for (var i = 0; i < component.blocks.length; ++i) {
					var currLoc = component.blocks[i].location;
					
					if (currLoc === '') {
						continue;
					}
					
					if (loc === 'TBA') {
						loc = currLoc;
					} else if (loc !== currLoc) { // multiple locations, just add (+)
						loc += ' (+)';
						return loc;
					}
				}	
				
				return loc;
			},
			
			// nextSchedule increments (with wrap-around) the index for the schedule array
			nextSchedule: function() {
				this.index++;
				if (this.index >= this.schedules.length) {
					this.index = 0;
				}
				this.notifyObservers();
			},

			// prevSchedule decrements (with wrap-around) the index for the schedule array
			prevSchedule: function() {
				if (this.index <= 0) {
					this.index = this.schedules.length;
				}				
				this.index--;
				this.notifyObservers();
			}
		}
	}
]);