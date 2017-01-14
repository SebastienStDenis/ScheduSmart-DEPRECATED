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
			
			getProfs: function (component) {
				return 'Prof. Feridun';
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