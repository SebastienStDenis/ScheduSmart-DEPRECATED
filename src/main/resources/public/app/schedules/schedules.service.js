angular.
	module('schedules').
	factory('Schedules', ['$http', function($http) {
		return {		
			schedules: [],
			index: 0,
			
			getSchedules: function (path, successFunc) {
				var self = this;
				$http.get(path).success(function (data) {
					self.schedules = data;
					successFunc();
				}).error(function (data, status) {
					console.log('Error: ' + data);
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