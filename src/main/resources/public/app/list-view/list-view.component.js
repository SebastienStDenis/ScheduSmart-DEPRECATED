// the listView component shows the current schedule in schedules as a list
angular.
	module('listView').
	component('listView', {
		templateUrl: 'app/list-view/list-view.template.html',
		controller: ['Schedules',
			function ListViewController(Schedules) {
				this.Schedules = Schedules;
		}
	]
});