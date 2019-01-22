angular.module('HelloApp', [])
    .controller('DocVisit', ['$scope', '$http', function ($scope, $http) {
        $scope.doctorArrOne = [];
        $scope.doctorArrTwo = [];
        $scope.docAvailDates = [];
        $scope.object = {};
        $scope.selectedDate = null;
        $scope.idx = -1;
        $scope.pid = null;

        $scope.loadDoctors = function () {
            if ($scope.city) {
                $http.get("api/findDoctors?city=" + $scope.city).success(function (response) {
                    $scope.doctors = response;
                });
            }
        };

        $scope.checkDoctorAvail = function () {
            $http.get('api/getschedule?dt=' + $scope.availDate + "&doctorId=" + $scope.object.doctorid).success(function (resp) {
                if (resp.result && resp.result === 1) {
                    if (resp.schedule.length > 0) {
                        $scope.docAvailDates = [];
                        for (var i = 0; i < resp.schedule.length; i++) {
                            var obj = resp.schedule[i];
                            if (obj.timeend.endsWith(":00") || obj.timeend.endsWith(":30")) {

                            } else {
                                $scope.docAvailDates.push(obj)
                            }
                        }
                    }
                } else {
                    $scope.docAvailDates = [];
                }
            });
        };

        $scope.chooseDate = function(idx, dt) {
            $scope.selectedDate = dt;
            $scope.idx = idx;
        };

        $(function () {
            $("#datepicker").datepicker({
                autoclose: true,
                todayHighlight: true,
                daysOfWeekDisabled: [0,6],
                locale: 'ka'
            }).datepicker('update', new Date());
        });

        $scope.doVisit = function() {
            if ($('#pid').val()){
                var  obj = {
                    idn: $('#pid').val(),
                    visitdate: $scope.selectedDate.timestart,
                    doctorid: $scope.object.doctorid
                };

                $http.post('api/addVisit', obj).success(function (resp) {
                    if (resp.result && resp.result === 1) {
                        alert("ოპერაცია შესრულდა წარმატებით")
                        window.location.reload()
                    } else {
                        alert("შეცდომა დაფიქსირდა ოპერაციის შესრულებისას. სცადეთ თავიდან")
                    }
                })
            }

        };

        $scope.chooseDoc = function (obj) {
            $scope.object = obj;
            $scope.availDate = '';
            $scope.availDate = '';
            $scope.docAvailDates = [];
            $("#showAddEdit").modal();
        }
    }]);
