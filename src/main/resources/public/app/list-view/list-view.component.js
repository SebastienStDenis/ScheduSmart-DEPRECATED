angular.
	module('listView').
	component('listView', {
		templateUrl: 'app/list-view/list-view.template.html',
		controller: ['Schedules', // remove Schedules from here ?
			function ListViewController(Schedules) { // remove controller field?
				this.Schedules = Schedules;
		}
	]
});