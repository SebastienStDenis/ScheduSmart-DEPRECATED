angular.
	module('weekView').
	component('weekView', {
		templateUrl: 'app/week-view/week-view.template.html',
		controller: ['$scope', 'Schedules',
			function WeekViewController($scope, Schedules) {
				var self = this;
				
				self.months = ["JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"];
				self.days = ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"];
				
				self.colors = ['#F44336', '#2196F3', '#4CAF50', '#FF9800', '#9C27B0', '#607D8B', '#EC407A', '#00BCD4', '#3F51B5', ];
				self.colorsLen = self.colors.length;
				
				self.Schedules = Schedules;
				
				self.weekDates = new Array(7);
			
				self.week = new Array(48);
								
				self.firstDate = new Date();
				
				self.weekIndex = 0;
				
				self.getTime = function(index) {
					var hour = Math.floor(index/2).toString();
					var min = "00";
					if (index % 2 == 1) {
						min = "30";
					}
					
					return hour+':'+min;
				}
				
				self.getIndex = function(time) {
					var hourMin = time.split(":");
					
					if (hourMin.length < 2) {
						return -1;
					}
					
					var hour = parseInt(hourMin[0]);
					var min = parseInt(hourMin[1]);
					
					if (min == 0) {
						return 2 * hour;
					} else if (min <= 30) {
						return 2 * hour + 1;
					} else {
						return 2 * (hour + 1);
					}	
				}
				
				self.getHeight = function(startTime, endTime) {
					var startIndex = self.getIndex(startTime);
					var endIndex = self.getIndex(endTime);
					
					if (startIndex == -1 || endIndex == -1) {
						return '0%';
					}
					
					var height = (endIndex-startIndex)*100 - 17;
					return height.toString()+'%';					
				}
				
				self.inWeek = function(startDate, endDate) {
					var start1 = new Date(self.weekDates[0].getTime());
					var end1 = new Date(self.weekDates[6].getTime());
					
					var start2 = new Date(self.firstDate.getTime());
					
					var monthDay = startDate.split('/');
					if (monthDay.length < 2) {
						return false;
					}
					start2.setMonth(monthDay[0]-1);
					start2.setDate(monthDay[1]);
					
					var end2 = new Date(self.firstDate.getTime());
					
					monthDay = endDate.split('/');
					if (monthDay.length < 2) {
						return false;
					}
					end2.setMonth(monthDay[0]-1);
					end2.setDate(monthDay[1]);
						
					if (start2.getTime() < start1.getTime()) {
						var temp = start1;
						start1 = start2;
						start2 = temp;
						
						temp = end1;
						end1 = end2;
						end2 = temp;
					}
					
					if (end1.getTime() >= start2.getTime()) {
						return true;
					}
					return false;
				}
				
				self.addBlock = function(block, days, index) {
					for (var i = 0; i < days.length; ++i) {
						var dayIndex = 0;
						switch (days[i]) {
						case 'Su':
							dayIndex = 0;
							break;
						case 'M':
							dayIndex = 1;
							break;
						case 'T':
							dayIndex = 2;
							break;
						case 'W':
							dayIndex = 3;
							break;
						case 'Th':
							dayIndex = 4;
							break;
						case 'F':
							dayIndex = 5;
							break;
						case 'S':
							dayIndex = 6;
							break;
						}
						
						self.week[index][dayIndex] = block;
					}
				}
				
				self.update = function() {
					var sunday = new Date(self.firstDate.getTime());
					sunday.setDate(sunday.getDate() + 7*self.weekIndex);
					
					for (var i = 0; i < 7; ++i) {
						self.weekDates[i] = new Date(sunday.getTime());
						self.weekDates[i].setDate(self.weekDates[i].getDate()+i);
					}
					

					for (var i = 0; i < 48; ++i) {
						self.week[i] = new Array(7);
					}
					
					self.minInd = 48;
					self.maxInd = -1;
					
					if (self.Schedules.schedules.length > 0) {					
						var courses = self.Schedules.schedules[self.Schedules.index].courses;
						for (var i = 0; i < courses.length; ++i) {
							var color = self.colors[i % self.colorsLen];
							for (var j = 0; j < courses[i].length; ++j) {
								var blocks = courses[i][j].blocks;
								var name = courses[i][j].name + ' - ' + courses[i][j].sectionName;
								for (var k = 0; k < blocks.length; ++k) {
									var location = blocks[k].location;
									
									var instructor = 'TBA';								
									if (blocks[k].instructors.length > 0) {
										var nameSplit = blocks[k].instructors[0].split(",");
										if (nameSplit.length >= 2) {
											instructor = nameSplit[1] + ' ' + nameSplit[0];
										} else {
											instructor = nameSplit[0];
										}
									}								
									if (blocks[k].instructors.length > 1) {
										instructor += ' (+)';
									}
									
									var height = self.getHeight(blocks[k].startTime, blocks[k].endTime);
									var style = {'height':height, 'background':color};
									
									var block = {
										style: style,
										name: name,
										instructor: instructor,
										location: location,
										time: blocks[k].startTime + "-" + blocks[k].endTime, // !!!remove self field!!!
										date: blocks[k].startDate + "-" + blocks[k].endDate // !!!remove self field!!!
									}
									
									if ((blocks[k].startDate == undefined || blocks[k].endDate == undefined) ||
											self.inWeek(blocks[k].startDate, blocks[k].endDate)) {
										var startIndex = self.getIndex(blocks[k].startTime);
										var endIndex = self.getIndex(blocks[k].endTime);
										if (startIndex == -1 || endIndex == -1) {
											continue;
										}
																			
										if (startIndex < self.minInd) {
											self.minInd = startIndex;
										}									
										if (endIndex > self.maxInd) {
											self.maxInd = endIndex;
										}
										
										self.addBlock(block, blocks[k].days, startIndex);
									}
								}
								
							}
							
						}
					}				
					
					if (self.minInd >= 48) {
						self.minInd = 19;
					} else {
						self.minInd--;
					}
					
					if (self.maxInd <= -1) {
						self.maxInd = 32;
					}
					
					if (self.minInd != 0 && self.minInd % 2 == 0) {
						self.minInd--;
					}	
					
					if (self.maxInd % 2 == 1) {
						self.maxInd--;
					}					
				}
				
				self.resetAndUpdate = function() {	
					self.weekIndex = 0;
					
					self.firstDate = new Date();
					self.firstDate.setYear(self.Schedules.year);
					self.firstDate.setMonth(self.Schedules.firstMonth - 1);
					self.firstDate.setDate(1);
					self.firstDate.setHours(0, 0, 0, 0);
					
					while (self.firstDate.getDay() != 0) {
				        self.firstDate.setDate(self.firstDate.getDate() + 1);
				    }
					
					self.update();
				}
				
				Schedules.registerObserverCallback(self.resetAndUpdate);
								
				self.minInd = 19; // always odd
				self.maxInd = 32; // always even
				
				self.resetAndUpdate();
				
				self.nextWeek = function() {
					++self.weekIndex;
					if (self.weekIndex > 11) {
						self.weekIndex = 0;
					}
					
					self.update();					
				}
				
				self.prevWeek = function() {
					--self.weekIndex;
					if (self.weekIndex < 0) {
						self.weekIndex = 11;
					}
					
					self.update();
				}
				

				/*
				self.week[30][5] = {
						style: {'background':'#3F51B5', 'height':'183%'},
						name: "ACTSC 102 - TUT 101",
						instructor: "Joe John",
						location: "DC 2145"
				}
				*/
		}
	]
});