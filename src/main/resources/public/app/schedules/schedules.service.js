angular.
	module('schedules').
	factory('Schedules', ['$http', '$mdDialog', function($http, $mdDialog) {
		return {		
			schedules: [],
			index: 0,
			
			getSchedules: function (path, successFunc) {
				var self = this;
				
				$mdDialog.show({
	                controller: 'AppController',
	                templateUrl: 'assets/html/progressDialog.html',
	                parent: angular.element(document.body),
	                clickOutsideToClose: false,
	                fullscreen: false,
	                escapeToClose: false,
	              })
				
				$http.get(path).success(function (data) {
					self.schedules = data;
					self.index = 0;
					$mdDialog.cancel();
					
					if (self.schedules.length == 0) {
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
					$mdDialog.cancel();
					var msg = 'Cannot access service. Please try again later.';
					if (status == 400) {
						msg = data;
					}
					
					$mdDialog.show(
			        		$mdDialog.alert({
					            title: 'Error',
					            textContent: msg,
					            ok: 'Close'
			          }));
				});
			},
			
			getInstructors: function (component) {
				var instr = 'TBA';
				
				for (var i = 0; i < component.blocks.length; ++i) {
					var instructors = component.blocks[i].instructors;
					
					if (instructors != null) {
						for (var j = 0; j < instructors.length; ++j) {							
							var lastFirst = instructors[j].split(',');
							var name = 'TBA';
							
							if (lastFirst.length == 1 && lastFirst[0] != '') {
								name = lastFirst[0];
							} else if (lastFirst.length > 1)  {
								name = lastFirst[1] + ' ' + lastFirst[0];
							}
							
							if (instr == 'TBA') {
								instr = name;
							} else if (instr != name) {
								instr += ' (+)';
								return instr;
							}
						}
					}
				}
				return instr;
			},
			
			getLocations: function (component) {
				var loc = 'TBA';
				
				for (var i = 0; i < component.blocks.length; ++i) {
					var currLoc = component.blocks[i].location;
					
					if (currLoc == '') {
						continue;
					}
					
					if (loc == 'TBA') {
						loc = currLoc;
					} else if (loc != currLoc) {
						loc += ' (+)';
						return loc;
					}
				}	
				
				return loc;
			},
			
			nextSchedule: function() {
				this.index++;
				if (this.index >= this.schedules.length) {
					this.index = 0;
				}
			},
			
			prevSchedule: function() {
				if (this.index <= 0) {
					this.index = this.schedules.length;
				}
				
				this.index--;
			}
		}
	}
]);