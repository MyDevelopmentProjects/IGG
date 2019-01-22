angular.module('HelloApp', [])
    .controller('TplCtrl', ['$scope', '$http', function ($scope, $http) {

        $scope.object = {};

        $scope.add = function() {
            $('.btn').attr('disabled', 'disabled')
            $http.post('tpl/addPolicy', $scope.object).success(function (resp) {
                if (resp.transId && resp.transId !== "") {
                    window.location = 'http://178.128.198.46:8090/post.php?trans_id=' + encodeURIComponent(resp.transId)
                } else {
                    alert("შეცდომა დაფიქსირდა ოპერაციის შესრულებისას. სცადეთ თავიდან")
                    $('.btn').removeAttribute('disabled')
                }
            });
        };

        $(function () {
            $("#datepicker").datepicker({
                autoclose: true,
                todayHighlight: true
            }).datepicker('update', new Date());
        });
        
        $scope.calculate = function () {
            var price = 0;
            if ($scope.object && $scope.object.limit && $scope.object.limit > 0 && $scope.object.paymentschedule) {
                var _price = $('#limit option:selected').attr('price');
                price = _price;
                if (parseInt($scope.object.paymentschedule) === 2) {
                    price = _price / 4;
                } else if (parseInt($scope.object.paymentschedule) === 12) {
                    price = _price / 12;
                }
            }
            return parseFloat(price).toFixed(2);
        }

    }]);