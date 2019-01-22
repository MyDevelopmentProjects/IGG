angular.module('HelloApp', [])
    .controller('CascoCtrl', ['$scope', '$http', function ($scope, $http) {

        $scope.selectedPage = "stepOne";
        $scope.object = {};
        $scope.prem = 0;

        $scope.toggleNext = function () {
            if ($scope.selectedPage === 'stepOne') {
                $scope.selectedPage = 'stepTwo'
            } else {
                $scope.selectedPage = 'stepOne'
            }
        }

        $(function () {
            $("#datepicker").datepicker({
                autoclose: true,
                todayHighlight: true
            }).datepicker('update', new Date());
        });

        $scope.calculate = function() {
            var obj = angular.copy($scope.object);
            if (obj.driverage && obj.vehicletype && obj.price) {
                var _url = '?age=' + obj.driverage + '&price=' + obj.price + '&isSedan=' + (obj.vehicletype === 'sedan')
                var price = 0;
                $http.get('casco/calculate2' + _url).success(function (resp) {
                    if (resp && parseFloat(resp) > 0) {
                        if (obj.tpllimit) {
                            var _price = $('#tpllimit option:selected').attr('price');
                            price += parseFloat(_price);
                        }

                        if (obj.malimit) {
                            var _price = $('#malimit option:selected').attr('price');
                            price += parseFloat(_price);
                        }

                        $scope.prem = resp + price;
                    } else {
                        alert("შეცდომა დაფიქსირდა ოპერაციის შესრულებისას. დარწმუნდით რომ ყველა ველი სწორად გაქვთ შევსებული და სცადეთ თავიდან");
                        $('.btn').removeAttribute('disabled')
                    }
                });
            }
        };

        $scope.add = function() {
            $('.btn').attr('disabled', 'disabled')
            $http.post('casco/addPolicy', $scope.object).success(function (resp) {
                $scope.calculate()
            });
        }

    }]);